// Prints whose turn it is
function printTurn() {
    if(yourTurn) {
        document.getElementById("turn").textContent = "It's your turn!";
    } else {
        document.getElementById("turn").textContent = "It's " + opponentUsername + "'s turn!";
    }
}

// Shows a message at the end of the game
function showEndOfGameMessage(message, value) {
    if(document.getElementById("overlay").style.display === "block")
        return;
    document.getElementById("text").textContent = message;
    document.getElementById("overlay").style.display = "block";
    document.getElementById("message-div").style.display = "block"
    document.getElementById("goBackButton").value = value;
    setTimeout(function() { document.getElementById("goBackButton").click(); }, 3000);
}

// The user decides to surrender
function surrender () {
    let message = new Message("surrender", null, username, opponentUsername);
    sendWebSocket(message);
    showEndOfGameMessage("You have disconnected!", "false");
}