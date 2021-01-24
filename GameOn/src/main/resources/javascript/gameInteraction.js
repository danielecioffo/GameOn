/**
 * Function that prints whose turn it is
 */
function printTurn() {
    if(yourTurn) {
        document.getElementById("turn").textContent = "It's your turn!";
    } else {
        document.getElementById("turn").textContent = "It's " + opponentUsername + "'s turn!";
    }
}

/**
 * Function that shows a message at the end of the game, stating the results
 * @param message   message to be displayed
 * @param value false if the opponent has won
 */
function showEndOfGameMessage(message, value) {
    if(document.getElementById("overlay").style.display === "block")
        return;
    document.getElementById("text").textContent = message;
    document.getElementById("overlay").style.display = "block";
    document.getElementById("message-div").style.display = "block"
    document.getElementById("goBackButton").value = value;
    setTimeout(function() { document.getElementById("goBackButton").click(); }, 3000);
}

/**
 * Function that is called when the player decides to surrender (by clicking on the "Surrender" button)
 */
function surrender () {
    let message = new Message("surrender", null, username, opponentUsername);
    sendWebSocket(message);
    showEndOfGameMessage("You have disconnected!", "false");
}