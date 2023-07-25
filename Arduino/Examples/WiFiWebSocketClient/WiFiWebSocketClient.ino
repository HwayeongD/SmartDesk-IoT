#include <WiFiS3.h>
#include <WebSocketsClient.h>
// #include "arduino_secrets.h"

#define SECRET_SSID "HJC"
#define SECRET_PASS "76038169"

///////please enter your sensitive data in the Secret tab/arduino_secrets.h
char ssid[] = SECRET_SSID;        // your network SSID (name)
char pass[] = SECRET_PASS;    // your network password (use for WPA, or use as key for WEP)
int keyIndex = 0;            // your network key index number (needed only for WEP)

int status = WL_IDLE_STATUS;
char server[] = "192.168.171.2";    // WebSocket server address
int port = 8080;

WiFiClient wifiClient;
WebSocketsClient webSocket;

/* -------------------------------------------------------------------------- */
void setup() {
/* -------------------------------------------------------------------------- */
  // Initialize serial and wait for port to open:
  Serial.begin(9600);

  wifiInit();

  // Attempt to connect to WiFi network:
  while (status != WL_CONNECTED) {
    Serial.print("Attempting to connect to SSID: ");
    Serial.println(ssid);
    // Connect to WPA/WPA2 network. Change this line if using open or WEP network:
    status = WiFi.begin(ssid, pass);
    
    delay(3000);
  }
  Serial.println("Connected to WIFI");

  printWifiStatus();

  // Connect to the WebSocket server
  webSocket.begin("echo.websocket.org", 80, "/");
  // webSocket.begin("192.168.171.2", 8080, "/");
  delay(3000);
  // Set up event handler for WebSocket events
  webSocket.onEvent(webSocketEvent);
}

/* -------------------------------------------------------------------------- */
void loop() {
/* -------------------------------------------------------------------------- */
  // Maintain the WebSocket connection
  webSocket.loop();
}

/* -------------------------------------------------------------------------- */
void webSocketEvent(WStype_t type, uint8_t *payload, size_t length) {
/* -------------------------------------------------------------------------- */
  switch (type) {
    case WStype_DISCONNECTED:
      Serial.println("Disconnected from WebSocket server");
      break;
    case WStype_CONNECTED:
      Serial.println("Connected to WebSocket server");
      webSocket.sendTXT("7603");
      break;
    case WStype_TEXT:
      // Handle incoming text data from the WebSocket server
      Serial.print("Received data: ");
      for (size_t i = 0; i < length; i++) {
        Serial.print((char)payload[i]);
      }
      Serial.println();
      break;
  }
}

/* -------------------------------------------------------------------------- */
void printWifiStatus() {
/* -------------------------------------------------------------------------- */
  // Print the SSID of the network you're attached to:
  Serial.print("SSID: ");
  Serial.println(WiFi.SSID());

  // Print your board's IP address:
  IPAddress ip = WiFi.localIP();
  Serial.print("IP Address: ");
  Serial.println(ip);

  // Print the received signal strength:
  long rssi = WiFi.RSSI();
  Serial.print("Signal strength (RSSI):");
  Serial.print(rssi);
  Serial.println(" dBm");
}


void wifiInit(){
  while (!Serial) {
    ; // wait for serial port to connect. Needed for native USB port only
  }

  // Check for the WiFi module:
  if (WiFi.status() == WL_NO_MODULE) {
    Serial.println("Communication with WiFi module failed!");
    // Don't continue
    while (true);
  }

  String fv = WiFi.firmwareVersion();
  if (fv < WIFI_FIRMWARE_LATEST_VERSION) {
    Serial.println("Please upgrade the firmware");
  }

}