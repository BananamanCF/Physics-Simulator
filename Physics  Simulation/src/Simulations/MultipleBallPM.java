package Simulations;

import Start.Menu;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;

import Start.Main;

public class MultipleBallPM extends JPanel implements Runnable{
	
	JFrame frame;
	Menu menu;
	KeyListener keyListener;
	
	List<Ball> balls;
	
	float f = .8f;
	float groundF = .3f;
	float gravity = 0f;
	
	private final int FPS = 60;
    private final long TIME_PER_TICK = 1000 / FPS;
	
	public MultipleBallPM(JFrame frame, Menu menu) {
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
 		
		balls = new ArrayList<>();
		setBackground(Color.BLACK);
		
	}
	
	public void update() {
		updateX();
		updateY();
		checkCollisions();
	}
	public void addBall(float x, float y, int r, double angle, int speed, Color col) {
		float spx = (float) (speed*Math.cos(Math.toRadians(angle)));
		float spy = (float) (speed*Math.sin(Math.toRadians(angle)));
		balls.add(new Ball(x, y, r, spx, spy, col));
	}
	
	public void updateX() {
		for(Ball ball : balls) {
			ball.x += ball.sx;
			if(ball.x + ball.r > getWidth()) {
				ball.x = getWidth() - ball.r;
				ball.sx = -ball.sx * f;
			}
			else if(ball.x - ball.r < 0) {
				ball.x = ball.r;
				ball.sx = -ball.sx * f;
			}
			if(Math.abs(ball.sx) < .5)
				ball.sx = 0;
		}
	}
	public void updateY() {
		for(Ball ball : balls) {
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
			else if(ball.y - ball.r < 0) {
				ball.y = ball.r;
				ball.sy = -ball.sy * f;
			}
		}
	}
	public void checkCollisions() { 
		int n = balls.size();
		if(n == 0)
			return;
		
		synchronized(balls) {
			Collections.sort(balls, (b1, b2) -> Float.compare(b1.x - b1.r, b2.x - b2.r));
		}
		
		List<List<Ball>> intervals = new ArrayList<>();
		
		//float left = balls.get(0).x - balls.get(0).r;
		float right = balls.get(0).x + balls.get(0).r;
		List<Ball> tem = new ArrayList<>();
		tem.add(balls.get(0));
		intervals.add(tem);
		
		for(int i=1;i<n;i++) {
			float newLeft = balls.get(i).x - balls.get(i).r;
			float newRight = balls.get(i).x + balls.get(i).r;
			if(newLeft <= right) {
				right = Math.max(right, newRight);
				intervals.get(intervals.size()-1).add(balls.get(i));
			}
			else {
				List<Ball> temp = new ArrayList<>();
				temp.add(balls.get(i));
				intervals.add(temp);
				right = balls.get(i).x + balls.get(i).r;
			}
				
		}
		
		for(List<Ball> list : intervals) {
			int len = list.size();
			for(int i=0;i<len;i++) {
				for(int j=i+1;j<len;j++) {
					float x1 = list.get(i).x;
					float y1 = list.get(i).y;
					float x2 = list.get(j).x;
					float y2 = list.get(j).y;
					
					float r1 = list.get(i).r;
					float r2 = list.get(j).r;
					
					double distance = Math.pow(x1-x2, 2) + Math.pow(y2-y1, 2);
					if(distance <= (r1 + r2) * (r1 + r2)) {
						handleCollision(list.get(i), list.get(j));
					}
				}
			}
		}
	}
	public void handleCollision(Ball b1, Ball b2) {
		float nx = b2.x - b1.x;
		float ny = b2.y - b1.y;
		float distance = (float) Math.sqrt(nx*nx + ny*ny);
		
		nx /= distance;
		ny /= distance;
		float rvx = b2.sx - b1.sx;
	    float rvy = b2.sy - b1.sy;
	    
	    float velocityAlongNormal = rvx * nx + rvy * ny;
	    if (velocityAlongNormal > 0) return;
	    
	    	    
	    float j = -(1 + f) * velocityAlongNormal;
	    j /= (1/b1.m + 1/b2.m);
	    
	    float impulseX = j * nx;
	    float impulseY = j * ny;
	    b1.sx -= impulseX / b1.m;
	    b1.sy -= impulseY / b1.m;
	    b2.sx += impulseX / b2.m;
	    b2.sy += impulseY / b2.m;
	    float penetration = b1.r + b2.r - distance;
	    float percent = 0.2f; 
	    float slop = 0.01f;   
	    float correction = Math.max(penetration - slop, 0.0f) / (1/b1.m + 1/b2.m) * percent;
	    b1.x -= nx * correction * (1/b1.m);
	    b1.y -= ny * correction * (1/b1.m);
	    b2.x += nx * correction * (1/b2.m);
	    b2.y += ny * correction * (1/b2.m);
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
                repaint();
                deltaTime -= TIME_PER_TICK;
            }
            
        }
    }
	
	public void paintComponent(Graphics graphics) {
		super.paintComponent(graphics);
		
		Graphics2D g = (Graphics2D) (graphics);
		g.setColor(Color.BLACK );
		
		synchronized(balls) { //synchronized so that 2 threads dont try to edit simultanuelsyle
			for(Ball ball : balls) {
				g.setColor(ball.col);
				g.fillOval((int) (ball.x - ball.r), (int) (ball.y - ball.r),
						ball.r * 2, ball.r * 2);
			}
		}
	}
}
