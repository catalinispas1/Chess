# Chess Game README

Welcome to the Chess Game! This is a simple chess game implementation with several features, including square highlighting, checkmate detection, legal move validation, and a timer for each player. This README will guide you through the game's features, how to play, and how to set up the game.

## Table of Contents
- [Features](#features)
- [How to Play](#how-to-play)
- [Timer](#timer)
- [Setup](#setup)
- [Dependencies](#dependencies)
- [Contributing](#contributing)
- [License](#license)

---

## Features

### 1. Square Highlighting
- The chessboard squares change color when you click and release a piece, helping you visualize your selected piece.

### 2. Checkmate Detection
- The game automatically detects and ends when a checkmate condition is met, signaling the end of the game.

### 3. Legal Move Validation
- Pieces can only move legally according to the rules of chess. The game will prevent illegal moves, ensuring a fair and authentic chess-playing experience.

### 4. Timer
- Each player has a 10-minute timer to make their moves. The timer counts down during their turn and stops during their opponent's turn. If a player's time runs out, they lose the game.

### 5. User-Friendly Interface
- The game is designed with a user-friendly interface, making it easy to understand and play for both beginners and experienced chess players.

## How to Play

1. **Setup**: The chessboard is set up initially with pieces in their standard positions: pawns in the second row and other pieces in the first row.

2. **Selecting a Piece**: To move a piece, click on it. The selected piece will be highlighted, helping you identify your chosen piece.

3. **Valid Moves**: Legal moves for the selected piece will be highlighted on the board. Click on a valid destination square to move the piece there.

4. **Check and Checkmate**: The game will automatically check for check and checkmate conditions after each move. If a player's king is in check and no legal moves can be made to get out of check, it's a checkmate, and the game ends.

5. **Timer**: Keep an eye on your timer while playing. Each player has 10 minutes for their entire game. If your timer runs out, you lose.

6. **Game Over**: The game will announce the winner when checkmate occurs or when a player's timer runs out. You can then start a new game or exit.
