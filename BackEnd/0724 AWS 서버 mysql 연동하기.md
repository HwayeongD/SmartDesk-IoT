## pem파일 이용해서 MobaXterm 접속하기 

- pem 파일 ppk로 변환하기
  - Tools - MobaKeyGen
  - Parameters에 RSA 체크 후 Load 버튼 클릭
  - pem 파일 불러오기
  - Save private key 클릭
- Session 생성하기
  - SSH 클릭
  - Advanced SSH settings
    - Remote host에 EC2 퍼블릭 IP 또는 퍼블릭 DNS 넣기
      - 우리는 `i9A301.p.ssafy.io` 넣음.
    - Use Private Key 체크 후 ppk 파일 불러오기.

- 접속

  - login as : ubuntu

  



## mysql 설치하기

```cmd
sudo su
apt-get update
apt-get install mysql-server

cd /etc/mysql/mysql.conf.d
vi mysqld.cnf
```

- bind-address 0.0.0.0 으로 변경하기.



## mysql 유저 생성 및 권한

```sql
//유저 생성 및 권한 주기
CREATE USER '유저명'@'%' IDENTIFIED BY '비밀번호';
grant all privileges on *.* to '유저명'@'%' with grant option;

// 이거 해야하는지 확실하지 않음.
// ufw allow 포트 열어주기.
sudo service mysql restart
sudo ufw allow out 3306/tcp
sudo ufw allow in 3306/tcp
sudo service mysql restart

// DB 생성하기
CREATE DATABASE db명 default CHARACTER SET UTF8;

```



## mysql 테이블 생성하기

```sql
show databases;			// 데이터 베이스 출력

create database [DB이름];	// db 생성

use [DB이름];				// db 접속
show tables;			// 테이블 출력

mysql> create table [table이름](
    -> [column이름] [datatype] [옵션],
    -> [column이름] [datatype] [옵션],
    -> ...
    -> primary key ([pk가 될 column이름])
    ->);
    
    
    varchar(255) 붙여주기.
    
alter table [table이름] add [column이름] [datatype] [옵션]
// table에 column 추가하기.
    
    
insert into [table이름] ([column명],[column명],[column명])
value ([value],[value],[value]);
// value 추가하기.

```



## mysql workbench에 연동하기

- Connection Method : Standard(TCP/IP)
- Hostname : i9A301.p.ssafy.io
- Port : 3306
- Username : DAY6
- Password : `******`

- 





## springboot에서 mysql 연동하기

```yml
url: jdbc:mysql://i9A301.p.ssafy.io:3306/SERVER?serverTimezone=Asia/Seoul&characterEncoding=UTF-8


```















