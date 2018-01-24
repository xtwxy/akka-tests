var server = require('http').createServer();
var io = require('socket.io')(server);
io.on('connection', function(client){
  client.on('event', function(data){
    console.log(data);
    client.emit("event", "what happend?");
  });
  client.on('foo', function(data){
    console.log(data);
    client.emit("event", "nice to meet you!");
  });
  client.on('disconnect', function(){
    console.log("disconnected.");
  });
});
server.listen(3000);
