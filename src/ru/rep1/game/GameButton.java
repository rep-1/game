package ru.rep1.game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * Created by lshi on 22.11.2016.
 */
public class GameButton extends JLabel {
    private final Image image;
    private ButtonPress bp;
    private Runnable onClick;

    public GameButton(String imageSrc, Runnable action, Runnable onClick) {
        super();
        this.image = Utils.loadImage(imageSrc);
        if(action != null) {
            this.bp = new ButtonPress(action);
        }
        this.onClick = onClick;

        setIcon(new ImageIcon(image));

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(onClick != null) {
                    onClick.run();
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {
                if(bp != null) {
                    bp.mousePressed(e);
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if(bp != null) {
                    bp.mouseReleased(e);
                }
            }
        });
    }
}
