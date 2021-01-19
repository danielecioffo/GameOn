let yourTurn = true;

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
    if(moves[cellNum]===0) //If the cell is free
    {
        document.images['rc'+cellNum].src="resources/images/cross128.png";
        document.images['rc'+cellNum].alt=" X ";
        moves[cellNum]=1; // 1 indicates myself, 2 the opponent
        done++;
    }
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

// Timer
let time = startingSeconds;
var countdownEl = document.getElementById("countdown");
setInterval(updateCountdown, 1000);

function updateCountdown(){
    const mins = Math.floor(time / 60);
    let secs = time % 60;
    if(mins > 9) {
        if (secs > 9)
            countdownEl.innerHTML = mins + ":" + secs.toString();
        else
            countdownEl.innerHTML = mins + ":0" + secs.toString();
    }else{
        if(secs>9)
            countdownEl.innerHTML = "0" + mins + ":" + secs.toString();
        else
            countdownEl.innerHTML = "0" + mins + ":0" + secs.toString();
    }
    if(mins == 0 && secs==0){
        if (yourTurn)
        {
            failedTurnCounter--;
            console.log("Turn failed: " + failedTurnCounter);
            if (failedTurnCounter === 0)
            {
                surrender();
            }
            else
            {
                let message = new Message(0, "pass", null, username, opponentUsername);
                sendWebSocket(message);
                yourTurn = !yourTurn
                printTurn();
                restartCountdown();
            }
        }
        return;
    }
    time--;
}

function restartCountdown(){ time = startingSeconds; }