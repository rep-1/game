package ru.rep1.game.black;

import ru.rep1.game.Drawable;
import ru.rep1.game.Utils;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import static ru.rep1.game.Scale.*;

/**
 * Created by lshi on 17.11.2016.
 */
public class Cannon implements Drawable {
    private final Image image;
    private int x = 170;
    private int y = 40;
    private int w = 40;
    private int h = 132;
    private double angle = 0;

    private Point2D center;
    private Point2D firePoint;

    public Cannon() {
        image = Utils.loadImage("minigun_room.png");
    }

    public Cannon(String imgSrc, Rectangle bounds) {
        image = Utils.loadImage(imgSrc);
        x = (int)bounds.getX();
        y = (int)bounds.getY();
        w = (int)bounds.getWidth();
        h = (int)bounds.getHeight();
        center = new Point2D.Double($(800), $(342));
        firePoint = new Point2D.Double($(880), $(346));
    }

    @Override
    public void draw(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.addRenderingHints(new RenderingHints(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY));

        AffineTransform trans = new AffineTransform();
        trans.rotate(Math.toRadians(angle), center.getX(), center.getY());
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
        double nx = center.getX() + (firePoint.getX() - center.getX())*Math.cos(Math.toRadians(angle)) - (firePoint.getY() - center.getY())*Math.sin(Math.toRadians(angle));
        double ny = center.getY() + (firePoint.getX() - center.getX())*Math.sin(Math.toRadians(angle)) + (firePoint.getY() - center.getY())*Math.cos(Math.toRadians(angle));
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
