package com.piotrak.service.elementservice;

import com.piotrak.service.element.SwitchElement;
import com.piotrak.service.technology.Command;
import com.piotrak.service.technology.mqtt.MQTTCommand;
import com.piotrak.service.technology.mqtt.MQTTConnectionService;
import com.piotrak.service.technology.web.WebCommand;
import org.junit.Test;

import javax.naming.OperationNotSupportedException;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

public class TvElementServiceTests {

    @Test
    public void webCommandReceivedTest() throws OperationNotSupportedException {
        WebCommand webCommand = new WebCommand("ON");
        SwitchElement tv = mock(SwitchElement.class);
        MQTTConnectionService mqttConnectionService = mock(MQTTConnectionService.class);
        TvElementService tvElementService = new TvElementService(tv, mqttConnectionService);
        tvElementService.commandReceived(webCommand);
        verify(tv, times(1)).actOnCommand(isA(WebCommand.class));
        verify(mqttConnectionService, times(1)).actOnConnection(isA(MQTTCommand.class));
    }

    @Test
    public void MQTTCommandReceivedTest() throws OperationNotSupportedException {
        MQTTCommand mqttCommand = new MQTTCommand("aa", "aa");
        SwitchElement tv = mock(SwitchElement.class);
        MQTTConnectionService mqttConnectionService = mock(MQTTConnectionService.class);
        TvElementService tvElementService = new TvElementService(tv, mqttConnectionService);
        tvElementService.commandReceived(mqttCommand);
        verify(tv, times(1)).actOnCommand(mqttCommand);
        verify(mqttConnectionService, never()).actOnConnection(mqttCommand);
    }

    @Test
    public void translateCommandTest(){
        WebCommand webCommand = new WebCommand("ON");
        SwitchElement tv = mock(SwitchElement.class);
        MQTTConnectionService mqttConnectionService = mock(MQTTConnectionService.class);
        TvElementService tvElementService = new TvElementService(tv, mqttConnectionService);
        Command command = tvElementService.translateCommand(webCommand);
        assertTrue(command instanceof MQTTCommand);
    }
}
