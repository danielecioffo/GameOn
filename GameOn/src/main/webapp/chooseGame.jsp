<%@ page import="it.unipi.dii.inginf.dsmt.gameon.model.User" %>
<%@ page import="it.unipi.dii.inginf.dsmt.gameon.listener.SessionManager" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<head>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>GameOn</title>
    <link href="resources/css/chooseGamePage.css" rel="stylesheet" type="text/css">
    <link href="resources/css/general.css" rel="stylesheet" type="text/css">
</head>
<body>
<button onclick = "window.location.href='./logout-servlet'">Logout</button>
    <h1 class="center-text">Welcome <%
        User myself = (User) session.getAttribute("loggedUser");
        out.println(myself.getUsername() + "!");
    %> </h1>
    <div style="width: 50%; margin: 0 auto">
        <div class="imgcontainer">
            <img src='resources/images/icon.png' alt="App Icon" width="75%">
        </div>
        <button name="battleShipButton" class="mainButton" onclick="window.location.href='gameSelected.jsp'">Battle Ship</button>
        <button name="connectedFourButton" class="mainButton" onclick="window.location.href='gameSelected.jsp'">Connect Four</button>
        <%
            out.println("Online users: ");
            SessionManager sessionManager =
                    (SessionManager) session.getServletContext().getAttribute("sessionManager");
            for (User user: sessionManager.getOnlineUsers())
            {
                out.println(user.getUsername());
            }
        %>
    </div>
</body>
</html>
