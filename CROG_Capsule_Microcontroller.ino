#include <ESP8266WiFi.h>
#include <FirebaseArduino.h>
#include <dht.h>

#define FIREBASE_HOST "capsulecontroller-default-rtdb.firebaseio.com"
#define FIREBASE_AUTH "BEkACi1sJUBchc9nMIF5mcOxjfyR5qjieg5pUTen"
#define WIFI_SSID "SOEGIARTO"
#define WIFI_PASSWORD "soegiarto45"


int led = D4;
int pinDHT = A0;
int pintu = D2;
int fireStatus;
int doorStatus;

dht DHT;

void setup() {

// Setting Output Pins
  Serial.begin(9600);
  delay(1000);
  pinMode(LED_BUILTIN, OUTPUT);      
  pinMode(led, OUTPUT);
  pinMode(pintu,OUTPUT);

//Setting Up Wi-Fi Connection                 
  WiFi.begin(WIFI_SSID, WIFI_PASSWORD);
  Serial.print("Connecting to ");
  Serial.print(WIFI_SSID);
  while (WiFi.status() != WL_CONNECTED) {
    Serial.print(".");
    delay(500);
  }
  Serial.println();
  Serial.print("Connected to ");
  Serial.println(WIFI_SSID);
  Serial.print("IP Address is : ");
  Serial.println(WiFi.localIP());
  Firebase.begin(FIREBASE_HOST, FIREBASE_AUTH);

  Firebase.setInt("BARCODE PIN",12345678);
  Firebase.setInt("DOOR LOCK",0);
  Firebase.setString("LAMP STATUS", "OFF");
  Firebase.setFloat("ROOM TEMPERATURE", 0);
  //Firebase.setFloat("ROOM HUMIDITY", 0);
  delay(2000);
}

void loop() {
// UNLOCK/LOCK DOOR
  doorStatus = Firebase.getInt("DOOR LOCK");
  if (doorStatus == 1){
    digitalWrite(pintu,HIGH);
    DoorUnlock();
  }
  else{
    digitalWrite(pintu,LOW);
  }  
}

void DoorUnlock(){
// Reading DHT + Uploading Data to Firebase
  DHT.read11(pinDHT);
  Firebase.setFloat("ROOM TEMPERATURE", DHT.temperature);
  //Firebase.setFloat("ROOM HUMIDITY", DHT.humidity);
  delay(1000);

  
//Reading Firebase + LED Controlling
  fireStatus = Firebase.getInt("LAMP STATUS");
  if (fireStatus == 1) {
    Serial.println("Led Turned ON");                         
    digitalWrite(LED_BUILTIN, LOW);
    digitalWrite(led, HIGH);
  } 
  else if (fireStatus == 0) {
    Serial.println("Led Turned OFF");
    digitalWrite(LED_BUILTIN, HIGH);
    digitalWrite(led, LOW);
  }
  else {
    Serial.println("Wrong Credential! Please send ON/OFF");
  }
}
