<%@ page import="it.unipi.dii.inginf.dsmt.gameon.model.User" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <title>Tic-Tac-Toe</title>
    <link rel="icon" href="resources/images/Tic_Tac_Toe.png">
    <link type="text/css" rel="stylesheet" href="resources/css/ticTacToe.css">
    <link type="text/css" rel="stylesheet" href="resources/css/connectFourStyle.css">
    <link type="text/css" rel="stylesheet" href="resources/css/general.css">
</head>
<%
    User myself = (User) session.getAttribute("loggedUser");
    String opponent = request.getParameter("opponent");
    String start = request.getParameter("start"); //Which user start
%>
<body>
<div id="overlay">
    <div id="message-div">
        <p id="text">Message to show</p>
        <form action="result-servlet" id="form" method="post">
            <button class="goBackButton" id="goBackButton" name="hasWonTicTacToe" value="false">Go Back to Tic-Tac-Toe Lobby</button>
        </form>
    </div>
</div>
<div class="grid-container">
    <div class="header">
        <button type="button" onclick = "window.location.href='./logout-servlet'">Logout</button>
    </div>

    <div class="left"></div>

    <div class="middle">
        <table class="gameTable">
            <tr>
                <td class="gameCell">
                    <a href="javascript:sendMove(11)">
                        <img class="gameCellImg" name="rc11">
                    </a>
                </td>
                <td class="gameCell">
                    <a href="javascript:sendMove(12)">
                        <img class="gameCellImg" name="rc12">
                    </a>
                </td>
                <td class="gameCell">
                    <a href="javascript:sendMove(13)">
                        <img class="gameCellImg" name="rc13">
                    </a>
                </td>
            </tr>
            <tr>
                <td class="gameCell">
                    <a href="javascript:sendMove(21)">
                        <img class="gameCellImg" name="rc21">
                    </a>
                </td>
                <td class="gameCell">
                    <a href="javascript:sendMove(22)">
                        <img class="gameCellImg" name="rc22">
                    </a>
                </td>
                <td class="gameCell">
                    <a href="javascript:sendMove(23)">
                        <img class="gameCellImg" name="rc23">
                    </a>
                </td>
            </tr>
            <tr>
                <td class="gameCell">
                    <a href="javascript:sendMove(31)">
                        <img class="gameCellImg" name="rc31">
                    </a>
                </td>
                <td class="gameCell">
                    <a href="javascript:sendMove(32)">
                        <img class="gameCellImg" name="rc32">
                    </a>
                </td>
                <td class="gameCell">
                    <a href="javascript:sendMove(33)">
                        <img class="gameCellImg" name="rc33">
                    </a>
                </td>
            </tr>
        </table>
    </div>

    <div class="right">
        <h1>Tic-Tac-Toe</h1>
        <p>Time left:</p>
        <p id="countdown" style="font-weight: bold; font-size: 40px"></p>
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
<script type="text/javascript" src="resources/javascript/chat.js"></script>
<script type="text/javascript" src="resources/javascript/ticTacToe.js"></script>
<script>
    const username = '<%= myself.getUsername() %>';
    initWebSocket(username);
    const opponentUsername = '<% out.print(opponent);%>';
    const start = '<% out.print(start);%>'; //User that starts the game (first turn)
    initTicTacToe();
    if (start === opponentUsername)
    {
        yourTurn = false;
    }

    printTurn(); // Initial print

    // Send a message to register who is the opponent
    waitForSocketConnection(ws, function(){
        sendWebSocket(new Message(0, "opponent_registration", opponentUsername, username, null));
    });

    /**
     * Function used to send a move, called by the GUI
     * @param choice    What cell has been chosen
     */
    function sendMove (choice) //Row=1, Column=2 -> choice=12
    {
        if (yourTurn)
        {
            let obj = {};
            obj.row = parseInt(choice.toString().substring(0, 1));
            obj.column = parseInt(choice.toString().substring(1, 2));
            sendWebSocket(new Message(0, 'tic_tac_toe_move', obj, username, opponentUsername));

            setCell(choice);
            if (checkWinning(1))
            {
                showEndOfGameMessage("You have won!", "true");
            }
            else if (done > 8)
            {
                showEndOfGameMessage("Game is a tie!", "false");
            }

            yourTurn = !yourTurn;
            printTurn();

            restartCountdown();
        }
    }

    /**
     * Override of the onMessage function written in webSocket.js
     * @param event     The event that leads to this handler
     */
    ws.onmessage = function (event){
        var jsonString = JSON.parse(event.data);
        var sender = jsonString.sender;
        if (jsonString.type === 'tic_tac_toe_move') {
            console.log(sender + " made his move.");

            const row = jsonString.data.row;
            const column = jsonString.data.column;

            // If row=1 and column=2, then choice = 12
            const choice = String(row).concat(String(column));
            document.images['rc' + choice].src = "resources/images/circle128.png";
            document.images['rc' + choice].alt = "";
            moves[choice]=2; //taken by the opponent
            done++;

            yourTurn = !yourTurn;
            printTurn();

            if (checkWinning(2))
            {
                showEndOfGameMessage("You have lost!", "false");
            }
            else if (done > 8)
            {
                showEndOfGameMessage("Game is a tie!", "false");
            }

            restartCountdown();
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
        else if (jsonString.type === 'pass')
        {
            yourTurn = !yourTurn;
            printTurn();
            restartCountdown();
        }
    };

    function surrender ()
    {
        let message = new Message(0, "surrender", null, username, opponentUsername);
        sendWebSocket(message);
        showEndOfGameMessage("You have disconnected!", "false");
    }

    function showEndOfGameMessage(message, value) {
        if(document.getElementById("overlay").style.display === "block")
            return;
        document.getElementById("text").textContent = message;
        document.getElementById("overlay").style.display = "block";
        document.getElementById("message-div").style.display = "block"
        document.getElementById("goBackButton").value = value;
        setTimeout(function() { document.getElementById("goBackButton").click(); }, 3000);
    }

    function printTurn() {
        if(yourTurn) {
            document.getElementById("turn").textContent = "It's your turn!";
        } else {
            document.getElementById("turn").textContent = "It's " + opponentUsername + "'s turn!";
        }
    }
</script>
</body>
</html>



