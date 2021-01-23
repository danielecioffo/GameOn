<%@ page import="it.unipi.dii.inginf.dsmt.gameon.model.User" %>
<%@ page import="it.unipi.dii.inginf.dsmt.gameon.config.ConfigurationParameters" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <title>Connect Four</title>
        <link rel="icon" href="resources/images/ConnectFour.png">
        <link type="text/css" rel="stylesheet" href="resources/css/games.css">
        <link type="text/css" rel="stylesheet" href="resources/css/connectFour.css">
        <link type="text/css" rel="stylesheet" href="resources/css/general.css">
    </head>
    <%
        User myself = (User) session.getAttribute("loggedUser");
        String opponent = request.getParameter("opponent");
        ConfigurationParameters configurationParameters = ConfigurationParameters.getInstance();
        String color = request.getParameter("color");
    %>
    <body>
        <div id="overlay">
            <div id="message-div">
                <p id="text">Text to show</p>
                <form action="result-servlet" id="form" method="post">
                    <button class="goBackButton" id="goBackButton" name="hasWonConnectFour" value="false">Go Back to Connect Four Lobby</button>
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
                <button class ="mainButton" type="button" onclick="surrender()">Surrender</button>
                <p>Time left:</p>
                <p id="countdown" class="countdown"></p>
                <p id="turn"></p>
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
            const startingSeconds = '<% out.print(configurationParameters.getHowManySecondsForEachTurn()); %>';
            //The number of turns lost (starting from n going to 0)
            const totalTurnFallible = '<% out.print(configurationParameters.getHowManySkippedRoundsToStopTheGame());%>';

            let failedTurnCounter = parseInt(totalTurnFallible);

            // The color of the disc
            const color = '<% out.print(color); %>';

            // Usernames of the two players
            const username = '<%= myself.getUsername() %>';
            const opponentUsername = '<% out.print(opponent);%>';
        </script>
        <script type="text/javascript" src="resources/javascript/chat.js"></script>
        <script type="text/javascript" src="resources/javascript/timer.js"></script>
        <script type="text/javascript" src="resources/javascript/gameInteraction.js"></script>
        <script type="text/javascript" src="resources/javascript/connectFour.js"></script>
    </body>
</html>



