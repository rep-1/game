package ru.rep1.game;

import java.awt.*;

/**
 * Created by lshi on 17.11.2016.
 */
public class Bullet implements Drawable {
    private double x;
    private double y;
    private double angle;
    private double speed = 2;
    private double diam = 5;
    private boolean isShot = false;

    @Override
    public void draw(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setColor(Color.GREEN);
        g2.fillOval((int)x, (int)y, (int)diam, (int)diam);
        //g2.fillRect(getBounds().x, getBounds().y, getBounds().width, getBounds().height);
        g2.dispose();
    }

    public void move() {
        x += speed * Math.cos(Math.toRadians(angle));
        y -= speed * Math.sin(Math.toRadians(angle));
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
