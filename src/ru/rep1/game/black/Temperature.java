package ru.rep1.game.black;

import javafx.util.Pair;
import ru.rep1.game.Drawable;

import static ru.rep1.game.Scale.*;

import ru.rep1.game.Utils;

import java.awt.*;
import java.awt.geom.Rectangle2D;

/**
 * Created by lshi on 17.12.2016.
 */
public class Temperature implements Drawable {
    private Pair<Image, Rectangle2D>[] parts;
    private volatile byte state;
    public final static int MIN_STATE = 0;
    public final static int MAX_STATE = 6;

    public Temperature() {
        init();
    }

    private void init() {
        state = MIN_STATE;
        parts = new Pair[]{
                new Pair(null ,null),
                new Pair(Utils.loadImage("black/temp/temp1.png"), $(new Rectangle2D.Double(239, 589, 90, 86))),
                new Pair(Utils.loadImage("black/temp/temp2.png"), $(new Rectangle2D.Double(239, 509, 90, 80))),
                new Pair(Utils.loadImage("black/temp/temp3.png"), $(new Rectangle2D.Double(239, 430, 90, 80))),
                new Pair(Utils.loadImage("black/temp/temp4.png"), $(new Rectangle2D.Double(239, 350, 90, 80))),
                new Pair(Utils.loadImage("black/temp/temp5.png"), $(new Rectangle2D.Double(239, 270, 90, 80))),
                new Pair(Utils.loadImage("black/temp/temp6.png"), $(new Rectangle2D.Double(239, 177, 90, 91)))
        };
    }

    @Override
    public void draw(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.addRenderingHints(new RenderingHints(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY));

        for (int i = MIN_STATE; i <= state; i++) {
            if (i <= MIN_STATE) {
                continue;
            } else {
                Image img = parts[i].getKey();
                Rectangle2D bound = parts[i].getValue();
                g2.drawImage(img, (int) bound.getX(), (int) bound.getY(), (int) bound.getWidth(), (int) bound.getHeight(), null);
            }
        }

        g2.dispose();
    }

    public synchronized void inc() {
        state += 1;
        if (state > MAX_STATE) {
            state = MAX_STATE;
        }
    }

    public synchronized void dec() {
        state -= 1;
        if (state < MIN_STATE) {
            state = MIN_STATE;
        }
    }

    public byte getState() {
        return state;
    }
}
