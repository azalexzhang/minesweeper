package ui;

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
    private JButton runGameButton;
    private JButton viewLeaderboardButton;
    private JButton quitButton;
    private long startTime;
    public static GraphicalRunner runner = null;

    public static void main(String[] args) throws Exception {
        runner = new GraphicalRunner();
    }

    // EFFECTS: runs the graphics of the game
    public GraphicalRunner() throws Exception {
        super("Minesweeper");
        setLayout(new BorderLayout());
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        board = null;
        leaderboard = new Leaderboard();
        graphicalBoard = null;
        graphicalLeaderboard = new GraphicalLeaderboard();
        displayMainMenu();
        setVisible(true);
        //System.err.println("returning from constructor");

        /*long start = System.nanoTime();
        // busy loop
        while (!board.gameWon() && !board.gameLost()) {
            Thread.sleep(1);
        }
        long end = System.nanoTime();
        System.out.println((end - start) / 1000000000);*/
    }

    // EFFECTS: initializes the new buttons and their actions
    private void displayMainMenu() {
        runGameButton = new JButton("Play");
        viewLeaderboardButton = new JButton("View High Scores");
        quitButton = new JButton("Quit");

        runGameButton.setActionCommand("Play");
        runGameButton.addActionListener(this);
        viewLeaderboardButton.setActionCommand("View Leaderboard");
        viewLeaderboardButton.addActionListener(this);
        quitButton.setActionCommand("Quit");
        quitButton.addActionListener(this);

        add(runGameButton, BorderLayout.PAGE_START);
        add(viewLeaderboardButton);
        add(quitButton, BorderLayout.PAGE_END);
        pack();
    }

    // EFFECTS: runs a Minesweeper game
    private void runGame() throws Exception {
        initGame();
        startTime = System.nanoTime();
    }

    // EFFECTS: initializes a Minesweeper game
    private void initGame() {
        board = new Board();
        board.generateBoard();
        graphicalBoard = new GraphicalBoard(board);
        add(graphicalBoard, BorderLayout.PAGE_START);
        //setContentPane(graphicalBoard);
        pack();
    }

    // EFFECTS: displays graphical leaderboard
    private void displayLeaderboard() {
        graphicalLeaderboard = new GraphicalLeaderboard();
        add(graphicalLeaderboard, BorderLayout.PAGE_START);
        JButton button1 = new JButton("Main Menu");
        JButton button2 = new JButton("Wipe Leaderboard Data "
                + "**WARNING: THIS ACTION CANNOT BE UNDONE**");
        button1.addActionListener(e -> {
            returnToMainMenuFromLeaderboard(button1, button2);

        });

        button2.addActionListener(e -> {
            try {
                leaderboard.writeScoresToFile(new ArrayList<>());
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

    private void returnToMainMenuFromLeaderboard(JButton button1, JButton button2) {
        remove(graphicalLeaderboard);
        remove(button1);
        remove(button2);
        displayMainMenu();
        repaint();
    }

    // EFFECTS: removes all buttons from leaderboard screen

    @Override
    public void actionPerformed(ActionEvent e) {
        remove(runGameButton);
        remove(viewLeaderboardButton);
        remove(quitButton);
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
                dispose();
                break;
        }
    }

    @Override
    public void update(Observable o, Object arg) {
        if (board.gameWon()) {
            System.out.println("Game won");
            long end = System.nanoTime();
            long timeElapsed = (end - startTime) / 1000000000;
            try {
                leaderboard.addScoreToLeaderboard(timeElapsed, Leaderboard.LEADERBOARD_FILE);
            } catch (IOException e) {
                e.printStackTrace();
            }
            remove(graphicalBoard);
            displayMainMenu();
        } else if (board.gameLost()) {
            remove(graphicalBoard);
            displayMainMenu();
        }
    }
}
