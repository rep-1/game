package ru.rep1.game;

import java.awt.*;

/**
 * Created by lshi on 17.11.2016.
 */
public class Bullet implements Drawable {
    private int x;
    private int y;
    private double angle;
    private double speed = 2;

    @Override
    public void draw(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setColor(Color.ORANGE);
        g2.fillOval(x, y, 10, 10);
        g2.dispose();
    }

    public void move() {
        x += speed * Math.sin(Math.toRadians(angle));
        y += speed * Math.cos(Math.toRadians(angle));
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public double getAngle() {
        return angle;
    }

    public void setAngle(double angle) {
        this.angle = angle;
    }
}
