import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class signUp {

	JFrame signUpFrame;
	
	private JTextField signup_firstname, signup_age, signup_lastname, signup_gender, signup_email, signup_password;
	
	private JLabel SignUpBackDrop;
	
	private int screenWidth = 1366, screenHeight = 768;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					signUp window = new signUp();
					window.signUpFrame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public signUp() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		Image SignUpBGSrc = new ImageIcon(this.getClass().getResource("/Sign Up BG.png")).getImage();
		Image SignUpBGImg = SignUpBGSrc.getScaledInstance(screenWidth, screenHeight, Image.SCALE_DEFAULT);
		
		Image SignUpButtonSrc = new ImageIcon(this.getClass().getResource("/Sign Up.png")).getImage();
		Image SignUpButtonImg = SignUpButtonSrc.getScaledInstance(147, 147, Image.SCALE_DEFAULT);
		
		Image SignUpButtonInverseSrc = new ImageIcon(this.getClass().getResource("/Sign Up(Inverse).png")).getImage();
		Image SignUpButtonInverseImg = SignUpButtonInverseSrc.getScaledInstance(147, 147, Image.SCALE_DEFAULT);
		
		signUpFrame = new JFrame();
		signUpFrame.setBounds(100, 100, screenWidth + 14, screenHeight + 37);
		signUpFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		signUpFrame.getContentPane().setLayout(null);
		
		// FIRST NAME
		signup_firstname = new JTextField();
		signup_firstname.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				if (signup_firstname.getText().equals("FIRST NAME")) {
					signup_firstname.setText("");
				}
			}
			@Override
			public void focusLost(FocusEvent e) {
				if (signup_firstname.getText().equals("")) {
					signup_firstname.setText("FIRST NAME");
				}
			}
		});
		signup_firstname.setText("FIRST NAME");
		signup_firstname.setForeground(new Color(143, 15, 15));
		signup_firstname.setFont(new Font("SansSerif", Font.BOLD, 15));
		signup_firstname.setBounds(788, 246, 162, 47);
		signup_firstname.setOpaque(false);
		signup_firstname.setBorder(javax.swing.BorderFactory.createEmptyBorder());
		signup_firstname.setColumns(10);
		signUpFrame.getContentPane().add(signup_firstname);
		
		// AGE
		signup_age = new JTextField();
		signup_age.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				if (signup_age.getText().equals("AGE")) {
					signup_age.setText("");
				}
			}
			@Override
			public void focusLost(FocusEvent e) {
				if (signup_age.getText().equals("")) {
					signup_age.setText("AGE");
				}
			}
		});
		signup_age.setText("AGE");
		signup_age.setForeground(new Color(143, 15, 15));
		signup_age.setFont(new Font("SansSerif", Font.BOLD, 15));
		signup_age.setBounds(986, 246, 162, 47);
		signup_age.setOpaque(false);
		signup_age.setBorder(javax.swing.BorderFactory.createEmptyBorder());
		signup_age.setColumns(10);
		signUpFrame.getContentPane().add(signup_age);
		
		// LAST NAME
		signup_lastname = new JTextField();
		signup_lastname.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				if (signup_lastname.getText().equals("LAST NAME")) {
					signup_lastname.setText("");
				}
			}
			@Override
			public void focusLost(FocusEvent e) {
				if (signup_lastname.getText().equals("")) {
					signup_lastname.setText("LAST NAME");
				}
			}
		});
		signup_lastname.setText("LAST NAME");
		signup_lastname.setForeground(new Color(143, 15, 15));
		signup_lastname.setFont(new Font("SansSerif", Font.BOLD, 15));
		signup_lastname.setBounds(788, 316, 162, 47);
		signup_lastname.setOpaque(false);
		signup_lastname.setBorder(javax.swing.BorderFactory.createEmptyBorder());
		signup_lastname.setColumns(10);
		signUpFrame.getContentPane().add(signup_lastname);
		
		// GENDER
		signup_gender = new JTextField();
		signup_gender.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				if (signup_gender.getText().equals("GENDER")) {
					signup_gender.setText("");
				}
			}
			@Override
			public void focusLost(FocusEvent e) {
				if (signup_gender.getText().equals("")) {
					signup_gender.setText("GENDER");
				}
			}
		});
		signup_gender.setText("GENDER");
		signup_gender.setForeground(new Color(143, 15, 15));
		signup_gender.setFont(new Font("SansSerif", Font.BOLD, 15));
		signup_gender.setBounds(986, 316, 162, 47);
		signup_gender.setOpaque(false);
		signup_gender.setBorder(javax.swing.BorderFactory.createEmptyBorder());
		signup_gender.setColumns(10);
		signUpFrame.getContentPane().add(signup_gender);
		
		// EMAIL
		signup_email = new JTextField();
		signup_email.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				if (signup_email.getText().equals("EMAIL")) {
					signup_email.setText("");
				}
			}
			@Override
			public void focusLost(FocusEvent e) {
				if (signup_email.getText().equals("")) {
					signup_email.setText("EMAIL");
				}
			}
		});
		signup_email.setText("EMAIL");
		signup_email.setForeground(new Color(143, 15, 15));
		signup_email.setFont(new Font("SansSerif", Font.BOLD, 15));
		signup_email.setBounds(788, 379, 360, 47);
		signup_email.setOpaque(false);
		signup_email.setBorder(javax.swing.BorderFactory.createEmptyBorder());
		signup_email.setColumns(10);
		signUpFrame.getContentPane().add(signup_email);
		
		// PASSWORD
		signup_password = new JTextField();
		signup_password.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				if (signup_password.getText().equals("PASSWORD")) {
					signup_password.setText("");
				}
			}
			@Override
			public void focusLost(FocusEvent e) {
				if (signup_password.getText().equals("")) {
					signup_password.setText("PASSWORD");
				}
			}
		});
		signup_password.setText("PASSWORD");
		signup_password.setForeground(new Color(143, 15, 15));
		signup_password.setFont(new Font("SansSerif", Font.BOLD, 15));
		signup_password.setBounds(788, 451, 360, 47);
		signup_password.setOpaque(false);
		signup_password.setBorder(javax.swing.BorderFactory.createEmptyBorder());
		signup_password.setColumns(10);
		signUpFrame.getContentPane().add(signup_password);
		
		// Sign Up
		JLabel SignUpButton = new JLabel("");
		SignUpButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				SignUpButton.setIcon(new ImageIcon(SignUpButtonInverseImg));
			}
			@Override
			public void mouseExited(MouseEvent e) {
				SignUpButton.setIcon(new ImageIcon(SignUpButtonImg));
			}
		});
		SignUpButton.setBackground(new Color(54, 54, 55));
		SignUpButton.setIcon(new ImageIcon(SignUpButtonImg));
		SignUpButton.setBounds(885, 550, 148, 45);
		SignUpButton.setFocusable(true);
		signUpFrame.getContentPane().add(SignUpButton);
		
		SignUpBackDrop = new JLabel("");
		SignUpBackDrop.setBackground(new Color(54, 54, 55));
		SignUpBackDrop.setBounds(873, 547, 166, 56);
		SignUpBackDrop.setOpaque(true);
		signUpFrame.getContentPane().add(SignUpBackDrop);
		
		// BACKGROUND
		JLabel SignUpBG = new JLabel("");
		SignUpBG.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				SignUpBG.grabFocus();
			}
		});
		SignUpBG.setBackground(new Color(54, 54, 55));
		SignUpBG.setIcon(new ImageIcon(SignUpBGImg));
		SignUpBG.setBounds(0, 0, screenWidth, screenHeight);
		SignUpBG.setFocusable(true);
		signUpFrame.getContentPane().add(SignUpBG);
	}
}
