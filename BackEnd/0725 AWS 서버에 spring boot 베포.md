#### AWS에 spring boot 베포하기



- 자바 설치
  - sudo apt-get update
  - sudo apt install openjdk-17-jdk



- 파일 옮겨주기.

- 폴더로 이동하기.



- chmod -x gradlew
- ./gradlew build
- 빌드가 성공적으로 끝나면 build/libs 디렉토리가 새롭게 생성됨.
- cd build/libs
- 백그라운드에서 스프링부트 실행
  - nohup java -jar manymanyUsers-0.0.1-SNAPSHOT.jar &
- 로그 확인하는법
  - cat nohup.out



- 현재 돌아가고 있는 프로세스들 확인하기
  - jobs
  - fg %(인덱스)
    - 프로세스 선택하기
  - control + C 로 종료.



### 포트 열어주기

- 포트 확인하기
  - sudo ufw status
- 포트 열어주기
  - sudo ufw allow 8080

















