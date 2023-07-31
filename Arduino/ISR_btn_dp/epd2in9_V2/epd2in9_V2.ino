#include <SPI.h>
#include "epd2in9_V2.h"
#include "epdpaint.h"
#include "imagedata.h"
#include <avr/interrupt.h>

// dp paint
#define COLORED     1
#define UNCOLORED   0 // 검정

// btn
#define UpBtn 3
#define DownBtn 2

// ultrasonic sensor
#define trig 8
#define echo 9
float duration;
float distance;
float sumDistance = 0;
float averageDistance = 0;
int cnt = 100;

// 개인 선호 높이
int likeheight = 20;
// 출근 상태 여부와 개인 선호 높이로 책상이 움직인 후인지 아닌지
int state = 0; // 출근하면 1로 => 서버에서 출근했는지 여부를 받아서 판단
int statechange = 0; // change한번 하고나면 1로 
// 수동 조작할 때 flag가 1일 때만 코드가 돌아가도록
int flag = 0;

// LED Matrix
unsigned char image[1024];
Paint paint(image, 0, 0);
Epd epd;

// display status value
char name[] = "Jaechun";
bool status = false;
char team_name[] = "D4";
const char* status_print = "";
char dist[] = "";
// ultrasonic sensor value
/**/

void sonicvalue();
void dp_init();
void btn_ISR();

void setup() {
  // pinMode(UpBtn, INPUT_PULLUP);
  pinMode(DownBtn, INPUT_PULLUP);
  pinMode(trig,OUTPUT);
  pinMode(echo,INPUT);
  Serial.begin(9600);
}

void loop() {
  
  // if(state == 1 && statechange == 0){
  //   //출근했을 때 자동조작
  //   while(){
  //     digitalWrite(Dir1Pin, HIGH); //올라가는거
  //     digitalWrite(Dir2Pin, LOW);

  //     sonicvalue();

  //     if (averageDistance >= likeheight) {
  //       digitalWrite(Dir1Pin, LOW); // 멈춤
  //       digitalWrite(Dir2Pin, LOW);
  //       break;
  //     }
  //   }
  //   statechange = 1;
  // }
  // else if(state == 1 && statechange == 1){
  //   // 수동조작
  //   int upbtnstate = digitalRead(UpBtn);
  //   int downbtnstate = digitalRead(DownBtn);
  
  //   // 버튼이 눌렸는지 확인
  //   if(upbtnstate == 1 && downbtnstate == 0){
  //     flag = 1;
  //     digitalWrite(Dir1Pin, HIGH); // 올라가는거
  //     digitalWrite(Dir2Pin, LOW);
  //     Serial.println("up");
  //     delay(100);
  //   }
  //   else if(downbtnstate == 1 && upbtnstate == 0){
  //     flag = 1;
  //     digitalWrite(Dir1Pin, LOW); // 내려가는거
  //     digitalWrite(Dir2Pin, HIGH);
  //     Serial.println("down");
  //     delay(100);
  //   }
  //   // 모터 방향 설정
  //   else if (flag == 1 &&upbtnstate == 1 && downbtnstate == 1) {
  //     digitalWrite(Dir1Pin, LOW); // 멈춤
  //     digitalWrite(Dir2Pin, LOW);
  //     sonicvalue();
  //     //한 후의 averageDistance값을 명찰에 업데이트 해주기
  //     flag = 0;
  //   }
  // }
  if (!digitalRead(DownBtn)){
    flag = 1;
  }
  if (flag == 1){
    Serial.println("btn up");
    sonicvalue();
    dp_init();
    flag = 0;
  }
  Serial.println(flag);
  delay(1000);
}

void sonicvalue(){
  sumDistance = 0;
  for (int i = 0; i < cnt; i++) {
    digitalWrite(trig, HIGH); 
    delayMicroseconds(10);
    digitalWrite(trig, LOW);
  
    duration = pulseIn(echo, HIGH); // pulseIn함수의 단위는 ms(마이크로 세컨드)
  
    distance = ((34000 * duration) / 1000000) / 2;
    sumDistance += distance;
    delay(10); // Delay between individual measurements
  }

  averageDistance = sumDistance / cnt;
  // Serial.print("Average distance: ");
  //Serial.print(averageDistance);
  // Serial.println(" cm");
  averageDistance = int(averageDistance);
  if (averageDistance >= 100){
    averageDistance = 99;
  }
}

void dp_init(){
  sprintf(dist, "%dcm", int(averageDistance));
  if(status){
    status_print = "Present";
  }
  else{
    status_print = "Absence";
  }
  
  if (epd.Init() != 0) {
    Serial.print("e-Paper init failed");
    return;
  }
  
  epd.ClearFrameMemory(0xFF);
  epd.DisplayFrame();
  
  paint.SetRotate(ROTATE_90);


  paint.SetWidth(20); // 우리가 화면 보는 기준으로 세로
  //paint.SetWidth(100);
  paint.SetHeight(150); //가로

  paint.Clear(COLORED); 
  paint.DrawStringAt(0, 0, "____________________________", &Font20, UNCOLORED); // 사각형 안에서의 글씨 위치
  epd.SetFrameMemory(paint.GetImage(), 75, 0, paint.GetWidth(), paint.GetHeight());//사각형 위치
  //epd.DisplayFrame();


  paint.SetWidth(20); // 우리가 화면 보는 기준으로 세로
  //paint.SetWidth(100);
  paint.SetHeight(150); //가로

  paint.Clear(COLORED); 
  paint.DrawStringAt(0, 0, "____________________________", &Font20, UNCOLORED); // 사각형 안에서의 글씨 위치
  epd.SetFrameMemory(paint.GetImage(), 75, 150, paint.GetWidth(), paint.GetHeight());//사각형 위치
  //epd.DisplayFrame();


  paint.SetWidth(40); // 우리가 화면 보는 기준으로 세로
  //paint.SetWidth(100);
  paint.SetHeight(180); 

  paint.Clear(COLORED); 
  paint.DrawStringAt(5, 10, status_print, &Font20, UNCOLORED); // 사각형 안에서의 글씨 위치
  epd.SetFrameMemory(paint.GetImage(), 90, 10, paint.GetWidth(), paint.GetHeight());//사각형 위치


  paint.SetWidth(40); // 우리가 화면 보는 기준으로 세로
  //paint.SetWidth(100);
  paint.SetHeight(50); //가로

  paint.Clear(COLORED); 
  paint.DrawStringAt(5, 10, team_name, &Font20, UNCOLORED); // 사각형 안에서의 글씨 위치
  epd.SetFrameMemory(paint.GetImage(), 90, 250, paint.GetWidth(), paint.GetHeight());//사각형 위치
  //epd.DisplayFrame();


  paint.SetWidth(40); // 우리가 화면 보는 기준으로 세로
  //paint.SetWidth(100);
  paint.SetHeight(204); 

  paint.Clear(COLORED); 
  paint.DrawStringAt(5, 10, name, &Font24, UNCOLORED); // 사각형 안에서의 글씨 위치
  epd.SetFrameMemory(paint.GetImage(), 10, 10, paint.GetWidth(), paint.GetHeight());//사각형 위치
  epd.DisplayFrame();


  paint.SetWidth(40); // 우리가 화면 보는 기준으로 세로
  paint.SetHeight(150); 

  paint.Clear(COLORED); 
  paint.DrawStringAt(5, 10, dist, &Font20, UNCOLORED); // 사각형 안에서의 글씨 위치
  epd.SetFrameMemory(paint.GetImage(), 10, 220, paint.GetWidth(), paint.GetHeight());//사각형 위치
  epd.DisplayFrame();


  delay(2000);

  if (epd.Init() != 0) {
    Serial.print("e-Paper init failed ");
    return;
  }
}


