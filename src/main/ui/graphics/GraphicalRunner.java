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

    // EFFECTS: initializes the new buttons and their actions
    private void displayMainMenu() {
        instructionArea = displayInstructions();
        JButton runGameButton = new JButton("Play");
        JButton loadGameButton = new JButton("Load Game **DISABLED**");
        JButton viewLeaderboardButton = new JButton("View High Scores");
        JButton quitButton = new JButton("Quit");

        runGameButton.setFont(runGameButton.getFont().deriveFont(18f));
        runGameButton.setActionCommand("Play");
        runGameButton.addActionListener(this);
        viewLeaderboardButton.setFont(viewLeaderboardButton.getFont().deriveFont(18f));
        viewLeaderboardButton.setActionCommand("View Leaderboard");
        viewLeaderboardButton.addActionListener(this);
        quitButton.setFont(quitButton.getFont().deriveFont(18f));
        quitButton.setActionCommand("Quit");
        quitButton.addActionListener(this);
        loadGameButton.setFont(loadGameButton.getFont().deriveFont(18f));

        buttonPanel = new JPanel();
        buttonPanel.add(runGameButton, BorderLayout.PAGE_START);
        buttonPanel.add(loadGameButton);
        buttonPanel.add(viewLeaderboardButton);
        buttonPanel.add(quitButton, BorderLayout.PAGE_END);
        this.add(instructionArea, BorderLayout.PAGE_START);
        this.add(buttonPanel, BorderLayout.PAGE_END);
        pack();
    }

    // EFFECTS: displays set of instructions on how to play
    private JTextArea displayInstructions() {
        JTextArea textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setFont(textArea.getFont().deriveFont(18f));
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
        initGame();
        savedTime = 0;
        startTime = System.nanoTime();
    }

    // EFFECTS: loads a previously saved game.
    private void loadGame() {
        board = new Board();
        startTime = System.nanoTime();
    }

    // EFFECTS: initializes a Minesweeper game
    private void initGame() {
        board = new Board();
        board.generateBoard();
        graphicalBoard = new GraphicalBoard(board);
        add(graphicalBoard, BorderLayout.PAGE_START);
        JButton saveButton = new JButton("Save Game **DISABLED**");
        saveButton.setFont(saveButton.getFont().deriveFont(18f));
        add(saveButton, BorderLayout.PAGE_END);
        pack();
    }

    // EFFECTS: displays graphical leaderboard
    private void displayLeaderboard() {
        graphicalLeaderboard = new GraphicalLeaderboard();
        add(graphicalLeaderboard, BorderLayout.PAGE_START);
        JButton button1 = new JButton("Main Menu");
        JButton button2 = new JButton("Wipe Leaderboard Data "
                + "**WARNING: THIS ACTION CANNOT BE UNDONE**");

        button1.setFont(button1.getFont().deriveFont(18f));
        button1.addActionListener(e -> returnToMainMenuFromLeaderboard(button1, button2));

        button2.setFont(button2.getFont().deriveFont(18f));
        button2.addActionListener(e -> {
            try {
                leaderboard.writeScoresToFile(new ArrayList<>()); // write empty ArrayList to file
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
                try {
                    runGame();
                } catch (Exception exception) {
                    exception.printStackTrace();
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

    // EFFECTS: updates board based on whether or not the game is won or lost
    @Override
    public void update(Observable o, Object arg) {
        if (board.gameWon()) {
            double endTime = System.nanoTime();
            double timeElapsed = (endTime - startTime + savedTime) / 1000000000;
            try {
                leaderboard.addScoreToLeaderboard(timeElapsed, Leaderboard.LEADERBOARD_FILE);
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
        textArea.setFont(textArea.getFont().deriveFont(18f));
        textArea.setEditable(false);
        textArea.append("You win!\nYour time is: " + timeElapsed + " seconds\n");
        if (leaderboard.getLeaderboardIndex(leaderboard.getLeaderboard(), timeElapsed) != -1) {
            textArea.append("NEW HIGH SCORE! You ranked #"
                    + leaderboard.getLeaderboardIndex(leaderboard.getLeaderboard(), timeElapsed)
                    + ".");
        }

        JButton button = new JButton("OK");
        button.setFont(button.getFont().deriveFont(18f));
        button.addActionListener(e -> {
            remove(button);
            remove(textArea);
            displayMainMenu();
            repaint();
        });

        add(textArea);
        add(button, BorderLayout.PAGE_END);
        pack();
    }

    // EFFECTS: displays a dialog indicating the game is lost
    private void displayGameLostMessage() {
        JTextArea textArea = new JTextArea();
        textArea.setFont(textArea.getFont().deriveFont(18f));
        textArea.setEditable(false);
        textArea.append("Game over!");
        JButton button = new JButton("OK");
        button.setFont(button.getFont().deriveFont(18f));
        button.addActionListener(e -> {
            remove(button);
            remove(textArea);
            displayMainMenu();
            repaint();
        });

        add(textArea);
        add(button, BorderLayout.PAGE_END);
        pack();
    }
}
