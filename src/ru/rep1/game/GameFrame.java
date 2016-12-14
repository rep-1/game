package ru.rep1.game;

import ru.rep1.game.black.Lab;

import javax.swing.*;
import java.awt.*;

/**
 * Created by lshi on 17.11.2016.
 */
public class GameFrame extends JFrame {
    public GameFrame() throws HeadlessException {
        initUI();
    }

    private void initUI() {
        Lab room = new Lab();
        add(room);

        setResizable(false);
        pack();

        setTitle("Game");
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            GameFrame gameFrame = new GameFrame();
            gameFrame.setVisible(true);
        });
    }
}
