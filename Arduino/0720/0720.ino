#include <ESP8266WiFi.h>

#ifndef STASSID
#define STASSID "MULTI_GUEST"
#define STAPSK  "guest1357"
#endif

const char* ssid     = STASSID;
const char* password = STAPSK;

void setup() {
  Serial.begin(115200);

  // IPAddress ip (70, 12, 246, 143); //내가 원하는 IP
  // IPAddress gateway (70, 12, 240, 1);
  // IPAddress subnet (255, 255, 248, 0);
  // IPAddress primaryDNS (168, 126, 63, 1);
  // IPAddress secondaryDNS (168, 126, 63, 2);

  // WiFi.config (ip, gateway, subnet); //내가 원하는 설정 반영
  // WiFi.mode(WIFI_STA);
  WiFi.begin(ssid, password);

  while (WiFi.status() != WL_CONNECTED) {
    delay(500);
    Serial.print(".");
  }

  Serial.println("");
  Serial.println("WiFi connected");
  Serial.println("IP address: ");
  Serial.println(WiFi.localIP()); //예상되는 결과 : 70.12.246.143
}

void loop() {
 
}