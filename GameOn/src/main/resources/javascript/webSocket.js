/**
 * This file contains the generic function to handle the websocket
 */

/**
 * This class contains all the attribute of a Message sent and received on the web socket
 */
class Message {
    constructor(code, type, data, sender, receiver) {
        this.code = code; // Code of the message, 0: all ok, 1: some errors
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

/**
 * This function initializes the web socket
 * @param username  Username of the user, needed for the registration
 */
function initWebSocket (username) {
    ws = new WebSocket("ws://localhost:8090/ws");
    ws.onopen = function (event) {
        console.log('Connected');
        // At the beginning, send a message for registering the username
        ws.send(JSON.stringify(new Message(0, "username_registration", username, username, null)));
    };
    ws.onmessage = function (event) {
        console.log(event.data);
    }
    ws.onclose = function () {
        console.log('Connection closed');
    };
}

/**
 * Function used to send a message with the web socket
 * @param message   Message to send
 */
function sendWebSocket(message)
{
    waitForSocketConnection(ws, function(){
        ws.send(JSON.stringify(message));
        console.log('Message sent');
    });
}

/**
 * Function used to close the web socket
 */
function closeWebSocket ()
{
    ws.close();
}

/**
 * Function used to make the function wait until the connection is made
 * @param socket    The WebSocket
 * @param callback  The function to be performed
 */
function waitForSocketConnection(socket, callback) {
    setTimeout
    (
        function () {
            if (socket.readyState === 1) {
                // Connection made
                if (callback != null){
                    callback();
                }
            }
            else {
                waitForSocketConnection(socket, callback);
            }
        }, 5 // wait 5 millisecond for the connection
    );
}