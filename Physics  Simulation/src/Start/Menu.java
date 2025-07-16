package Start;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import Simulations.Ball;
import Simulations.Drop;
import Simulations.KeyClass;
import Simulations.MultipleBallPM;
import Simulations.ProjectileMotion;
import Simulations.Sand;
import Simulations.SlidingBlocksPI;

public class Menu extends JPanel implements Runnable{
	JFrame frame;
	public static final int WIDTH = 1700;
	public static final int HEIGHT = 1000;
	public static int state = 0;
	KeyListener keyListener;
	
	public Menu(){
		frame = new JFrame();
		keyListener = new KeyClass(frame, this);
		
	}
	
	
	public void run() {
		this.setBackground(Color.BLACK);
		this.setLayout(null);
		this.setFocusable(true);
		this.requestFocusInWindow();


		frame.setSize(WIDTH, HEIGHT);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.add(this);
        frame.setVisible(true);
        addKeyListener(keyListener);
        
        int width = getWidth() / 8;
	    int height = getHeight() / 8;
                
        JButton button1 = createButton((int)(width + width * .5), (int)(height * 2.5), "Sliding Blocks");
	    button1.addActionListener(e -> blockCollision(frame, 10, 100));
	    
	    JButton button2 = createButton((int)(width + width * .5), (int)(height * 4.5), "Multiple Balls");
	    button2.addActionListener(e -> shootMultipleBalls());

	    JButton button3 = createButton((int)(width + width * 3.5), (int)(height * 2.5), "Ball drop");
	    button3.addActionListener(e -> dropBall(frame, 100, 50));
	    
	    JButton button4 = createButton((int)(width + width * 3.5), (int)(height * 4.5), "Projectile Motion");
	    button4.addActionListener(e -> shootBall(frame, 25 , 45, 20)); //speed, angle deg, size
	    
	    JButton button5 = createButton((int)(width + width * .5), (int)(height * 6.5), "Sand");
	    button5.addActionListener(e -> dropSand(frame)); //speed, angle deg, size
	    
	    this.add(button1);
	    this.add(button2);
	    this.add(button3);
	    this.add(button4);
	    this.add(button5);
	    
	    this.repaint();
        
	}
	public void paintComponent(Graphics graphics) {
		super.paintComponent(graphics);
		
		Graphics2D g = (Graphics2D) (graphics);
		g.setColor(Color.WHITE);
		Font font = new Font("Arial", Font.BOLD, (int) (.05 * getHeight()));
		g.setFont(font);
		
		String text = "Physics Simulator";

	    FontMetrics metrics = g.getFontMetrics(font);
	    int x = (getWidth() - metrics.stringWidth(text)) / 2;
	    int y = (int) (.15 * getHeight());
	    g.drawString(text, x, y);
	    

	}
	public JButton createButton(int x, int y, String text) {
		JButton button = new JButton(text);
	    button.setFont(new Font("Arial", Font.BOLD, 20));
	    button.setFocusPainted(false);
	    button.setBackground(Color.LIGHT_GRAY);
	    button.setBounds(x, y, 400, 80); // x, y, width, height
	    return button;
	}
	
	
	public void dropBall(JFrame frame, int height, int rad) {
		frame.getContentPane().removeAll();
		Drop drop = new Drop(height, rad, frame, this);
		drop.requestFocusInWindow(); 
		drop.setBackground(Color.BLACK);
		
		frame.add(drop);
		frame.revalidate();
	    frame.repaint();
		new Thread(drop).start();
	}
	public void shootBall(JFrame frame, int speed, double angle, int rad) {
		frame.getContentPane().removeAll();
		ProjectileMotion pm = new ProjectileMotion(speed, angle, rad, frame, this);
		pm.setBackground(Color.BLACK);
		
		frame.add(pm);
		frame.setVisible(true);
		frame.revalidate();
	    frame.repaint();
	    
	    
		new Thread(pm).start();
	}
	public void shootMultipleBalls() {
		frame.getContentPane().removeAll();
		MultipleBallPM pm = new MultipleBallPM(frame, this); //x, y, r, angle, speed, mass
		pm.requestFocusInWindow(); 
		
		for(int i=0;i<100;i++) {
			int r = (int) (Math.random()* 45 + 5);
			float x = (float) (Math.random()*(WIDTH-r) + r);
			float y = (float) (Math.random()*(HEIGHT-r) + r);
			double angle = Math.random() * 180;
			int speed = (int) (Math.random() * 75);
			pm.addBall(x, y, r, angle, speed, 
					new Color((int)(Math.random()*255), (int)(Math.random()*255), (int)(Math.random()*255)));
		}
		
		frame.add(pm);
		frame.revalidate();
	    frame.repaint();
		
		new Thread(pm).start();
	}
	public void blockCollision(JFrame frame, int m1, int m2) {
		frame.getContentPane().removeAll();
		SlidingBlocksPI sb = new SlidingBlocksPI(m1, m2, frame, this);
		
		frame.add(sb);
		
		frame.revalidate();
	    frame.repaint();
	    sb.init();
		
		new Thread(sb).start();
	}
	public void dropSand(JFrame fram) {
		frame.getContentPane().removeAll();
		Sand sand = new Sand(frame, this);
		Color col = new Color((int)(Math.random()*255), (int)(Math.random()*255), (int)(Math.random()*255));
		
		for(int i=0;i<2000;i++) {
			int r = 3;
			float x = (float) (Math.random()*(WIDTH-r) + r);
			float y = (float) (Math.random()*(HEIGHT-r) + r);
			double angle = Math.random() * 180;
			int speed = (int) (Math.random() * 1);
			
			sand.addBall(x, y, r, angle, speed, col);
		}
		
		frame.add(sand);
		
		frame.revalidate();
		frame.repaint();
		
		new Thread(sand).start();
	}

}
