package ru.rep1.game;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

/**
 * Created by lshi on 17.11.2016.
 */
public class Cannon implements Drawable {
    private final Image image;
    private int x = 170;
    private int y = 40;
    private final int WIDTH = 40;
    private final int LENGTH = 132;
    private double angle = 0;

    public Cannon() {
        image = Utils.loadImage("minigun_room.png");
    }

    @Override
    public void draw(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        AffineTransform trans = new AffineTransform();
        trans.rotate(Math.toRadians(angle), x, y);
        g2.transform(trans);

        g2.drawImage(image, x, y, null);

        g2.dispose();
    }

    public void turnLeft() {
        angle += 1;
        controlAngle();
    }

    public void turnRight() {
        angle -= 1;
        controlAngle();
    }

    public Point2D getFirePosition() {
        double nx = (x + WIDTH/2) + LENGTH * Math.sin(Math.toRadians(-angle));
        double ny = y + LENGTH * Math.cos(Math.toRadians(angle));
        return new Point2D.Double(nx, ny);
    }

    public double getAngle() {
        return angle;
    }

    private void controlAngle() {
        if (angle > 30) {
            angle = 30;
        }
        if (angle < -30) {
            angle = -30;
        }
    }
}
