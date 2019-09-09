#include <ESP8266WiFi.h>
#include <ESP8266HTTPClient.h>
#include <ArduinoJson.h>
#include <Vector.h>

String serverAddress = "192.168.43.221";
HTTPClient http;

//Wifi access
const char* ssid     = "AndroidAP_SYN";
const char* password = "12345679";
const char* deviceName = "NodeMCU";

//Port and Server
const int port = 8881;
WiFiServer server(port);

//Number of colors that can be stored
const int MAX_COLORS = 12;
//Pin definitions
const int redPin = D1;
const int greenPin = D2;
const int bluePin = D3;

//Saved the RGB values of a color
class Color {
  public:
    byte red;
    byte green;
    byte blue;
};
Color colorArray[MAX_COLORS];
Vector<Color> colors;

//Display color mode [s]ingle,[m]ultiple, [f]ading
char colorMode = 's';
//Mow many seconds until color is changed
unsigned int secondsToNextColor = 1;
//Next color to display
int nextColor = 0;
//Last time in millis() the Color changed
unsigned long lastUpdated = 0;
//Last time in millis() the fading color was updated
unsigned long fadeUpdated = 0;
//Values for fading color to display
double fadeRed = 0;
double fadeGreen = 0;
double fadeBlue = 0;
//Value that gets added to the fading color values each millisecond
double stepRed = 0;
double stepGreen = 0;
double stepBlue = 0;

//ID of Group of the Stripe
byte groupId;

//Writes colors to pins
void writePins(Color color) {
  writePins(color.red,
            color.green,
            color.blue);
}

//Writes colors to pins
void writePins(byte red, byte green, byte blue) {
  analogWrite(redPin, red);
  analogWrite(greenPin, green);
  analogWrite(bluePin, blue);
}

//Displays first color of vector
void displaySingleColor() {
  writePins(colors.at(0));
}

//Switches colors of vector after secondsToNextColor
void displayMultipleColors() {
  if(millis() - lastUpdated >= secondsToNextColor * 1000) {
    writePins(colors.at(nextColor));
    lastUpdated = millis();
    nextColor = (nextColor + 1) % colors.size();
  }
}

//Fades colors of vector. Next total color after secondsToNextColor
void displayFadingColors() {

  //Update fade color Values and display them
  unsigned long now = millis();
  fadeRed += stepRed * (now - fadeUpdated);
  fadeGreen += stepGreen * (now - fadeUpdated);
  fadeBlue += stepBlue * (now - fadeUpdated);
  fadeUpdated = now;
  writePins(fadeRed, fadeGreen, fadeBlue);

  //Calculate stats for next color
  if(now - lastUpdated >= secondsToNextColor * 1000) {
    nextColor = (nextColor + 1) % colors.size();
    calcColorFadeStats();
    lastUpdated = now;
  }
}

//Calculates Values for color fading
void calcColorFadeStats() {
  double redDiff = colors.at(nextColor).red - fadeRed;
  stepRed = redDiff / (secondsToNextColor * 1000);
  double greenDiff = colors.at(nextColor).green - fadeGreen;
  stepGreen = greenDiff / (secondsToNextColor * 1000);
  double blueDiff = colors.at(nextColor).blue - fadeBlue;
  stepBlue = blueDiff / (secondsToNextColor * 1000);
}

//Sets up values after color change
void initColorStates() {
  nextColor = 0;
  lastUpdated = millis();
  if(colorMode == 'f') {
     calcColorFadeStats();
     fadeUpdated = millis();
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
    case 'f':
      displayFadingColors();
      break;
  }
}

//Converts String to a JSON oject and executes method based on its type
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

//Assignes values from a command type JSON object
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
  initColorStates();
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

//Sends a POST request to the Server for registering or updating the ip
boolean registerAtServer() {
  String url = "http://" + serverAddress + "/stripe/register";
  String params = "ip=" + WiFi.localIP().toString() + String("&mac=") + String(WiFi.macAddress());
  http.begin(url);
  http.addHeader("Content-Type", "application/x-www-form-urlencoded");
  int httpCode = http.POST(params);
  http.end();
  return httpCode == 200;
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

  //Connect to server
  Serial.print("Connecting to server: ");
  Serial.println(serverAddress);
  while(!registerAtServer()) {
    Serial.print(".");
    delay(500);
  }
  Serial.println("Server connected.");
  Serial.println("");

  //Setup storage and start server
  colors.setStorage(colorArray);
  server.begin();
}

void loop() {

  //Retrieve JSON String if there is any
  String input = readInput();

  //If String received print and interpret it
  if(input.length() > 0) {
    Serial.println(input);
    interpretJson(&input);
  }

  //Update the displayed color
  updateColor();
}
