package ru.rep1.game;

import javax.print.DocFlavor;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
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

    public Room() {
        init();
    }

    public void init() {
        roomImg = Utils.loadImage("room.png");
        cannon = new Cannon();
        targets = new Target[]{
                new Target(Utils.getRandomNumberInRange(37, 160), 358, 37, 160),
                new Target(Utils.getRandomNumberInRange(170, 253), 358, 170, 253),
                new Target(Utils.getRandomNumberInRange(270, 368), 358, 270, 350)};
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

        state = Constant.State.PLAY;

        EventBus.getInstance().subscribe(Constant.Event.ON_RELOAD_START.name(), () -> {state = Constant.State.RELOAD;});
        EventBus.getInstance().subscribe(Constant.Event.ON_RELOAD_END.name(), () -> {state = Constant.State.PLAY;});
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

        draw(g);
    }

    public void draw(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g.drawImage(roomImg, 0, 0, null);

        if(state == Constant.State.RELOAD) {
            Graphics2D g21 = (Graphics2D)g2.create();
            g21.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            g21.setColor(Color.RED);
            g21.setFont(new Font(Font.MONOSPACED, Font.BOLD, 40));
            g21.drawString("ПЕРЕЗАРЯДКА!", 159, 239);
            g21.dispose();
        }

        cannon.draw(g);

        Stream.of(targets).forEach((t) -> {
            t.draw(g);
        });

        bullets.stream().forEach((b) -> {
            b.draw(g);
        });

        bulletsHolder.draw(g);

        Toolkit.getDefaultToolkit().sync();
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

            Stream.of(targets).forEach((t) -> {
                t.move();
            });

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
