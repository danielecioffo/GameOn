<%@ page import="it.unipi.dii.inginf.dsmt.gameon.model.User" %><%--
  Created by IntelliJ IDEA.
  User: francesco
  Date: 24/12/20
  Time: 17:14
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Waiting for a match</title>
    <link href="resources/css/chooseGamePage.css" rel="stylesheet" type="text/css">
    <link href="resources/css/general.css" rel="stylesheet" type="text/css">
    <style type="text/css">
        table{
            border: 1px solid #2274ba;
            font-size: 20px;
            text-align: center;
        }

        th,td{
            border: 1px solid #2274ba;
            padding-left: 10px;
            padding-right: 10px;
        }
    </style>
</head>
<body>
<button onclick = "window.location.href='./logout-servlet'">Logout</button>
<h1 class="center-text">Let's play:
<%
    String gameName = (String) session.getAttribute("gameName");
    if (gameName.equals("connectedFour"))
        out.println("Connect Four!");
    else if (gameName.equals("battleShip"))
        out.println("Battle Ship!");
    User myself = (User) session.getAttribute("loggedUser");
%>
</h1>
<h2 class="center-text">Waiting for a match</h2>
    <div style="width: 50%; margin: 0 auto">
        <div>
            <table id="online" style="float: left">
                <tr>
                    <th>Username</th>
                </tr>
                <c:forEach var="item" items="${usrs}">
                    <tr>
                        <td onclick="sendGameRequest('${item}');"> ${item}</td>
                    </tr>
                </c:forEach>
            </table>
            <table id="ranking" style="float: right">
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
        </div>
        <div class="center-text">
            <button class="mainButton" onclick = "window.location.href='chooseGame.jsp'">Go back to List's Games</button>
        </div>
    </div>
    <ul id="gameRequestList">
    </ul>
    <script src="resources/javascript/webSocket.js"></script>
    <script>
        var username = '<%= myself.getUsername() %>';
        initWebSocket(username);
        function sendGameRequest (to_username) // send a game request to this user
        {
            if (to_username !== username) // If the user has not clicked on himself
            {
                sendWebSocket(new Message(0, 'game_request', null, username, to_username))
            }
        }
        ws.onmessage = function (event){
            var jsonString = JSON.parse(event.data);
            if (jsonString.type === 'game_request') // I have received a game request
            {
                var sender = jsonString.sender;
                var node = document.createElement("LI");
                var textNode = document.createTextNode(sender + " wanna play with you!");
                node.appendChild(textNode);
                document.getElementById("gameRequestList").appendChild(node);
            }
        };
    </script>
</body>
</html>
