package Simulations;

import Start.Menu;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JFrame;

public class KeyClass implements KeyListener{

	JFrame frame;
	Menu menu;
	
	public KeyClass(JFrame frame, Menu menu) {
		this.frame = frame;
		this.menu = menu;
	}
	
	@Override
	public void keyTyped(KeyEvent e) {
//		System.out.println("Key typed: " + e.getKeyChar());
//		if(e.getKeyCode() == KeyEvent.VK_ESCAPE)
//			System.out.println("Escape pressed during mbs");
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		//System.out.println("Key typed: " + e.getKeyChar());
		if(e.getKeyCode() == KeyEvent.VK_ESCAPE) {
			System.out.println("Escape pressed during mbs");
			frame.getContentPane().removeAll();
			frame.add(menu);
			menu.repaint();
			new Thread(menu).start();
		}
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		//System.out.println("Key typed: " + e.getKeyChar());
//		if(e.getKeyCode() == KeyEvent.VK_ESCAPE)
//			System.out.println("Escape pressed during mbs");
		
	}

}
