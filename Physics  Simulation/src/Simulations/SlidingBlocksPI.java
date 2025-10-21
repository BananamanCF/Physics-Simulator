package Simulations;

import Start.Menu;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class SlidingBlocksPI extends JPanel implements Runnable {
    JFrame frame;
    Menu menu;
    KeyListener keyListener;

    int m1, m2;
    int s1, s2;

    double x1, y1, x2, y2;
    double v1, v2;
    int collisionCount = 0;
    boolean colliding = false;

    private final int FPS = 60;
    private final long TIME_PER_TICK = 1000 / FPS;

    public SlidingBlocksPI(int m1, int m2, JFrame frame, Menu menu) {
        this.frame = frame;
        this.menu = menu;

        keyListener = new KeyClass(frame, menu);
        setFocusable(true);
        addKeyListener(keyListener);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                requestFocusInWindow();
            }
        });

        this.m1 = m1;
        this.m2 = m2;
        s2 = 125;
        s1 = 50;
        setBackground(Color.black);
    }

    public void init() {
        x1 = getWidth() / 3.0;
        y1 = getHeight() - s1;

        x2 = getWidth() * 2 / 3.0;
        y2 = getHeight() - s2;

        v1 = 5;
        v2 = -10;
    }

    public void run() {
        long lastTime = System.currentTimeMillis();
        long deltaTime = 0;

        while (true) {
            long now = System.currentTimeMillis();
            deltaTime += (now - lastTime);
            lastTime = now;

            if (deltaTime >= TIME_PER_TICK) {
                update();
                repaint();
                deltaTime -= TIME_PER_TICK;
            }
        }
    }

    public void update() {
        x1 += v1;
        x2 += v2;

        // Wall bounce
        if (x1 < 0) {
            v1 = -v1;
            x1 = 0;
        } else if (x1 + s1 > getWidth()) {
            v1 = -v1;
            x1 = getWidth() - s1;
        }

        if (x2 < 0) {
            v2 = -v2;
            x2 = 0;
        } else if (x2 + s2 > getWidth()) {
            v2 = -v2;
            x2 = getWidth() - s2;
        }

        // Collision check
        if (x2 - (x1 + s1) <= 0) {
            if (!colliding) {
                colliding = true;
                collisionCount++;
                System.out.println("Collision: " + collisionCount);

                double dis = (x1 + s1) - x2;
                x1 -= dis * 0.5;
                x2 += dis * 0.5;

                double u1 = v1;
                double u2 = v2;
                v1 = ((m1 - m2) * u1 + 2 * m2 * u2) / (m1 + m2);
                v2 = ((m2 - m1) * u2 + 2 * m1 * u1) / (m1 + m2);
            }
        } else {
            colliding = false;
        }
    }

    public void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);

        Graphics2D g = (Graphics2D) graphics;
        g.setFont(new Font("Arial", Font.BOLD, 30));

        g.setColor(Color.WHITE);
        g.fillRect((int) x1, (int) y1, s1, s1);

        g.setColor(Color.YELLOW);
        g.fillRect((int) x2, (int) y2, s2, s2);

        g.setColor(Color.RED);
        g.drawString("" + m1, (int) x1, (int) y1 + s1 / 2);
        g.drawString("" + m2, (int) x2, (int) y2 + s2 / 2);
    }
}

