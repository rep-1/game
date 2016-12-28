package ru.rep1.game.black;

import ru.rep1.game.Drawable;

import java.awt.*;
import java.awt.geom.AffineTransform;

/**
 * Created by lshi on 17.11.2016.
 */
public class Bullet implements Drawable {
    private double x;
    private double y;
    private double angle;
    private double speed = 4;
    private double diam = 5;
    private boolean isShot = false;

    @Override
    public void draw(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setColor(new Color(255, 253, 205));
        g2.fillOval((int)x, (int)y, (int)diam, (int)diam);

        g2.dispose();
    }

    public void move() {
        x += speed * Math.sin(Math.toRadians(angle));
        y += speed * Math.cos(Math.toRadians(angle));
    }

    public Rectangle getBounds() {
        return new Rectangle((int)x, (int)y, (int)diam, (int)diam);
    }

    public void shot() {
        isShot = true;
    }

    public boolean isShot() {
        return isShot;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getAngle() {
        return angle;
    }

    public void setAngle(double angle) {
        this.angle = angle;
    }
}
