package ru.rep1.game;

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
        Field field = new Field();
        add(field);

        setResizable(false);
        pack();

        field.placeButtons();

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
