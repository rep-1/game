package ru.rep1.game.black;

import ru.rep1.game.Cannon;
import ru.rep1.game.Constant;
import ru.rep1.game.Utils;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import static ru.rep1.game.Scale.*;

/**
 * Created by lshi on 10.12.2016.
 */
public class Lab extends JPanel implements Runnable {
    private Image roomImg;
    private Cannon cannon;//747,303, 118x76

    public Lab() {
        init();
    }

    public void init() {
        setBackground(Color.BLACK);
        setPreferredSize(new Dimension(Constant.GAME_WIDTH, Constant.GAME_HEIGHT));
        setDoubleBuffered(true);
        setLayout(null);

        initResources();
        initRotateButtons();
        initFireButton();
    }

    private void initResources() {
        roomImg = Utils.loadImage("black/ResearchTerminal.png");
    }

    private void initRotateButtons() {
        Rectangle2D leftBounds = $(new Rectangle2D.Double(120, 793, 163, 195));
        Button leftButton = new Button("black/left_button.png", "black/left_button_pressed.png");
        add(leftButton);
        leftButton.setBounds(leftBounds.getBounds());

        Rectangle2D rightBounds = $(new Rectangle2D.Double(284, 793, 160, 195));
        Button rightButton = new Button("black/right_button.png", "black/right_button_pressed.png");
        add(rightButton);
        rightButton.setBounds(rightBounds.getBounds());
    }

    private void initFireButton() {
        Rectangle2D fireBounds = $(new Rectangle2D.Double(1475, 792, 321, 196));
        Button fireButton = new Button("black/fire_button.png", "black/fire_button_pressed.png");
        add(fireButton);
        fireButton.setBounds(fireBounds.getBounds());
    }

    @Override
    public void run() {

    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        drawRoom(g);
    }


    public void drawRoom(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g.drawImage(roomImg, 0, 0, Constant.GAME_WIDTH, Constant.GAME_HEIGHT, null);

        g2.dispose();
    }
}
