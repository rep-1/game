package ru.rep1.game;

import java.awt.*;
import java.awt.geom.Dimension2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by lshi on 17.11.2016.
 */
public class Target implements Drawable {
    private volatile double x;
    private volatile double y;
    private double radius = 10;
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
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(color);
        shape.setFrameFromCenter(x, y, x + radius, y + radius);
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
            color = Color.BLACK;

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

    private Point2D[] trajectory;
    private volatile int tIndex;
    private Point2D orig;
    private double trSpeed = 1;
    public void setTrajectory(Point2D[] t) {
        this.trajectory = t;
        tIndex = 0;
        orig = new Point2D.Double(x, y);
    }
    public void moveByTrajectory() {
        if(trajectory == null) return;

        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                synchronized (Target.class) {
                    if (tIndex < trajectory.length) {
                        Point2D to = trajectory[tIndex];

                        double angle = Utils.getAngle(to, orig);

                        x = x - trSpeed * Math.cos(Math.toRadians(angle));
                        y = y + trSpeed * Math.sin(Math.toRadians(angle));

                        Point2D curr = new Point2D.Double(x, y);

                        if (Utils.getDistance(to, curr) < 2D) {
                            tIndex++;
                            orig = to;
                        }
                    }
                    EventBus.getInstance().publish(Constant.Event.ON_TARGET_IN_PLACE.name());
                }
            }
        }, 0, 10);
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

    public double getRadius() {
        return radius;
    }
}
