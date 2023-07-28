#define Dir1Pin 5 // 초록색 IN3
#define Dir2Pin 6 // 파란색 IN4
#define UpBtn 3
#define DownBtn 2
#define trig 8
#define echo 9

//초음파 거리계산 부분 변수
float duration;
float distance;
float sumDistance = 0;
float averageDistance = 0; 
int cnt = 100;
// 개인 선호 높이
int likeheight = 60;
// 출근 상태 여부와 개인 선호 높이로 책상이 움직인 후인지 아닌지
int state = 0; // 출근하면 1로 => 서버에서 출근했는지 여부를 받아서 판단
int statechange = 0; // change한번 하고나면 1로 
// 수동 조작할 때 flag가 1일 때만 코드가 돌아가도록
int flag = 0;

void sonicvalue();

void setup() 
{
  pinMode(UpBtn, INPUT_PULLUP);
  pinMode(DownBtn, INPUT_PULLUP);
  pinMode(Dir1Pin, OUTPUT);
  pinMode(Dir2Pin, OUTPUT);
  pinMode(trig, OUTPUT);
  pinMode(echo, INPUT);

  Serial.begin(9600);
}

void loop() 
{

  if(state == 1 && statechange == 0){
    //출근했을 때 자동조작
    while(){
      digitalWrite(Dir1Pin, HIGH); //올라가는거
      digitalWrite(Dir2Pin, LOW);

      sonicvalue();

      if (averageDistance >= likeheight) {
        digitalWrite(Dir1Pin, LOW); // 멈춤
        digitalWrite(Dir2Pin, LOW);
        break;
      }
    }
    statechange = 1;
  }
  else if(state == 1 && statechange == 1){
    // 수동조작
    int upbtnstate = digitalRead(UpBtn);
    int downbtnstate = digitalRead(DownBtn);
  
    // 버튼이 눌렸는지 확인
    if(upbtnstate == 1 && downbtnstate == 0){
      flag = 1;
      digitalWrite(Dir1Pin, HIGH); // 올라가는거
      digitalWrite(Dir2Pin, LOW);
      Serial.println("up");
      delay(100);
    }
    else if(downbtnstate == 1 && upbtnstate == 0){
      flag = 1;
      digitalWrite(Dir1Pin, LOW); // 내려가는거
      digitalWrite(Dir2Pin, HIGH);
      Serial.println("down");
      delay(100);
    }
    // 모터 방향 설정
    else if (flag == 1 &&upbtnstate == 1 && downbtnstate == 1) {
      digitalWrite(Dir1Pin, LOW); // 멈춤
      digitalWrite(Dir2Pin, LOW);
      sonicvalue();
      //한 후의 averageDistance값을 명찰에 업데이트 해주기
      flag = 0;
    }
   
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

}
