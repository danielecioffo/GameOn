<%@ page import="it.unipi.dii.inginf.dsmt.gameon.model.User" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <title>Connect Four</title>
        <link rel="icon" href="resources/images/ConnectFour.png">
        <link type="text/css" rel="stylesheet" href="resources/css/connectFourStyle.css">
        <link type="text/css" rel="stylesheet" href="resources/css/general.css">
    </head>
    <%
        User myself = (User) session.getAttribute("loggedUser");
        String opponent = request.getParameter("opponent");
    %>
    <body>
        <div id="overlay">
            <div id="message-div">
                <p id="text">Prova</p>
                <form action="result-servlet" method="post">
                    <button class="goBackButton" id="goBackButton" name="hasWon" value="false">Go Back to Connect Four Lobby</button>
                </form>
            </div>
        </div>
        <div class="grid-container">
            <div class="header">
                <button type="button" onclick = "window.location.href='./logout-servlet'">Logout</button>
            </div>

            <div class="left"></div>

            <div class="middle">
                <div class="game-board">
                    <div class="cell row-top col-0"></div>
                    <div class="cell row-top col-1"></div>
                    <div class="cell row-top col-2"></div>
                    <div class="cell row-top col-3"></div>
                    <div class="cell row-top col-4"></div>
                    <div class="cell row-top col-5"></div>
                    <div class="cell row-top col-6"></div>
                    <div class="cell row-0 col-0 left-border top-border"></div>
                    <div class="cell row-0 col-1 top-border"></div>
                    <div class="cell row-0 col-2 top-border"></div>
                    <div class="cell row-0 col-3 top-border"></div>
                    <div class="cell row-0 col-4 top-border"></div>
                    <div class="cell row-0 col-5 top-border"></div>
                    <div class="cell row-0 col-6 top-border right-border"></div>
                    <div class="cell row-1 col-0 left-border"></div>
                    <div class="cell row-1 col-1"></div>
                    <div class="cell row-1 col-2"></div>
                    <div class="cell row-1 col-3"></div>
                    <div class="cell row-1 col-4"></div>
                    <div class="cell row-1 col-5"></div>
                    <div class="cell row-1 col-6 right-border"></div>
                    <div class="cell row-2 col-0 left-border"></div>
                    <div class="cell row-2 col-1"></div>
                    <div class="cell row-2 col-2"></div>
                    <div class="cell row-2 col-3"></div>
                    <div class="cell row-2 col-4"></div>
                    <div class="cell row-2 col-5"></div>
                    <div class="cell row-2 col-6 right-border"></div>
                    <div class="cell row-3 col-0 left-border"></div>
                    <div class="cell row-3 col-1"></div>
                    <div class="cell row-3 col-2"></div>
                    <div class="cell row-3 col-3"></div>
                    <div class="cell row-3 col-4"></div>
                    <div class="cell row-3 col-5"></div>
                    <div class="cell row-3 col-6 right-border"></div>
                    <div class="cell row-4 col-0 left-border"></div>
                    <div class="cell row-4 col-1"></div>
                    <div class="cell row-4 col-2"></div>
                    <div class="cell row-4 col-3"></div>
                    <div class="cell row-4 col-4"></div>
                    <div class="cell row-4 col-5"></div>
                    <div class="cell row-4 col-6 right-border"></div>
                    <div class="cell row-5 col-0 bottom-border left-border"></div>
                    <div class="cell row-5 col-1 bottom-border"></div>
                    <div class="cell row-5 col-2 bottom-border"></div>
                    <div class="cell row-5 col-3 bottom-border"></div>
                    <div class="cell row-5 col-4 bottom-border"></div>
                    <div class="cell row-5 col-5 bottom-border"></div>
                    <div class="cell row-5 col-6 bottom-border right-border"></div>
                </div>
            </div>

            <div class="right">
                <h1>Connect Four</h1>
                <p id="turn"></p>
                <button class ="mainButton" type="button" onclick="surrender()">Surrender</button>
            </div>

            <div class="footer">
                <div class="wrapper">
                    <p class="title">Chatbox</p>

                    <div id="chatBox" class="chatbox">
                    </div>

                    <input name="usermsg" type="text" id="usermsg" placeholder="Type a message...">
                    <button id="sendButton" class="sendButton">Send</button>
                </div>
            </div>
        </div>
        <script src="resources/javascript/webSocket.js"></script>
        <script>
            function showEndOfGameMessage(message, value) {
                if(document.getElementById("overlay").style.display === "block")
                    return;
                document.getElementById("text").textContent = message;
                document.getElementById("overlay").style.display = "block";
                document.getElementById("message-div").style.display = "block"
                document.getElementById("goBackButton").value = value;
            }

            function printTurn() {
                if(yourTurn) {
                    document.getElementById("turn").textContent = "It's your turn!";
                } else {
                    document.getElementById("turn").textContent = "It's " + opponent + "'s turn!";
                }
            }

            const username = '<%= myself.getUsername() %>';
            initWebSocket(username);
            const opponentUsername = '<% out.print(opponent);%>';
            // Send a message to register who is the opponent
            waitForSocketConnection(ws, function(){
                sendWebSocket(new Message(0, "opponent_registration", opponentUsername, username, null));
            });

            /**
             * Override of the onMessage function written in webSocket.js
             * @param event     The event that leads to this handler
             */
            ws.onmessage = function (event){
                var jsonString = JSON.parse(event.data);
                var sender = jsonString.sender;
                if (jsonString.type === 'connect_four_move') {
                    console.log(sender + " made their move.");

                    const row = jsonString.data.row;
                    const column = jsonString.data.column;

                    const localCell = rows[row][column];
                    localCell.classList.add(opponentColor);

                    checkStatusOfGame(localCell);

                    yourTurn = !yourTurn;
                    printTurn();

                    if (!gameIsLive) {
                        showEndOfGameMessage(winningText, "false");
                    }
                }
                else if (jsonString.type === 'chat_message')
                {
                    let p = document.createElement("P");
                    let receivedMessage = jsonString.data;
                    let text = document.createTextNode(sender + ": " + receivedMessage);
                    p.appendChild(text);
                    p.className = "message-left";
                    chatBox.appendChild(p);
                }
                else if (jsonString.type === 'surrender') //the opponent surrender
                {
                    showEndOfGameMessage(opponentUsername + " has surrendered!", "true");
                }
                else if (jsonString.type === 'opponent_disconnected')
                {
                    showEndOfGameMessage(opponentUsername + " disconnected!", "true");
                }
            };

            function surrender ()
            {
                let message = new Message(0, "surrender", null, username, opponentUsername);
                sendWebSocket(message);
            }
        </script>
        <script type="text/javascript" src="resources/javascript/chat.js"></script>
        <script type="text/javascript" src="resources/javascript/connectFour.js"></script>
    </body>
</html>



