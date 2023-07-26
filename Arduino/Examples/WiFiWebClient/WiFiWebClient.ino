#include <WiFiS3.h>
#include "arduino_secrets.h"

///////please enter your sensitive data in the Secret tab/arduino_secrets.h
char ssid[] = SECRET_SSID;        // your network SSID (name)
char pass[] = SECRET_PASS;    // your network password (use for WPA, or use as key for WEP)
int keyIndex = 0;            // your network key index number (needed only for WEP)

int status = WL_IDLE_STATUS;
char server[] = "192.168.171.2";    // WebSocket server address

WiFiClient wifiClient;

/* -------------------------------------------------------------------------- */
void setup() {
/* -------------------------------------------------------------------------- */
  // Initialize serial and wait for port to open:
  Serial.begin(9600);
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

  // Attempt to connect to WiFi network:
  while (status != WL_CONNECTED) {
    Serial.print("Attempting to connect to SSID: ");
    Serial.println(ssid);
    // Connect to WPA/WPA2 network. Change this line if using open or WEP network:
    status = WiFi.begin(ssid, pass);

    // Wait 10 seconds for connection:
    delay(3000);
  }

  printWifiStatus();

  Serial.println("\nStarting connection to server...");
  // if you get a connection, report back via serial:
  if (wifiClient.connect(server, 8000)) {
    Serial.println("connected to server");
    // Make a HTTP request:
    // wifiClient.println("GET /tables/list/ HTTP/1.1");
    wifiClient.println("GET /tables/list/ HTTP/1.1");
    wifiClient.println("Host: 192.168.171.2");
    wifiClient.println("Connection: close");
    wifiClient.println();
  }
}


/* just wrap the received data up to 80 columns in the serial print*/
/* -------------------------------------------------------------------------- */
void read_response() {
/* -------------------------------------------------------------------------- */  
  uint32_t received_data_num = 0;
  while (wifiClient.available()) {
    /* actual data reception */
    char c = wifiClient.read();
    /* print data to serial port */
    Serial.print(c);
    /* wrap data to 80 columns*/
    received_data_num++;
    if(received_data_num % 80 == 0) { 
      Serial.println();
    }
  }  
}

/* -------------------------------------------------------------------------- */
void loop() {
/* -------------------------------------------------------------------------- */
  // Maintain the WebSocket connection
  read_response();

  // if the server's disconnected, stop the client:
  if (!wifiClient.connected()) {
    Serial.println();
    Serial.println("disconnecting from server.");
    wifiClient.stop();

    // do nothing forevermore:
    while (true);
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
