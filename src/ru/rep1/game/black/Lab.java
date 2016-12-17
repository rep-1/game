package ru.rep1.game.black;

import ru.rep1.game.*;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Iterator;
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
    private java.util.List<Bullet> bullets;
    private Rectangle2D[] walls;
    private volatile Constant.State state;

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
        changeState(Constant.State.PLAY);

        setBackground(Color.BLACK);
        setPreferredSize(new Dimension(Constant.GAME_WIDTH, Constant.GAME_HEIGHT));
        setDoubleBuffered(true);
        setLayout(null);

        initResources();
        initRotateButtons();
        initFireButton();
        initWalls();
        initCannon();
        initBullets();
        initTargets();
        initTemperature();
    }

    private void initTemperature() {
        this.temperature = new Temperature();

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
        Target t1 = new Target($(1175), $(342), $(309), $(418));
        targets = new Target[]{t1};
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
            cannon.turnLeft();
            repaint();
        });

        Rectangle2D rightBounds = $(new Rectangle2D.Double(284, 793, 160, 195));
        Button rightButton = new Button("black/right_button.png", "black/right_button_pressed.png");
        add(rightButton);
        rightButton.setBounds(rightBounds.getBounds());
        rightButton.setOnPress(() -> {
            cannon.turnRight();
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
        Bullet bullet = new Bullet();
        bullet.setX((int) cannon.getFirePosition().getX());
        bullet.setY((int) cannon.getFirePosition().getY());
        bullet.setAngle(90 - cannon.getAngle());
        synchronized (bullets) {
            bullets.add(bullet);
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
            if(state == Constant.State.PLAY) {
                detectCollisions();
            }

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

        bullets.stream().forEach((b) -> {
            b.draw(g);
        });

        Stream.of(targets).forEach((t) -> {
            t.draw(g);
        });
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
