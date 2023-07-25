const WebSocket = require('ws');

// WebSocket 서버 생성
const wss = new WebSocket.Server({ host: '0.0.0.0', port: 8080 });

// 클라이언트가 연결되었을 때 처리
wss.on('connection', (ws) => {
  console.log('클라이언트가 연결되었습니다.');

  // 클라이언트로부터 메시지를 받았을 때 처리
  ws.on('message', (message) => {
    console.log('받은 메시지: ', message);

    // 클라이언트에게 메시지 전송
    ws.send('서버가 받았습니다.');
  });

  // 클라이언트와 연결이 끊겼을 때 처리
  ws.on('close', () => {
    console.log('클라이언트와 연결이 끊겼습니다.');
  });
});
