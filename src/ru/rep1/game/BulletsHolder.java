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
    private Image bulletImage;

    private final int MAX = 7;
    private volatile int currentCount = MAX;

    private final int WIDTH = 70;

    public BulletsHolder() {
        initView();
    }

    private void initView() {
        bulletImage = Utils.loadImage("bullet.png");
    }

    private void drawBullets(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        int x = 537;
        synchronized (BulletsHolder.class) {
            for (int i = 0; i < currentCount; i++) {
                x += 30;
                g2.drawImage(bulletImage, x, 210, null);
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
        EventBus.getInstance().publish(Constant.Event.ON_RELOAD_START.name());
        new Thread(() -> {

            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            synchronized (BulletsHolder.class) {
                if (currentCount <= 0) {
                    currentCount = MAX;
                    EventBus.getInstance().publish(Constant.Event.ON_RELOAD_END.name());
                }
            }
        }).start();
    }

    @Override
    public void draw(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();

        drawBullets(g);


        synchronized (BulletsHolder.class) {
            if (currentCount <= 0) {
                reload();
            }
        }
    }
}
