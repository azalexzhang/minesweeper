# My Personal Project: Recreating Minesweeper in Java

## Alex Zhang

**Overview**  
This project intends to re-create the classic Minesweeper game. The object of the game is to clear a board
with individual squares, without hitting a square containing a mine. Each individual square will contain an
integer from 1 to 8 to give a hint of how many mines there are in adjacent squares. The scoring system that
will be implemented will simply be the time taken to beat the game in seconds, therefore meaning that a
lower score is better.

**Who will use it?**  
It can be used by pretty much anyone, including other CPSC 210 students who just need a break from their
Java projects as well. :)

**Why is this project of interest to me?**  
I used to often play this game during the Windows 7 years, when Minesweeper came preinstalled (although I
wasn't very good), and figured that it would be fairly easy to implement in Java.

**User Stories**  
- As a user, I want to be able to choose a difficulty level.
- As a user, I want to be able to select a square and clear it.
- As a user, I want to be able to "flag" a square I believe holds a mine.
- As a user, I want to be able to save my current game so that I can load it when I open it again.
- As a user, I want to be able to load a game I had previously left off and resume it.
- As a user, I want to be able to save my score to a leaderboard. (This is actually done automatically when
the game is won.)
- As a user, I want to be able to view the leaderboard.
- As a user, I want to be able to erase all data from the leaderboard.

**Notes for Phase 4**
- 

## Instructions for Grader (Phase 3)
- You can generate the first required event by clicking a button on the main menu. Click the button "View
Leaderboard", and the program will load the leaderboard from the text file *leaderboard.txt*.
- You can generate the second required event by clicking a button in the leaderboard page. You can
completely erase all leaderboard data by clicking the button "Wipe Leaderboard Data".
- You can locate my visual component by starting the game. Click "Play Game" in the main menu. The board is
a grid of PNG files representing the state of each square on the board.
- You can save the state of my application by clicking the "Save Game" button while playing a game.
- You can reload the state of my application by clicking the "Load Game" button from the main menu.