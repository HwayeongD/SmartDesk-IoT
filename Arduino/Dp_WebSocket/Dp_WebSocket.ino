/*
  Simple WebSocket client for ArduinoHttpClient library
  Connects to the WebSocket server, and sends a hello
  message every 5 seconds

  created 28 Jun 2016
  by Sandeep Mistry
  modified 22 Jan 2019
  by Tom Igoe

  this example is in the public domain
*/
/*
  책상에서 필요한 데이터
    - 닉네임, 개인 선호 높이, 현재 책상 높이, 부서, 상태
  DP에 표시할 데이터
    - 닉네임, 현재 책상 높이, 부서, 상태
  모터 제어 시 필요한 데이터
    - 현재 책상 높이, 개인 선호 높이
*/
#include <ArduinoHttpClient.h>
#include <WiFiS3.h>
#include "arduino_secrets.h"

#include "Arduino_LED_Matrix.h"
ArduinoLEDMatrix matrix;

#define TABLE_ID 15;

///////please enter your sensitive data in the Secret tab/arduino_secrets.h
/////// WiFi Settings ///////
char ssid[] = SECRET_SSID;
char pass[] = SECRET_PASS;

char serverAddress[] = "hjj.kro.kr";  // server address
int port = 8081;

WiFiClient wifi;
WebSocketClient client = WebSocketClient(wifi, serverAddress, port);
int status = WL_IDLE_STATUS;
int count = 0;

void setup() {
  Serial.begin(9600);
  while ( status != WL_CONNECTED) {
    Serial.print("Attempting to connect to Network named: ");
    Serial.println(ssid);                   // print the network name (SSID);

    // Connect to WPA/WPA2 network:
    status = WiFi.begin(ssid, pass);
  }
  matrix.loadSequence(frames);
  matrix.begin();

  matrix.play(true);

  // print the SSID of the network you're attached to:
  Serial.print("SSID: ");
  Serial.println(WiFi.SSID());

  // print your WiFi shield's IP address:
  IPAddress ip = WiFi.localIP();
  Serial.print("IP Address: ");
  Serial.println(ip);
}

void loop() {
  Serial.println("starting WebSocket client");
  client.begin();

  while (client.connected()) {
    Serial.println("D,%d", TABLE_ID);

    Serial.println("Send Table ID, trial : ", count);

    // send a hello #
    client.beginMessage(TYPE_TEXT);
    client.println("D");
    client.print(count);
    client.endMessage();
    delay(50);
    // increment count for next message
    count++;

    // check if a message is available to be received
    int messageSize = client.parseMessage();

    if (messageSize > 0) {
      Serial.println("Received a message:");
      Serial.println(client.readString());
    }

    // wait 5 seconds
    delay(3000);
  }

  Serial.println("disconnected");
}
