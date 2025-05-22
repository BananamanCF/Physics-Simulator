package Start;
import java.awt.Color;
import javax.swing.JFrame;
import Simulations.Drop;
import Simulations.MultipleBallPM;
import Simulations.ProjectileMotion;
import Simulations.SlidingBlocksPI;

public class Main {
	
			
	public static void main(String[] args) {
		
		Menu menu = new Menu();
		new Thread(menu).start();
		
	}

}
