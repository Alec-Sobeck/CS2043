import java.awt.EventQueue;

/**
 * Contains the main method for the program. 
 * @author Alec Sobeck
 * @author Kieran Lea
 */
public class Launcher {
 
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					BillboardListGUI frame = new BillboardListGUI();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}
