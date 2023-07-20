#include <SoftwareSerial.h> 

int RX = 3;
int TX = 2;
SoftwareSerial ESP01(RX, TX); 

void setup() { 
  Serial.begin(74880);  //아두이노 우노 보드레이트 9600
  ESP01.begin(74880);   //와이파이 모듈 보드레이트 115200 / 74880
  ESP01.setTimeout(5000); 
  delay(1000);
  Serial.print("Start");
} 

void loop() { 
  if (Serial.available()){
    ESP01.write(Serial.read()); 
  } 
  if (ESP01.available()) { 
    Serial.write(ESP01.read()); 
  }
}