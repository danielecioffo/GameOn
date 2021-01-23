let won, done; //done is the total number of move
let moves=[];
let choices=[11,12,13,21,22,23,31,32,33];
let ways=[];
ways[1]=[0,11,12,13];
ways[2]=[0,21,22,23];
ways[3]=[0,31,32,33];
ways[4]=[0,11,21,31];
ways[5]=[0,12,22,32];
ways[6]=[0,13,23,33];
ways[7]=[0,11,22,33];
ways[8]=[0,13,22,31];

/**
 * Function that initializes the Tic-Tac-Toe game logic
 */
function initTicTacToe()
{
    won=false;
    done=0;
    moves[11]=0; moves[12]=0; moves[13]=0; moves[21]=0; moves[22]=0; moves[23]=0; moves[31]=0; moves[32]=0; moves[33]=0;
    for(let i=0;i<=8;i++)
    {
        document.images['rc' + choices[i]].src = "resources/images/nothing128.png";
        document.images['rc' + choices[i]].alt = "";
    }
}

/**
 * Function that try to insert the symbol X on the cell
 * @param cellNum   Which cell
 */
function setCell(cellNum)
{
        document.images['rc'+cellNum].src="resources/images/cross128.png";
        document.images['rc'+cellNum].alt=" X ";
        moves[cellNum]=1; // 1 indicates myself, 2 the opponent
        done++;
}

/**
 * Function used to check if me or the opponent has won
 * @param player        Which player to check
 * @returns {boolean}   True if the player has won, false otherwise
 */
function checkWinning (player)
{
    //player=1 -> myself, player=2 -> opponent
    won = false;
    for (let n=1; n<=8; n++)
    {
        if( (moves[ways[n][1]]===player) && (moves[ways[n][2]]===player)
            && (moves[ways[n][3]]===player) )
        {
            won=true;
            break;
        }
    }
    return won;
}

//
// ----- INITIALIZATION OF THE GAME -----
//

// Set who is starting
if (start === opponentUsername)
{
    yourTurn = false;
}

// Initialize the game logic
initTicTacToe();

// Opens the web socket
initWebSocket(username);

// First print
printTurn();

// Timer
setInterval(updateCountdown, 1000);

// Send a message to register who is the opponent
waitForSocketConnection(ws, function(){
    sendWebSocket(new Message("opponent_registration", opponentUsername, username, null));
});

/**
 * Function used to send a move, called by the GUI
 * @param choice    What cell has been chosen
 */
function sendMove (choice) //Row=1, Column=2 -> choice=12
{
    if (yourTurn && (moves[choice] === 0)) // 0 indicates that the cell is free
    {
        failedTurnCounter = parseInt(totalTurnFallible);
        let obj = {};
        obj.row = parseInt(choice.toString().substring(0, 1));
        obj.column = parseInt(choice.toString().substring(1, 2));
        sendWebSocket(new Message('tic_tac_toe_move', obj, username, opponentUsername));

        setCell(choice);
        if (checkWinning(1))
        {
            showEndOfGameMessage("You have won!", "true");
        }
        else if (done > 8)
        {
            showEndOfGameMessage("Game is a tie!", "false");
        }

        yourTurn = !yourTurn;
        printTurn();

        restartCountdown();
    }
}

/**
 * Override of the onMessage function written in webSocket.js
 * @param event     The event that leads to this handler
 */
ws.onmessage = function (event){
    var jsonString = JSON.parse(event.data);
    var sender = jsonString.sender;
    if (jsonString.type === 'tic_tac_toe_move') {
        console.log(sender + " made his move.");

        const row = jsonString.data.row;
        const column = jsonString.data.column;

        // If row=1 and column=2, then choice = 12
        const choice = String(row).concat(String(column));
        document.images['rc' + choice].src = "resources/images/circle128.png";
        document.images['rc' + choice].alt = "";
        moves[choice]=2; //taken by the opponent
        done++;

        yourTurn = !yourTurn;
        printTurn();

        if (checkWinning(2))
        {
            showEndOfGameMessage("You have lost!", "false");
        }
        else if (done > 8)
        {
            showEndOfGameMessage("Game is a tie!", "false");
        }

        restartCountdown();
    }
    else if (jsonString.type === 'chat_message')
    {
        let p = document.createElement("P");
        let receivedMessage = jsonString.data;
        let text = document.createTextNode(sender + ": " + receivedMessage);
        p.appendChild(text);
        p.className = "message-left";
        chatBox.appendChild(p);
    }
    else if (jsonString.type === 'surrender') //the opponent surrender
    {
        showEndOfGameMessage(opponentUsername + " has surrendered!", "true");
    }
    else if (jsonString.type === 'opponent_disconnected')
    {
        showEndOfGameMessage(opponentUsername + " disconnected!", "true");
    }
    else if (jsonString.type === "receiver_not_reachable")
    {
        showEndOfGameMessage(opponentUsername + " not reachable!", "false");
    }
    else if (jsonString.type === 'pass')
    {
        yourTurn = !yourTurn;
        printTurn();
        restartCountdown();
    }
};