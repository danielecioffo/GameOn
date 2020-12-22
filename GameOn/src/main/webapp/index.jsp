<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!-- <!DOCTYPE html>
<html>
<head>
    <title>JSP - Hello World</title>
</head>
<body>
<h1><%= "Hello World!" %>
</h1>
<br/>
<a href="hello-servlet">Hello Servlet</a>
</body>
</html> -->

<!DOCTYPE html>
<html>
<head>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>GameOn</title>
    <style>
        body {font-family: Arial, Helvetica, sans-serif;}
        form {border: 3px solid #f1f1f1;}

        input[type=text], input[type=password] {
            width: 100%;
            padding: 12px 20px;
            margin: 8px 0;
            display: inline-block;
            border: 1px solid #ccc;
            box-sizing: border-box;
        }

        button {
            background-color: #3399ff;
            color: white;
            padding: 14px 20px;
            margin: 8px 0;
            border: none;
            cursor: pointer;
            width: 100%;
        }

        button:hover {
            opacity: 0.8;
        }

        .imgcontainer {
            text-align: center;
            margin: 24px 0 12px 0;
        }

        img.icon {
            width: 40%;
            border-radius: 50%;
        }

        .container {
            padding: 16px;
        }

        .center-text {
            text-align: center;
        }
    </style>
</head>
<body>

<h1 class="center-text">Welcome to GameOn!</h1>

<form action="hello-servlet" method="post">
    <div class="imgcontainer">
        <img src="/img/AppIcon.png" alt="App Icon" class="icon">
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
