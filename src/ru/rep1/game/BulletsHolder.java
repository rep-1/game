package ru.rep1.game;

import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.stream.Stream;

/**
 * Created by lshi on 18.11.2016.
 */
public class BulletsHolder implements Drawable {
    private Rectangle2D rect;

    private final int MAX = 7;
    private volatile int currentCount = MAX;

    private final int WIDTH = 70;

    public BulletsHolder() {
        initView();
    }

    private void initView() {
        this.rect = new Rectangle2D.Double(Constant.GAME_WIDTH - 100, Constant.GAME_HEIGHT - 100, WIDTH, 20);
    }

    private void drawBullets(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        double x = rect.getX() + 10;
        synchronized (BulletsHolder.class) {
            for (int i = 0; i < currentCount; i++) {
                Line2D bulletView = new Line2D.Double(new Point2D.Double(x, rect.getY() + 2), new Point2D.Double(x, rect.getY() + 16));
                x = x + (WIDTH / MAX) - 2;
                g2.setColor(Color.GREEN);
                g2.draw(bulletView);
            }
        }
        g2.dispose();
    }

    private volatile boolean flag;
    private volatile boolean working;

    public boolean getBullet() {
        synchronized (BulletsHolder.class) {
            if (!working) {
                working = true;
                new Thread(() -> {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    flag = true;
                    working = false;
                }).start();

                if (flag) {
                    currentCount--;
                }
                if (currentCount < 0) {
                    currentCount = 0;
                    flag = false;
                }

                return flag;
            }
            return false;
        }
    }

    private void reload() {
        new Thread(() -> {

            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            synchronized (BulletsHolder.class) {
                if (currentCount <= 0) {
                    currentCount = MAX;
                }
            }
        }).start();
    }

    @Override
    public void draw(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setColor(Color.GREEN);

        Stroke oldStroke = g2.getStroke();
        g2.setStroke(new BasicStroke(2));
        g2.draw(this.rect);
        g2.setStroke(oldStroke);

        g2.dispose();

        drawBullets(g);


        synchronized (BulletsHolder.class) {
            if (currentCount <= 0) {
                reload();
            }
        }
    }
}
