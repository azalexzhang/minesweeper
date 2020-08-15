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

**Phase 4: Task 2**  
Reader is a class in which some of the methods throw a checked exception (Java's built-in IOException for
all of them).

**Phase 4: Task 3**
- In Phase 1 I had the older Minesweeper class running the console version of the game to handle at least
two responsibilities, which were the game itself and the leaderboard. I had refactored the class so that the
code handling the leaderboard was put into a separate class. (This probably doesn't count for Task 3 of this
phase, but I thought it was good to mention anyway as an example of how I improved cohesion in the past.)
- The computer I use has a 4K screen (3840 by 2160 pixels), so the default font size of the text and the
buttons is way too small for me. I made the text in each JButton and JTextArea bigger with a line like the
following:  
textArea.setFont(textArea.getFont().derive(18f));  
What I ended up doing was not very cohesive, and I ended up copying a line like this maybe 10 times or so.
I wanted the font size of the text in the buttons and the text areas to be consistent, so I eventually
extracted two methods, one for JButtons, and one for JTextAreas, so that if I wanted to change the font size
for all of them at once, I would only need to change two values.
- There was some duplicate code in the methods displayGameWonMessage and displayGameLostMessage, so I
extracted the method makeDialogBox. There might be a way to make it simpler since there is still duplicate
code in those two methods. (The if statement in displayGameWonMessage is making it tough to figure out.)
- Initially I used the fields X_DIMENSION and Y_DIMENSION in the constructor for GraphicalBoard, but now I
have two private fields and two new int parameters x and y passed to the constructor. This will especially
help if I want to implement difficulty levels.
- Not necessarily related to cohesion and coupling, but I finally fixed LeaderboardTest.java. It now
rewrites the original test scores back to the test files so that the tests can pass every time they are run.

## Instructions for Grader (Phase 3)
- You can generate the first required event by clicking a button on the main menu. Click the button "View
Leaderboard", and the program will load the leaderboard from the text file *leaderboard.txt*.
- You can generate the second required event by clicking a button in the leaderboard page. You can
completely erase all leaderboard data by clicking the button "Wipe Leaderboard Data".
- You can locate my visual component by starting the game. Click "Play Game" in the main menu. The board is
a grid of PNG files representing the state of each square on the board.
- You can save the state of my application by clicking the "Save Game" button while playing a game.
- You can reload the state of my application by clicking the "Load Game" button from the main menu."# minesweeper" 
