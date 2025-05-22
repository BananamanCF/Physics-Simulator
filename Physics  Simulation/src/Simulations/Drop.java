package Simulations;
import Start.Menu;

import java.awt.*;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class Drop extends JPanel implements Runnable{
	
	JFrame frame;
	Menu menu;
	KeyListener keyListener;
	
	float x;
	float y;
	int r;
	
	float v = 0;
	float a = .3f;
	float friction = .8f;
	
    private final int FPS = 60;
    private final long TIME_PER_TICK = 1000 / FPS;

	public Drop(float y, int r, JFrame frame, Menu menu) {
		this.menu = menu;
		this.frame = frame;
		keyListener = new KeyClass(frame, menu);
		setFocusable(true);

        addKeyListener(keyListener);
        
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {  //temp until I make mouse class
                requestFocusInWindow();
            }
        });
		
		this.y = y;
		this.r = r;
		x = Menu.WIDTH/2;
		setBackground(Color.BLACK);
	}
	
	public void run() {
		
		this.setFocusable(true);
		this.requestFocusInWindow();
		
        long lastTime = System.currentTimeMillis();
        long deltaTime = 0;
        
        
        while (true) {
            long now = System.currentTimeMillis();
            deltaTime += (now - lastTime);
            lastTime = now;

            if (deltaTime >= TIME_PER_TICK) {
                update();
                deltaTime -= TIME_PER_TICK;
            }
            
        }
    }
	 
	private void update() {
		v += a;
		y += v;
		
		if(y + r > getHeight()) {
			y = getHeight() - r;
			v = -v * friction;
			
			if(Math.abs(v) < 2)
				v = 0;
		}		
		
		
		repaint();
	}
	
	public void paintComponent(Graphics graphics) {
		super.paintComponent(graphics);
		
		Graphics2D g = (Graphics2D) (graphics);
		g.setColor(Color.RED);
		
		g.fillOval((int) (x - r/2.0), (int) Math.floor(y), r, r);
	}
}
