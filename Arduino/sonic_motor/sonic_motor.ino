#define Dir1Pin 3 // 초록색 IN3
#define Dir2Pin 5 // 파란색 IN4
#define trig 8
#define echo 9

float duration;
float distance;
float sumDistance = 0;
int cnt = 100;

void setup() 
{
  pinMode(Dir1Pin, OUTPUT);
  pinMode(Dir2Pin, OUTPUT);
  pinMode(trig, OUTPUT);
  pinMode(echo, INPUT);

  Serial.begin(9600);
}

void loop() 
{
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
 // Serial.print("Average distance: ");
  //Serial.print(averageDistance);
 // Serial.println(" cm");

  if (averageDistance >= 10) {
    digitalWrite(Dir1Pin, LOW); // 멈춤
    digitalWrite(Dir2Pin, LOW);
    delay(100);
  }
  else if(averageDistance<10 && averageDistance >=5){  
    digitalWrite(Dir1Pin, HIGH); // 올라가는거
    digitalWrite(Dir2Pin, LOW);
    delay(100);
  }
  else if(averageDistance>=0 && averageDistance<5){
    digitalWrite(Dir1Pin, LOW); // 내려가는거
    digitalWrite(Dir2Pin, HIGH);
    delay(100);
  }

}
