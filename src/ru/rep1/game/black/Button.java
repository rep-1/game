package ru.rep1.game.black;

import ru.rep1.game.ButtonPress;
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

    private Runnable onPress;
    private Runnable onClick;

    private ButtonPress bp;

    public Button(String imgDefault, String imgPressed) {
        super();
        imageDefault = Utils.loadImage(imgDefault);
        imagePressed = Utils.loadImage(imgPressed);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (onClick != null) {
                    onClick.run();
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {
                changeState(State.PRESSED);
                if (bp != null) {
                    bp.mousePressed(e);
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (bp != null) {
                    bp.mouseReleased(e);
                }
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
        g2.addRenderingHints(new RenderingHints(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY));

        if (state == State.PRESSED) {
            g2.drawImage(imagePressed, 0, 0, getWidth(), getHeight(), null);
        } else {
            g2.drawImage(imageDefault, 0, 0, getWidth(), getHeight(), null);
        }

        g2.dispose();
    }

    public Runnable getOnPress() {
        return onPress;
    }

    public void setOnPress(Runnable onPress) {
        this.onPress = onPress;
        if (this.onPress != null) {
            this.bp = new ButtonPress(onPress);
        }
    }

    public Runnable getOnClick() {
        return onClick;
    }

    public void setOnClick(Runnable onClick) {
        this.onClick = onClick;
    }
}
