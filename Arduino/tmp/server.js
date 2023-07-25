import { Server } from "socket.io";

const io = new Server(3000);

io.on('connection', socket => {
  // either with send()
//   socket.send('Hello!');

  // or with emit() and custom event names
  socket.emit('hello', 'world');

  // handle the event sent with socket.send()
  socket.on('message', (data) => {
    console.log(data);
  });

  // handle the event sent with socket.emit()
//   socket.on('salutations', (elem1, elem2, elem3) => {
//     console.log(elem1, elem2, elem3);
//   });
});