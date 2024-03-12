import java.awt.EventQueue;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class getStarted{
	
	JFrame getStartedFrame;
	
	private int screenWidth = 1366, screenHeight = 768;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					getStarted window = new getStarted();
					window.getStartedFrame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public getStarted() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		
		// || ========================================================== || FRAME 1 || ========================================================== || \\
		
		Image GetStartedBGSrc = new ImageIcon(this.getClass().getResource("/GetStartedBG.jpg")).getImage();
		Image GetStartedBGImg = GetStartedBGSrc.getScaledInstance(screenWidth, screenHeight, Image.SCALE_DEFAULT);
		
		Image ShopNowSrc = new ImageIcon(this.getClass().getResource("/Shop Now Button.png")).getImage();
		Image ShopNowImg = ShopNowSrc.getScaledInstance(142, 142, Image.SCALE_DEFAULT);
		
		Image ShopNowBlackSrc = new ImageIcon(this.getClass().getResource("/Shop Now Button(Inverse).png")).getImage();
		Image ShopNowBlackImg = ShopNowBlackSrc.getScaledInstance(142, 142, Image.SCALE_DEFAULT);
		
		getStartedFrame = new JFrame();
		getStartedFrame.setBounds(100, 100, screenWidth + 14, screenHeight + 37);
		getStartedFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getStartedFrame.getContentPane().setLayout(null);
		
		JLabel GetStartedLabel = new JLabel("");
		GetStartedLabel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				GetStartedLabel.setIcon(new ImageIcon(ShopNowBlackImg));
			}
			@Override
			public void mouseExited(MouseEvent e) {
				GetStartedLabel.setIcon(new ImageIcon(ShopNowImg));
			}
			@SuppressWarnings("deprecation")
			@Override
			public void mouseClicked(MouseEvent e) {
				login ln = new login();
				ln.loginFrame.show();
				getStartedFrame.dispose();
			}
		});
		GetStartedLabel.setIcon(new ImageIcon(ShopNowImg));
		GetStartedLabel.setBounds(296, 405, 142, 44);
		getStartedFrame.getContentPane().add(GetStartedLabel);
		
		JLabel startUpScreenBG = new JLabel("");
		startUpScreenBG.setIcon(new ImageIcon(GetStartedBGImg));
		startUpScreenBG.setBounds(0, 0, screenWidth, screenHeight);
		getStartedFrame.getContentPane().add(startUpScreenBG);
	}
}
