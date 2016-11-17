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
    private final Line2D rect;
    private int width = 50;
    private int x = Constant.GAME_WIDTH / 2;
    private int y = Constant.GAME_HEIGHT - 50;
    private double angle = 90;
    private Color cannonColor = Color.YELLOW;

    public Cannon() {
        rect = new Line2D.Double();
    }

    @Override
    public void draw(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(cannonColor);
        rect.setLine(new Point2D.Double(x, y), new Point2D.Double(x + Math.cos(Math.toRadians(angle)) * width, y - Math.sin(Math.toRadians(angle)) * width));
        g2.draw(rect);
        g2.dispose();
    }

    public void turnLeft() {
        angle += 1;
    }

    public void turnRight() {
        angle -= 1;
    }

    public Point2D getFirePosition() {
        return rect.getP2();
    }

    public double getAngle() {
        return angle;
    }
}
