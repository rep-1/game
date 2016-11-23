package ru.rep1.game;

import com.sun.xml.internal.ws.util.StreamUtils;

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
    private BulletsHolder bulletsHolder;
    private java.util.List<Bullet> bullets;
    private Target[] targets;

    private Image roomImg;

    public Field() {
        initField();
    }

    private void initField() {
        roomImg = Toolkit.getDefaultToolkit().getImage("room.png");

        bulletsHolder = new BulletsHolder();
        bullets = new ArrayList<>();

        targets = new Target[]{
                new Target(Utils.getRandomNumberInRange(50, 100), 30, 50, 150),
                new Target(Utils.getRandomNumberInRange(150, 300), 100, 150, 300),
                new Target(Utils.getRandomNumberInRange(300, 450), 60, 300, 450)};

        setBackground(Color.BLACK);
        setPreferredSize(new Dimension(Constant.GAME_WIDTH, Constant.GAME_HEIGHT));
        setDoubleBuffered(true);

        setLayout(null);

        cannon = new Cannon();

        btnLeft = new JButton("<");
        btnRight = new JButton(">");
        btnFire = new JButton("Fire");

        btnLeft.addMouseListener(new ButtonPress(() -> {
            x = x - 1;
            cannon.turnLeft();
            repaint();
        }));
        btnRight.addMouseListener(new ButtonPress(() -> {
            x = x + 1;
            cannon.turnRight();
            repaint();
        }));
        btnFire.addActionListener((e) -> {
            if(bulletsHolder.getBullet()) {
                Bullet bullet = new Bullet();
                bullet.setX((int) cannon.getFirePosition().getX());
                bullet.setY((int) cannon.getFirePosition().getY());
                bullet.setAngle(cannon.getAngle());
                bullets.add(bullet);
                repaint();
            }
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


    private void drawRoom(Graphics g) {
        //Graphics g2 = g.create();

        g.drawImage(roomImg, 0, 0, null);

        //g2.dispose();
    }

    private void draw(Graphics g) {
        if (checkWin()) {
            Graphics g2 = g.create();
            g2.setColor(Color.WHITE);
            g2.setFont(new Font(Font.MONOSPACED, Font.BOLD, 40));
            g2.drawString("You Win!", 200, Constant.GAME_HEIGHT / 2);
            g2.dispose();
        }

        drawRoom(g);

        cannon.draw(g);

        bulletsHolder.draw(g);
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
                if (bullet.isShot() || (bullet.getX() > Constant.GAME_WIDTH || bullet.getY() > Constant.GAME_HEIGHT) || (bullet.getX() < 0 || bullet.getY() < 0)) {
                    bulletIterator.remove();
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
    }

    private boolean checkWin() {
        return Stream.of(targets).filter((t) -> {
            return t.isShot();
        }).count() == targets.length;
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
