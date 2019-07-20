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

const char* WIFI_SSID     = "TP-LINK_5DBD15";
const char* WIFI_PASSWORD = ""; //WIFI password here
const char* MQTT_SERVER = "192.168.1.101";
const int MQTT_PORT = 1883;
const String DEVICE_NAME = "listwa1";

const String MESSAGE_WELCOME = DEVICE_NAME + " connected";
const String MESSAGE_GOODBYE = DEVICE_NAME + " disconnected";
const String TOPIC_DEVICES = "devices/" + DEVICE_NAME;
const String SUBTOPIC_IN = "in";
const String SUBTOPIC_OUT = "out";
const String PUBLISH_TOPIC = DEVICE_NAME + "/" + SUBTOPIC_OUT + "/";
const String SENSOR_HUM = "hum";
const String SENSOR_TEMP = "temp";
const String SENSOR_WEATHER = "weather";

const String SUBSCRIBE_TOPICS[] = {
              DEVICE_NAME + "/" + SUBTOPIC_IN + "/#"
            };

dht DHT;
#define DHT11_PIN 16//D0
long previousMillis = -700000;
const long dhtInterval = 600000;

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
  if(checkWiFi()){
    checkMQTT();
  }
  if(wifiRetryCount == 5 || mqttRetryCount == 5){
    activateOfflineMode();
  } else {
    client.loop();
    updateDht();
  }
}

/*
 * Set up device pins
 */
void setupPins(){
  pinMode(4, OUTPUT);//D2
//  pinMode(5, OUTPUT);//D1
  pinMode(12, OUTPUT);//D6
  pinMode(13, OUTPUT);//D7
  pinMode(14, OUTPUT);//D5
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
    client.publish(TOPIC_DEVICES.c_str(), MESSAGE_WELCOME.c_str());
  }
}

/*
 * Connect to the MQTT broker
 */
boolean connectMQTTClient(){
  Serial.println("Connecting to MQTT...");
  if (client.connect(DEVICE_NAME.c_str(), TOPIC_DEVICES.c_str(), 0, true, MESSAGE_GOODBYE.c_str())){
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
  digitalWrite(4, HIGH);//D2
  digitalWrite(12, HIGH);//D6
  digitalWrite(13, HIGH);//D7
  digitalWrite(14, HIGH);//D5
}

/*
 * Act on message arrived from MQTT
 * if received from MQTT switch pins and publish the pin status
 * if reading the pins statuses just switch pins
 */
void callback(char* top, byte* payload, unsigned int length) {
  String topic = top;
  String msg = String((char*)payload);
  msg = msg.substring(0, length);
  Serial.println("Received message from the broker:\t=" + msg + "\tin topic: " + topic);
  topic.remove(0, DEVICE_NAME.length() + 1);
  if (topic.startsWith(SUBTOPIC_IN)) {
    topic.remove(0, SUBTOPIC_IN.length() + 1);
    int port = topic.substring(0, topic.indexOf("/")).toInt();
    actOnCommand(port, msg);
    String publishTopic = PUBLISH_TOPIC + port;
    client.publish(publishTopic.c_str(), msg.c_str(), true);
  } else if (topic.startsWith(SUBTOPIC_OUT)) {
    topic.remove(0, SUBTOPIC_OUT.length() + 1);
    int port = topic.substring(0, topic.indexOf("/")).toInt();
    actOnCommand(port, msg);
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
 * Read DHT11 values every 10 minutes and publish to the MQTT broker
 */
void updateDht(){
  unsigned long currentMillis = millis();
  if (currentMillis - previousMillis < dhtInterval) {
    return;
  }
  previousMillis = currentMillis;
  int chk = DHT.read11(DHT11_PIN);
  String hum = String(DHT.humidity, 1).c_str();
  String temp = String(DHT.temperature, 1).c_str();
  Serial.print("DHT: ");
  switch (chk){
    case DHTLIB_OK:
      Serial.println("OK");
      break;
    case DHTLIB_ERROR_CHECKSUM:
      Serial.println("Checksum error");
      hum = "N/A";
      temp = "N/A";
      break;
    case DHTLIB_ERROR_TIMEOUT:
      Serial.println("Time out error, \t");
      hum = "N/A";
      temp = "N/A";
      break;
    default:
      Serial.println("Unknown error, \t");
      hum = "N/A";
      temp = "N/A";
      break;
  }
  String publishTopic = PUBLISH_TOPIC + SENSOR_WEATHER + "/";
  client.publish((publishTopic + SENSOR_HUM).c_str(), hum.c_str(), true);
  client.publish((publishTopic + SENSOR_TEMP).c_str(), temp.c_str(), true);
}