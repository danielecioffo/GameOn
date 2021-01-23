<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>GameOn</title>
    <link href="resources/css/general.css" rel="stylesheet" type="text/css">
</head>
<body>

<h1 class="center-text">Welcome to GameOn!</h1>

    <div class="divFifty">
    <form action="access-servlet" class="formStyle" method="post">
        <div class="imgcontainer">
            <img src='resources/images/icon.png' alt="App Icon" width="75%">
        </div>

        <div class="container">
            <label><b>Username</b></label>
            <input type="text" class="inputWidth" placeholder="Enter Username" name="username" required>

            <label><b>Password</b></label>
            <input type="password" class="inputWidth" placeholder="Enter Password" name="password" required>

            <button class="mainButton" type="submit" name="loginButton" value="login">Login</button>
            <button class="mainButton" type="submit" name="registerButton" value="register">Register</button>
        </div>
    </form>

</div>
</body>
</html>
