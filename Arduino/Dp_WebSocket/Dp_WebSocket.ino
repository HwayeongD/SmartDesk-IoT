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

#define TABLE_ID 15

///////please enter your sensitive data in the Secret tab/arduino_secrets.h
/////// WiFi Settings ///////
char ssid[] = SECRET_SSID;
char pass[] = SECRET_PASS;

char serverAddress[] = "i9a301.p.ssafy.io";  // server address
int port = 8080;

WiFiClient wifi;
WebSocketClient client = WebSocketClient(wifi, serverAddress, port);
int status = WL_IDLE_STATUS;

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

  Serial.println("starting WebSocket client");
  client.begin("/ws/chat");
  client.beginMessage(TYPE_TEXT);
  client.print("Hand Shake Test");
  client.endMessage();
  delay(100);
}

void loop() {

  while (client.connected()) {
    
    Serial.print("Send Table ID : ");
    Serial.print(TABLE_ID);


    // send a hello #
    client.beginMessage(TYPE_TEXT);
    client.print("Send Table ID : ");
    client.print(TABLE_ID);
    client.print(", ");
    client.print("80");
    
    client.endMessage();
    delay(50);
    // increment count for next message

    // check if a message is available to be received
    int messageSize = client.parseMessage();
    Serial.println(messageSize);
    if (messageSize > 0) {
      Serial.println("Received a message:");
      Serial.println(client.readString());
    }

    // wait 5 seconds
    delay(3000);
  }

  Serial.println("disconnected");
}
