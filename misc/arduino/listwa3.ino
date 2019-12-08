//--------------------  INFO ----------------------------------//
/*
   Sciagnac pubsubclient wersja co najmniej 2.4
   odpalic pubsubclient.h
   i zmienic wersje MQTT na 3_1
*/

/*
* DHT11:
* VCC DATA  X GND
*/

#include <ESP8266WiFi.h>
#include <PubSubClient.h>
#include <dht.h>

const int SOCKET_1 = 5;//D1
const int SOCKET_2 = 4;//D2
const int SOCKET_3 = 0;//D3
const int SOCKET_4 = 2;//D4

const int ULTRASONIC_TRIG = 14;//D5
const int ULTRASONIC_ECHO = 12;//D6

const int DESK_UP = 13;//D7
const int DESK_DOWN = 15;//D8

const int PC_BUTTON = 3;//RX

const char* WIFI_SSID     = "TP-LINK_5DBD15";
const char* WIFI_PASSWORD = ""; //WIFI password here
const char* MQTT_SERVER = "192.168.1.101";
const int MQTT_PORT = 1883;
const String DEVICE_NAME = "listwa3";

const String MESSAGE_WELCOME = "connected";
const String MESSAGE_GOODBYE = "disconnected";
const String SUBTOPIC_IN = "in";
const String SUBTOPIC_OUT = "out";
const String SUBTOPIC_STATUS = "status";
const String TOPIC_PC = "pc";
const String PUBLISH_TOPIC = DEVICE_NAME + "/" + SUBTOPIC_OUT + "/";
const String DHT_HUM = "hum";
const String DHT_TEMP = "temp";
const String DHT_ = "dht";
const String SUBTOPIC_DESK = "desk";

const String SUBSCRIBE_TOPICS[] = {
              DEVICE_NAME + "/" + SUBTOPIC_IN + "/#",
              TOPIC_PC + "/" + SUBTOPIC_IN
            };

long currentMillis;

dht DHT;
#define DHT11_PIN 16//D0
long previousReadMillis = 0;
const long dhtInterval = 3000;
boolean readDHT = true;
int dhtReadTries = 0;
const int MAX_DHT_READ_TRIES = 30;
const String DHT_ERR1 = "ERR1";

const long DISTANCE_DELTA = 2;
long deskHeight = 0;
boolean setHeight = false;
long previousUltrasonicReadMillis = 0;
const long ultrasonicInterval = 300;
int deskChangeTries = 0;
const int MAX_DESK_CHANGE_TRIES = 30;
const String DESK_ERR1 = "ERR1";

WiFiClient espClient;
PubSubClient client(espClient);

int wifiRetryCount = 0;
int mqttRetryCount = 0;

/*
 * Set up the device:
 * prepare pins
 * connect to WiFi network
 * connect to MQTT broker
 */
void setup() {
  Serial.begin(115200);
  delay(10);
  Serial.println("Starting device: " + DEVICE_NAME);
  WiFi.mode(WIFI_STA);
  WiFi.setAutoConnect(false);
  WiFi.hostname(DEVICE_NAME.c_str());
  WiFi.begin(WIFI_SSID, WIFI_PASSWORD);
  delay(1000);
  setupPins();
  checkWiFi();
  checkMQTT();
}

/*
 * check connection status, connect if necessary
 * if unable to connect to WiFi or MQTT activate offline mode (all output pins ON)
 * if connected proceed with MQTT service and update DHT read-outs
 */
void loop() {
  currentMillis = millis();
  if(checkWiFi() && checkMQTT()){
    client.loop();
    if (readDHT) {
      getDht();
    }
    if(setHeight){
      setDeskHeight();
    }
  } else {
    if(wifiRetryCount == 5 || mqttRetryCount == 5){
      activateOfflineMode();
    }
  }
}

/*
 * Set up device pins
 */
void setupPins(){
  pinMode(SOCKET_1, OUTPUT);
  pinMode(SOCKET_2, OUTPUT);
  pinMode(SOCKET_3, OUTPUT);
  pinMode(SOCKET_4, OUTPUT);
  pinMode(ULTRASONIC_TRIG, OUTPUT);
  pinMode(DESK_UP, OUTPUT);
  pinMode(DESK_DOWN, OUTPUT);
  pinMode(ULTRASONIC_ECHO, INPUT);
  pinMode(PC_BUTTON, FUNCTION_3);//override RX
  pinMode(PC_BUTTON, OUTPUT);
  digitalWrite(PC_BUTTON, HIGH);
}

/*
 * check WiFi connection status, connect if necessary
 */
boolean checkWiFi(){
  if (WiFi.status() == WL_CONNECTED) {
    wifiRetryCount = 0;
    return true;
  } else {
    mqttRetryCount = 0;
    wifiRetryCount++;
    connectWiFi();
    return false;
  }
}

/*
 * check MQTT connection status, connect if necessary
 */
boolean checkMQTT(){
  if (client.connected()) {
    mqttRetryCount = 0;
    return true;
  } else {
    connectMQTT();
    mqttRetryCount++;
    return false;
  }
}

/*
 * Connect to WiFi
 */
void connectWiFi(){
  Serial.println("Connecting to WIFI: " + String(WIFI_SSID) + " with password: " + String(WIFI_PASSWORD) + " ...");
  if (WiFi.status() != WL_CONNECTED) {
    Serial.println(" failed. Connection try number: " + ((String)wifiRetryCount));
    delay(2000);
  }
}

/*
 * Connect to MQTT broker
 * if connected subscribe to the topics, get the pins setup and publish a welcome message
 */
void connectMQTT() {
  client.setServer(MQTT_SERVER, MQTT_PORT);
  client.setCallback(callback);
  if(connectMQTTClient()){
    subscribeMQTT();
    setupPinsFromMQTT();
    client.publish((DEVICE_NAME + "/" + SUBTOPIC_STATUS).c_str(), MESSAGE_WELCOME.c_str(), true);
  }
}

/*
 * Connect to the MQTT broker
 */
boolean connectMQTTClient(){
  Serial.println("Connecting to MQTT server...");
  if (client.connect(DEVICE_NAME.c_str(), (DEVICE_NAME + "/" + SUBTOPIC_STATUS).c_str(), 0, true, MESSAGE_GOODBYE.c_str())){
    Serial.println(MESSAGE_WELCOME);
    return true;
  } else {
    Serial.println("failed with state: " + client.state());
    delay(5000);
  }
  return false;
}

/*
 * Subscribe to the MQTT topics
 */
void subscribeMQTT() {
  Serial.println("Subscribing to the topics:");
  for(int i=0; i<sizeof(SUBSCRIBE_TOPICS) / sizeof(SUBSCRIBE_TOPICS[0]); i++){
    Serial.println(SUBSCRIBE_TOPICS[i]);
    client.subscribe(SUBSCRIBE_TOPICS[i].c_str());
  }
  Serial.println("Done");
}

/*
 * Get the pins setup from MQTT
 */
void setupPinsFromMQTT() {
  client.subscribe((DEVICE_NAME + "/" + SUBTOPIC_OUT + "/#").c_str());
  delay(100);
  client.unsubscribe((DEVICE_NAME + "/" + SUBTOPIC_OUT + "/#").c_str());
}

/*
 * Turn all the output pins ON
 */
void activateOfflineMode(){
  Serial.println("Device is offline");
  digitalWrite(SOCKET_1, LOW);
  digitalWrite(SOCKET_2, LOW);
  digitalWrite(SOCKET_3, LOW);
  digitalWrite(SOCKET_4, LOW);
}

/*
 * Act on message arrived from MQTT
 * if received from MQTT switch pins and publish the pin status
 * if reading the pins statuses just switch pins (useful when device just got connected)
 */
void callback(char* top, byte* payload, unsigned int length) {
  String topic = top;
  String msg = String((char*)payload);
  msg = msg.substring(0, length);
  Serial.println("Received message from the broker:\t=" + msg + "\tin topic: " + topic);
  topic.remove(0, DEVICE_NAME.length() + 1);
  if (topic.startsWith(SUBTOPIC_IN)) {
    topic.remove(0, SUBTOPIC_IN.length() + 1);
    String element = topic.substring(0, topic.indexOf("/"));
    if(element.equalsIgnoreCase("DHT")){
      readDHT = true;
      dhtReadTries = 0;
    } else if(element.equalsIgnoreCase(SUBTOPIC_DESK)){
      int newHeight = msg.toInt();
      if(newHeight > 0){
        setHeight = true;
        deskHeight = newHeight;
        deskChangeTries = 0;
      }
    } else {
      int port = element.toInt();
      actOnCommand(port, msg);
      String publishTopic = PUBLISH_TOPIC + port;
      client.publish(publishTopic.c_str(), msg.c_str(), true);
    }
  } else if (topic.startsWith(SUBTOPIC_OUT)) {
    topic.remove(0, SUBTOPIC_OUT.length() + 1);
    int port = topic.substring(0, topic.indexOf("/")).toInt();
    if(port >0){
      actOnCommand(port, msg);
    }
  }

  if (((String)top).equalsIgnoreCase(TOPIC_PC + "/" + SUBTOPIC_IN)) {
    if(msg.equalsIgnoreCase("ON")){
        turnOnPc();
      }
  }
}

/*
 * Turn ON or OFF the device
 */
void actOnCommand(int port, String msg){
  if(msg.equalsIgnoreCase("ON")){
    digitalWrite(port, LOW);
  } else if(msg.equalsIgnoreCase("OFF")){
    digitalWrite(port, HIGH);
//  } else{
//    analogWrite(port, msg.toInt());
  }
}

/*
 * Read DHT11 values and if successfull publish to the MQTT broker
 */
void getDht(){
  if (currentMillis - previousReadMillis < dhtInterval) {
    return;
  }
  if(dhtReadTries > MAX_DHT_READ_TRIES){
    readDHT = false;
    String publishTopic = PUBLISH_TOPIC + DHT_ + "/";
    client.publish((publishTopic + DHT_HUM).c_str(), DHT_ERR1.c_str(), true);
    client.publish((publishTopic + DHT_TEMP).c_str(), DHT_ERR1.c_str(), true);
    return;
  }
  dhtReadTries++;
  previousReadMillis = currentMillis;
  Serial.println("reading DHT");
  int chk = DHT.read11(DHT11_PIN);
  String hum = "";
  String temp = "";
  Serial.print("DHT: ");
  switch (chk){
    case DHTLIB_OK:
      Serial.println("OK");
      readDHT = false;
      hum = String(DHT.humidity, 1).c_str();
      temp = String(DHT.temperature, 1).c_str();
      break;
    case DHTLIB_ERROR_CHECKSUM:
      Serial.println("Checksum error");
      break;
    case DHTLIB_ERROR_TIMEOUT:
      Serial.println("Time out error, \t");
      break;
    default:
      Serial.println("Unknown error, \t");
      break;
  }
  if(chk != DHTLIB_OK){
    Serial.println("Could not read DHT values");
  } else{
    Serial.println("Temp: " + temp + ", hum: " + hum);
    String publishTopic = PUBLISH_TOPIC + DHT_ + "/";
    client.publish((publishTopic + DHT_HUM).c_str(), hum.c_str(), true);
    client.publish((publishTopic + DHT_TEMP).c_str(), temp.c_str(), true);
  }
}

/*
 * Move the desk up or down
 */
void setDeskHeight(){
  if (currentMillis - previousUltrasonicReadMillis < ultrasonicInterval) {
    return;
  }
  if(deskChangeTries > MAX_DESK_CHANGE_TRIES){
    setHeight = false;
    digitalWrite(DESK_UP, LOW);
    digitalWrite(DESK_DOWN, LOW);
    client.publish((PUBLISH_TOPIC + SUBTOPIC_DESK).c_str(), DESK_ERR1.c_str(), true);
    return;
  }
  Serial.println("Setting desk height at " + String(deskHeight));
  deskChangeTries++;
  previousUltrasonicReadMillis = currentMillis;
  digitalWrite(ULTRASONIC_TRIG, LOW);
  delayMicroseconds(2);
  digitalWrite(ULTRASONIC_TRIG, HIGH);
  delayMicroseconds(10);
  digitalWrite(ULTRASONIC_TRIG, LOW);
  long ultrasonicDuration = pulseIn(ULTRASONIC_ECHO, HIGH);
  int distance = ultrasonicDuration*0.034/2;
  Serial.println("Distance: " + String(distance));

  if((distance > (deskHeight - DISTANCE_DELTA)) && (distance < (deskHeight + DISTANCE_DELTA))){
    Serial.println("Desk set at " + String(deskHeight));
    setHeight = false;
    digitalWrite(DESK_UP, LOW);
    digitalWrite(DESK_DOWN, LOW);
    String height = String(deskHeight);
    client.publish((PUBLISH_TOPIC + SUBTOPIC_DESK).c_str(), height.c_str(), true);
    return;
  }

  if(distance > deskHeight){
    Serial.println("Going down");
    digitalWrite(DESK_DOWN, LOW);
    digitalWrite(DESK_UP, LOW);
    digitalWrite(DESK_DOWN, HIGH);
    delay(50);
  } else {
    Serial.println("Going up");
    digitalWrite(DESK_UP, LOW);
    digitalWrite(DESK_DOWN, LOW);
    digitalWrite(DESK_UP, HIGH);
    delay(50);
  }
}

void turnOnPc(){
  Serial.println("Turning on the pc");
  digitalWrite(PC_BUTTON, LOW);
  delay(100);
  digitalWrite(PC_BUTTON, HIGH);
}
