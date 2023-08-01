### 실전 활용

#### Local

1. 파일 수정

2. Docker Image build

   ```bash
   docker build -t [계정명]/[프로젝트명] .
   docker images // 생성된 도커 이미지 확인
   ```

3. Container 실행해보기

   ```bash
   docker run -d -p 8080:8080 [계정명]/[프로젝트명]
   docker ps
   ```

4. 이상 없으면 push 하기

   ```bash
   docker push [계정명]/[프로젝트명]
   ```

#### EC2

1. pull 해오기

   ```bash
   docker pull [계정명]/[프로젝트명]
   
   docker images
   // 받은 이미지 확인하기
   ```

2. Container 실행

   ```bash
   docker run -d -p 8080:8080 [계정명]/[프로젝트명]
   
   docker ps
   // 실행중인 컨테이너 확인
   ```

3. 종료

   ```bash
   docker stop [컨테이너ID] or [컨테이너NAME]
   ```

   





### Docker spring boot 연동하기

Docker Hub push 전까지

https://devfoxstar.github.io/java/springboot-docker-ec2-deploy/#google_vignette

EC2에서 연동

https://kku-jun.tistory.com/10

Docker 명령어 모음

https://imjeongwoo.tistory.com/111



#### Docker 시스템 흐름도

![Docker Workflow](https://devfoxstar.github.io/static/9c4b9c922b615ad8099eef60bcf9ae92/b5cea/docker-workflow-steps.png)





#### 로컬에 Docker 설치하기

Docker Desktop for Windows 설치



##### Docker Desktop 설치했을때 오류 나올 경우

`Docker Desktop requires a newer WSL kernel version` 오류

- PowerShell 실행
- `wsl --update`





#### SpringBoot 프로젝트에 도커 설정하기

Dockerfile 파일 생성

```dockerfile
FROM openjdk:17
ARG JAR_FILE=*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
```



#### Docker Image 만들기

```bash
docker build -t [계정명]/[프로젝트명] .

docker images // 생성된 도커 이미지 확인
```



#### Container 실행

```bash
docker run -d -p 8080:8080 [계정명]/[프로젝트명]
// Host Port : Container Port
// -d : 백그라운드 실행
// -p 8080:8080 : 호스트와 컨테이너 간의 포트 매핑을 설정

docker ps
// 실행중인 컨테이너 확인
```



#### Docker Hub에 Push 하기

```bash
docker login

docker push [계정명]/[프로젝트명]
```



#### AWS EC2에 Docker 설치하기

```bash
sudo su // sudo 귀찮으면 미리 쓰기


sudo apt update

sudo apt install apt-transport-https

sudo apt install ca-certificates

sudo apt install curl

sudo apt install software-properties-common

curl -fsSL https://download.docker.com/linux/ubuntu/gpg | sudo apt-key add -

sudo add-apt-repository "deb [arch=amd64] https://download.docker.com/linux/ubuntu bionic stable"

sudo apt update

apt-cache policy docker-ce

sudo apt install docker-ce
```



#### docker hub에서 pull 해오기

```bash
docker pull [계정명]/[프로젝트명]

docker images
// 받은 이미지 확인하기
```



#### Container 실행

```bash
docker run -d -p 8080:8080 [계정명]/[프로젝트명]
// Host Port : Container Port
// -d : 백그라운드 실행
// -p 8080:8080 : 호스트와 컨테이너 간의 포트 매핑을 설정

docker ps
// 실행중인 컨테이너 확인
```



#### Container 정지

```bash
docker stop [컨테이너ID] or [컨테이너NAME]
```













