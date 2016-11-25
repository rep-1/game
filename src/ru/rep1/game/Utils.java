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

    public static double getAngle(Point2D target, Point2D orig) {
        double angle = (double) Math.toDegrees(Math.atan2(target.getY() - orig.getY(), target.getX() - orig.getX()));

        if(angle < 0){
            angle += 360;
        }

        return angle;
    }

    public static double getDistance(Point2D target, Point2D orig) {
        return Math.hypot(target.getX() - orig.getX(), target.getY() - orig.getY());
    }
}
