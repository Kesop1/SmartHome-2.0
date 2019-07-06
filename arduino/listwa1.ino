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
dht DHT;

const char* ssid     = "UPC6354859";
const char* password = "AHBTYCHS";
const char* mqttServer = "192.168.0.21";
const int mqttPort = 1883;
const char* mqttUser = "listwa1";
const char* mqttPassword = "password";
const char* deviceName = "listwa1";

#define DHT11_PIN 16//D0

const String TOPIC_DEVICE = "listwa1";
const String SUBTOPIC_IN = "in";
const String SUBTOPIC_OUT = "out";
const String PUBLISH_TOPIC = TOPIC_DEVICE + "/" + SUBTOPIC_OUT + "/";
const String SENSOR_HUM = "hum";
const String SENSOR_TEMP = "temp";
const String SENSOR_WEATHER = "weather";

const String SUBSCRIBE_TOPICS[] = {
              TOPIC_DEVICE + "/" + SUBTOPIC_IN + "/#"
            };

long previousMillis = -700000;
const long dhtInterval = 600000;

WiFiClient espClient;
PubSubClient client(espClient);

void setup() {
  Serial.begin(115200);
  delay(10);
  setupPins();
  connectWiFi();
  connectMQTT();  
  subscribeMQTT();
  client.publish("main", deviceName);
}

void loop() {
  client.loop();
  updateDht();
}

void setupPins(){
  pinMode(4, OUTPUT);//D2
//  pinMode(5, OUTPUT);//D1
  pinMode(12, OUTPUT);//D6
  pinMode(13, OUTPUT);//D7
  pinMode(14, OUTPUT);//D5
}

void connectWiFi(){
  Serial.print("Connecting to ");
  Serial.println(ssid);
  WiFi.hostname(deviceName);
  WiFi.begin(ssid, password);
 
  while (WiFi.status() != WL_CONNECTED) {
    delay(500);
    Serial.print(".");
  }
  Serial.println("");
  Serial.println(" connected");
 
  // Print the IP address
  Serial.print("IP address: ");
  Serial.println(WiFi.localIP());
}

void connectMQTT() {
  client.setServer(mqttServer, mqttPort);
  client.setCallback(callback);
  
  while (!client.connected()) {
    Serial.println("Connecting to MQTT...");
    if (client.connect(deviceName, mqttUser, mqttPassword )) {
      Serial.println("connected");  
    } else {
      Serial.print("failed with state ");
      Serial.print(client.state());
      delay(2000);
    }
  }
}

void subscribeMQTT() {
  Serial.println("Subscribing to the topics:");
  for(int i=0; i<sizeof(SUBSCRIBE_TOPICS) / sizeof(SUBSCRIBE_TOPICS[0]); i++){
    Serial.println(SUBSCRIBE_TOPICS[i]);
    client.subscribe(SUBSCRIBE_TOPICS[i].c_str());
  }
  Serial.println("Done");
}

void callback(char* top, byte* payload, unsigned int length) {
  String topic = top;
  String msg = String((char*)payload);
  msg = msg.substring(0, length);
  Serial.println("Received message from the broker:\t=" + msg + "\tin topic: " + topic);
  topic.remove(0, TOPIC_DEVICE.length() + 1);
  if (topic.startsWith(SUBTOPIC_IN)) {
    topic.remove(0, SUBTOPIC_IN.length() + 1);
    int port = topic.substring(0, topic.indexOf("/")).toInt();
    actOnCommand(port, msg);
  } 
}

void actOnCommand(int port, String msg){
  if(msg.equalsIgnoreCase("ON")){
    Serial.println("ONNN");
    digitalWrite(port, HIGH);
  } else if(msg.equalsIgnoreCase("OFF")){
    Serial.println("OFFF");
    digitalWrite(port, LOW);
  } else{
    analogWrite(port, msg.toInt());
  }
  String publishTopic = PUBLISH_TOPIC + port;
  client.publish(publishTopic.c_str(), msg.c_str(), true);
}

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
  String publishTopic = PUBLISH_TOPIC + SENSOR_WEATHER;
  client.publish(publishTopic.c_str(), (SENSOR_HUM + "=" + hum).c_str(), true);
  client.publish(publishTopic.c_str(), (SENSOR_TEMP + "=" + temp).c_str(), true);
}
