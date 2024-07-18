var guess = [];
var currentRow = 1;
var currentBox = 1;

window.addEventListener('DOMContentLoaded', function() {
    fetch('/api/generatePath')
        .then(response => response.json())
        .then(path => {
            console.log("Received path:", path);
        })
        .catch(error => console.error('Error:', error));
});

function inputGuess() {
    alert('Button clicked!');
}

const letterButtons = document.querySelectorAll('.letter');
letterButtons.forEach(function(button) {button.addEventListener('click', updateGUI);});

const enterButton = document.querySelector("#enter");
enterButton.addEventListener('click', updateRowGUI);

const deleteButton = document.querySelector("#delete");
deleteButton.addEventListener('click', deleteBoxGUI);

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
            if(currentRow == 6) {
                alert('END GAME!');
            } else {
                // currentRow++;
                currentBox = 1;
                guess = [];
            }
        })
        .catch(error => console.error('Error:', error));
    }
}

async function updateRowGUIHelper() {
    fetch('/api/updateBoard')
        .then(response => response.json())
        .then(colors => {
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

                console.log("Board updated successfully.");
            } else {
                console.error("Received colors array is invalid:", colors);
            }

            currentRow++;
        })
        .catch(error => console.error('Error:', error));
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
    console.log(guess);
}

function updateGUI(event) {
    if(guess.length < 3) {
        updateBoxGUI(event);
        currentBox++;
    }
}
