<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>GameOn</title>
    <link href="resources/css/welcomePage.css" rel="stylesheet" type="text/css">
</head>
<body>

<h1 class="center-text">Welcome to GameOn!</h1>

<form action="hello-servlet" method="post">
    <div class="imgcontainer">
        <img src='resources/images/Icon.png' alt="App Icon" width="400">
    </div>

    <div class="container">
        <label><b>Username</b></label>
        <input type="text" placeholder="Enter Username" name="username" required>

        <label><b>Password</b></label>
        <input type="password" placeholder="Enter Password" name="password" required>

        <button type="submit">Login</button>
        <button type="submit">Register</button>
    </div>
</form>

</body>
</html>
