// ********* app.js 파일


// 디렉터리 관리를 위해 path 모듈 사용
const path = require("path");

// HTTP 서버(express) 생성 및 구동

// 1. express 객체 생성
const express = require('express');
const app = express();

// mysql
const mysql = require('mysql2/promise');

const pool = mysql.createPool({
    host: 'localhost',
    port: '3306',
    user: 'testid',
    password: 'test01!',
    database: 'testDB'
})



// 2. "/" 경로 라우팅 처리
app.use("/", (req, res)=>{
    res.sendFile(path.join(__dirname, './index.html')); // index.html 파일 응답
})

// 3. 30001 port에서 서버 구동
const HTTPServer = app.listen(8081, ()=>{
    console.log("Server is open at port:8081");
});


// WebSocekt 서버(ws) 생성 및 구동

// 1. ws 모듈 취득
const wsModule = require('ws');
const { parseArgs } = require("util");
const { connected } = require("process");

// 2. WebSocket 서버 생성/구동
const webSocketServer = new wsModule.Server( 
    {
        server: HTTPServer, // WebSocket서버에 연결할 HTTP서버를 지정한다.
        //port: 30002 // WebSocket연결에 사용할 port를 지정한다(생략시, http서버와 동일한 port 공유 사용)
    }
);


// connection(클라이언트 연결) 이벤트 처리
webSocketServer.on('connection', (ws, request)=>{

    // 1) 연결 클라이언트 IP 취득
    const ip = request.headers['x-forwarded-for'] || request.connection.remoteAddress;
    
    console.log(`새로운 클라이언트[${ip}] 접속`);
    
    // const ipstr = ip.toString();
    // connectedSockets[ipstr] = ws;

    // 2) 클라이언트에게 메시지 전송
    if(ws.readyState === ws.OPEN){ // 연결 여부 체크
        ws.send(`클라이언트[${ip}] 접속을 환영합니다 from 서버`); // 데이터 전송
        
    }
    
    // 3) 클라이언트로부터 메시지 수신 이벤트 처리(버튼 눌렀을 때)
    ws.on('message', async (msg)=>{
        const cmd = msg.toString();
        console.log(`[${ip}] : ${msg}`);
        ws.send('데이터 수신 완료 -주인장-');
        
        // 만약 책상에서 보내준거라면(앱에 저장할 때 말고는 안쓴다!)
        if (cmd[0] === 'd') {
            const parsedArray = cmd.split(" ");
            // 0 "desk", 1 seatid, 2 height
            // 예문: desk 1 130
            
            try{
                const connection = await pool.getConnection();
                const createTableQuery = `
                    UPDATE desk
                    SET deskHeightNow = ${parsedArray[2]}
                    WHERE seatid = ${parsedArray[1]};
                `;
                await connection.query(createTableQuery);
                connection.release();
                console.log('책상의 현재 높이가 수정되었습니다.');
            } catch(error) {
                console.error('선호 좌석 높이 생성중 오류', error)
            }

        }

        else {
            console.log("출근 처리.");

            
            try{
                const connection = await pool.getConnection();
                const selectQuery = `SELECT seatIp FROM testdb.desk WHERE seatId = ${cmd};`;

                const [rows, fields] = await connection.query(selectQuery);
                connection.release();
                console.log(rows[0]);
                if (rows.length > 0) {
                    const deskIP = rows[0].seatIp; // 필드값 가져오기
                    console.log(`특정 테이블 필드값: ${deskIP}`);

                    //이 아이피의 소켓으로 데이터를 전송해야함
                    if (connectedSockets[deskIP] &&  connectedSockets[deskIP].readyState === ws.OPEN) {
                        const sendMessage = "보낼 메세지";
                        connectedSockets[deskIP].send(sendMessage);
                    }
                } 
                else {
                        console.log("데이터베이스에서 해당 조건의 레코드를 찾을 수 없습니다.");
                }
            } catch (error) {
                console.error('IP를 가져오지 못했습니다!',error);
            }
        }
        // try {
        //     const connection = await pool.getConnection();
        //     const createTableQuery = `
        //     UPDATE desk
        //     SET deskHeightNow = 100
        //     WHERE empid IN (SELECT empid FROM employee WHERE empid IS NOT NULL);
            
        //     `;
        //     await connection.query(createTableQuery);
        //     connection.release();
        //     console.log('데이터베이스의 "testDB"에 "messages" 테이블이 생성되었습니다.');
        // } catch (error) {
        //     console.error('테이블 생성 중 오류 발생:', error);
        // }
    })
    
    // 4) 에러 처러
    ws.on('error', (error)=>{
        console.log(`클라이언트[${ip}] 연결 에러발생 : ${error}`);
    })
    
    // 5) 연결 종료 이벤트 처리
    ws.on('close', ()=>{
        console.log(`클라이언트[${ip}] 웹소켓 연결 종료`);
    })
});