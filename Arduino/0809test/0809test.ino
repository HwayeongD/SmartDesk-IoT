#include <SPI.h>
#include "epd2in9_V2.h"
#include "epdpaint.h"
#include "imagedata.h"
#include <cstdlib>

#include <ArduinoHttpClient.h>
#include <WiFiS3.h>
#include "arduino_secrets.h"
#include "Arduino_LED_Matrix.h"

#define TABLE_ID 301


// dp paint
#define COLORED     1
#define UNCOLORED   0 // 검정

// btn
#define Dir1Pin 5 // 초록색 IN3
#define Dir2Pin 6 // 파란색 IN4
#define UpBtn 3
#define DownBtn 2
#define trig 8
#define echo 9

//g : 출근, 선호높이
//a : 새벽 책생 제어
//x : 자리상태 변화
//c : 취소, 퇴근

//초음파 거리계산 부분 변수
float duration;
float distance;
float sumDistance = 0;
float nowDistance = 0; 
int cnt = 120;
// 개인 선호 높이
int likeheight = 0;
// 현재 높이
int nowheight = 0;


// 수동 조작할 때 flag가 1일 때만 코드가 돌아가도록
int flag = 0;
// 앱에서 선호높이 버튼 눌렀을 때/출근일 때 동작
int gflag = 0;
// 취소 나 퇴근
int cflag = 0;
// 자리상태 변화
int xflag = 0;
// 새벽책상 제어
int aflag = 0;
// 선호높이만 없는 경우
int nflag = 0;

// LED Matrix
ArduinoLEDMatrix matrix;
unsigned char image[1024];
Paint paint(image, 0, 0);
Epd epd;

// display status value

char* name = "";
bool state_dp;
char* team_name = "";
char* status_print = "";
char* dist = "";
// ultrasonic sensor value
/**/

char ssid[] = SECRET_SSID;
char pass[] = SECRET_PASS;

char serverAddress[] = "i9a301.p.ssafy.io";  // server address
int port = 8080;
WiFiClient wifi;
WebSocketClient client = WebSocketClient(wifi, serverAddress, port);
int status = WL_IDLE_STATUS;


void sonicvalue();
void dp_init();
void send_MSG();
void get_MSG();
void dp_remove_init();

void setup() 
{
  pinMode(UpBtn, INPUT_PULLUP);
  pinMode(DownBtn, INPUT_PULLUP);
  pinMode(Dir1Pin, OUTPUT);
  pinMode(Dir2Pin, OUTPUT);
  pinMode(trig, OUTPUT);
  pinMode(echo, INPUT);

  // Serial.begin(9600);

  while ( status != WL_CONNECTED) {
    //Serial.print("Attempting to connect to Network named: ");
    //Serial.println(ssid);                   // print the network name (SSID);

    // Connect to WPA/WPA2 network:
    status = WiFi.begin(ssid, pass);
  }

  // print the SSID of the network you're attached to:
  //Serial.print("SSID: ");
  //Serial.println(WiFi.SSID());

  // print your WiFi shield's IP address:
  IPAddress ip = WiFi.localIP();
  //Serial.print("IP Address: ");
  //Serial.println(ip);

  //Serial.println("starting WebSocket client");
  client.begin("/ws/chat");
  // client.beginMessage(TYPE_TEXT);
  // client.print("Hand Shake Test");
  // client.endMessage();

  matrix.loadSequence(frames);
  matrix.begin();
  matrix.play(true);
  dp_remove_init();
  delay(100);

}

void loop() 
{
  get_MSG();
  // Serial.println("Waiting...");
  // 서버에서 출근이나 선호높이 버튼을 눌러서 신호 보내줄 때 
  if(gflag == 1){
    dp_init();
    likeheight = atoi(dist);
    sonicvalue();
    nowheight = nowDistance;
    // Serial.print("likeheight : ");
    // Serial.println(likeheight);
    // Serial.print("nowheight : ");
    // Serial.println(nowheight);
    if(likeheight>nowheight){
      while(1){
        sonicvalue();
        // Serial.print("nowDistance : ");
        // Serial.println(nowDistance);
        if (nowDistance >= likeheight) {
          digitalWrite(Dir1Pin, LOW); // 멈춤
          digitalWrite(Dir2Pin, LOW);
          break;
        }
        // Serial.println("올라감");
        digitalWrite(Dir1Pin, HIGH); //올라가는거
        digitalWrite(Dir2Pin, LOW);
        delay(10);
      }
      gflag = 0;
      nowheight = nowDistance;
      nowDistance = likeheight;
      send_MSG();
    }
    else if(likeheight<nowheight){
      while(1){
        sonicvalue();
        // Serial.print("nowDistance : ");
        // Serial.println(nowDistance);
        if (nowDistance <= likeheight) {
          digitalWrite(Dir1Pin, LOW); // 멈춤
          digitalWrite(Dir2Pin, LOW);
          break;
        }
        // Serial.println("내려감");
        digitalWrite(Dir1Pin, LOW); //내려가는거
        digitalWrite(Dir2Pin, HIGH);
        delay(10);
      }
      gflag = 0;
      nowheight = nowDistance;
      nowDistance = likeheight;
      send_MSG();


    }

  }
  else if(cflag == 1){
    dp_remove_init();
    cflag=0;
  }
  else if(xflag == 1){
    dp_init();
    xflag=0;
  }
  else if(aflag == 1){
    digitalWrite(Dir1Pin, LOW); //내려가는거
    digitalWrite(Dir2Pin, HIGH);
    delay(20000);
    aflag = 0;
  }
  else if(nflag == 1){
    sonicvalue();
    dp_init();
    nflag=0;
  }
  else{
    // 수동조작
    int upbtnstate = digitalRead(UpBtn);
    int downbtnstate = digitalRead(DownBtn);
    
    // 버튼이 눌렸는지 확인
    if(upbtnstate == 1 && downbtnstate == 0){
      flag = 1;
      digitalWrite(Dir1Pin, HIGH); // 올라가는거
      digitalWrite(Dir2Pin, LOW);
      //Serial.println("up");
      delay(50);
    }
    else if(downbtnstate == 1 && upbtnstate == 0){
      flag = 1;
      digitalWrite(Dir1Pin, LOW); // 내려가는거
      digitalWrite(Dir2Pin, HIGH);
      //Serial.println("down");
      delay(50);
    }
    else if (flag == 1 &&upbtnstate == 1 && downbtnstate == 1) {

      digitalWrite(Dir1Pin, LOW); // 멈춤
      digitalWrite(Dir2Pin, LOW);
      sonicvalue();
      //한 후의 averageDistance값을 명찰에 업데이트 해주기
      dp_init();
      send_MSG();

      nowheight = nowDistance;
      flag = 0;
    }   
  }
  delay(500);
}

void sonicvalue(){
  sumDistance = 0;
  int mcnt = 0;
  
  for (int i = 0; i < cnt; i++) {

    digitalWrite(trig, HIGH); 
    delayMicroseconds(50);
    digitalWrite(trig, LOW);
    delayMicroseconds(100);
  
    duration = pulseIn(echo, HIGH); // pulseIn함수의 단위는 ms(마이크로 세컨드)

    distance = ((34000 * duration) / 1000000) / 2;

    if(i>=0&&i<10) continue;
    if(i>=110&&i<120)continue;
    if(distance<= 18 || distance >=30){
      mcnt++;
      continue;
    }
    

    sumDistance += distance;
    
    //delay(10); 
  }

  nowDistance = sumDistance / 100 - mcnt;

}

void dp_init(){
  if (epd.Init() != 0) {
    //Serial.print("e-Paper init failed");
    return;
  }
  
  if (nowDistance){
    sprintf(dist, "%d", int(nowDistance));
  }
  if (strlen(name)){
    if(state_dp){
      status_print = "Present";
    }
    else{
      status_print = "Absence";
    }
  
    epd.ClearFrameMemory(0xFF);
    epd.DisplayFrame();
    
    paint.SetRotate(ROTATE_90);


    paint.SetWidth(20); // 우리가 화면 보는 기준으로 세로
    //paint.SetWidth(100);
    paint.SetHeight(150); //가로

    paint.Clear(COLORED); 
    paint.DrawStringAt(0, 0, "____________________________", &Font20, UNCOLORED); // 사각형 안에서의 글씨 위치
    epd.SetFrameMemory(paint.GetImage(), 75, 10, paint.GetWidth(), paint.GetHeight());//사각형 위치
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
    // epd.DisplayFrame();


    paint.SetWidth(40); // 우리가 화면 보는 기준으로 세로
    paint.SetHeight(50); 

    paint.Clear(COLORED); 
    paint.DrawStringAt(5, 10, dist, &Font20, UNCOLORED); // 사각형 안에서의 글씨 위치
    epd.SetFrameMemory(paint.GetImage(), 10, 220, paint.GetWidth(), paint.GetHeight());//사각형 위치
    // epd.DisplayFrame();

    paint.SetWidth(40); // 우리가 화면 보는 기준으로 세로
    paint.SetHeight(50); 

    paint.Clear(COLORED); 
    paint.DrawStringAt(5, 10, "cm", &Font20, UNCOLORED); // 사각형 안에서의 글씨 위치
    epd.SetFrameMemory(paint.GetImage(), 10, 250, paint.GetWidth(), paint.GetHeight());//사각형 위치
    epd.DisplayFrame();

    delay(500);
  }
  else{
    //Serial.println("No Data");
  }
}

void send_MSG(){
  int f = 0;
  while (f == 0){
    if (client.connected()) {
      
      //Serial.print("Send data : ");
      //Serial.print(TABLE_ID);
      //Serial.print(" ");
      //Serial.println(dist);

      // send a hello #
      client.beginMessage(TYPE_TEXT);
      client.print(TABLE_ID);
      client.print(" ");
      client.print(dist);
      
      client.endMessage();
      delay(50);
      // increment count for next message

      // check if a message is available to be received
      // int messageSize = client.parseMessage();
      // Serial.println(messageSize);
      // if (messageSize > 0) {
      //   Serial.println("Received a message:");
      //   Serial.println(client.readString());
      // }
      // // wait 5 seconds
      // delay(500);
      break;
    }
    // else{
    //   Serial.println("Connecting");
    // }
  }
}

//g : 출근, 선호높이
//a : 새벽 책생 제어
//x : 자리상태 변화
//c : 취소, 퇴근

void get_MSG(){
  // 출근이나 선호높이 조정하는 경우랑 퇴근/취소하는 경우랑 자리비움에 대해서 flag 나눠서 생각해야할듯
  int messageSize = client.parseMessage();
  if (messageSize > 0) {
    //Serial.println("Received a message from get_MSG");
    String msg = client.readString(); // 메시지를 String 객체로 받아옴
    char buffer[1024]; // 충분히 큰 버퍼를 준비 (동적 메모리 할당 대신 정적 배열 사용)
    msg.toCharArray(buffer, sizeof(buffer)); // String 객체를 char 배열로 복사
    //Serial.println(buffer);
    char* statechange = strtok(buffer,",");
    name = strtok(NULL, ",");
    dist = strtok(NULL, ",");
    team_name = strtok(NULL, ",");
    char* state_str = strtok(NULL, ",");
    state_dp = (strcmp(state_str, "1") == 0);
    // Serial.println("Parsed Data:");
    // Serial.println(statechange);
    // Serial.println(name);
    // Serial.println(dist);
    // Serial.println(team_name);
    // Serial.println(state_dp);

    if((strcmp(statechange,"g")==0) && (strcmp(dist,"-1") != 0)){
      // Serial.println("ggg");
      gflag = 1;
    }
    else if(strcmp(statechange,"a")==0){
      // Serial.println("aaa");
      aflag = 1;
    }
    else if(strcmp(statechange,"x")==0){
      // Serial.println("xxx");
      xflag = 1;
    }
    else if(strcmp(statechange,"c")==0){
      // Serial.println("ccc");
      cflag = 1;
    }
    else if((strcmp(statechange,"g")==0) && (strcmp(dist,"-1") == 0)){
      // Serial.println("nnn");
      nflag = 1;
    }
    nowDistance = 0;

    //dp_init();

  }
}

void dp_remove_init(){
  if (epd.Init() != 0) {
    //Serial.print("e-Paper init failed");
    return;
  }
    epd.ClearFrameMemory(0xFF);
    epd.DisplayFrame();
  
    delay(500);
}