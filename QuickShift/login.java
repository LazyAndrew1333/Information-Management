import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Image;

import javax.swing.*;
import java.awt.event.*;

public class login{

	public JFrame loginFrame;
	
	public JTextField login_email;
	
	public JPasswordField login_password;
	
	public JLabel LogInBG;
	
	private JLabel LoginButton, SignUpHereButton, ExclamationMark, ExclamationMark_1, SignUpBackDrop;
	
	private int screenWidth = 1366, screenHeight = 768;
	
	@SuppressWarnings("deprecation")
	public void LoginMethod() {
		if (login_email.getText().equals("root") && login_password.getText().equals("admin")) {
			productListing pl = new productListing();
			pl.productListingFrame.show();
			loginFrame.dispose();
		} else {
			ExclamationMark.setVisible(true);
			ExclamationMark_1.setVisible(true);
		}
	}

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					login window = new login();
					window.loginFrame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public login() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		Image LogInBGSrc = new ImageIcon(this.getClass().getResource("/Log In BG.png")).getImage();
		Image LogInBGImg = LogInBGSrc.getScaledInstance(screenWidth, screenHeight, Image.SCALE_DEFAULT);

		Image LogInButtonSrc = new ImageIcon(this.getClass().getResource("/Log In Button.png")).getImage();
		Image LogInButtonImg = LogInButtonSrc.getScaledInstance(365, 365, Image.SCALE_DEFAULT);

		Image LogInButtonInverseSrc = new ImageIcon(this.getClass().getResource("/Log In Button(Inverse).png"))
				.getImage();
		Image LogInButtonInverseImg = LogInButtonInverseSrc.getScaledInstance(365, 365, Image.SCALE_DEFAULT);

		Image SignUpHereSrc = new ImageIcon(this.getClass().getResource("/Sign Up Here.png")).getImage();
		Image SignUpHereImg = SignUpHereSrc.getScaledInstance(147, 147, Image.SCALE_DEFAULT);

		Image SignUpHereInverseSrc = new ImageIcon(this.getClass().getResource("/Sign Up Here(Inverse).png"))
				.getImage();
		Image SignUpHereInverseImg = SignUpHereInverseSrc.getScaledInstance(147, 147, Image.SCALE_DEFAULT);

		Image ExclamationSrc = new ImageIcon(this.getClass().getResource("/Exclamation.png")).getImage();
		Image Exclamationmg = ExclamationSrc.getScaledInstance(30, 30, Image.SCALE_DEFAULT);

		loginFrame = new JFrame();
		loginFrame.setBounds(100, 100, screenWidth + 14, screenHeight + 37);
		loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		loginFrame.getContentPane().setLayout(null);

		login_email = new JTextField();
		login_email.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				LoginMethod();
			}
		});
		login_email.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				if (login_email.getText().equals("EMAIL")) {
					login_email.setText("");
				}
			}

			@Override
			public void focusLost(FocusEvent e) {
				if (login_email.getText().equals("")) {
					login_email.setText("EMAIL");
				}
			}
		});
		login_email.setText("EMAIL");
		login_email.setForeground(new Color(143, 15, 15));
		login_email.setFont(new Font("SansSerif", Font.BOLD, 15));
		login_email.setBounds(526, 331, 318, 46);
		login_email.setOpaque(false);
		login_email.setBorder(javax.swing.BorderFactory.createEmptyBorder());
		login_email.setColumns(10);
		loginFrame.getContentPane().add(login_email);

		login_password = new JPasswordField();
		login_password.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				LoginMethod();
			}
		});
		login_password.addFocusListener(new FocusAdapter() {
			@SuppressWarnings("deprecation")
			@Override
			public void focusGained(FocusEvent e) {
				if (login_password.getText().equals("PASSWORD")) {
					login_password.setText("");
					login_password.setEchoChar('*');
				}
			}

			@SuppressWarnings("deprecation")
			@Override
			public void focusLost(FocusEvent e) {
				if (login_password.getText().equals("")) {
					login_password.setText("PASSWORD");
					login_password.setEchoChar((char) 0);
				}
			}
		});
		login_password.setText("PASSWORD");
		login_password.setEchoChar((char) 0);
		login_password.setForeground(new Color(143, 15, 15));
		login_password.setFont(new Font("SansSerif", Font.BOLD, 15));
		login_password.setBounds(526, 402, 318, 46);
		login_password.setOpaque(false);
		login_password.setBorder(javax.swing.BorderFactory.createEmptyBorder());
		login_password.setColumns(10);
		loginFrame.getContentPane().add(login_password);

		LoginButton = new JLabel("");
		LoginButton.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				LoginButton.setIcon(new ImageIcon(LogInButtonInverseImg));
			}

			@Override
			public void focusLost(FocusEvent e) {
				LoginButton.setIcon(new ImageIcon(LogInButtonImg));
			}
		});
		LoginButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				LoginButton.setIcon(new ImageIcon(LogInButtonInverseImg));
			}

			@Override
			public void mouseExited(MouseEvent e) {
				LoginButton.setIcon(new ImageIcon(LogInButtonImg));
			}

			@Override
			public void mouseClicked(MouseEvent e) {
				LoginMethod();
			}
		});
		LoginButton.setIcon(new ImageIcon(LogInButtonImg));
		LoginButton.setBounds(502, 467, 365, 47);
		LoginButton.setFocusable(true);
		loginFrame.getContentPane().add(LoginButton);

		SignUpHereButton = new JLabel("");
		SignUpHereButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				SignUpHereButton.setIcon(new ImageIcon(SignUpHereInverseImg));
			}

			@Override
			public void mouseExited(MouseEvent e) {
				SignUpHereButton.setIcon(new ImageIcon(SignUpHereImg));
			}

			@SuppressWarnings("deprecation")
			@Override
			public void mouseClicked(MouseEvent e) {
				signUp su = new signUp();
				su.signUpFrame.show();
				loginFrame.dispose();
			}
		});
		SignUpHereButton.setIcon(new ImageIcon(SignUpHereImg));
		SignUpHereButton.setBounds(612, 578, 147, 48);
		loginFrame.getContentPane().add(SignUpHereButton);

		SignUpBackDrop = new JLabel("");
		SignUpBackDrop.setBackground(new Color(54, 54, 55));
		SignUpBackDrop.setBounds(602, 576, 166, 56);
		SignUpBackDrop.setOpaque(true);
		loginFrame.getContentPane().add(SignUpBackDrop);

		ExclamationMark = new JLabel("");
		ExclamationMark.setIcon(new ImageIcon(Exclamationmg));
		ExclamationMark.setBounds(828, 340, 30, 30);
		ExclamationMark.setVisible(false);
		loginFrame.getContentPane().add(ExclamationMark);

		ExclamationMark_1 = new JLabel("");
		ExclamationMark_1.setIcon(new ImageIcon(Exclamationmg));
		ExclamationMark_1.setBounds(828, 410, 30, 30);
		ExclamationMark_1.setVisible(false);
		loginFrame.getContentPane().add(ExclamationMark_1);

		LogInBG = new JLabel("");
		LogInBG.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				LogInBG.grabFocus();
			}
		});
		LogInBG.setBackground(new Color(54, 54, 55));
		LogInBG.setIcon(new ImageIcon(LogInBGImg));
		LogInBG.setBounds(0, 0, screenWidth, screenHeight);
		LogInBG.setFocusable(true);
		loginFrame.getContentPane().add(LogInBG);
	}

}
