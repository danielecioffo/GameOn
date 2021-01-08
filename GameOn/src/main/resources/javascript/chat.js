var chatBox = document.getElementById("chatBox");
var messageInput = document.getElementById("usermsg");
var sendButton = document.getElementById("sendButton");

sendButton.onclick = function () {
    let p = document.createElement("P");
    let text = document.createTextNode(username + ": " + messageInput.value);
    p.appendChild(text);
    p.className = "message-right";
    chatBox.appendChild(p);

    // send the message
    var message = new Message(0, "chat_message", messageInput.value, username, opponentUsername);
    sendWebSocket(message);

    messageInput.value=''; // clear the input for the message
}
