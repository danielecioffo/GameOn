
class Message {
    constructor(code, type, data, sender, receiver) {
        this.code = code; // Code of the message, 0 all ok, 1 some error
        this.type = type; // Type of the message, explain the content of the message (or type of error)
        this.data = data; // Data contained in the message, can be an object
        this.sender = sender; // username of sender
        this.receiver = receiver; // username of receiver
    }
}

var ws = new Object;
if (!("WebSocket" in window)) {
    alert("This browser does not support WebSockets");
}

function initWebSocket (username)
{
    ws = new WebSocket("ws://localhost:8090/ws");
    ws.onopen = function(event) {
        console.log('Connected');
        // At the beginning, send a message for registering the username
        ws.send(JSON.stringify(new Message (0, "username_registration", username, username, null))); // No receiver for this message
    };
    ws.onmessage = function (event) {
        console.log(event.data);
    }

    ws.onclose = function()
    {
        console.log('Connection closed');
    };
}

function sendWebSocket(message)
{
    ws.send(JSON.stringify(message));
    console.log('Message sent');
}

function closeWebSocket ()
{
    ws.close();
}