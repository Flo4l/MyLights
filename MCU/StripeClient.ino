#include <ESP8266WiFi.h>

const char* ssid     = "AndroidAP_SYN";
const char* password = "12345679";
const char* deviceName = "NodeMCU";
const int port = 8881;

WiFiServer server(port);

String readInput() {
  
  String input = "";
  WiFiClient client = server.available();
  while(client.connected()) {
    while(client.available()) {
      char c = client.read();
      //Serial.print(c);
      input += c;
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
  Serial.print("Connecting to ");
  Serial.println(ssid);
  WiFi.begin(ssid, password);
  while (WiFi.status() != WL_CONNECTED) 
  {
    delay(500);
    Serial.print(".");
  }
  Serial.println("");
  Serial.println("WiFi connected.");
  Serial.println("IP address: ");
  Serial.println(WiFi.localIP());
  server.begin();
}

void loop() {
  String input = readInput();
  if(input.length() > 0) {
    Serial.println(input);
  }
}
