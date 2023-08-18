# -*- coding: utf-8 -*-
from mfrc522 import SimpleMFRC522
import RPi.GPIO as GPIO
import requests
from time import sleep

Auth_Card = [549813049020, 681737985529]
GPIO.setwarnings(False)

LED1_PIN = 4
LED2_PIN = 14

# GPIO 설정
GPIO.setmode(GPIO.BCM)
GPIO.setup(LED1_PIN, GPIO.OUT)
GPIO.setup(LED2_PIN, GPIO.OUT)

GPIO.output(LED2_PIN, True)

# 서버 URL 설정
SERVER_URL = "http://i9a301.p.ssafy.io:8080/home/att"  # 실제 서버 URL로 변경해야 합니다.

# RFID 리더기 초기화
reader = SimpleMFRC522()
while True:
    try:
        # print("Hold a tag near the reader")
        tag_id = reader.read()[0]  # 카드의 ID 값을 읽어옵니다.
        
        if tag_id in Auth_Card:
            # LED1 켜기, LED2 끄기
            GPIO.output(LED1_PIN, True)
            GPIO.output(LED2_PIN, False)
            sleep(1)
            GPIO.output(LED1_PIN, False)
            GPIO.output(LED2_PIN, True)
            
        
        # print("Tag ID:", tag_id)

        # 서버에 전송할 데이터
        datas = {
            "empIdCard" : tag_id
        }

        # 서버에 HTTP POST 요청 보내기
        response = requests.put(SERVER_URL, json=datas)

        # 서버 응답 확인
        # if response.status_code == 200:
        #     print("Data sent successfully.")
        # else:
        #     print("Failed to send data. Status Code:", response.status_code)

    # except KeyboardInterrupt:
        # 프로그램 종료 시 Ctrl+C 입력을 처리합니다.
        # print("Exiting...")
        # break

    # except Exception as e:
    #     print("An error occurred:", str(e))
    


