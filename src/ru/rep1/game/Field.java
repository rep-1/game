package ru.rep1.game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by lshi on 17.11.2016.
 */
public class Field extends JPanel implements ActionListener, Runnable {

    private Thread animator;

    private int x, y;

    private JButton btnLeft;
    private JButton btnRight;
    private JButton btnFire;

    public Field() {
        initField();
    }

    private void initField() {
        setBackground(Color.BLACK);
        setPreferredSize(new Dimension(500, 500));
        setDoubleBuffered(true);

        setLayout(null);

        btnLeft = new JButton("<");
        btnRight = new JButton(">");
        btnFire = new JButton("Fire");

        btnLeft.addActionListener((e) -> { x = x -1; repaint();});

        add(btnLeft);
        add(btnRight);
        add(btnFire);
    }

    public void placeButtons() {
        btnLeft.setBounds(100, getHeight()- 20, 30, 20);
        btnRight.setBounds(130, getHeight() - 20, 30, 20);
        btnFire.setBounds(160, getHeight() - 20, 70, 20);
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
        g.drawString("Test", x, y);
        Toolkit.getDefaultToolkit().sync();
    }

    @Override
    public void run() {
        long prevTime = System.currentTimeMillis();
        long diff;
        long sleep;

        while (true) {
            x += 1;
            y += 1;

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
