package ru.rep1.game;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Point2D;
import java.util.Random;

/**
 * Created by lshi on 17.11.2016.
 */
public class Utils {
    public static int getRandomNumberInRange(int min, int max) {

        if (min >= max) {
            throw new IllegalArgumentException("max must be greater than min");
        }

        Random r = new Random();
        return r.nextInt((max - min) + 1) + min;
    }

    public static Image loadImage(String name) {
        ImageIcon image = new ImageIcon(Utils.class.getClassLoader().getResource(name));
        return image.getImage();
    }

    public static double getAngle(Point2D p1, Point2D p2) {
        final double deltaY = (p1.getY() - p2.getY());
        final double deltaX = (p2.getX() - p1.getX());
        final double result = Math.toDegrees(Math.atan2(deltaY, deltaX));
        return (result < 0) ? (360d + result) : result;
    }

    public static double getDistance(Point2D target, Point2D orig) {
        return Math.hypot(target.getX() - orig.getX(), target.getY() - orig.getY());
    }
}
