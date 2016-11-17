package ru.rep1.game;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Created by lshi on 17.11.2016.
 */
public class ButtonPress extends MouseAdapter {
    private volatile boolean isPressed = false;
    private volatile boolean isRunning = false;
    private static final int sleep = 100;

    private final Runnable action;

    public ButtonPress(Runnable action) {
        this.action = action;
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1) {
            isPressed = true;
            thread();
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1) {
            isPressed = false;
        }
    }

    private synchronized boolean check() {
        if (isRunning) return false;
        isRunning = true;
        return true;
    }

    private void thread() {
        if (check()) {
            new Thread(() -> {
                do {
                    action.run();
                    try {
                        Thread.sleep(sleep);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } while (isPressed);
                isRunning = false;
            }).start();
        }
    }
}
