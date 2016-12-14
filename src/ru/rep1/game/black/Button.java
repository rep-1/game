package ru.rep1.game.black;

import ru.rep1.game.Constant;
import ru.rep1.game.Utils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Created by lshi on 14.12.2016.
 */
public class Button extends JLabel {
    private State state = State.DEFAULT;
    private final Image imageDefault;
    private final Image imagePressed;

    public Button(String imgDefault, String imgPressed) {
        super();
        imageDefault = Utils.loadImage(imgDefault);
        imagePressed = Utils.loadImage(imgPressed);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
            }

            @Override
            public void mousePressed(MouseEvent e) {
                changeState(State.PRESSED);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                changeState(State.DEFAULT);
            }
        });
    }

    private enum State {
        DEFAULT,
        PRESSED
    }

    private void changeState(State state) {
        this.state = state;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.addRenderingHints(new RenderingHints(RenderingHints.KEY_RENDERING,RenderingHints.VALUE_RENDER_QUALITY));

        if(state == State.PRESSED) {
            g2.drawImage(imagePressed, 0, 0, getWidth(), getHeight(), null);
        } else {
            g2.drawImage(imageDefault, 0, 0, getWidth(), getHeight(), null);
        }

        g2.dispose();
    }
}
