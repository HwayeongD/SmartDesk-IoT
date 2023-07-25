#include <SPI.h>
#include <MFRC522.h>

#define RST_PIN 9
#define SS_PIN 10
// rfid blue card id : 158 186 192 29
// rfid white card id : 128 3 105 86 
MFRC522 mfrc(SS_PIN, RST_PIN);


void setup() {
  // put your setup code here, to run once:
  Serial.begin(9600);
  SPI.begin();

  mfrc.PCD_Init();
}

void loop() {
  // put your main code here, to run repeatedly:
  // 태그 접촉이 되지 않았을 때 또는 아이디가 읽히지 않았을 때
  if(!mfrc.PICC_IsNewCardPresent()||!mfrc.PICC_ReadCardSerial()){
    delay(500);
    return;
  }
  // 카드 아이디 출력 
  // Serial.print("Card UID:");
  // for(byte i = 0;i<4;i++){
  //   Serial.print(mfrc.uid.uidByte[i]);

  //   Serial.print(" ");
  // }
  // Serial.println();
  if(mfrc.uid.uidByte[0]==128 && mfrc.uid.uidByte[1]==3 && mfrc.uid.uidByte[2]==105&& mfrc.uid.uidByte[3]==86){
    Serial.println("HI,YERI");
  }
  else{
    Serial.println("NO");
  }

}
