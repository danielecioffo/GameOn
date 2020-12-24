<%@ page import="it.unipi.dii.inginf.dsmt.gameon.model.User" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.ArrayList" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    List<String> gameNames = new ArrayList<>();
    gameNames.add("Battle Ship");
    gameNames.add("Connect Four");
%>
<html>
<head>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Title</title>
    <link href="resources/css/chooseGamePage.css" rel="stylesheet" type="text/css">
    <link href="resources/css/general.css" rel="stylesheet" type="text/css">
</head>
<body>
    <button onclick="" name="logoutButton" value="logout">Logout</button>
    <h1 class="center-text">Welcome <%
        User user = (User) session.getAttribute("loggedUser");
        out.println(user.getUsername() + "!");
    %> </h1>
    <div style="width: 50%; margin: 0 auto">
        <div class="imgcontainer">
            <img src='resources/images/icon.png' alt="App Icon" width="75%">
        </div>
        <%
            for (String gameName: gameNames)
            {
                out.println("<button class=\"mainButton\">" + gameName + "</button>");
            }
        %>
    </div>
</body>
</html>
