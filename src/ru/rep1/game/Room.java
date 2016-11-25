package ru.rep1.game;

import javax.print.DocFlavor;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.concurrent.CountDownLatch;
import java.util.stream.Stream;

/**
 * Created by lshi on 22.11.2016.
 */
public class Room extends JPanel implements Runnable {
    private Thread animator;
    private Image roomImg;
    private Cannon cannon;
    private Target[] targets;
    private java.util.List<Bullet> bullets;
    private BulletsHolder bulletsHolder;
    private Rectangle2D[] walls;
    private Constant.State state;

    private CountDownLatch introLatch;

    public Room() {
        init();
    }

    public void init() {
        state = Constant.State.INTRO;

        roomImg = Utils.loadImage("room.png");
        cannon = new Cannon();
        initTargets();
        bullets = new ArrayList<>();
        bulletsHolder = new BulletsHolder();
        walls = new Rectangle2D[]{new Rectangle2D.Double(0, 0, 85, 331),
                new Rectangle2D.Double(0, 332, 36, 74),
                new Rectangle2D.Double(0, 405, 95, 64),
                new Rectangle2D.Double(85, 430, 55, 190),
                new Rectangle2D.Double(295, 76, 122, 254),
                new Rectangle2D.Double(395, 329, 470, 300),
                new Rectangle2D.Double(226, 447, 639, 181)};

        setBackground(Color.WHITE);
        setPreferredSize(new Dimension(Constant.GAME_WIDTH, Constant.GAME_HEIGHT));
        setDoubleBuffered(true);

        setLayout(null);

        GameButton leftButton = new GameButton("left_arrow.png", () -> {
            cannon.turnLeft();
            repaint();
        }, null);
        add(leftButton);
        leftButton.setBounds(500, 122, 80, 80);

        GameButton rightButton = new GameButton("right_arrow.png", () -> {
            cannon.turnRight();
            repaint();
        }, null);
        add(rightButton);
        rightButton.setBounds(740, 122, 80, 80);

        GameButton fireButton = new GameButton("minigun_control.png", null, this::fire);
        add(fireButton);
        fireButton.setBounds(500, 10, 324, 92);

        EventBus.getInstance().subscribe(Constant.Event.ON_RELOAD_START.name(), () -> {state = Constant.State.RELOAD;});
        EventBus.getInstance().subscribe(Constant.Event.ON_RELOAD_END.name(), () -> {state = Constant.State.PLAY;});
        EventBus.getInstance().subscribe(Constant.Event.ON_TARGET_IN_PLACE.name(), () -> {introLatch.countDown();});
        EventBus.getInstance().subscribe(Constant.Event.ON_GAME_START.name(), () -> {
            introLatch = new CountDownLatch(3);
            targets[0].moveByTrajectory();
            targets[1].moveByTrajectory();
            targets[2].moveByTrajectory();
            try {
                introLatch.await();
                EventBus.getInstance().publish(Constant.Event.ON_GAME_PLAY.name());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        EventBus.getInstance().subscribe(Constant.Event.ON_GAME_PLAY.name(), () -> {
            state = Constant.State.PLAY;
        });
        EventBus.getInstance().publish(Constant.Event.ON_GAME_START.name());
    }

    public void initTargets() {
        if(state == Constant.State.INTRO) {
            Target t1 = new Target(252, 494, 50, 175);
            t1.setTrajectory(new Point2D[]{
                    new Point2D.Double(178, 480),
                    new Point2D.Double(145, 366),
                    new Point2D.Double(80, 367)});
            Target t2 = new Target(283, 494, 185, 250);
            t2.setTrajectory(new Point2D[]{
                    new Point2D.Double(183, 480),
                    new Point2D.Double(233, 366),
                    new Point2D.Double(336, 364)});
            Target t3 = new Target(318, 494, 260, 318);
            t3.setTrajectory(new Point2D[]{
                    new Point2D.Double(183, 480),
                    new Point2D.Double(233, 366)});
            targets = new Target[]{
                    t1,
                    t2,
                    t3};
        }else if(state == Constant.State.PLAY) {
            //targets = new Target[]{
            //        new Target(Utils.getRandomNumberInRange(37, 160), 358, 37, 160),
            //        new Target(Utils.getRandomNumberInRange(170, 253), 358, 170, 253),
            //        new Target(Utils.getRandomNumberInRange(270, 350), 358, 270, 350)};
        }
    }

    private void fire() {
        if(bulletsHolder.getBullet()) {
            Bullet bullet = new Bullet();
            bullet.setX((int) cannon.getFirePosition().getX());
            bullet.setY((int) cannon.getFirePosition().getY());
            bullet.setAngle(-cannon.getAngle());
            synchronized (bullets) {
                bullets.add(bullet);
            }
            repaint();
        }
    }

    @Override
    public void addNotify() {
        super.addNotify();

        animator = new Thread(this);
        animator.start();
    }

    @Override
    public void run() {
        loop();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        drawRoom(g);

        if(state == Constant.State.INTRO) {
            drawIntro(g);
        } else if(state == Constant.State.PLAY){
            drawPlay(g);
        } else if(state == Constant.State.RELOAD) {
            drawPlay(g);
            drawReload(g);
        }
    }

    public void drawRoom(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g.drawImage(roomImg, 0, 0, null);

        g2.dispose();
    }

    public void drawIntro(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        Stream.of(targets).forEach((t) -> {
            t.draw(g);
        });

        g2.dispose();
    }

    public void drawPlay(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        cannon.draw(g);

        Stream.of(targets).forEach((t) -> {
            t.draw(g);
        });

        bullets.stream().forEach((b) -> {
            b.draw(g);
        });

        bulletsHolder.draw(g);

        g2.dispose();

        Toolkit.getDefaultToolkit().sync();
    }

    public void drawReload(Graphics g) {
        Graphics2D g21 = (Graphics2D)g.create();
        g21.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g21.setColor(Color.RED);
        g21.setFont(new Font(Font.MONOSPACED, Font.BOLD, 40));
        g21.drawString("ПЕРЕЗАРЯДКА!", 159, 239);
        g21.dispose();
    }

    public void loop() {
        long prevTime = System.currentTimeMillis();
        long diff;
        long sleep;

        while (true) {
            detectCollisions();

            bullets.stream().forEach((b) -> {
                b.move();
            });

            if(state == Constant.State.PLAY) {
                Stream.of(targets).forEach((t) -> {
                    t.move();
                });
            }

            repaint();

            diff = System.currentTimeMillis() - prevTime;
            sleep = 100 - diff;
            if (sleep < 0) sleep = 10;

            try {
                Thread.sleep(sleep);
            } catch (InterruptedException e) {
                System.out.println("Interrupted: " + e.getMessage());
            }

            prevTime = System.currentTimeMillis();
        }
    }

    private void detectCollisions() {
        synchronized (bullets) {
            Iterator<Bullet> bulletIterator = bullets.iterator();
            while (bulletIterator.hasNext()) {
                Bullet bullet = bulletIterator.next();
                if (bullet.isShot() ||
                        (bullet.getX() > Constant.GAME_WIDTH || bullet.getY() > Constant.GAME_HEIGHT) ||
                        (bullet.getX() < 0 || bullet.getY() < 0)) {
                    bulletIterator.remove();
                    continue;
                }
                Stream.of(walls).forEach((w) -> {
                    if(w.intersects(bullet.getBounds())) {
                        bulletIterator.remove();
                    }
                });
            }

            synchronized (targets) {
                Stream.of(targets).forEach((t) -> {
                    bullets.forEach((b) -> {
                        if (!b.isShot() && t.getBounds().intersects(b.getBounds())) {
                            t.shot();
                            b.shot();
                        }
                    });
                });
            }
        }
    }
}
