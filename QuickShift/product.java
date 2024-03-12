import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.JPanel;

public class product {

	JFrame productFrame;
	
	private int screenWidth = 1366, screenHeight = 768;
	
	JPanel panel;
	
	int startingYPos = 11, currentYPos = 11;
	
	private String url = "jdbc:mysql://localhost:3306/quickshift", user = "root", password = "root";
	private JLabel backButton;
	
	public void refresh() {
		currentYPos = startingYPos;
		panel.removeAll();
		loadData();
	}
	
	public void restock(String productCode, String inStock, String restockAmount, String reorderPoints) {
		if (!restockAmount.isEmpty()) {
			// UPDATE product_instock
			try (Connection conn = DriverManager.getConnection(url, user, password);
					PreparedStatement stmt = conn.prepareStatement("UPDATE products SET product_instock = ?  WHERE product_code = ?")) {

					stmt.setInt(1, Integer.parseInt(inStock) + Integer.parseInt(restockAmount));
					stmt.setString(2, productCode);
				

				stmt.executeUpdate();
			} catch (SQLException f) {
				f.printStackTrace();
			}
			
			// UPDATE products_restocked
			try (Connection conn = DriverManager.getConnection(url, user, password);
					PreparedStatement stmt = conn.prepareStatement("UPDATE products SET products_restocked = ?  WHERE product_code = ?")) {

					stmt.setInt(1, Integer.parseInt(restockAmount));
					stmt.setString(2, productCode);
				

				stmt.executeUpdate();
			} catch (SQLException f) {
				f.printStackTrace();
			}
			
			// UPDATE product_reorderpoints
			try (Connection conn = DriverManager.getConnection(url, user, password);
					PreparedStatement preparedStmt = conn.prepareStatement("UPDATE products SET product_reorderpoints = ?  WHERE product_code = ?")) {
				
					preparedStmt.setInt(1, (Integer.parseInt(reorderPoints) - Integer.parseInt(restockAmount)));
					preparedStmt.setString(2, productCode);
				

					preparedStmt.executeUpdate();
			} catch (SQLException f) {
				f.printStackTrace();
			}
			
			// UPDATE product_restockdate
			try (Connection conn = DriverManager.getConnection(url, user, password);
					PreparedStatement preparedStmt = conn.prepareStatement("UPDATE products SET product_restockdate = ?  WHERE product_code = ?")) {

				preparedStmt.setString(1, LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
				preparedStmt.setString(2, productCode);

				preparedStmt.executeUpdate();
			} catch (SQLException f) {
				f.printStackTrace();
			}
			
			refresh();
		}
	}
	
	public void loadData() {
		Image backDropSrc = new ImageIcon(this.getClass().getResource("/Transaction History Backdrop.png")).getImage();
		Image backDropImg = backDropSrc.getScaledInstance(1175, 1175, Image.SCALE_DEFAULT);
		
		Image RestockButtonSrc = new ImageIcon(this.getClass().getResource("/Restock Button.png")).getImage();
		Image RestockButtonImg = RestockButtonSrc.getScaledInstance(170, 170, Image.SCALE_DEFAULT);
		
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			
			Connection conn = DriverManager.getConnection(url, user, password);
			
			Statement stmt = conn.createStatement();
			
			ResultSet rs = stmt.executeQuery("SELECT * FROM products");
			
			while (rs.next()) {
				// PRODUCT ID
				JLabel productID = new JLabel("");
				productID.setForeground(new Color(255, 255, 255));
				productID.setHorizontalAlignment(SwingConstants.CENTER);
				productID.setBounds(30, currentYPos, 80, 80);
				productID.setText(Integer.toString(rs.getInt("product_code")));
				panel.add(productID);
				
				// PRODUCT NAME
				JLabel productName = new JLabel("");
				productName.setForeground(new Color(255, 255, 255));
				productName.setHorizontalAlignment(SwingConstants.CENTER);
				productName.setBounds(179, currentYPos, 80, 80);
				productName.setText(rs.getString("product_name"));
				panel.add(productName);
				
				// PRODUCT CATEGORY
				JLabel category = new JLabel("");
				category.setForeground(new Color(255, 255, 255));
				category.setHorizontalAlignment(SwingConstants.CENTER);
				category.setBounds(340, currentYPos, 80, 80);
				category.setText(rs.getString("product_category"));
				panel.add(category);
				
				// PRODUCT PRICE
				JLabel price = new JLabel("");
				price.setForeground(new Color(255, 255, 255));
				price.setHorizontalAlignment(SwingConstants.CENTER);
				price.setBounds(456, currentYPos, 80, 80);
				price.setText(Integer.toString(rs.getInt("product_price")));
				panel.add(price);
				
				// PRODUCT STOCK
				JLabel instock = new JLabel("");
				instock.setForeground(new Color(255, 255, 255));
				instock.setHorizontalAlignment(SwingConstants.CENTER);
				instock.setBounds(561, currentYPos, 80, 80);
				instock.setText(Integer.toString(rs.getInt("product_instock")));
				panel.add(instock);
				
				// PRODUCT REORDER POINTS
				JLabel reorderPoints = new JLabel("");
				reorderPoints.setForeground(new Color(255, 255, 255));
				reorderPoints.setHorizontalAlignment(SwingConstants.CENTER);
				reorderPoints.setBounds(662, currentYPos, 80, 80);
				reorderPoints.setText(Integer.toString(rs.getInt("product_reorderpoints")));
				panel.add(reorderPoints);
				
				// PRODUCTS LAST RESTOCKED
				JLabel lastRestocked = new JLabel("");
				lastRestocked.setForeground(new Color(255, 255, 255));
				lastRestocked.setHorizontalAlignment(SwingConstants.CENTER);
				lastRestocked.setBounds(777, currentYPos, 80, 80);
				lastRestocked.setText(rs.getString("product_restockdate"));
				panel.add(lastRestocked);
				
				// PRODUCTS RESTOCKED
				JLabel restocked = new JLabel("");
				restocked.setForeground(new Color(255, 255, 255));
				restocked.setHorizontalAlignment(SwingConstants.CENTER);
				restocked.setBounds(890, currentYPos, 80, 80);
				restocked.setText(Integer.toString(rs.getInt("products_restocked")));
				panel.add(restocked);
				
				// RESTOCK BUTTONS
				int restockPoints = rs.getInt("product_reorderpoints");
				
				JLabel restockAmount = new JLabel("");
				restockAmount.setBounds(1101, currentYPos + 29, 29, 18);
				restockAmount.setForeground(new Color(255, 255, 255));
				restockAmount.setHorizontalAlignment(SwingConstants.CENTER);
				restockAmount.setText("0");
				panel.add(restockAmount);
				
				JLabel restock = new JLabel("");
				restock.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseClicked(MouseEvent e) {
						restock(productID.getText(), instock.getText(), restockAmount.getText(), Integer.toString(restockPoints));
					}
				});
				restock.setBounds(1022, currentYPos + 20, 114, 36);
				panel.add(restock);
				
				JLabel restockButtonAdd = new JLabel("");
				restockButtonAdd.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseClicked(MouseEvent e) {
						if (Integer.parseInt(restockAmount.getText()) < restockPoints) {
							restockAmount.setText(Integer.toString(Integer.parseInt(restockAmount.getText()) + 1));	
						}				
					}
				});
				restockButtonAdd.setBounds(992, currentYPos + 25, 25, 25); 
				panel.add(restockButtonAdd);
				
				JLabel restockButtonSubtract = new JLabel("");
				restockButtonSubtract.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseClicked(MouseEvent e) {
						if (Integer.parseInt(restockAmount.getText()) > 0) {
							restockAmount.setText(Integer.toString(Integer.parseInt(restockAmount.getText()) - 1));	
						}
					}
				});
				restockButtonSubtract.setBounds(1140, currentYPos + 25, 25, 25);
				panel.add(restockButtonSubtract);
				
				JLabel restockButton = new JLabel("");
				restockButton.setBounds(993, currentYPos - 1, 170, 80);
				panel.add(restockButton);
				restockButton.setIcon(new ImageIcon(RestockButtonImg));
				
				// BACKDROP
				JLabel backdrop = new JLabel("");
				backdrop.setBounds(0, currentYPos, 1175, 80);
				backdrop.setIcon(new ImageIcon(backDropImg));
				panel.add(backdrop);
				
				currentYPos += 90;
				
				panel.revalidate();
				panel.repaint();
				panel.setPreferredSize(new Dimension(915, currentYPos + 11));
			}
			rs.close();
			stmt.close();
			conn.close();
		}  catch (ClassNotFoundException f) {
			System.err.println("Could not load JDBC driver: " + f.getMessage());
		} catch (SQLException f) {
			System.err.println("SQL Exception: " + f.getMessage());
		}
	}

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					product window = new product();
					window.productFrame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public product() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		
		Image ProductBGSrc = new ImageIcon(this.getClass().getResource("/Products BG.png")).getImage();
		Image ProductBGImg = ProductBGSrc.getScaledInstance(screenWidth, screenHeight, Image.SCALE_DEFAULT);
		
		Image BackButtonSrc = new ImageIcon(this.getClass().getResource("/Back Button.png")).getImage();
		Image BackButtonImg = BackButtonSrc.getScaledInstance(47, 47, Image.SCALE_DEFAULT);
		
		Image BackButtonInverseSrc = new ImageIcon(this.getClass().getResource("/Back Button(Inverse).png")).getImage();
		Image BackButtonInverseImg = BackButtonInverseSrc.getScaledInstance(47, 47, Image.SCALE_DEFAULT);
		
		productFrame = new JFrame();
		productFrame.setBounds(100, 100, screenWidth + 14, screenHeight + 37);
		productFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		productFrame.getContentPane().setLayout(null);
		
		// PRODUCTS SCROLLVIEW
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(96, 199, 1175, 517);
		scrollPane.setOpaque(false);
		scrollPane.getViewport().setOpaque(false);
		productFrame.getContentPane().setLayout(null);
		scrollPane.setBorder(BorderFactory.createEmptyBorder());
		scrollPane.setViewportBorder(BorderFactory.createEmptyBorder());
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
		productFrame.getContentPane().add(scrollPane);
		
		panel = new JPanel();
		scrollPane.setViewportView(panel);
		panel.setOpaque(false);
		panel.setLayout(null);
		
		// LOAD PRODUCTS
		loadData();
		
		// BACK BUTTON
		backButton = new JLabel("");
		backButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				backButton.setIcon(new ImageIcon(BackButtonInverseImg));
			}
			@Override
			public void mouseReleased(MouseEvent e) {
				backButton.setIcon(new ImageIcon(BackButtonImg));
			}
			@SuppressWarnings("deprecation")
			@Override
			public void mouseClicked(MouseEvent e) {
				productListing pl = new productListing();
				pl.productListingFrame.show();
				productFrame.dispose();
			}
		});
		backButton.setIcon(new ImageIcon(BackButtonImg));
		backButton.setBounds(1207, 48, 47, 47);
		productFrame.getContentPane().add(backButton);
		
		// BACKGROUND
		JLabel productBG = new JLabel("");
		productBG.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				productBG.grabFocus();
			}
		});
		productBG.setBackground(new Color(54, 54, 55));
		productBG.setIcon(new ImageIcon(ProductBGImg));
		productBG.setBounds(0, 0, screenWidth, screenHeight);
		productBG.setFocusable(true);
		productFrame.getContentPane().add(productBG);
	}
}
