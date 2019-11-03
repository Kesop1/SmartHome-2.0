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
const String DEVICE_NAME = "listwa1";

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

const String SUBSCRIBE_TOPICS[] = {
              DEVICE_NAME + "/" + SUBTOPIC_IN + "/#",
              TOPIC_PC + "/" + SUBTOPIC_IN
            };

long currentMillis;

dht DHT;
#define DHT11_PIN 16//D0
long previousReadMillis = 0;
const long dhtInterval = 3000;
boolean dhtReadSuccessful = false;

const long DISTANCE_DELTA = 2;
long deskHeight = 0;
boolean heightSetSuccessful = true;
long previousUltrasonicReadMillis = 0;
const long ultrasonicInterval = 300;

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
  WiFi.mode(WIFI_STA);
  WiFi.setAutoConnect(false);
  setupPins();
  connectWiFi();
  connectMQTT();
}

/*
 * check connection status, connect if necessary
 * if unable to connect to WiFi or MQTT activate offline mode (all output pins ON)
 * if connected proceed with MQTT service and update DHT read-outs
 */
void loop() {
  currentMillis = millis();
  if(checkWiFi()){
    checkMQTT();
  }
  if(wifiRetryCount == 5 || mqttRetryCount == 5){
    activateOfflineMode();
  } else {
    client.loop();
    if (!dhtReadSuccessful) {
      getDht();
    }
    if(!heightSetSuccessful){
      setDeskHeight(deskHeight);
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
    connectWiFi();
    wifiRetryCount++;
    return false;
  }
}

/*
 * check MQTT connection status, connect if necessary
 */
void checkMQTT(){
  if (client.connected()) {
    mqttRetryCount = 0;
  } else {
    connectMQTT();
    mqttRetryCount++;
  }
}

/*
 * Connect to WiFi
 */
void connectWiFi(){
  Serial.print("Connecting to WIFI: ");
  Serial.println(WIFI_SSID);
  WiFi.hostname(DEVICE_NAME.c_str());
  WiFi.begin(WIFI_SSID, WIFI_PASSWORD);

  if (WiFi.status() != WL_CONNECTED) {
    delay(5000);
  } else {
    Serial.println("");
    Serial.println(" connected");

    // Print the IP address
    Serial.print("IP address: ");
    Serial.println(WiFi.localIP());
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
  Serial.println("Connecting to MQTT...");
  if (client.connect(DEVICE_NAME.c_str(), (DEVICE_NAME + "/" + SUBTOPIC_STATUS).c_str(), 0, true, MESSAGE_GOODBYE.c_str())){
    Serial.println(MESSAGE_WELCOME);
    return true;
  } else {
    Serial.print("failed with state ");
    Serial.println(client.state());
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
  digitalWrite(SOCKET_1, HIGH);
  digitalWrite(SOCKET_2, HIGH);
  digitalWrite(SOCKET_3, HIGH);
  digitalWrite(SOCKET_4, HIGH);
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
      dhtReadSuccessful = false;
    } else if(element.equalsIgnoreCase("desk")){
      int newHeight = msg.toInt();
      if(newHeight > 0){
        heightSetSuccessful = false;
        deskHeight = newHeight;
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
    digitalWrite(port, HIGH);
  } else if(msg.equalsIgnoreCase("OFF")){
    digitalWrite(port, LOW);
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
  previousReadMillis = currentMillis;
  Serial.println("reading DHT");
  int chk = DHT.read11(DHT11_PIN);
  String hum = "";
  String temp = "";
  Serial.print("DHT: ");
  switch (chk){
    case DHTLIB_OK:
      Serial.println("OK");
      dhtReadSuccessful = true;
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
void setDeskHeight(long height){
  if (currentMillis - previousUltrasonicReadMillis < ultrasonicInterval) {
    return;
  }
  Serial.println("Setting desk height at " + height);
  previousUltrasonicReadMillis = currentMillis;
  digitalWrite(ULTRASONIC_TRIG, LOW);
  delayMicroseconds(2);
  digitalWrite(ULTRASONIC_TRIG, HIGH);
  delayMicroseconds(10);
  digitalWrite(ULTRASONIC_TRIG, LOW);
  long ultrasonicDuration = pulseIn(ULTRASONIC_ECHO, HIGH);
  int distance = ultrasonicDuration*0.034/2;
  Serial.println("Distance: " + distance);

  if((distance > (height - DISTANCE_DELTA)) && (distance < (height + DISTANCE_DELTA))){
    Serial.println("Desk set at " + deskHeight);
    heightSetSuccessful = true;
    digitalWrite(DESK_UP, LOW);
    digitalWrite(DESK_DOWN, LOW);
    return;
  }

  if(distance > height){
    Serial.println("Going down");
    digitalWrite(DESK_UP, HIGH);
    digitalWrite(DESK_DOWN, LOW);
  } else {
    Serial.println("Going up");
    digitalWrite(DESK_DOWN, HIGH);
    digitalWrite(DESK_UP, LOW);
  }
}

void turnOnPc(){
  Serial.println("Turning on the pc");
  digitalWrite(PC_BUTTON, HIGH);
  delay(100);
  digitalWrite(PC_BUTTON, LOW);
}