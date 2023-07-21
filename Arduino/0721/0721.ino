#include <ESP8266WiFi.h>
#include <WiFiClient.h>

const char* ssid = "MULTI_GUEST";
const char* password = "guest1357";
const char* serverURL = "http://your-server-url.com/api/data"; // Replace with your server URL

void setup() {
  Serial.begin(115200);
  WiFi.begin(ssid, password);

  while (WiFi.status() != WL_CONNECTED) {
    delay(1000);
    Serial.println("Connecting to WiFi...");
  }

  Serial.println("Connected to WiFi!");
}

void loop() {
  if (WiFi.status() == WL_CONNECTED) {
    WiFiClient client;

    Serial.print("Connecting to server: ");
    Serial.println(serverURL);

    if (client.connect(serverURL, 80)) {
      Serial.println("Connected to server!");
      client.print("GET / HTTP/1.1\r\n");
      client.print("Host: ");
      client.print(serverURL);
      client.print("\r\n\r\n");

      while (client.available()) {
        String line = client.readStringUntil('\r');
        Serial.print(line);
      }

      client.stop();
    } else {
      Serial.println("Connection to server failed.");
    }

    delay(5000); // Wait for 5 seconds before the next request
  }
}
