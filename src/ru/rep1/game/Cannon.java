package ru.rep1.game;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

/**
 * Created by lshi on 17.11.2016.
 */
public class Cannon implements Drawable {
    private int width = 100;
    private int height = 10;
    private int x = Constant.GAME_WIDTH / 2;
    private int y = Constant.GAME_HEIGHT - 50;
    private double angle = -90;
    private Color cannonColor = Color.YELLOW;
    Rectangle2D rect;

    public Cannon() {
        rect = new Rectangle2D.Double();
    }

    @Override
    public void draw(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.translate(x, y);
        g2.rotate(Math.toRadians(angle));
        g2.setColor(cannonColor);
        rect.setRect(0, 0, width, height);
        g2.fill(rect);
        g2.dispose();
    }

    public void turnLeft() {
        angle -= 1;
    }

    public void turnRight() {
        angle += 1;
    }

    public Point2D getFirePosition() {
        Rectangle2D r = new Rectangle2D.Double(x, y, width, height);
        return new Point2D.Double(r.getCenterX() , r.getCenterY());
    }

    public double getAngle() {
        return angle;
    }
}
