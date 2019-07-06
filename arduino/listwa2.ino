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

#include <Arduino.h>
#include <ESP8266WiFi.h>
#include <PubSubClient.h>
#include <IRremoteESP8266.h>
#include <IRsend.h>
#include <dht.h>
dht DHT;

const char* ssid     = "UPC6354859";
const char* password = "AHBTYCHS";
const char* mqttServer = "192.168.0.21";
const int mqttPort = 1883;
const char* mqttUser = "listwa2";
const char* mqttPassword = "password";
const char* deviceName = "listwa2";

#define DHT11_PIN 16//D0

const uint16_t kIrLed = 4;  // ESP8266 GPIO pin to use. Recommended: 4 (D2).
IRsend irsend(kIrLed);  // Set the GPIO to be used to sending the message.

const String TOPIC_DEVICE = "listwa2";
const String SUBTOPIC_IN = "in";
const String SUBTOPIC_OUT = "out";
const String SUBTOPIC_IR = "ir";
const String PUBLISH_TOPIC = TOPIC_DEVICE + "/" + SUBTOPIC_OUT + "/";
const String IR_DEVICE_AMP = "amp";
const String IR_DEVICE_TV = "tv";
const String IR_DEVICE_VACUUM = "vacuum";
const String SENSOR_HUM = "hum";
const String SENSOR_TEMP = "temp";
const String SENSOR_WEATHER = "weather";

const String SUBSCRIBE_TOPICS[] = {
              TOPIC_DEVICE + "/" + SUBTOPIC_IN + "/#",
              TOPIC_DEVICE + "/" + SUBTOPIC_IR + "/#"
            };

uint16_t TV_ON[67] = {4500,4450, 550,1700, 550,1650, 550,1700, 600,550, 
                      550,600, 550,550, 600,550, 550,600, 550,1700, 
                      550,1700, 550,1650, 550,600, 550,600, 550,600, 
                      550,600, 550,550, 600,550, 600,1650, 550,600, 
                      550,600, 550,600, 550,550, 600,550, 550,600, 
                      550,1700, 550,600, 550,1650, 550,1700, 550,1700, 
                      550,1650, 600,1650, 550,1700, 550};

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
  irsend.begin();
}

void loop() {
  client.loop();
  updateDht();
}

void setupPins(){
//  pinMode(4, OUTPUT);//D2
  pinMode(5, OUTPUT);//D1
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
  } else if(topic.startsWith(SUBTOPIC_IR)){
    topic.remove(0, SUBTOPIC_IR.length() + 1);
    String device = topic.substring(0, topic.indexOf("/"));
    actOnIrCommand(device, msg);
  }
}

void actOnCommand(int port, String msg){
  if(msg.equalsIgnoreCase("ON")){
    digitalWrite(port, LOW);
  } else if(msg.equalsIgnoreCase("OFF")){
    digitalWrite(port, HIGH);
  } else{
    analogWrite(port, msg.toInt());
  }
  String publishTopic = PUBLISH_TOPIC + port;
  client.publish(publishTopic.c_str(), msg.c_str(), true);
}

void actOnIrCommand(String device, String msg){
  int repeat = 1;
  if(msg.indexOf("_") > 0){
    repeat = msg.substring(0, msg.indexOf("_")).toInt();
    msg.remove(0, msg.indexOf("_") + 1);
  }
  long signal = strtol((char*)msg.c_str(), NULL, 16);
  for(int i = 0; i<repeat; i++){
    if(IR_DEVICE_AMP.equals(device)){
      irsend.sendNEC(signal, 32);
    } else if(IR_DEVICE_TV.equals(device)){
      irsend.sendRaw(TV_ON, 67, 38);
    } else if(IR_DEVICE_VACUUM.equals(device)){
      irsend.sendNEC(signal, 32);
    }
    delay(100);
    irsend.sendNEC(0xFFFFFFFF, 32);
    delay(40);
  }
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
