// DOM Elements
const allCells = document.querySelectorAll('.cell:not(.row-top)');
const topCells = document.querySelectorAll('.cell.row-top');

// columns
const column0 = [allCells[35], allCells[28], allCells[21], allCells[14], allCells[7], allCells[0], topCells[0]];
const column1 = [allCells[36], allCells[29], allCells[22], allCells[15], allCells[8], allCells[1], topCells[1]];
const column2 = [allCells[37], allCells[30], allCells[23], allCells[16], allCells[9], allCells[2], topCells[2]];
const column3 = [allCells[38], allCells[31], allCells[24], allCells[17], allCells[10], allCells[3], topCells[3]];
const column4 = [allCells[39], allCells[32], allCells[25], allCells[18], allCells[11], allCells[4], topCells[4]];
const column5 = [allCells[40], allCells[33], allCells[26], allCells[19], allCells[12], allCells[5], topCells[5]];
const column6 = [allCells[41], allCells[34], allCells[27], allCells[20], allCells[13], allCells[6], topCells[6]];
const columns = [column0, column1, column2, column3, column4, column5, column6];


// rows
const topRow = [topCells[0], topCells[1], topCells[2], topCells[3], topCells[4], topCells[5], topCells[6]];
const row0 = [allCells[0], allCells[1], allCells[2], allCells[3], allCells[4], allCells[5], allCells[6]];
const row1 = [allCells[7], allCells[8], allCells[9], allCells[10], allCells[11], allCells[12], allCells[13]];
const row2 = [allCells[14], allCells[15], allCells[16], allCells[17], allCells[18], allCells[19], allCells[20]];
const row3 = [allCells[21], allCells[22], allCells[23], allCells[24], allCells[25], allCells[26], allCells[27]];
const row4 = [allCells[28], allCells[29], allCells[30], allCells[31], allCells[32], allCells[33], allCells[34]];
const row5 = [allCells[35], allCells[36], allCells[37], allCells[38], allCells[39], allCells[40], allCells[41]];
const rows = [row0, row1, row2, row3, row4, row5, topRow];

// variables
var yourTurn = false;
var gameIsLive = true;
var opponentColor = 'yellow';
var winningText = "";

// Functions for the logic of the game
const getClassListArray = (cell) => {
    const classList = cell.classList;
    return [...classList];
};

const getCellLocation = (cell) => {
    const classList = getClassListArray(cell);

    const rowClass = classList.find(className => className.includes('row'));
    const colClass = classList.find(className => className.includes('col'));
    const rowIndex = rowClass[4];
    const colIndex = colClass[4];
    const rowNumber = parseInt(rowIndex, 10);
    const colNumber = parseInt(colIndex, 10);

    return [rowNumber, colNumber];
};

const getFirstOpenCellForColumn = (colIndex) => {
    const column = columns[colIndex];
    const columnWithoutTop = column.slice(0, 6);

    for (const cell of columnWithoutTop) {
        const classList = getClassListArray(cell);
        if (!classList.includes('yellow') && !classList.includes('red')) {
            return cell;
        }
    }

    return null;
};

const clearColorFromTop = (colIndex) => {
    const topCell = topCells[colIndex];
    topCell.classList.remove('yellow');
    topCell.classList.remove('red');
};

const getColorOfCell = (cell) => {
    const classList = getClassListArray(cell);
    if (classList.includes('yellow')) return 'yellow';
    if (classList.includes('red')) return 'red';
    return null;
};

const checkWinningCells = (cells) => {
    if (cells.length < 4) return false;

    gameIsLive = false;
    for (const cell of cells) {
        cell.classList.add('win');
    }
    winningText = yourTurn ? "You have won!" : opponentUsername + " has won!";
    return true;
};

const checkStatusOfGame = (cell) => {
    const color = getColorOfCell(cell);
    if (!color) return;
    const [rowIndex, colIndex] = getCellLocation(cell);

    // Check horizontally
    let winningCells = [cell];
    let rowToCheck = rowIndex;
    let colToCheck = colIndex - 1;
    while (colToCheck >= 0) {
        const cellToCheck = rows[rowToCheck][colToCheck];
        if (getColorOfCell(cellToCheck) === color) {
            winningCells.push(cellToCheck);
            colToCheck--;
        } else {
            break;
        }
    }
    colToCheck = colIndex + 1;
    while (colToCheck <= 6) {
        const cellToCheck = rows[rowToCheck][colToCheck];
        if (getColorOfCell(cellToCheck) === color) {
            winningCells.push(cellToCheck);
            colToCheck++;
        } else {
            break;
        }
    }
    let isWinningCombo = checkWinningCells(winningCells);
    if (isWinningCombo) return;


    // Check vertically
    winningCells = [cell];
    rowToCheck = rowIndex - 1;
    colToCheck = colIndex;
    while (rowToCheck >= 0) {
        const cellToCheck = rows[rowToCheck][colToCheck];
        if (getColorOfCell(cellToCheck) === color) {
            winningCells.push(cellToCheck);
            rowToCheck--;
        } else {
            break;
        }
    }
    rowToCheck = rowIndex + 1;
    while (rowToCheck <= 5) {
        const cellToCheck = rows[rowToCheck][colToCheck];
        if (getColorOfCell(cellToCheck) === color) {
            winningCells.push(cellToCheck);
            rowToCheck++;
        } else {
            break;
        }
    }
    isWinningCombo = checkWinningCells(winningCells);
    if (isWinningCombo) return;


    // Check diagonally /
    winningCells = [cell];
    rowToCheck = rowIndex + 1;
    colToCheck = colIndex - 1;
    while (colToCheck >= 0 && rowToCheck <= 5) {
        const cellToCheck = rows[rowToCheck][colToCheck];
        if (getColorOfCell(cellToCheck) === color) {
            winningCells.push(cellToCheck);
            rowToCheck++;
            colToCheck--;
        } else {
            break;
        }
    }
    rowToCheck = rowIndex - 1;
    colToCheck = colIndex + 1;
    while (colToCheck <= 6 && rowToCheck >= 0) {
        const cellToCheck = rows[rowToCheck][colToCheck];
        if (getColorOfCell(cellToCheck) === color) {
            winningCells.push(cellToCheck);
            rowToCheck--;
            colToCheck++;
        } else {
            break;
        }
    }
    isWinningCombo = checkWinningCells(winningCells);
    if (isWinningCombo) return;


    // Check diagonally \
    winningCells = [cell];
    rowToCheck = rowIndex - 1;
    colToCheck = colIndex - 1;
    while (colToCheck >= 0 && rowToCheck >= 0) {
        const cellToCheck = rows[rowToCheck][colToCheck];
        if (getColorOfCell(cellToCheck) === color) {
            winningCells.push(cellToCheck);
            rowToCheck--;
            colToCheck--;
        } else {
            break;
        }
    }
    rowToCheck = rowIndex + 1;
    colToCheck = colIndex + 1;
    while (colToCheck <= 6 && rowToCheck <= 5) {
        const cellToCheck = rows[rowToCheck][colToCheck];
        if (getColorOfCell(cellToCheck) === color) {
            winningCells.push(cellToCheck);
            rowToCheck++;
            colToCheck++;
        } else {
            break;
        }
    }
    isWinningCombo = checkWinningCells(winningCells);
    if (isWinningCombo) return;

    // Check to see if we have a tie
    const rowsWithoutTop = rows.slice(0, 6);
    for (const row of rowsWithoutTop) {
        for (const cell of row) {
            const classList = getClassListArray(cell);
            if (!classList.includes('yellow') && !classList.includes('red')) {
                return;
            }
        }
    }

    gameIsLive = false;
    winningText = "Game is a tie!";
};

// Web socket function
function sendMove(to_username, cell) {
    let obj = {};
    [obj.row, obj.column] = getCellLocation(cell);
    sendWebSocket(new Message('connect_four_move', obj, username, to_username));
    restartCountdown();
    failedTurnCounter = parseInt(totalTurnFallible);
}

// Event Handlers
const handleCellMouseOver = (e) => {
    if (!gameIsLive || !yourTurn) return;
    const cell = e.target;
    const [rowIndex, colIndex] = getCellLocation(cell);

    const topCell = topCells[colIndex];
    topCell.classList.add(color);
};

const handleCellMouseOut = (e) => {
    const cell = e.target;
    const [rowIndex, colIndex] = getCellLocation(cell);
    clearColorFromTop(colIndex);
};

const handleCellClick = (e) => {
    if (!gameIsLive || !yourTurn) return;
    const cell = e.target;
    const [rowIndex, colIndex] = getCellLocation(cell);

    const openCell = getFirstOpenCellForColumn(colIndex);

    if (!openCell) return;

    openCell.classList.add(color);
    checkStatusOfGame(openCell);

    yourTurn = !yourTurn;
    printTurn();
    clearColorFromTop(colIndex);

    sendMove(opponentUsername, openCell);

    if (!gameIsLive) {
        showEndOfGameMessage(winningText, "true");
    }
};

//
// ----- INITIALIZATION OF THE GAME -----
//

// The first player is the yellow one
if(color === 'yellow') {
    yourTurn = true;
    opponentColor = 'red';
}

// Opens the web socket
initWebSocket(username);

// Send a message to register who is the opponent
waitForSocketConnection(ws, function(){
    sendWebSocket(new Message("opponent_registration", opponentUsername, username, null));
});

// First print
printTurn();

// Timer
setInterval(updateCountdown, 1000);

// Adding Event Listeners
for (const row of rows) {
    for (const cell of row) {
        cell.addEventListener('mouseover', handleCellMouseOver);
        cell.addEventListener('mouseout', handleCellMouseOut);
        cell.addEventListener('click', handleCellClick);
    }
}

// Override of the onMessage function written in webSocket.js
ws.onmessage = function (event){
    var jsonString = JSON.parse(event.data);
    var sender = jsonString.sender;
    if (jsonString.type === 'connect_four_move') {
        console.log(sender + " made their move.");

        const row = jsonString.data.row;
        const column = jsonString.data.column;

        const localCell = rows[row][column];
        localCell.classList.add(opponentColor);

        checkStatusOfGame(localCell);

        yourTurn = !yourTurn;
        printTurn();

        if (!gameIsLive) {
            showEndOfGameMessage(winningText, "false");
        }
        restartCountdown();
    }
    else if (jsonString.type === 'chat_message') {
        let p = document.createElement("P");
        let receivedMessage = jsonString.data;
        let text = document.createTextNode(sender + ": " + receivedMessage);
        p.appendChild(text);
        p.className = "message-left";
        chatBox.appendChild(p);
    } else if (jsonString.type === 'surrender') { //the opponent surrender
        showEndOfGameMessage(opponentUsername + " has surrendered!", "true");
    } else if (jsonString.type === 'opponent_disconnected') {
        showEndOfGameMessage(opponentUsername + " disconnected!", "true");
    } else if (jsonString.type === "receiver_not_reachable") {
        showEndOfGameMessage(opponentUsername + " not reachable!", "false");
    } else if (jsonString.type === 'pass') {
        yourTurn = !yourTurn;
        printTurn();
        restartCountdown();
    }
};


