let time = startingSeconds;
let countdownEl = document.getElementById("countdown");

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
    if(mins === 0 && secs===0){
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
                let message = new Message("pass", null, username, opponentUsername);
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