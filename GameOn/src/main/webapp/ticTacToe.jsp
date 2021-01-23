<%@ page import="it.unipi.dii.inginf.dsmt.gameon.model.User" %>
<%@ page import="it.unipi.dii.inginf.dsmt.gameon.config.ConfigurationParameters" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <title>Tic-Tac-Toe</title>
    <link rel="icon" href="resources/images/Tic_Tac_Toe.png">
    <link type="text/css" rel="stylesheet" href="resources/css/games.css">
    <link type="text/css" rel="stylesheet" href="resources/css/ticTacToe.css">
    <link type="text/css" rel="stylesheet" href="resources/css/general.css">
</head>
<%
    User myself = (User) session.getAttribute("loggedUser");
    String opponent = request.getParameter("opponent");
    ConfigurationParameters configurationParameters = ConfigurationParameters.getInstance();
    String start = request.getParameter("start"); //Which user start
%>
<body>
<div id="overlay">
    <div id="message-div">
        <p id="text">Message to show</p>
        <form action="result-servlet" id="form" class="formStyle" method="post">
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
        <p id="countdown" class="countdown"></p>
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
    //Time in seconds for the timer
    const startingSeconds = '<% out.print(configurationParameters.getHowManySecondsForEachTurn()); %>'
    //The number of turns lost (starting from n going to 0)
    let failedTurnCounter = '<% out.print(configurationParameters.getHowManySkippedRoundsToStopTheGame());%>';

    // Username of the two players

    const username = '<%= myself.getUsername() %>';
    const opponentUsername = '<% out.print(opponent);%>';

    //User that starts the game (first turn)
    const start = '<% out.print(start);%>';

    let yourTurn = true;
</script>
<script type="text/javascript" src="resources/javascript/chat.js"></script>
<script type="text/javascript" src="resources/javascript/timer.js"></script>
<script type="text/javascript" src="resources/javascript/gameInteraction.js"></script>
<script type="text/javascript" src="resources/javascript/ticTacToe.js"></script>
</body>
</html>



