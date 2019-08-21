#include <ESP8266WiFi.h>

const char* ssid     = "AndroidAP_SYN";
const char* password = "12345679";
const char* deviceName = "NodeMCU";
const int port = 8881;

const int redPin = D1;
const int greenPin = D2;
const int bluePin = D3;

WiFiServer server(port);

//Saved the RGB values of a color
struct Color {
  byte red;
  byte green;
  byte blue;
};

//Writes colors to pins
void writePins(byte red, byte green, byte blue) {
  analogWrite(redPin, red);
  analogWrite(greenPin, green);
  analogWrite(bluePin, blue);
}

//Returns a String if one is received
String readInput() {
  String input = "";
  WiFiClient client = server.available();
  while(client.connected()) {
    while(client.available()) {
      input += (char) client.read();
    }
  }
  client.stop();
  return input;
}

void setup() {
  Serial.begin(115200);
  delay(1000);
  Serial.println("");
  Serial.println("");

  //Init PWM pins
  pinMode(redPin, OUTPUT);
  pinMode(greenPin, OUTPUT);
  pinMode(bluePin, OUTPUT);

  //Init WiFi connection
  Serial.print("Connecting to ");
  Serial.println(ssid);
  WiFi.begin(ssid, password);
  while (WiFi.status() != WL_CONNECTED) {
    delay(500);
    Serial.print(".");
  }
  Serial.println("");
  Serial.println("WiFi connected.");
  Serial.println("IP address: ");
  Serial.println(WiFi.localIP());

  //Start server
  server.begin();
}

void loop() {
  String input = readInput();
  if(input.length() > 0) {
    Serial.println(input);
  } 
}
