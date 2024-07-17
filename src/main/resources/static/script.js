var guess = [];
var currentRow = 1;
var currentBox = 1;

function inputGuess() {
    alert('Button clicked!');
}

function updateBoard(event) {
    if(currentBox == 4) {
        if(currentRow == 6) {
            alert("END GAME!");
        } else {
            currentRow++;
            currentBox = 1;

            // Send the data to the Spring Boot controller using Fetch API
            fetch('/api/submitData', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(guess)
            })
            .then(response => response.text())
            .then(responseText => {
                console.log(responseText);
            })
            .catch(error => console.error('Error:', error));

            guess = [];
        }
    }

    const board = document.getElementById("board");
    const row = board.querySelector("#row" + currentRow);
    const box = row.querySelector("#box" + currentBox);

    const label = box.querySelector(".board-input");
    label.id = event.target.id;
    label.innerHTML = event.target.innerHTML;

    guess.push(label.innerHTML);
    console.log(guess);
    currentBox++;
}

// TODO: take button & keyboard input
const buttons = document.querySelectorAll('button');
buttons.forEach(function(button) {button.addEventListener('click', updateBoard);});
