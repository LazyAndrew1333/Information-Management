import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.JPanel;
import java.awt.Font;

public class dashboard {

	public JPanel panel;
	
	public JFrame dashboardFrame;
	
	int startingYPos = 11, currentYPos = 11;
	
	private int screenWidth = 1366, screenHeight = 768;
	
	private JLabel totalSales, mostSoldItem, logout;
	
	private String url = "jdbc:mysql://localhost:3306/quickshift", user = "root", password = "root";
	private JLabel product;
	private JLabel purchase;
	private JLabel restockProduct;
	
	public void header() {
		try {
			Connection conn = DriverManager.getConnection(url, user, password);
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT product_code, SUM(quantity_sold) as quantitySold FROM purchase_history GROUP BY product_code ORDER BY quantitySold DESC LIMIT 1");
			while (rs.next()) {
				Connection conn4 = DriverManager.getConnection(url, user, password);
				Statement stmt4 = conn4.createStatement();
				ResultSet rs4 = stmt4.executeQuery("SELECT product_name FROM products WHERE product_code = " + rs.getInt("product_code"));
				while (rs4.next()) {
					mostSoldItem.setText(rs4.getString("product_name"));
				}
			}
			
			rs.close();
			stmt.close();
			conn.close();
			
			Connection conn2 = DriverManager.getConnection(url, user, password);
			Statement stmt2 = conn2.createStatement();
			ResultSet rs2 = stmt2.executeQuery("SELECT SUM(total_amount) as total_sales FROM purchase_history");
			while (rs2.next()) {
				totalSales.setText("P" + rs2.getString("total_sales"));
			}
			
			rs2.close();
			stmt2.close();
			conn2.close();
		} catch (SQLException f) {
			System.err.println("SQL Exception: " + f.getMessage());
		}
	}
	
	public void loadData() {
		Image backDropSrc = new ImageIcon(this.getClass().getResource("/Transaction History Backdrop.png")).getImage();
		Image backDropImg = backDropSrc.getScaledInstance(881, 881, Image.SCALE_DEFAULT);
		
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			
			Connection conn = DriverManager.getConnection(url, user, password);
			
			Statement stmt = conn.createStatement();
			
			ResultSet rs = stmt.executeQuery("SELECT * FROM sales_transaction");
			
			while (rs.next()) {
				JLabel transactionID = new JLabel("");
				transactionID.setForeground(new Color(255, 255, 255));
				transactionID.setHorizontalAlignment(SwingConstants.CENTER);
				transactionID.setBounds(80, currentYPos, 80, 62);
				transactionID.setText(Integer.toString(rs.getInt("transaction_id")));
				panel.add(transactionID);
				
				JLabel customerID = new JLabel("");
				customerID.setForeground(new Color(255, 255, 255));
				customerID.setHorizontalAlignment(SwingConstants.CENTER);
				customerID.setBounds(290, currentYPos, 80, 62);
				customerID.setText(rs.getString("customer_id"));
				customerID.setOpaque(false);
				customerID.setBorder(javax.swing.BorderFactory.createEmptyBorder());
				panel.add(customerID);
				
				JLabel employeeID = new JLabel("");
				employeeID.setForeground(new Color(255, 255, 255));
				employeeID.setHorizontalAlignment(SwingConstants.CENTER);
				employeeID.setBounds(510, currentYPos, 80, 62);
				employeeID.setText(rs.getString("employee_id"));
				employeeID.setOpaque(false);
				employeeID.setBorder(javax.swing.BorderFactory.createEmptyBorder());
				panel.add(employeeID);
				
				JLabel date = new JLabel("");
				date.setForeground(new Color(255, 255, 255));
				date.setHorizontalAlignment(SwingConstants.CENTER);
				date.setBounds(713, currentYPos, 80, 62);
				date.setText(rs.getString("purchase_date"));
				date.setOpaque(false);
				date.setBorder(javax.swing.BorderFactory.createEmptyBorder());
				panel.add(date);
				
				header();
				
				JLabel backdrop = new JLabel("");
				backdrop.setBounds(0, currentYPos, 881, 62);
				backdrop.setIcon(new ImageIcon(backDropImg));
				panel.add(backdrop);
				
				currentYPos += 73;
				
				panel.revalidate();
				panel.repaint();
				panel.setPreferredSize(new Dimension(881, currentYPos + 11));
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
					dashboard window = new dashboard();
					window.dashboardFrame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public dashboard() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		
		Image dashboardBGSrc = new ImageIcon(this.getClass().getResource("/DB BG.png")).getImage();
		Image dashboardBGImg = dashboardBGSrc.getScaledInstance(screenWidth, screenHeight, Image.SCALE_DEFAULT);
		
		dashboardFrame = new JFrame();
		dashboardFrame.setBounds(100, 100, screenWidth + 14, screenHeight + 37);
		dashboardFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JScrollPane transactionHistoryPanel = new JScrollPane();
		transactionHistoryPanel.setBounds(376, 450, 881, 269);
		transactionHistoryPanel.setOpaque(false);
		transactionHistoryPanel.getViewport().setOpaque(false);
		dashboardFrame.getContentPane().setLayout(null);
		transactionHistoryPanel.setBorder(BorderFactory.createEmptyBorder());
		transactionHistoryPanel.setViewportBorder(BorderFactory.createEmptyBorder());
		transactionHistoryPanel.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		transactionHistoryPanel.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
		dashboardFrame.getContentPane().add(transactionHistoryPanel);
		
		totalSales = new JLabel("");
		totalSales.setHorizontalAlignment(SwingConstants.CENTER);
		totalSales.setForeground(new Color(255, 255, 255));
		totalSales.setFont(new Font("Tahoma", Font.BOLD, 34));
		totalSales.setBounds(788, 273, 251, 59);
		dashboardFrame.getContentPane().add(totalSales);
		
		mostSoldItem = new JLabel("");
		mostSoldItem.setForeground(new Color(255, 255, 255));
		mostSoldItem.setHorizontalAlignment(SwingConstants.CENTER);
		mostSoldItem.setFont(new Font("Tahoma", Font.BOLD, 34));
		mostSoldItem.setBounds(788, 203, 251, 59);
		dashboardFrame.getContentPane().add(mostSoldItem);
		
		panel = new JPanel();
		transactionHistoryPanel.setViewportView(panel);
		panel.setOpaque(false);
		panel.setLayout(null);
		
		// LOAD TRANSACTION HISTORY
		loadData();
	
		// PRODUCT LISTING
		product = new JLabel("");
		product.addMouseListener(new MouseAdapter() {
			@SuppressWarnings("deprecation")
			@Override
			public void mouseClicked(MouseEvent e) {
				productListing pl = new productListing();
				pl.productListingFrame.show();
				dashboardFrame.dispose();
			}
		});
		product.setBounds(110, 232, 116, 33);
		dashboardFrame.getContentPane().add(product);
		
		// PURCHASE HISTORY
		purchase = new JLabel("");
		purchase.addMouseListener(new MouseAdapter() {
			@SuppressWarnings("deprecation")
			@Override
			public void mouseClicked(MouseEvent e) {
				purchaseHistory ph = new purchaseHistory();
				ph.purchaseHistoryFrame.show();
				dashboardFrame.dispose();
			}
		});
		purchase.setBounds(111, 353, 130, 33);
		dashboardFrame.getContentPane().add(purchase);
		
		// RESTOCK PRODUCT
		restockProduct = new JLabel("");
		restockProduct.addMouseListener(new MouseAdapter() {
			@SuppressWarnings("deprecation")
			@Override
			public void mouseClicked(MouseEvent e) {
				product pd = new product();
				pd.productFrame.show();
				dashboardFrame.dispose();
			}
		});
		restockProduct.setBounds(113, 420, 194, 33);
		dashboardFrame.getContentPane().add(restockProduct);
		
		// LOGOUT
		logout = new JLabel("");
		logout.addMouseListener(new MouseAdapter() {
			@SuppressWarnings("deprecation")
			@Override
			public void mouseClicked(MouseEvent e) {
				login ln = new login();
				ln.loginFrame.show();
				dashboardFrame.dispose();
			}
		});
		logout.setBounds(111, 673, 116, 33);
		dashboardFrame.getContentPane().add(logout);
		
		// BACKGROUND
		JLabel dashboardBG = new JLabel("");
		dashboardBG.setBounds(0, 0, 1366, 768);
		dashboardBG.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				dashboardBG.grabFocus();
			}
		});
		dashboardBG.setBackground(new Color(54, 54, 55));
		dashboardBG.setIcon(new ImageIcon(dashboardBGImg));
		dashboardBG.setFocusable(true);
		dashboardFrame.getContentPane().add(dashboardBG);
	}
}
