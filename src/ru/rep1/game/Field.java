package ru.rep1.game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.stream.Stream;

/**
 * Created by lshi on 17.11.2016.
 */
public class Field extends JPanel implements ActionListener, Runnable {

    private Thread animator;

    private int x, y;

    private JButton btnLeft;
    private JButton btnRight;
    private JButton btnFire;

    private Cannon cannon;
    private java.util.List<Bullet> bullets;
    private Target[] targets;

    public Field() {
        initField();
    }

    private void initField() {
        bullets = new ArrayList<>();
        targets = new Target[]{new Target(100, 100), new Target(250, 100), new Target(400, 100)};

        setBackground(Color.BLACK);
        setPreferredSize(new Dimension(Constant.GAME_WIDTH, Constant.GAME_HEIGHT));
        setDoubleBuffered(true);

        setLayout(null);

        cannon = new Cannon();

        btnLeft = new JButton("<");
        btnRight = new JButton(">");
        btnFire = new JButton("Fire");

        //btnLeft.addActionListener((e) -> { x = x - 1; cannon.turnLeft(); repaint();});
        btnLeft.addMouseListener(new ButtonPress(() -> { x = x - 1; cannon.turnLeft(); repaint();}));
        //btnRight.addActionListener((e) -> { x = x + 1; cannon.turnRight(); repaint();});
        btnRight.addMouseListener(new ButtonPress(() -> { x = x + 1; cannon.turnRight(); repaint();}));
        btnFire.addActionListener((e) -> {
            Bullet bullet = new Bullet();
            bullet.setX((int) cannon.getFirePosition().getX());
            bullet.setY((int) cannon.getFirePosition().getY());
            bullet.setAngle(cannon.getAngle());
            bullets.add(bullet);
            repaint();
        });

        add(btnLeft);
        add(btnRight);
        add(btnFire);
    }

    public void placeButtons() {
        btnLeft.setBounds(100, getHeight() - 20, 60, 20);
        btnRight.setBounds(160, getHeight() - 20, 60, 20);
        btnFire.setBounds(220, getHeight() - 20, 70, 20);
    }

    @Override
    public void addNotify() {
        super.addNotify();

        animator = new Thread(this);
        animator.start();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        draw(g);
    }

    private void draw(Graphics g) {
        cannon.draw(g);
        bullets.stream().forEach((b) -> {
            b.draw(g);
        });
        Stream.of(targets).forEach((t) -> {
            t.draw(g);
        });
        Toolkit.getDefaultToolkit().sync();
    }

    private void checkCollisions() {
        synchronized (bullets) {
            Iterator<Bullet> bulletIterator = bullets.iterator();
            while (bulletIterator.hasNext()) {
                Bullet bullet = bulletIterator.next();
                if ((bullet.getX() > Constant.GAME_WIDTH || bullet.getY() > Constant.GAME_HEIGHT) || (bullet.getX() < 0 || bullet.getY() < 0)) {
                    bulletIterator.remove();
                }
            }
        }
        Stream.of(targets).forEach((t) -> {
            bullets.forEach((b) -> {
                if (!b.isShot() && t.getBounds().intersects(b.getBounds())) {
                    t.shot();
                    b.shot();
                }
            });
        });
    }

    @Override
    public void run() {
        long prevTime = System.currentTimeMillis();
        long diff;
        long sleep;

        while (true) {
            x += 1;
            y += 1;

            bullets.stream().forEach((b) -> {
                b.move();
            });
            Arrays.stream(targets).forEach((t) -> {
                t.move();
            });

            checkCollisions();

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

    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
