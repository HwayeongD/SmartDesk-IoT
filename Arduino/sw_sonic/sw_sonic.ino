// 상수
#define UpBtn 13 
#define DownBtn 12
#define trig 8
#define echo 9

float duration;
float distance;
float sumDistance = 0;
int cnt = 100;

void setup() 
{
  // 핀 초기화
  pinMode(UpBtn, INPUT_PULLUP);
  pinMode(DownBtn, INPUT_PULLUP);
  pinMode(trig, OUTPUT);
  pinMode(echo, INPUT);
  
  // 시리얼 초기화
   Serial.begin(9600);
}

// 루프
void loop() 
{
  // 버튼 상태 읽기
  int upbtnstate = digitalRead(UpBtn);
  int downbtnstate = digitalRead(DownBtn);
  
  // 버튼이 눌렸는지 확인
  if(upbtnstate == 1 && downbtnstate == 0){
      Serial.println("up");
      delay(100);
  }
  else if(downbtnstate == 1 && upbtnstate == 0){
      Serial.println("down");
      delay(100);
  }
  // 모터 방향 설정
  else if (upbtnstate == 1 && downbtnstate == 1) {
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

    float averageDistance = sumDistance / cnt;
    Serial.print("Average distance: ");
    Serial.print(averageDistance);
    Serial.println(" cm");
    delay(100);

  }
}