from mfrc522 import SimpleMFRC522
from time import sleep

while True:
    print("Hold a tag near the reader")
    id = SimpleMFRC522().read()[0]
    print(id)
    sleep(0.3)