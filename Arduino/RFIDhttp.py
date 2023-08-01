from mfrc522 import SimpleMFRC522
import RPi.GPIO as GPIO
import requests

GPIO.setwarnings(False)

# 서버 URL 설정
SERVER_URL = "http://i9a301.p.ssafy.io:8080/member/save"

# RFID 리더기 초기화
reader = SimpleMFRC522()

while True:
    try:
        print("Hold a tag near the reader")
        tag_id = reader.read()[0]  # 카드의 ID 값 읽어오기
        print("Tag ID:", tag_id)

        # 서버에 전송할 데이터
        datas = {"memberEmail" : tag_id,
                "memberName" : "yeri",
                "memberPassword" : 131321312
        
        }

        # 서버에 HTTP POST 요청 보내기
        response = requests.post(SERVER_URL, data=datas)

        # 서버 응답 확인
        if response.status_code == 200:
            print("Data sent successfully.")
        else:
            print("Failed to send data. Status Code:", response.status_code)

    except KeyboardInterrupt:
        # 프로그램 종료 시 Ctrl+C 입력을 처리
        print("Exiting...")
        break

    except Exception as e:
        print("An error occurred:", str(e))


