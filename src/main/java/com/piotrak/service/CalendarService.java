package com.piotrak.service;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.Events;
import com.piotrak.config.ServicesConfiguration;
import com.piotrak.service.element.Element;
import com.piotrak.service.element.TemplateElement;
import com.piotrak.service.logger.WebLogger;
import com.piotrak.service.technology.time.DelayedCommand;
import com.piotrak.service.technology.web.WebCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.naming.OperationNotSupportedException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;

/**
 * Service for reading Google calendar events
 */
@Service("calendarService")
@ConfigurationProperties("calendar")
public class CalendarService {

    @Autowired
    private WebLogger webLogger;

    @Autowired
    private ServicesConfiguration servicesConfiguration;

    @Autowired
    private List<Element> templatesList;

    private String applicationName = "";
    private String name = "primary";
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private static final String TOKENS_DIRECTORY_PATH = "tokens";

    /**
     * Global instance of the scopes required by this quickstart.
     * If modifying these scopes, delete your previously saved tokens/ folder.
     */
    private static final List<String> SCOPES = Collections.singletonList(CalendarScopes.CALENDAR_READONLY);

    /**
     * Credentials file generated by the Google APIs:
     * https://console.developers.google.com
     * Select project -> credentials -> create OAuth -> Other
     */
    private static final String CREDENTIALS_FILE_PATH = "/credentials.json";

    @PostConstruct
    public void setUp(){
        webLogger.setUp(this.getClass().getName());
    }

    /**
     * Check calendar every hour
     */
    @Scheduled(fixedRate = 3600000)
    public void checkCalendarEvents() {
        webLogger.log(Level.INFO, "Checking calendar events...");
        try {
            List<Event> items = getCalendarEvents();
            if (items.isEmpty()) {
                webLogger.log(Level.INFO, "No upcoming events found.");
            } else {
                for (Event event : items) {
                    executeDelayedCommand(event);
                }
            }
        } catch (GeneralSecurityException | IOException | CalendarException e) {
            webLogger.log(Level.SEVERE, e.getMessage());
        }
    }

    /**
     * Create delayed commands for each command (separated with ";") in the event's summary
     * @param event
     * @throws CalendarException
     */
    private void executeDelayedCommand(Event event) throws CalendarException {
        DateTime startTime = event.getStart().getDateTime();
        if (startTime == null) {
            startTime = event.getStart().getDate();
        }
        long delay = startTime.getValue() - System.currentTimeMillis();
        if(delay < 0){
            return;
        }
        webLogger.log(Level.INFO, String.format("Setting up calendar command: %s (%s)\n", event.getSummary(), startTime));
        CommandService delayedCommandService = servicesConfiguration.getServiceMap().get("delayed");
        String[] commands = event.getSummary().split(";");
        for (String s : commands) {
            String[] command = s.split(" ");
            if (command.length < 2) {
                throw new CalendarException(String.format("Invalid calendar command received: %s", s));
            }
            if("Template".equalsIgnoreCase(command[0])){
                Optional<Element> template = templatesList.stream().filter(t -> t.getName().equalsIgnoreCase(command[1])).findFirst();
                template.ifPresent(t -> new Thread(() -> {
                    try {
                        Thread.sleep(delay);
                        ((TemplateElement)t).switchElement("ON");
                    } catch (OperationNotSupportedException | InterruptedException e) {
                        webLogger.log(Level.WARNING, e.getMessage());
                    }
                }).start());
            } else {
                CommandService elementService = servicesConfiguration.getServiceMap().get(command[0]);
                WebCommand webCommand = new WebCommand(command[1]);
                delayedCommandService.commandReceived(new DelayedCommand(delay, webCommand, elementService));
            }
        }
    }

    /**
     * Get calendar events for the next hour
     * @return list of events in the next hour
     */
    private List<Event> getCalendarEvents() throws GeneralSecurityException, IOException {
        // Build a new authorized API client service.
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        Calendar service = new Calendar.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
                .setApplicationName(applicationName)
                .build();
//         List the next 10 events from the primary calendar.
        DateTime now = new DateTime(System.currentTimeMillis());
        DateTime end = new DateTime(System.currentTimeMillis() + 3600_000);
        Events events = service.events().list(name)
                .setTimeMin(now)
                .setTimeMax(end)
                .setOrderBy("startTime")
                .setSingleEvents(true)
                .execute();
         return events.getItems();
    }

    /**
     * Creates an authorized Credential object.
     * @param HTTP_TRANSPORT The network HTTP Transport.
     * @return An authorized Credential object.
     * @throws IOException If the credentials.json file cannot be found.
     */
    private static Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT) throws IOException {
        // Load client secrets.
        InputStream in = CalendarService.class.getResourceAsStream(CREDENTIALS_FILE_PATH);
        if (in == null) {
            throw new FileNotFoundException("Resource not found: " + CREDENTIALS_FILE_PATH);
        }
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        // Build flow and trigger user authorization request.
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH)))
                .setAccessType("offline")
                .build();
        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
        return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
    }

    public String getApplicationName() {
        return applicationName;
    }

    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
