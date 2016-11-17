package ru.rep1.game;

import java.awt.*;
import java.awt.geom.Dimension2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;

/**
 * Created by lshi on 17.11.2016.
 */
public class Target implements Drawable {
    private double x;
    private double y;
    private double radius = 20;
    private Ellipse2D shape;
    private int shotCount = 2;
    private Color color = Color.RED;
    private int speed = 3;
    private double from;
    private double to;

    public Target() {
        shape = new Ellipse2D.Double();
    }

    public Target(double x, double y, double from, double to) {
        this();
        this.x = x;
        this.y = y;
        this.from = from;
        this.to = to;
    }

    @Override
    public void draw(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setColor(color);
        shape.setFrame(new Point2D.Double(x, y), new Dimension((int)radius, (int)radius));
        g2.fill(shape);
        g2.dispose();
    }

    public Rectangle getBounds() {
        return shape.getBounds();
    }

    public void shot() {
        shotCount--;
        if(shotCount == 1) {
            color = Color.PINK;
        }
        if(shotCount == 0) {
            color = Color.WHITE;

            speed = 0;
        }
    }

    public boolean isShot() {
        return shotCount <= 0;
    }

    public void move() {
        x += speed;

        if(x > to) {
            speed = -speed;
        }
        if(x <  from) {
            speed = -speed;
        }
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
}
