<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
    <head>
        <title>Connect Four</title>
        <link rel="icon" href="resources/images/ConnectFour.png">
        <link type="text/css" rel="stylesheet" href="resources/css/connectFourStyle.css">
    </head>
    <body>
        <div class="grid-container">
            <div class="header">
                <button type="button" onclick = "window.location.href='./logout-servlet'">Logout</button>
            </div>

            <div class="left"></div>

            <div class="middle">
                <div class="game-board">
                    <div class="square"></div>
                    <div class="square"></div>
                    <div class="square"></div>
                    <div class="square"></div>
                    <div class="square"></div>
                    <div class="square"></div>
                    <div class="square"></div>
                    <div class="square"></div>
                    <div class="square"></div>
                    <div class="square"></div>
                    <div class="square"></div>
                    <div class="square"></div>
                    <div class="square"></div>
                    <div class="square"></div>
                    <div class="square"></div>
                    <div class="square"></div>
                    <div class="square"></div>
                    <div class="square"></div>
                    <div class="square"></div>
                    <div class="square"></div>
                    <div class="square"></div>
                    <div class="square"></div>
                    <div class="square"></div>
                    <div class="square"></div>
                    <div class="square"></div>
                    <div class="square"></div>
                    <div class="square"></div>
                    <div class="square"></div>
                    <div class="square"></div>
                    <div class="square"></div>
                    <div class="square"></div>
                    <div class="square"></div>
                    <div class="square"></div>
                    <div class="square"></div>
                    <div class="square"></div>
                    <div class="square"></div>
                    <div class="square"></div>
                    <div class="square"></div>
                    <div class="square"></div>
                    <div class="square"></div>
                    <div class="square"></div>
                    <div class="square"></div>
                </div>
            </div>

            <div class="right">
                <h1>Connect Four</h1>
                <button class="mainButton" type="button" onclick="">Play</button>
                <button class ="mainButton" type="button" onclick="">Surrender</button>
            </div>

            <div class="footer">
                <div class="wrapper">
                    <p class="title">Chatbox</p>

                    <div class="chatbox"></div>

                    <form name="message" action="">
                        <input name="usermsg" type="text" id="usermsg" placeholder="Type a message..." required/>
                        <button class="mainButton" type="submit">Send</button>
                    </form>
                </div>
            </div>
        </div>
    </body>
</html>



