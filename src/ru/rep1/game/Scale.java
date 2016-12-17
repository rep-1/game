package ru.rep1.game;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

/**
 * Created by lshi on 14.12.2016.
 */
public class Scale {
    public static double $(double value) {
        return value * Constant.GLOBAL_SCALE_REV;
    }

    public static Rectangle2D.Double $(Rectangle2D.Double r) {
        return new Rectangle2D.Double($(r.getX()), $(r.getY()), $(r.getWidth()), $(r.getHeight()));
    }

    public static Point2D $(Point2D p) {
        return new Point2D.Double($(p.getX()), $(p.getY()));
    }
}
