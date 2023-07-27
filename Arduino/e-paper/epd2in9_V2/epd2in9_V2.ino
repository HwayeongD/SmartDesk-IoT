#include <SPI.h>
#include "epd2in9_V2.h"
#include "epdpaint.h"
#include "imagedata.h"

#define COLORED     1
#define UNCOLORED   0 // 검정

unsigned char image[1024];
Paint paint(image, 0, 0);
Epd epd;

// display status value
char name[] = "Jaechun";
bool status = false;
char team_name[] = "D4";
const char* status_print = "";

void dp_init();

void setup() {
  Serial.begin(115200);
  dp_init();

}

void loop() {
  // Nothing to do in the loop for this example
}

void dp_init(){

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


  delay(2000);

  if (epd.Init() != 0) {
    Serial.print("e-Paper init failed ");
    return;
  }
}
