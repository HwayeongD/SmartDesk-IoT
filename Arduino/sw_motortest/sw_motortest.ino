// 참조
#include <SoftwareSerial.h>
#include <MsTimer2.h>

// 상수
#define Dir1Pin 3 // 초록색
#define Dir2Pin 5
#define UpBtn 7
#define DownBtn 8

#define LED 4

bool state = 0;
bool flag = 0;

unsigned long starttime = 0; // 모터 동작 시작 시간 기록
unsigned long standardtime = 5000; // 모터 동작 지속 시간 (5초)
unsigned long endtime = 180000; // 1분뒤에 모터길이 0으로 돌리기
unsigned long changetime = 0;
unsigned long savetime = 0;

void setup() 
{
  // 핀 초기화
  pinMode(Dir1Pin, OUTPUT);
  pinMode(Dir2Pin, OUTPUT);
  pinMode(UpBtn, INPUT_PULLUP);
  pinMode(DownBtn, INPUT_PULLUP);
  pinMode(LED, OUTPUT);
  
  // 시리얼 초기화
   Serial.begin(9600);
  
}

// 루프
void loop() 
{
  if(millis()>= endtime){
    digitalWrite(Dir1Pin, LOW); //내려가는거
    digitalWrite(Dir2Pin, HIGH);
  }

  // 버튼 상태 읽기
  int upbtnstate = digitalRead(UpBtn);
  int downbtnstate = digitalRead(DownBtn);
  
  // 버튼이 눌렸는지 확인
  if (upbtnstate == LOW) {
    // 버튼이 눌리면 모터 방향 전환
    Serial.print("1");
    //state = !state;
    if(flag == 0){
      starttime = millis();
      flag = 1;
    }
    //delay(200); 
  }

  if (downbtnstate == LOW) {
    // 버튼이 눌리면 모터 방향 전환
    Serial.print("1");
    if(flag==0){
      starttime = millis();
      flag = 1;
    }
    //state = !state;
    //delay(200); 
  }

  // 모터 방향 설정
  if (upbtnstate == 1 && downbtnstate == 1) {
    Serial.print("ok");
    digitalWrite(LED,HIGH);
    digitalWrite(Dir1Pin, LOW); // 멈춤
    digitalWrite(Dir2Pin, LOW);
    delay(100);
  } else {
    // Serial.print("no");
    if(upbtnstate == 0){
      digitalWrite(LED,LOW);
      digitalWrite(Dir1Pin, HIGH); //올라가는거
      digitalWrite(Dir2Pin, LOW);

      changetime = millis() - starttime;
      savetime = savetime + changetime;
      starttime = millis();

      
      if(savetime >= standardtime){
        digitalWrite(Dir1Pin, LOW);
        digitalWrite(Dir2Pin, LOW);
        flag = 0;
      }
      Serial.print("up");
      delay(100);
    }
    else if(downbtnstate == 0){
      digitalWrite(LED,LOW);
      digitalWrite(Dir1Pin, LOW); //내려가는거
      digitalWrite(Dir2Pin, HIGH);
      Serial.print("down");

      changetime = millis() - starttime;
      savetime = savetime - changetime;
      starttime = millis();

      delay(100);
    }
  }
  
}