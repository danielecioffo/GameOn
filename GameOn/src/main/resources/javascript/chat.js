var chatBox = document.getElementById("chatBox");
var chatList = document.getElementById("chatList");
var messageInput = document.getElementById("usermsg");
var sendButton = document.getElementById("sendButton");

sendButton.onclick = function () {
    var li = document.createElement("LI");
    li.appendChild(document.createTextNode(username + ": " + messageInput.value));
    chatList.appendChild(li);

    // send the message
    var message = new Message(0, "chat_message", messageInput.value, username, opponentUsername);
    sendWebSocket(message);

    messageInput.value=''; // clear the input for the message
}
