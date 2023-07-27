// 참조
#include <SoftwareSerial.h>
// #include <MsTimer2.h>

// 상수
#define Dir1Pin 3
#define Dir2Pin 5
#define LMButton 7

bool state = 0;

void setup() 
{
  // 핀 초기화
  pinMode(Dir1Pin, OUTPUT);
  pinMode(Dir2Pin, OUTPUT);
  pinMode(LMButton, INPUT_PULLUP);
  
  // 시리얼 초기화
  Serial.begin(9600);
  
}

// 루프
void loop() 
{
  // 버튼 상태 읽기
  int buttonState = digitalRead(LMButton);
  
  // 버튼이 눌렸는지 확인
  if (buttonState == LOW) {
    // 버튼이 눌리면 모터 방향 전환
    Serial.print("1");
    state = !state;
    delay(200); 
  }

  // 모터 방향 설정
  if (state == 0) {
   //Serial.print("ok");
    digitalWrite(Dir1Pin, HIGH);
    digitalWrite(Dir2Pin, LOW);
    //delay(1000);
  } else {
    //Serial.print("no");
    digitalWrite(Dir1Pin, LOW);
    digitalWrite(Dir2Pin, HIGH);
    //delay(1000);
  }
}
