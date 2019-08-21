#include <ESP8266WiFi.h>
#include <ArduinoJson.h>
#include <Vector.h>

const char* ssid     = "AndroidAP_SYN";
const char* password = "12345679";
const char* deviceName = "NodeMCU";
const int port = 8881;

const int redPin = D1;
const int greenPin = D2;
const int bluePin = D3;

const int MAX_COLORS = 12;
int currentColor = 0;
unsigned long lastUpdated = 0;

char colorMode = 's';
unsigned int secondsToNextColor = 1;
byte groupId;

WiFiServer server(port);

//Saved the RGB values of a color
class Color {
  public:
    byte red;
    byte green;
    byte blue;
};
Color colorArray[MAX_COLORS];
Vector<Color> colors;

//Writes colors to pins
void writePins(Color color) {
  analogWrite(redPin, color.red);
  analogWrite(greenPin, color.green);
  analogWrite(bluePin, color.blue);
}

//Writes first color in vector
void displaySingleColor() {
  writePins(colors.at(0));
}

//Switches colors in vector after secondsToNextColor
void displayMultipleColors() {
  if(millis() - lastUpdated >= secondsToNextColor * 1000) {
    currentColor = (currentColor + 1) % colors.size();
    writePins(colors.at(currentColor));
    lastUpdated = millis();
  }
}

//Invokes method to display color based on the mode
void updateColor() {
  switch (colorMode) {
  case 's':
    displaySingleColor();
    break;
  case 'm':
    displayMultipleColors();
    break;
  }
}

void interpretJson(String* jsonString) {
  //Convert String to JsonDocument
  const int capacity = JSON_OBJECT_SIZE(1)
                      + JSON_OBJECT_SIZE(4)
                      + JSON_ARRAY_SIZE(MAX_COLORS)
                      + MAX_COLORS * JSON_OBJECT_SIZE(3);
  DynamicJsonDocument doc(capacity);
  DeserializationError err=deserializeJson(doc, *jsonString);
  if(err) {
    Serial.println(err.c_str());
  }

  //Execute method based on type of JSON Object
  if(doc.containsKey("command")) {
    extractCommand(doc);
  }
}

void extractCommand(DynamicJsonDocument doc) {
  //Extract command values
  const char* c = doc["command"]["mode"];
  colorMode = c[0];
  secondsToNextColor = doc["command"]["secondsToNextColor"];
  groupId = doc["command"]["groupId"];
  JsonArray jsonColors = doc["command"]["colors"];

  //Add colors to vector
  colors.clear();
  for(int i = 0; i < jsonColors.size(); i++) {
    Color c;
    c.red = jsonColors[i]["red"];
    c.green = jsonColors[i]["green"];
    c.blue = jsonColors[i]["blue"];
    colors.push_back(c);
  }
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
  Serial.print("IP address: ");
  Serial.println(WiFi.localIP());
  Serial.println("");

  //Start server
  colors.setStorage(colorArray);
  server.begin();
}

void loop() {
  String input = readInput();
  if(input.length() > 0) {
    Serial.println(input);
    interpretJson(&input);
  }
  updateColor();
}
