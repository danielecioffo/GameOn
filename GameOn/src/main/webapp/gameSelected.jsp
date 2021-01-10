<%@ page import="it.unipi.dii.inginf.dsmt.gameon.model.User" %>
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
    if (gameName.equals("connectFour"))
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
    <h2>Game requests received: </h2>
    <table id="gameRequests">
        <tr>
            <th>Username</th>
            <th>Accept</th>
        </tr>
    </table>
    <script src="resources/javascript/webSocket.js"></script>
    <script>
        var username = '<%= myself.getUsername() %>';
        var gameName = '<% out.print(gameName);%>';
        initWebSocket(username);
        function sendGameRequest (to_username) // send a game request to this user
        {
            if (to_username !== username) // If the user has not clicked on himself
            {
                sendWebSocket(new Message(0, 'game_request', null, username, to_username))
            }
        }

        function sendGameRequestAccepted (to_username) {
            sendWebSocket(new Message(0, 'game_request_accepted', null, username, to_username));
            // We need to wait some milliseconds, otherwise there can be some problems
            setTimeout
            (
                function () {
                    if (gameName === "connectFour")
                        window.location.href = "connectFour.jsp?color=red&opponent="+to_username;
                }, 500
            );
        }

        /**
         * Override of the onMessage function written in webSocket.js
         * @param event     The event that leads to this handler
         */
        ws.onmessage = function (event){
            var jsonString = JSON.parse(event.data);
            var sender = jsonString.sender;
            console.log("Message received");
            if (jsonString.type === 'game_request') // I have received a game request
            {
                var tr = document.createElement("TR");

                var tdUsername = document.createElement("TD");
                tdUsername.textContent = sender;
                tr.appendChild(tdUsername);

                var button = document.createElement("BUTTON");
                button.textContent = "Accept";
                button.onclick = function() { sendGameRequestAccepted(sender); }
                var tdButton = document.createElement("TD");
                tdButton.appendChild(button);
                tr.appendChild(tdButton);

                document.getElementById("gameRequests").appendChild(tr);
            }
            else if (jsonString.type === 'game_request_accepted')
            {
                if (gameName === "connectFour")
                    window.location.href = "connectFour.jsp?color=yellow&opponent="+sender;
            }
        };
    </script>
</body>
</html>
