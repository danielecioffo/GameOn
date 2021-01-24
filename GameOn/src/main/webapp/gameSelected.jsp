<%@ page import="it.unipi.dii.inginf.dsmt.gameon.model.User" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Waiting for a match</title>
    <link href="resources/css/general.css" rel="stylesheet" type="text/css">
    <link rel="icon" type="image/png" href="resources/images/icon.png"/>
</head>
<body>
<button onclick = "window.location.href='./logout-servlet'">Logout</button>
<h1 class="center-text">Let's play:
<%
    String gameName = (String) session.getAttribute("gameName");
    int howManyMatches = 0;
    if (gameName.equals("connectFour")) {
        out.println("Connect Four!");
        howManyMatches = (int) session.getAttribute("howManyMatchesConnectFour");
    }
    else if (gameName.equals("ticTacToe")) {
        out.println("Tic-Tac-Toe!");
        howManyMatches = (int) session.getAttribute("howManyMatchesTicTacToe");
    }
    User myself = (User) session.getAttribute("loggedUser");
%>
</h1>
<h2 class="center-text">Waiting for a match</h2>
    <div class="divFifty">
        <div class="center-text minWidth">
            <table id="online" class="floatTableLeft">
                <tr>
                    <th>Username</th>
                </tr>
            </table>
            <table id="ranking" class="floatTableRight">
                <tr>
                    <th>Username</th>
                    <th>Ranking</th>
                </tr>
                <c:forEach var="rank" items="${ranking}">
                    <tr>
                        <td>${rank.key}</td>
                        <td>${rank.value}</td>
                    </tr>
                </c:forEach>
            </table>
            <button class="mainButton" onclick = "window.location.href='chooseGame.jsp'">Go back to the List of Games</button>
        </div>
    </div>
    <div class="center-text divFifty">
        <h2>Game requests received: </h2>
        <table id="gameRequests" class="centerTable">
            <tr>
                <th>Username</th>
                <th>Accept</th>
            </tr>
        </table>
    </div>
    <script>
        var username = '<%= myself.getUsername() %>';
        var gameName = '<% out.print(gameName);%>';
        var howManyMatches = parseInt('<% out.print(howManyMatches);%>');
    </script>
    <script src="resources/javascript/webSocket.js"></script>
    <script src="resources/javascript/gameSelected.js"></script>
</body>
</html>
