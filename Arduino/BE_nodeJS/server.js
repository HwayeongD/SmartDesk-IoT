const WebSocket = require('ws');

// 웹 소켓 서버 생성
const wss = new WebSocket.Server({ host: '0.0.0.0', port: 8080 });
let cnt = 0;
// 허용할 클라이언트 IP 주소들을 배열로 정의합니다.
const allowedIPs = ['127.0.0.1', '192.168.171.72', '192.168.171.2', '192.168.171.151'];

// 클라이언트가 연결되었을 때 처리
wss.on('connection', (ws, req) => {
  // 클라이언트의 IP 주소를 가져옵니다.
  const clientIP = req.socket.remoteAddress;

  // 허용되지 않은 IP 주소라면 연결을 종료합니다.
  if (!allowedIPs.includes(clientIP)) {
    console.log(`클라이언트 ${clientIP}의 연결이 거부되었습니다.`);
    ws.terminate(); // 연결 종료
    return;
  }

  console.log(`클라이언트 ${clientIP}가 연결되었습니다.`);

  // 클라이언트로부터 메시지를 받았을 때 처리
  ws.on('message', (message) => {
    console.log(`받은 메시지(${clientIP}):`, message.toString());

    // 클라이언트에게 메시지 전송
    ws.send('서버가 받았습니다.' + cnt);
    cnt++;
  });

  // 클라이언트와 연결이 끊겼을 때 처리
  ws.on('close', () => {
    console.log(`클라이언트 ${clientIP}와의 연결이 끊겼습니다.`);
  });
});
