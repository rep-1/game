package ru.rep1.game.black;

import ru.rep1.game.*;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.CountDownLatch;
import java.util.stream.Stream;

import static ru.rep1.game.Scale.*;

/**
 * Created by lshi on 10.12.2016.
 */
public class Lab extends JPanel implements Runnable {
    private Thread animator;
    private Image roomImg;
    private Cannon cannon;
    private Target[] targets;
    private Temperature temperature;
    private TempController tempController;
    private BulletsHolder bulletsHolder;
    private java.util.List<Bullet> bullets;
    private Rectangle2D[] walls;
    private volatile Constant.State state;

    private CountDownLatch introLatch;

    public Lab() {
        init();
    }

    public synchronized void changeState(Constant.State state) {
        System.out.println("Change state from " + this.state + " to " + state);
        this.state = state;
    }

    public synchronized Constant.State getState() {
        return state;
    }

    public void init() {
        changeState(Constant.State.INTRO);

        setBackground(Color.BLACK);
        setPreferredSize(new Dimension(Constant.GAME_WIDTH, Constant.GAME_HEIGHT));
        setDoubleBuffered(true);
        setLayout(null);

        initResources();
        initRotateButtons();
        initFireButton();
        initWalls();
        initCannon();
        initBulletsHolder();
        initBullets();
        initTargets();
        initTemperature();

        initEvents();

        EventBus.getInstance().subscribe(Constant.Event.ON_GAME_PLAY.name(), () -> {
            changeState(Constant.State.PLAY);
        });
        EventBus.getInstance().publish(Constant.Event.ON_GAME_START.name());
    }

    private void initBulletsHolder() {
        this.bulletsHolder = new BulletsHolder();
    }

    private void initEvents() {
        introLatch = new CountDownLatch(3);
        EventBus.getInstance().subscribe(Constant.Event.ON_GAME_START.name(), () -> {
            new Thread(() -> {
                try {
                    targets[0].moveByTrajectory();
                    targets[1].moveByTrajectory();
                    targets[2].moveByTrajectory();
                    introLatch.await();
                    EventBus.getInstance().publish(Constant.Event.ON_GAME_PLAY.name());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    Thread.currentThread().interrupt();
                }
            }).start();
        });

        EventBus.getInstance().subscribe(Constant.Event.ON_TARGET_IN_PLACE.name(), () -> {
            if (getState() == Constant.State.OUTRO) {
                changeState(Constant.State.FINISH);
                EventBus.getInstance().publish(Constant.Event.ON_GAME_FINISH.name());
            } else if (getState() == Constant.State.INTRO) {
                introLatch.countDown();
            }
        });

        EventBus.getInstance().subscribe(Constant.Event.ON_GAME_OUTRO.name(), () -> {
            changeState(Constant.State.OUTRO);
            Target winner = Stream.of(targets).filter((t) -> {
                return !t.isShot();
            }).findFirst().orElse(null);
            if (winner != null) {
                winner.setTrajectory(new Point2D[]{
                        $(new Point2D.Double(1045, 422)),
                        $(new Point2D.Double(815, 440)),
                        $(new Point2D.Double(813, 508))});
                winner.setTrajectorySpeed(0.6D);
                winner.moveByTrajectory();
            } else {
                System.out.println("Winner not found");
            }
        });
    }

    private void initTemperature() {
        this.temperature = new Temperature();
        this.tempController = new TempController();
        this.tempController.setTemperature(temperature);
        this.tempController.start();
    }

    private void initWalls() {
        walls = new Rectangle2D[]{$(new Rectangle2D.Double(0, 0, 753, 1080)),
                $(new Rectangle2D.Double(0, 0, 1920, 282)),
                $(new Rectangle2D.Double(0, 0, 1920, 188)),
                $(new Rectangle2D.Double(1313, 0, 607, 215)),
                $(new Rectangle2D.Double(1348, 0, 572, 1080)),
                $(new Rectangle2D.Double(0, 503, 1039, 577)),
                $(new Rectangle2D.Double(0, 615, 1920, 465)),
                $(new Rectangle2D.Double(1219, 505, 701, 575)),
                $(new Rectangle2D.Double(1310, 473, 610, 607)),
                $(new Rectangle2D.Double(1310, 305, 610, 75))};
    }

    private void initTargets() {
        Target t1 = new Target($(1332), $(426), $(221), $(354));
        if (getState() == Constant.State.INTRO) {
            t1.setTrajectory(new Point2D[]{
                    $(new Point2D.Double(1332, 426)),
                    $(new Point2D.Double(1232, 433)),
                    $(new Point2D.Double(1180, 339))});
        }

        Target t2 = new Target($(1382), $(403), $(385), $(461));
        if (getState() == Constant.State.INTRO) {
            t2.setTrajectory(new Point2D[]{
                    $(new Point2D.Double(1382, 403)),
                    $(new Point2D.Double(1232, 433)),
                    $(new Point2D.Double(1170, 462))});
        }

        Target t3 = new Target($(1418), $(453), $(482), $(588));
        if (getState() == Constant.State.INTRO) {
            t3.setTrajectory(new Point2D[]{
                    $(new Point2D.Double(1418, 453)),
                    $(new Point2D.Double(1232, 433)),
                    $(new Point2D.Double(1132, 515))});
        }

        targets = new Target[]{t1, t2, t3};
    }

    private void initBullets() {
        bullets = new ArrayList<>();
    }

    private void initCannon() {
        cannon = new Cannon("black/cannon.png", $(new Rectangle2D.Double(747, 303, 118, 76)).getBounds());
    }

    private void initResources() {
        roomImg = Utils.loadImage("black/ResearchTerminal.png");
    }

    private void initRotateButtons() {
        Rectangle2D leftBounds = $(new Rectangle2D.Double(120, 793, 163, 195));
        Button leftButton = new Button("black/left_button.png", "black/left_button_pressed.png");
        add(leftButton);
        leftButton.setBounds(leftBounds.getBounds());
        leftButton.setOnPress(() -> {
            cannon.turnRight();
            repaint();
        });

        Rectangle2D rightBounds = $(new Rectangle2D.Double(284, 793, 160, 195));
        Button rightButton = new Button("black/right_button.png", "black/right_button_pressed.png");
        add(rightButton);
        rightButton.setBounds(rightBounds.getBounds());
        rightButton.setOnPress(() -> {
            cannon.turnLeft();
            repaint();
        });
    }

    private void initFireButton() {
        Rectangle2D fireBounds = $(new Rectangle2D.Double(1475, 792, 321, 196));
        Button fireButton = new Button("black/fire_button.png", "black/fire_button_pressed.png");
        add(fireButton);
        fireButton.setBounds(fireBounds.getBounds());
        fireButton.setOnClick(this::fire);
    }

    private void fire() {
        if(!tempController.isOverheat() && bulletsHolder.getBullet()) {
            EventBus.getInstance().publish(Constant.Event.ON_FIRE.name());
            Bullet bullet = new Bullet();
            bullet.setX((int) cannon.getFirePosition().getX());
            bullet.setY((int) cannon.getFirePosition().getY());
            bullet.setAngle(90 - cannon.getAngle());
            synchronized (bullets) {
                bullets.add(bullet);
            }
            this.temperature.inc();
        }
        repaint();
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

    private void loop() {
        long prevTime = System.currentTimeMillis();
        long diff;
        long sleep;

        while (true) {
            if (state == Constant.State.PLAY) {
                detectCollisions();
            }

            bullets.stream().forEach((b) -> {
                b.move();
            });

            if (state == Constant.State.PLAY) {
                Stream.of(targets).filter(t -> !t.isShot()).forEach((t) -> {
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
                Thread.currentThread().interrupt();
            }

            prevTime = System.currentTimeMillis();
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        drawRoom(g);

        cannon.draw(g);

        temperature.draw(g);

        bulletsHolder.draw(g);

        final Constant.State tmpState = getState();
        if (tmpState == Constant.State.INTRO) {
            drawIntro(g);
        } else if (tmpState == Constant.State.PLAY) {
            drawPlay(g);
        } else if (tmpState == Constant.State.OUTRO || tmpState == Constant.State.FINISH) {
            drawOutro(g);
        }

        Toolkit.getDefaultToolkit().sync();
    }

    private void drawIntro(Graphics g) {
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

        Stream.of(targets).forEach((t) -> {
            t.draw(g);
        });

        bullets.stream().forEach((b) -> {
            b.draw(g);
        });

        g2.dispose();
    }

    public void drawOutro(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        Stream.of(targets).forEach((t) -> {
            t.draw(g);
        });

        g2.dispose();
    }


    public void drawRoom(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g.drawImage(roomImg, 0, 0, Constant.GAME_WIDTH, Constant.GAME_HEIGHT, null);

        g2.dispose();
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
                    if (w.intersects(bullet.getBounds())) {
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
                checkWin();
            }
        }
    }

    private void checkWin() {
        if (state == Constant.State.PLAY) {
            if (Stream.of(targets).filter((t) -> {
                return t.isShot();
            }).count() >= 2) {
                EventBus.getInstance().publish(Constant.Event.ON_GAME_OUTRO.name());
            }
        }
    }
}
