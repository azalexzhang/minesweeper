package ui;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Observable;
import java.util.Observer;

// Represents an individual square on the board with its own MouseListener.
public class Listener extends Observable implements MouseListener {
    private int xxCoord; // x-coordinate of the square
    private int yyCoord; // y-coordinate of the square

    // EFFECTS: constructs a listener with the coordinates of the square and an observer (the graphical board)
    public Listener(int x, int y, Observer observer) {
        this.xxCoord = x;
        this.yyCoord = y;
        addObserver(observer);
        addObserver(GraphicalRunner.runner);
    }

    // EFFECTS: listens for mouse clicks on the component (the square on the board) and notifies runner
    @Override
    public void mouseClicked(MouseEvent e) {
        System.err.println("mouse clicked " + xxCoord + " " + yyCoord);
        setChanged();
        notifyObservers(e);
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    public int getX() {
        return xxCoord;
    }

    public int getY() {
        return yyCoord;
    }
}
