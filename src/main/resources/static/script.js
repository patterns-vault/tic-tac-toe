// Winning combinations for Tic-Tac-Toe
const winningCombinations = [
    [0, 1, 2], // Top row
    [3, 4, 5], // Middle row
    [6, 7, 8], // Bottom row
    [0, 3, 6], // Left column
    [1, 4, 7], // Middle column
    [2, 5, 8], // Right column
    [0, 4, 8], // Diagonal from top-left to bottom-right
    [2, 4, 6]  // Diagonal from top-right to bottom-left
];

// A helper function to determine the game status based on the board state
function determineStatus(board) {
    // Check for any winning combination
    for (const [a, b, c] of winningCombinations) {
        if (board[a] && board[a] === board[b] && board[a] === board[c]) {
            return `Player ${board[a]} Wins!`;
        }
    }

    // Check if the board is full (no null values) and there is no winner
    if (board.every(cell => cell !== null)) {
        return "Draw";
    }

    // Default case: determine whose turn it is
    return board.filter(cell => cell === null).length % 2 === 0 ? "Player X's turn" : "Player O's turn";
}

// This function fetches the game state from the backend
async function fetchGameState() {
    const url = "/api/v1/game-state"; // URL to the Spring Boot controller endpoint

    try {
        // Make a GET request to the backend
        const response = await fetch(url);

        // Check if the response is successful
        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }

        // Parse the JSON response
        const { boardState } = await response.json();

        return {
            board: boardState.split('').map(cell => (cell === '-' ? null : cell)),
            status: determineStatus(boardState.split('').map(cell => (cell === '-' ? null : cell)))
        };

    } catch (error) {
        // Log any errors during the fetch
        console.error("Error fetching game state data:", error);
        return null; // Return null if fetching fails
    }
}

// Track the last rendered board state
let lastRenderedBoard = null;
let isGameCompleted = false; // Flag to track if the game has completed

// Function to fetch and render the game state
async function fetchAndRenderGameState() {
    const gameState = await fetchGameState();

    if (gameState) {
        const isEmptyBoard =
            gameState.board.every(cell => cell === null) || // All null cells
            gameState.board.join('') === '---------';      // Original string check (if needed)

        if (isEmptyBoard) {
            // Check if this is the **start of a new game**
            if (isGameCompleted) {
                console.log("Starting a new game.");
                isGameCompleted = false; // Reset the flag for new game detection
            } else if (lastRenderedBoard && lastRenderedBoard.some(cell => cell !== null)) {
                // Prevent rendering after a game ends (end of previous game)
                console.log("Backend sent empty board - keeping previous board state.");
                return;
            }
        }

        // Update the rendered board and track the last rendered board state
        updateGameBoard(gameState.board);
        updateStatus(gameState.status);
        lastRenderedBoard = gameState.board;

        // Mark the game as completed if there's a winner or a draw
        if (gameState.status.includes("Wins") || gameState.status === "Draw") {
            isGameCompleted = true;
            highlightWinningLine(gameState.board);
        }
    } else {
        console.error("No game state fetched!");
    }
}

// Function to render the game board
function updateGameBoard(board) {
    const cells = document.querySelectorAll('.cell');
    cells.forEach((cell, index) => {
        const cellValue = board[index]; // Get the cell value (e.g., 'X', 'O', or null)

        cell.textContent = cellValue; // Update cell content
        cell.classList.toggle('taken', cellValue !== null); // Add 'taken' class if occupied

        // Dynamically apply color based on the current role
        if (cellValue === currentRole) {
            cell.style.color = '#007bff'; // Apply blue color to current role figures
        } else {
            cell.style.color = ''; // Reset color for other figures
        }

        cell.classList.remove('winning'); // Remove any previous winning highlight
    });
}

// Function to update the game status
function updateStatus(status) {
    const statusElement = document.getElementById('status');
    statusElement.textContent = status; // Update the status (e.g., "Player X's turn", "Game Over")
}

// Function to highlight the winning line
function highlightWinningLine(board) {
    const cells = document.querySelectorAll('.cell');
    for (let combination of winningCombinations) {
        const [a, b, c] = combination;
        if (board[a] && board[a] === board[b] && board[a] === board[c]) {
            // Highlight the winning line
            combination.forEach(index => {
                cells[index].classList.add('winning'); // Add 'winning' class to winning cells
            });
            break;
        }
    }
}

// Track the current role of the instance
let currentRole = null;

// Function to fetch the game state from the backend
async function fetchInstanceRole() {
    const url = "/api/v1/instance-role"; // Endpoint to get the instance and role

    try {
        // Make a GET request to the backend API
        const response = await fetch(url);

        // Check if the response is successful
        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }

        // Parse the JSON response
        const { instance, role } = await response.json();

        // Update the instance and role in the HTML
        updateInstanceAndRole(instance, role);
    } catch (error) {
        console.error("Error fetching instance role data:", error);
    }
}

// Function to update the instance and role in the HTML
function updateInstanceAndRole(instance, role) {
    const instanceNameElement = document.getElementById("instance-name");
    const roleElement = document.getElementById("role");

    // Update the instance name and role text
    instanceNameElement.textContent = instance;
    roleElement.textContent = role;

    // Update the current role globally for styling purposes
    currentRole = role;

    // Ensure the current board reflects the role-based colors
    updateGameBoard(lastRenderedBoard);
}
// Polling logic to fetch instance and role from the backend every 1 second
setInterval(fetchInstanceRole, 500);

// Initial fetch of the instance and role
fetchInstanceRole();

// Polling logic to fetch and render the game state every 1 second
const pollingInterval = setInterval(fetchAndRenderGameState, 500);

// Initial render of the board
fetchAndRenderGameState();
