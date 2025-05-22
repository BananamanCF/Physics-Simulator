package Simulations;

import Start.Menu;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;

import Start.Main;

public class ProjectileMotion extends JPanel implements Runnable{
	Menu menu;
	JFrame frame;
	KeyListener keyListener;
	
	Ball ball;

	float f = .8f;
	float groundF = .99f;
	float gravity = .3f;
	
	private final int FPS = 60;
    private final long TIME_PER_TICK = 1000 / FPS;
	
	public ProjectileMotion(int speed, double angle, int r, JFrame frame, Menu menu) {
		
		this.frame = frame;
		this.menu = menu;
		
		
 		keyListener = new KeyClass(frame, menu);
 		
        setFocusable(true);

        addKeyListener(keyListener);
        
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {  //temp until I make mouse class
                requestFocusInWindow();
            }
        });
		
		ball = new Ball(0, 0, r);
		
		ball.sx = (float) (speed * Math.cos(Math.toRadians(angle)));
		ball.sy = (float) (speed * Math.sin(Math.toRadians(angle)));
		setBackground(Color.BLACK);
	}
	
	public void update() {
		updateX();
		updateY();
	}
	public void updateX() {
		ball.x += ball.sx;
		if(ball.x + ball.r > getWidth()) {
			ball.x = getWidth() - ball.r;
			ball.sx = -ball.sx * f;
		}
		else if(ball.x < 0) {
			ball.x = 0;
			ball.sx = -ball.sx * f;
		}
		if(Math.abs(ball.sx) < .5)
			ball.sx = 0;
	}
	public void updateY() {
		ball.sy += gravity;
		ball.y += ball.sy;
		if(ball.y + ball.r > getHeight()) {
			ball.y = getHeight() - ball.r;
			ball.sy = -ball.sy * f;
			
			if(Math.abs(ball.sy) < .5) {
				ball.sy = 0;
				ball.sx *= groundF;
			}
		}
		else if(ball.y < 0) {
			ball.y = 0;
			ball.sy = -ball.sy * f;
		}
	}
	
	public void run() {
		
		this.setFocusable(true);
		this.requestFocusInWindow();
		
		ball.x=0;
		ball.y = getHeight() - ball.r;
		
        long lastTime =  System.currentTimeMillis();
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
	
	public void paintComponent(Graphics graphics) {
		super.paintComponent(graphics);
		
		Graphics2D g = (Graphics2D) (graphics);
		g.setColor(Color.WHITE );
		
		g.fillOval((int) ball.x, (int) ball.y, ball.r, ball.r);
	}
}
