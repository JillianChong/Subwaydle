var guess = [];
var currentRow = 1;
var currentBox = 1;
var win = false;

var greenHex = "#27BB5F";
var blueHex = "#27A3BB";
var yellowHex = "#F0DF4F";
var grayHex = "#959ba1";

window.addEventListener('DOMContentLoaded', async function() {
    try {
        var codeStart;
        var codeEnd;
        const response = await fetch('/api/generatePath');
        const path = await response.json();
    
        console.log("Received path:", path);

        var codeStart = path[3];
        var trainStart = path[0];
        var codeEnd = path[6];
        var trainEnd = path[2];

        if(codeStart.includes("&") || codeStart.includes("(")) {
            codeStart = await fetchCodeName(codeStart, trainStart);
        }

        if(codeEnd.includes("&") || codeEnd.includes("(")) {
            codeEnd = await fetchCodeName(codeEnd, trainEnd);
        }

        document.getElementById('instructions-text').innerText = 
            'Transfer from ' + codeStart + ' to '+ codeEnd + ' using 2 transfers.';

        } catch (error) {
            console.error('Error:', error)
        }
});

window.addEventListener('DOMContentLoaded', updateCarNumber());

window.addEventListener("keydown", takeKeyInput);

const letterButtons = document.querySelectorAll('.letter');
letterButtons.forEach(function(button) {button.addEventListener('click', updateBoxGUI);});

const enterButton = document.querySelector("#enter");
enterButton.addEventListener('click', updateRowGUI);

const deleteButton = document.querySelector("#delete");
deleteButton.addEventListener('click', deleteBoxGUI);

const closeButton = document.querySelector("#close-popup");
closeButton.addEventListener('click', function () {
    const popup = document.querySelector(".popup");
    popup.style.display = 'none';
})

function updateRowGUI(event) { // when enter is clicked
    // Send the data to the Spring Boot controller using Fetch API
    if(guess.length == 3) {
        fetch('/api/submitData', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(guess)
        })
        .then(response => response.text())
        .then(responseText => {
            return updateRowGUIHelper();
        }).then(() => {
            if(currentRow == 7 && !win) {
                updateWindow("YOU LOSE!");
            } else if(currentRow == 7 && win) {
                updateWindow("YAY! YOU WIN!");
            } else {
                // currentRow++;
                currentBox = 1;
                // guess = [];
            }
        })
        .catch(error => console.error('Error:', error));
    }
}

async function updateRowGUIHelper() {
    try {
        const response = await fetch('/api/updateBoard');
        const colors = await response.json();
            console.log("Received colors:", colors);

            if(Array.isArray(colors) && colors.length == 3) {
                // console.log("ROW :" + currentRow);
                const box1 = document.querySelector("#row"+(currentRow) + " #box1");
                const box2 = document.querySelector("#row"+(currentRow) + " #box2");
                const box3 = document.querySelector("#row"+(currentRow) + " #box3");
                console.log(box1 + " " + box2 + " " + box3);

                box1.style.backgroundColor = colors[0];
                box2.style.backgroundColor = colors[1];
                box3.style.backgroundColor = colors[2];
        
                updateKeyboardGUIHelper(colors);

                console.log("Board updated successfully.");

                if(colors[0] == greenHex && colors[1] == greenHex && colors[2] == greenHex) {
                    win = true;
                    await updateWindow("YAY! YOU WIN!"); // NEED TO MAKE TEHSE AWAIT
                }
            } else {
                console.error("Received colors array is invalid:", colors);
            }

            currentRow++;
            guess = [];
    } catch(error) {
        console.error('Error:', error);
    }
}

function updateKeyboardGUIHelper(colors) {
    const keyBox1 = document.getElementById("letter" + guess[0]);
    const keyBox2 = document.getElementById("letter" + guess[1]);
    const keyBox3 = document.getElementById("letter" + guess[2]);

    const keyBoxes = [keyBox1, keyBox2, keyBox3];

    for(var i = 0; i < keyBoxes.length; i++) {
        const keyBox = keyBoxes[i];
        console.log(keyBox.style.backgroundColor);

        if(keyBox.classList.contains("greenHex")) {
            continue;
        } else if(keyBox.classList.contains("blueHex")) {
            if(colors[i] == greenHex) {
                keyBox.classList.remove("blueHex", "yellowHex", "grayHex");
                keyBox.classList.add("greenHex");
            }
        } else if(keyBox.classList.contains("yellowHex")) {
            if(colors[i] == greenHex || colors[i] == blueHex) {
                keyBox.classList.remove("yellowHex", "grayHex");

                if(colors[i] == greenHex) {
                    keyBox.classList.add("greenHex");
                } else {
                    keyBox.classList.add("blueHex");
                }
            }
        } else {
            if(colors[i] != grayHex) {
                keyBox.classList.remove("grayHex");

                if(colors[i] == greenHex) {
                    keyBox.classList.add("greenHex");
                } else if(colors[i] == blueHex) {
                    keyBox.classList.add("blueHex");
                } else {
                    keyBox.classList.add("yellowHex");
                }
            } else {
                keyBox.classList.add("grayHex");
            }
        }
    }
}

function deleteBoxGUI() {
    if(currentBox > 4) {
        currentBox--;
    } else if(currentBox > 0) {
        const board = document.getElementById("board");
        const row = board.querySelector("#row" + currentRow);
        const box = row.querySelector("#box" + (currentBox-1));
    
        const label = box.querySelector(".board-input");
        label.removeAttribute("id");
        label.innerHTML = "";
    
        guess.pop();
        currentBox--;
        console.log(guess);
    }
}

function updateBoxGUI(event) { // when letter button is clicked
    if(guess.length < 3) {
        const board = document.getElementById("board");
        const row = board.querySelector("#row" + currentRow);
        const box = row.querySelector("#box" + currentBox);
    
        let eventLabel;
        if(event.target.classList.contains("char")) {
            eventLabel = event.target;
        } else {
            eventLabel = event.target.querySelector(".char");
        }
    
        const label = box.querySelector(".board-input");
        label.id = eventLabel.id;
        label.innerHTML = eventLabel.innerHTML;
    
        guess.push(label.innerHTML);
        currentBox++;
        console.log(guess);
    }
}

function takeKeyInput(event) {
    const keys = ['1', '2', '3', '4', '5', '6', '7', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'J', 'L', 'M', 'N', 'Q', 'R', 'W', 'Z'];
    const key = event.key.toUpperCase();

    if(keys.includes(key)) {
        //call updateBoxGUI with event
        const button = document.querySelector('#letter' + key);

        if(button == null) {
            return;
        }

        // Create a new event to pass to updateBoxGUI
        const simulatedEvent = {
            target: button
        };

        // Call updateBoxGUI with the simulated event
        updateBoxGUI(simulatedEvent);
    } else if(event.keyCode == 13) {
        updateRowGUI(event);
    } else if(event.keyCode == 8) { // BACKSPACE
        deleteBoxGUI();
    }
}

async function fetchCodeName(codeName, train) {
    const input = [codeName, train];

    try {
        const response = await fetch('/api/getDisplayName', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(input)
        });
        const displayName = await response.text();

        console.log(displayName);
        return displayName;
    } catch(error) {
        console.error('Error:', error);
    }
}

async function updateWindow(outcome) {
    const popup = document.getElementById('end-game');

    const constText = document.getElementById("outcome");
    constText.innerHTML = outcome;

    const pathText = document.getElementById("path");

    try{
        const response = await fetch('/api/printPath');
        const path = await response.text();

        pathText.innerHTML = path;
        popup.style.display = 'flex';
    } catch(error) { 
        console.error('Error:', error);
    }
}

function updateCarNumber() {
    const date = new Date();

    var day = date.getDate();
    day = String(day).padStart(2, '0');

    var month = date.getMonth() + 1;
    month = String(month).padStart(2, '0');

    console.log("DAY: " + day);
    console.log("MONTH: " + month);

    const dateElements = document.querySelectorAll("#car-number");
    dateElements.forEach(function(dateElement) {dateElement.innerHTML = `${month}${day}`;});

}