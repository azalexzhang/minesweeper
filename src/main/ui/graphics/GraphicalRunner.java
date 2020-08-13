package ui.graphics;

import model.Board;
import model.Leaderboard;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import static model.Board.*;

public class GraphicalRunner extends JFrame implements ActionListener, Observer {
    private GraphicalBoard graphicalBoard;
    private GraphicalLeaderboard graphicalLeaderboard;
    private Board board;
    private Leaderboard leaderboard;
    private JTextArea instructionArea;
    private JPanel buttonPanel;
    private double startTime;
    private double savedTime;

    // EFFECTS: constructs a graphical runner that runs the graphics of the game
    public GraphicalRunner() {
        super("Minesweeper");
        setLayout(new BorderLayout());
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        board = null;
        leaderboard = new Leaderboard();
        graphicalBoard = null;
        graphicalLeaderboard = new GraphicalLeaderboard();
        displayMainMenu();
        setVisible(true);
    }

    // EFFECTS: displays the main menu with instructions and a button panel
    private void displayMainMenu() {
        instructionArea = displayInstructions();
        addButtonPanel();
        this.add(instructionArea, BorderLayout.PAGE_START);
        this.add(addButtonPanel(), BorderLayout.PAGE_END);
        pack();
    }

    // EFFECTS: adds buttons to main menu
    private JPanel addButtonPanel() {
        JButton runGameButton = new JButton("Play");
        JButton loadGameButton = new JButton("Load Game");
        JButton viewLeaderboardButton = new JButton("View High Scores");
        JButton quitButton = new JButton("Quit");

        setButtonFont(runGameButton);
        runGameButton.setActionCommand("Play");
        runGameButton.addActionListener(this);
        setButtonFont(viewLeaderboardButton);
        viewLeaderboardButton.setActionCommand("View Leaderboard");
        viewLeaderboardButton.addActionListener(this);
        setButtonFont(quitButton);
        quitButton.setActionCommand("Quit");
        quitButton.addActionListener(this);
        setButtonFont(loadGameButton);
        loadGameButton.setActionCommand("Load Game");
        loadGameButton.addActionListener(this);

        buttonPanel = new JPanel();
        buttonPanel.add(runGameButton, BorderLayout.PAGE_START);
        buttonPanel.add(loadGameButton);
        buttonPanel.add(viewLeaderboardButton);
        buttonPanel.add(quitButton, BorderLayout.PAGE_END);
        return buttonPanel;
    }

    // EFFECTS: displays set of instructions on how to play
    private JTextArea displayInstructions() {
        JTextArea textArea = new JTextArea();
        textArea.setEditable(false);
        setTextAreaFont(textArea);
        textArea.append("MINESWEEPER\n");
        textArea.append("- The goal of Minesweeper is to clear a board with individual squares, without"
                + " touching any mines.\n");
        textArea.append("- You can either select a square to uncover it, or flag it to indicate the"
                + " location of a mine.\n");
        textArea.append("- Hover your mouse over the square you want to modify. Left click to uncover"
                + " it. Right click to flag it.\n");
        textArea.append("- If you manage to clear all squares that don't hold a mine, you win the game."
                + " If you uncover a square that holds a mine, however, the game is over!");
        return textArea;
    }

    // EFFECTS: runs a Minesweeper game
    private void runGame() {
        board = new Board();
        board.generateBoard();
        savedTime = 0;
        startTime = System.nanoTime();
        initGame(startTime, savedTime);
    }

    // EFFECTS: loads a previously saved game.
    private void loadGame() throws IOException {
        board = new Board();
        savedTime = board.loadBoard(DIMENSIONS_TIME_FILE, COLUMN_FILE);

        if (savedTime == 0) {
            handleLoadGameFailed();
        } else {
            startTime = System.nanoTime();
            initGame(startTime, savedTime);
        }
    }

    // EFFECTS: initializes a Minesweeper game
    private void initGame(double startTime, double savedTime) {
        graphicalBoard = new GraphicalBoard(board, X_DIMENSION, Y_DIMENSION);
        add(graphicalBoard, BorderLayout.PAGE_START);
        JButton saveButton = new JButton("<html>Save Game and Quit<br>**WARNING: This will overwrite"
                + "<br>any previously saved game!**</html>");
        setButtonFont(saveButton);
        saveButton.addActionListener(e -> {
            saveBoardAndTimeElapsed(startTime, savedTime);
            dispose();
        });
        add(saveButton, BorderLayout.PAGE_END);
        pack();
    }

    // EFFECTS: saves board with time elapsed so far
    private void saveBoardAndTimeElapsed(double startTime, double savedTime) {
        try {
            double endTime = System.nanoTime();
            savedTime += endTime - startTime;
            board.saveBoard(savedTime, DIMENSIONS_TIME_FILE, COLUMN_FILE);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    // EFFECTS: sets the font of the text on the buttons
    private void setButtonFont(JButton button) {
        // The default font size was too small on my 4K (3840 by 2160 pixels) computer screen.
        button.setFont(button.getFont().deriveFont(18f));
    }

    // EFFECTS: sets the font of the text in text areas
    private void setTextAreaFont(JTextArea textArea) {
        textArea.setFont(textArea.getFont().deriveFont(18f));
    }

    // EFFECTS: displays graphical leaderboard
    private void displayLeaderboard() {
        graphicalLeaderboard = new GraphicalLeaderboard();
        add(graphicalLeaderboard, BorderLayout.PAGE_START);
        JButton button1 = new JButton("Main Menu");
        JButton button2 = new JButton("Wipe Leaderboard Data "
                + "**WARNING: THIS ACTION CANNOT BE UNDONE**");

        setButtonFont(button1);
        button1.addActionListener(e -> returnToMainMenuFromLeaderboard(button1, button2));

        setButtonFont(button2);
        button2.addActionListener(e -> {
            try {
                // write empty ArrayList to file
                leaderboard.writeScoresToFile(new ArrayList<>(), Leaderboard.LEADERBOARD_FILE_EASY);
            } catch (IOException exception) {
                exception.printStackTrace();
            }
            returnToMainMenuFromLeaderboard(button1, button2);
        });

        add(button1);
        add(button2, BorderLayout.PAGE_END);
        pack();
        repaint();
    }

    // EFFECTS: removes all buttons from leaderboard screen
    private void returnToMainMenuFromLeaderboard(JButton button1, JButton button2) {
        remove(graphicalLeaderboard);
        remove(button1);
        remove(button2);
        displayMainMenu();
        repaint();
    }

    // EFFECTS: decides which action to perform based on button clicked from main menu
    @Override
    public void actionPerformed(ActionEvent e) {
        remove(instructionArea);
        remove(buttonPanel);

        switch (e.getActionCommand()) {
            case "Play":
                runGame();
                break;
            case "Load Game":
                try {
                    loadGame();
                } catch (Exception exception) {
                    handleLoadGameFailed();
                }
                break;
            case "View Leaderboard":
                displayLeaderboard();
                break;
            case "Quit":
                dispose(); // closes window
                break;
        }
    }

    // EFFECTS: if an exception was thrown while loading saved game (such as when there was no saved
    //          game in the first place), displays dialog asking if the user wants to start a new game
    private void handleLoadGameFailed() {
        JTextArea noLoadedGameTextArea = new JTextArea();
        setTextAreaFont(noLoadedGameTextArea);
        noLoadedGameTextArea.append("Game could not be loaded: Either there is no previously saved game"
                + " or there was an error trying to load the previously saved game.\nWould you like"
                + " to start a new game instead?");

        JButton yesButton = new JButton("Yes");
        JButton noButton = new JButton("No");

        setButtonFont(yesButton);
        yesButton.addActionListener(e -> {
            removeLoadGameMessage(noLoadedGameTextArea, yesButton, noButton);
            runGame();
        });

        setButtonFont(noButton);
        noButton.addActionListener(e -> {
            removeLoadGameMessage(noLoadedGameTextArea, yesButton, noButton);
            displayMainMenu();
            repaint();
        });

        add(noLoadedGameTextArea, BorderLayout.PAGE_START);
        add(yesButton);
        add(noButton, BorderLayout.PAGE_END);
        pack();
    }

    // EFFECTS: removes load game failed message box
    private void removeLoadGameMessage(JTextArea textArea, JButton yesButton, JButton noButton) {
        remove(yesButton);
        remove(noButton);
        remove(textArea);
    }

    // EFFECTS: updates board based on whether or not the game is won or lost
    @Override
    public void update(Observable o, Object arg) {
        if (board.gameWon()) {
            double endTime = System.nanoTime();
            double timeElapsed = (endTime - startTime + savedTime) / 1000000000;
            try {
                leaderboard.addScoreToLeaderboard(timeElapsed, Leaderboard.LEADERBOARD_FILE_EASY);
            } catch (IOException e) {
                e.printStackTrace();
            }

            remove(graphicalBoard);
            displayGameWonMessage(timeElapsed);
        } else if (board.gameLost()) {
            remove(graphicalBoard);
            displayGameLostMessage();
        }
    }

    // EFFECTS: displays a dialog indicating the game is won
    private void displayGameWonMessage(double timeElapsed) {
        JTextArea textArea = new JTextArea();
        JButton button = new JButton("OK");

        makeDialogBox(textArea, button);

        textArea.append("You win!\nYour time is: " + timeElapsed + " seconds\n");
        if (leaderboard.getLeaderboardIndex(leaderboard.getLeaderboard(), timeElapsed) != -1) {
            textArea.append("NEW HIGH SCORE! You ranked #"
                    + leaderboard.getLeaderboardIndex(leaderboard.getLeaderboard(), timeElapsed)
                    + ".");
        }

        add(textArea);
        add(button, BorderLayout.PAGE_END);
        pack();
    }

    // EFFECTS: displays a dialog indicating the game is lost
    private void displayGameLostMessage() {
        JTextArea textArea = new JTextArea();
        JButton button = new JButton("OK");

        makeDialogBox(textArea, button);

        textArea.append("Game over!");

        add(textArea);
        add(button, BorderLayout.PAGE_END);
        pack();
    }

    // EFFECTS: creates the dialog box with text indicating if the game is won or lost
    private void makeDialogBox(JTextArea textArea, JButton button) {
        setTextAreaFont(textArea);
        textArea.setEditable(false);
        setButtonFont(button);
        button.addActionListener(e -> {
            remove(button);
            remove(textArea);
            displayMainMenu();
            repaint();
        });
    }
}
