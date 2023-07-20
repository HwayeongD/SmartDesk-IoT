#include <SoftwareSerial.h>
#include <ESP8266WiFi.h>
#include <WebSocketsClient.h>
#include <ArduinoJson.h>

// SoftwareSerial 객체를 생성하여 ESP8266과 시리얼 통신
SoftwareSerial espSerial(1, 0); // RX, TX

// Wi-Fi 연결 정보
const char* ssid = "HJC";
const char* password = "76038169";
const char* webSocketServerIP = "192.168.93.2"; // Django 서버의 IP 주소
const int webSocketServerPort = 8000; // Django 웹소켓 포트

WebSocketsClient webSocket;

void webSocketEvent(WStype_t type, uint8_t *payload, size_t length) {
  switch (type) {
    case WStype_DISCONNECTED:
      Serial.println("Disconnected");
      break;
    case WStype_CONNECTED:
      Serial.println("Connected");
      break;
    case WStype_TEXT:
      // 웹소켓으로 받은 JSON 데이터 파싱
      StaticJsonDocument<200> doc;
      DeserializationError error = deserializeJson(doc, payload, length);
      if (!error) {
        const int value = doc["value"];
        Serial.print("Received value: ");
        Serial.println(value);
      }
      break;
  }
}

void setup() {
  Serial.print(000);
  Serial.begin(9600);
  espSerial.begin(9600); // ESP8266과 시리얼 통신 속도 설정

  // Wi-Fi 연결
  Serial.print(111);
  WiFi.begin(ssid, password);
  Serial.print(222);
  while (WiFi.status() != WL_CONNECTED) {
    delay(1000);
    Serial.print(".@");
  }
  Serial.println("\nWiFi connected");

  // 웹소켓 서버 연결
  webSocket.begin(webSocketServerIP, webSocketServerPort);
  webSocket.onEvent(webSocketEvent);
}

void loop() {
  // ESP8266 시리얼 통신 처리
  while (espSerial.available()) {
    char c = espSerial.read();
    Serial.write(c); // 시리얼 모니터에 출력
  }

  webSocket.loop();

  if (webSocket.isConnected()) {
    // 아두이노에서 Django 서버로 데이터 요청
    StaticJsonDocument<100> doc;
    doc["command"] = "get_data";
    String payload;
    serializeJson(doc, payload);
    webSocket.sendTXT(payload);
  }

  delay(1000); // 1초에 한 번씩 데이터 요청
}