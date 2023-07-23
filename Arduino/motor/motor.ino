// 참조
#include <SoftwareSerial.h>
#include <MsTimer2.h>

// 상수
#define Dir1Pin 3 // 초록색
#define Dir2Pin 5
#define UpBtn 8
#define DownBtn 7

#define LED 4

bool state = 0;
bool upstate = 0;
bool downstate = 0;
bool flag = 0;

// unsigned long firsttime = 0;
// //unsigned long starttime = 0; // 모터 동작 시작 시간 기록
// unsigned long standardtime = 5000; // 모터 동작 지속 시간 (5초)
// unsigned long endtime = 60000; // 1분뒤에 모터길이 0으로 돌리기
// unsigned long changetime = 0;
// unsigned long savetime = 0;

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
  // if(millis()-firsttime == endtime){
  //   digitalWrite(Dir1Pin, LOW); //내려가는거
  //   digitalWrite(Dir2Pin, HIGH);
  // }

  // if(savetime >= standardtime){
  //   digitalWrite(Dir1Pin, LOW);
  //   digitalWrite(Dir2Pin, LOW);
  // }

  // 버튼 상태 읽기
  int upbtnstate = digitalRead(UpBtn);
  int downbtnstate = digitalRead(DownBtn);
  
  // 버튼이 눌렸는지 확인
  if (upbtnstate == LOW) {
    // 버튼이 눌리면 모터 방향 전환
    // Serial.print("1");
    //state = !state;
    // if(flag ==0){
    //   //starttime = millis();
    //   changetime = millis();
    //   flag = 1;
    // }
    // else{
    //   changetime = millis() - changetime;
    //   savetime = savetime + changetime;
    // }
    upstate = !upstate;
    downstate = 0;
    //delay(200); 
  }

  if (downbtnstate == LOW) {
    // 버튼이 눌리면 모터 방향 전환
    // Serial.print("1");
    //state = !state;
    // if(flag ==0){
    //   //starttime = millis();
    //   changetime = millis();
    //   flag = 1;
    // }
    // else{
    //   changetime = millis() - changetime;
    //   savetime = savetime - changetime;
    // }
    downstate = !downstate;
    upstate = 0;
    //delay(200); 
  }

  // 모터 방향 설정
  if (upstate == 0 && downstate == 0) {
    // Serial.print("ok");
    digitalWrite(LED,HIGH);
    digitalWrite(Dir1Pin, LOW); // 멈춤
    digitalWrite(Dir2Pin, LOW);
    //delay(100);
  } else {
    // Serial.print("no");
    if(upstate == 1){
      digitalWrite(LED,LOW);
      digitalWrite(Dir1Pin, HIGH); //올라가는거
      digitalWrite(Dir2Pin, LOW);
      // Serial.print("up");
      //delay(100);
    }
    else if(downstate == 1){
      digitalWrite(LED,LOW);
      digitalWrite(Dir1Pin, LOW); //내려가는거
      digitalWrite(Dir2Pin, HIGH);
      // Serial.print("down");
      // delay(100);
    }
  }
}