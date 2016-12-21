package ru.rep1.game;

import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.stream.Stream;

import static ru.rep1.game.Constant.BULLETHOLDER_MAX;
import static ru.rep1.game.Constant.BULLETHOLDER_RELOAD;
import static ru.rep1.game.Constant.BULLETHOLDER_WAIT;
import static ru.rep1.game.Scale.*;

/**
 * Created by lshi on 18.11.2016.
 */
public class BulletsHolder implements Drawable {
    private Rectangle2D rect;
    private Image[] bulletImage;
    private Image magazineImage;

    private final int MAX = BULLETHOLDER_MAX;
    private volatile int currentCount = MAX;

    private final int WIDTH = 70;

    public BulletsHolder() {
        initView();
    }

    private void initView() {

        bulletImage = new Image[]{
                Utils.loadImage("black/bullets_holder/bullets_0.png"),
                Utils.loadImage("black/bullets_holder/bullets_1.png"),
                Utils.loadImage("black/bullets_holder/bullets_2.png"),
                Utils.loadImage("black/bullets_holder/bullets_3.png"),
                Utils.loadImage("black/bullets_holder/bullets_full.png")};
        magazineImage = Utils.loadImage("black/bullets_holder/magazine.png");;
    }

    private void drawBullets(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        synchronized (BulletsHolder.class) {
            Image bullets = bulletImage[Math.min((int)Math.ceil(4 * (currentCount / (double)MAX)), 4)];
            g2.drawImage(bullets, (int)$(1600), (int)$(572), (int)$(92), (int)$(67), null);
        }
        g2.dispose();
    }



    private void drawMagazine(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.addRenderingHints(new RenderingHints(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY));

        g2.drawImage(magazineImage, (int)$(1476), (int)$(510), (int)$(325), (int)$(185), null);

        g2.dispose();
    }

    private volatile boolean flag = true;
    private volatile boolean working;

    public boolean getBullet() {
        synchronized (BulletsHolder.class) {
            if (!working) {
                working = true;
                new Thread(() -> {
                    try {
                        Thread.sleep(BULLETHOLDER_WAIT);
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
                Thread.sleep(BULLETHOLDER_RELOAD);
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

        drawMagazine(g);

        drawBullets(g);


        synchronized (BulletsHolder.class) {
            if (currentCount <= 0) {
                reload();
            }
        }
    }
}
