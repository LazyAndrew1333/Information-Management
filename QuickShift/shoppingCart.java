import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.JTextField;

public class shoppingCart {

	JFrame shoppingCartFrame;

	private int screenWidth = 1366, screenHeight = 768;
	
	String url = "jdbc:mysql://localhost:3306/quickshift";

	String user = "root";

	String password = "root";

	public int quantity = 1, discount = 0, itemPrice = 0, product_code = 0;
	
	private JTextField applyVoucherInput;
	
	public JLabel orderID, productIcon, quantityCounter, totalPrice, subTotal, productName;
	
	private JLabel applyVoucherClose, applyVoucherApply, voucherInput, orderComplete, total, discountLbl;
	private JLabel product;
	private JLabel dashboard;
	private JLabel purchase;
	private JLabel restockProduct;
	private JLabel logout;
	
	public void checkout() {
		int transactionID = 0;
		
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");

			Connection conn = DriverManager.getConnection(url, user, password);

			String sql = "INSERT INTO sales(customer_id, product_code, products_purchased, quantity_sold, total_amount, discount_code, promotion_code, purchase_date)"
					+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
			
			PreparedStatement preparedStmt = conn.prepareStatement(sql);
			
			String discountCode = (discount > 0) ? applyVoucherInput.getText().toUpperCase() : "N/A";
			
			preparedStmt.setInt(1, 1);
			preparedStmt.setInt(2, product_code);
			preparedStmt.setString(3, productName.getText());
			preparedStmt.setInt(4, quantity);
			preparedStmt.setInt(5, Integer.parseInt(total.getText()));
			preparedStmt.setString(6, discountCode);
			preparedStmt.setString(7, discountCode);
			preparedStmt.setString(8, LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
			
			Statement stmt = conn.createStatement();

			ResultSet rs = stmt.executeQuery("SELECT MAX(transaction_id) FROM sales");

			while (rs.next()) {
				transactionID = rs.getInt("MAX(transaction_id)") + 1;
				orderID.setText("TRANSACTION ID: #00" + Integer.toString(transactionID));
			}
			
			preparedStmt.execute();
			preparedStmt.close();
			rs.close();
			conn.close();
		} catch (Exception f) {
			System.err.println("Got an exception!");
			f.printStackTrace();
			System.out.println(f);
		}
		
		// RETURN TRANSACTION ID
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");

			Connection conn = DriverManager.getConnection(url, user, password);
			
			Statement stmt = conn.createStatement();

			ResultSet rs = stmt.executeQuery("SELECT MAX(transaction_id) FROM sales");

			while (rs.next()) {
				transactionID = rs.getInt("MAX(transaction_id)");
				orderID.setText("TRANSACTION ID: #00" + Integer.toString(transactionID));
				
				String discountCode = (discount > 0) ? applyVoucherInput.getText().toUpperCase() : "N/A";
				
				transaction(transactionID, "N/A", discountCode, 1, product_code, quantity, Integer.parseInt(total.getText()), LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
			}
			
			rs.close();
			conn.close();
		} catch (Exception f) {
			System.err.println("Got an exception!");
			f.printStackTrace();
			System.out.println(f);
		}
		
		// UPDATE STOCK COUNT
		try (Connection conn = DriverManager.getConnection(url, user, password);
				PreparedStatement preparedStmt = conn.prepareStatement("UPDATE products SET product_instock = ?  WHERE product_code = ?")) {

				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery("SELECT * FROM products WHERE product_code = " + product_code);
				
				while (rs.next()) {
					int inStock = rs.getInt("product_instock");
					
					preparedStmt.setInt(1, inStock - quantity);
					preparedStmt.setString(2, Integer.toString(product_code));
				
					preparedStmt.executeUpdate();
				}
		} catch (SQLException f) {
			f.printStackTrace();
		}
		
		// UPDATE REORDER POINTS
		try (Connection conn = DriverManager.getConnection(url, user, password);
				PreparedStatement preparedStmt = conn.prepareStatement("UPDATE products SET product_reorderpoints = ?  WHERE product_code = ?")) {

			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM products WHERE product_code = " + product_code);

			while (rs.next()) {
				int reorderPoints = rs.getInt("product_reorderpoints");

				preparedStmt.setInt(1, reorderPoints + quantity);
				preparedStmt.setString(2, Integer.toString(product_code));

				preparedStmt.executeUpdate();
			}
		} catch (SQLException f) {
			f.printStackTrace();
		}
	}
	
	public void transaction(int transactionID, String promoCode, String discountCode, int customerID, int productCode, int quantitySold, int totalAmount, String purchaseDate) {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");

			Connection conn = DriverManager.getConnection(url, user, password);

			String sql = "INSERT INTO purchase_history(transaction_id, promotion_code, discount_code, customer_id, product_code, quantity_sold, total_amount, purchase_date)"
					+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

			PreparedStatement preparedStmt = conn.prepareStatement(sql);
			
			preparedStmt.setInt(1, transactionID);
			preparedStmt.setString(2, promoCode);
			preparedStmt.setString(3, discountCode);
			preparedStmt.setInt(4, customerID);
			preparedStmt.setInt(5, productCode);
			preparedStmt.setInt(6, quantitySold);
			preparedStmt.setInt(7, totalAmount);
			preparedStmt.setString(8, purchaseDate);
			
			preparedStmt.execute();
			preparedStmt.close();
			conn.close();
		} catch (Exception f) {
			System.err.println("Got an exception!");
			f.printStackTrace();
			System.out.println(f);
		}
		
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");

			Connection conn = DriverManager.getConnection(url, user, password);

			String sql = "INSERT INTO sales_transaction(transaction_id, customer_id, employee_id, purchase_date)"
					+ "VALUES (?, ?, ?, ?)";

			PreparedStatement preparedStmt = conn.prepareStatement(sql);
			
			preparedStmt.setInt(1, transactionID);
			preparedStmt.setInt(2, customerID);
			preparedStmt.setInt(3, 1);
			preparedStmt.setString(4, purchaseDate);
			
			preparedStmt.execute();
			preparedStmt.close();
			conn.close();
		} catch (Exception f) {
			System.err.println("Got an exception!");
			f.printStackTrace();
			System.out.println(f);
		}
	}
	
	public void itemPrice() {
		int total = quantity * itemPrice;
		totalPrice.setText(Integer.toString(total));
		subTotal.setText(Integer.toString(total));
		totalPrice();
	}
	
	public void totalPrice() {
		total.setText(Integer.toString((quantity * itemPrice) - discount));
	}
	
	public void applyVoucher(String code) {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			
			Connection conn = DriverManager.getConnection(url, user, password);
			
			Statement stmt = conn.createStatement();

			ResultSet rs = stmt.executeQuery("SELECT * FROM promotion_and_discount");

			while (rs.next()) {
				if (code.equalsIgnoreCase(rs.getString("discount_code"))) {
					discount = rs.getInt("discount_rate");
					discountLbl.setText(code + " - P" + discount);
				}
			}

			rs.close();
			stmt.close();
			conn.close();

		} catch (ClassNotFoundException f) {
			System.err.println("Could not load JDBC driver: " + f.getMessage());
		} catch (SQLException f) {
			System.err.println("SQL Exception: " + f.getMessage());
		}
		itemPrice();
		applyVoucherClose.setVisible(false);
		applyVoucherInput.setVisible(false);
		voucherInput.setVisible(false);
	}

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					shoppingCart window = new shoppingCart();
					window.shoppingCartFrame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public shoppingCart() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {

		Image ShoppingCartBGSrc = new ImageIcon(this.getClass().getResource("/Cart BG.png")).getImage();
		Image ShoppingCartBGImg = ShoppingCartBGSrc.getScaledInstance(screenWidth, screenHeight, Image.SCALE_DEFAULT);

		Image InputVoucherBGSrc = new ImageIcon(this.getClass().getResource("/Apply voucher.png")).getImage();
		Image InputVoucherBGImg = InputVoucherBGSrc.getScaledInstance(600, 338, Image.SCALE_DEFAULT);

		Image orderCompleteBGSrc = new ImageIcon(this.getClass().getResource("/Order received.png")).getImage();
		Image orderCompleteBGImg = orderCompleteBGSrc.getScaledInstance(600, 338, Image.SCALE_DEFAULT);

		shoppingCartFrame = new JFrame();
		shoppingCartFrame.setBounds(100, 100, screenWidth + 14, screenHeight + 37);
		shoppingCartFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		shoppingCartFrame.getContentPane().setLayout(null);

		// ORDER COMPLETE
		orderID = new JLabel("");
		orderID.setForeground(new Color(255, 255, 255));
		orderID.setHorizontalAlignment(SwingConstants.CENTER);
		orderID.setBounds(623, 452, 259, 42);
		orderID.setVisible(false);
		shoppingCartFrame.getContentPane().add(orderID);
		
		JLabel orderCompleteClose = new JLabel("");
		orderCompleteClose.addMouseListener(new MouseAdapter() {
			@SuppressWarnings("deprecation")
			@Override
			public void mouseClicked(MouseEvent e) {
				productListing pl = new productListing();
				pl.productListingFrame.show();
				orderCompleteClose.setVisible(false);
				orderComplete.setVisible(false);
				quantity = 1;
				shoppingCartFrame.dispose();
			}
		});
		orderCompleteClose.setBounds(603, 222, 21, 21);
		orderCompleteClose.setVisible(false);
		shoppingCartFrame.getContentPane().add(orderCompleteClose);

		orderComplete = new JLabel("");
		orderComplete.setIcon(new ImageIcon(orderCompleteBGImg));
		orderComplete.setBounds(453, 201, 600, 338);
		orderComplete.setVisible(false);
		shoppingCartFrame.getContentPane().add(orderComplete);

		// INPUT VOUCHER
		applyVoucherClose = new JLabel("");
		applyVoucherClose.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				voucherInput.setVisible(false);
				applyVoucherApply.setVisible(false);
				applyVoucherClose.setVisible(false);
				applyVoucherInput.setVisible(false);
			}
		});
		applyVoucherClose.setBounds(484, 246, 32, 32);
		applyVoucherClose.setVisible(false);
		shoppingCartFrame.getContentPane().add(applyVoucherClose);

		applyVoucherInput = new JTextField();
		applyVoucherInput.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				applyVoucher(applyVoucherInput.getText());
			}
		});
		applyVoucherInput.setBounds(533, 371, 288, 55);
		shoppingCartFrame.getContentPane().add(applyVoucherInput);
		applyVoucherInput.setColumns(10);
		applyVoucherInput.setVisible(false);

		applyVoucherApply = new JLabel("");
		applyVoucherApply.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				applyVoucher(applyVoucherInput.getText());
			}
		});
		applyVoucherApply.setBounds(826, 371, 147, 54);
		applyVoucherApply.setVisible(false);
		shoppingCartFrame.getContentPane().add(applyVoucherApply);

		voucherInput = new JLabel("");
		voucherInput.setIcon(new ImageIcon(InputVoucherBGImg));
		voucherInput.setBounds(453, 201, 600, 338);
		voucherInput.setVisible(false);
		shoppingCartFrame.getContentPane().add(voucherInput);

		// Product Icon (Cart)
		productIcon = new JLabel("");
		productIcon.setBounds(496, 252, 88, 88);
		shoppingCartFrame.getContentPane().add(productIcon);

		// Quantity Counter (Cart)
		quantityCounter = new JLabel("1");
		quantityCounter.setHorizontalAlignment(SwingConstants.CENTER);
		quantityCounter.setBounds(817, 262, 26, 19);
		shoppingCartFrame.getContentPane().add(quantityCounter);

		// Quantity Selector (Cart)
		JLabel quantityCounterAdd = new JLabel("");
		quantityCounterAdd.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				try {
					Class.forName("com.mysql.cj.jdbc.Driver");
					
					Connection conn = DriverManager.getConnection(url, user, password);
					
					Statement stmt = conn.createStatement();

					ResultSet rs = stmt.executeQuery("SELECT * FROM products WHERE product_code = " + product_code);

					while (rs.next()) {
						if (quantity < rs.getInt("product_instock")) {
							quantity += 1;
							quantityCounter.setText(Integer.toString(quantity));
							itemPrice();
						}
					}

					rs.close();
					stmt.close();
					conn.close();

				} catch (ClassNotFoundException f) {
					System.err.println("Could not load JDBC driver: " + f.getMessage());
				} catch (SQLException f) {
					System.err.println("SQL Exception: " + f.getMessage());
				}
			}
		});
		quantityCounterAdd.setHorizontalAlignment(SwingConstants.CENTER);
		quantityCounterAdd.setBounds(791, 262, 26, 19);
		shoppingCartFrame.getContentPane().add(quantityCounterAdd);

		JLabel quantityCounterSubtract = new JLabel("");
		quantityCounterSubtract.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (quantity > 1) {
					quantity -= 1;
					quantityCounter.setText(Integer.toString(quantity));
					itemPrice();
				}
			}
		});
		quantityCounterSubtract.setHorizontalAlignment(SwingConstants.CENTER);
		quantityCounterSubtract.setBounds(841, 262, 26, 19);
		shoppingCartFrame.getContentPane().add(quantityCounterSubtract);

		// CHECK OUT
		JLabel checkout = new JLabel("");
		checkout.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				checkout();
				orderID.setVisible(true);
				orderCompleteClose.setVisible(true);
				orderComplete.setVisible(true);
			}
		});
		checkout.setBounds(470, 653, 284, 42);
		shoppingCartFrame.getContentPane().add(checkout);

		// CONTINUE SHOPPING
		JLabel continueShopping = new JLabel("");
		continueShopping.addMouseListener(new MouseAdapter() {
			@SuppressWarnings("deprecation")
			@Override
			public void mouseClicked(MouseEvent e) {
				productListing pl = new productListing();
				quantity = 1;
				quantityCounter.setText("1");
				pl.productListingFrame.show();
				shoppingCartFrame.dispose();
			}
		});
		continueShopping.setBounds(766, 653, 259, 42);
		shoppingCartFrame.getContentPane().add(continueShopping);

		// APPLY VOUCHER
		JLabel applyVoucher = new JLabel("");
		applyVoucher.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				voucherInput.setVisible(true);
				applyVoucherApply.setVisible(true);
				applyVoucherClose.setVisible(true);
				applyVoucherInput.setVisible(true);
			}
		});
		applyVoucher.setBounds(902, 606, 123, 31);
		shoppingCartFrame.getContentPane().add(applyVoucher);

		// TOTAL PRICE
		totalPrice = new JLabel("");
		totalPrice.setForeground(new Color(255, 255, 255));
		totalPrice.setHorizontalAlignment(SwingConstants.CENTER);
		totalPrice.setBounds(896, 262, 101, 19);
		shoppingCartFrame.getContentPane().add(totalPrice);

		subTotal = new JLabel("");
		subTotal.setForeground(Color.WHITE);
		subTotal.setBounds(536, 620, 101, 19);
		shoppingCartFrame.getContentPane().add(subTotal);
		
		total = new JLabel("");
		total.setForeground(Color.WHITE);
		total.setBounds(536, 600, 101, 19);
		shoppingCartFrame.getContentPane().add(total);
		
		discountLbl = new JLabel("");
		discountLbl.setForeground(Color.WHITE);
		discountLbl.setBounds(758, 621, 101, 19);
		shoppingCartFrame.getContentPane().add(discountLbl);

		// PRODUCT NAME
		productName = new JLabel("");
		productName.setHorizontalAlignment(SwingConstants.CENTER);
		productName.setForeground(Color.WHITE);
		productName.setBounds(594, 264, 101, 19);
		shoppingCartFrame.getContentPane().add(productName);
		
		// PRODUCT LISTING
		product = new JLabel("");
		product.addMouseListener(new MouseAdapter() {
			@SuppressWarnings("deprecation")
			@Override
			public void mouseClicked(MouseEvent e) {
				productListing pl = new productListing();
				quantity = 1;
				quantityCounter.setText("1");
				pl.productListingFrame.show();
				shoppingCartFrame.dispose();
			}
		});
		product.setBounds(181, 219, 120, 29);
		shoppingCartFrame.getContentPane().add(product);
		
		// DASHBOARD
		dashboard = new JLabel("");
		dashboard.addMouseListener(new MouseAdapter() {
			@SuppressWarnings("deprecation")
			@Override
			public void mouseClicked(MouseEvent e) {
				dashboard db = new dashboard();
				db.dashboardFrame.show();
				shoppingCartFrame.dispose();
			}
		});
		dashboard.setBounds(181, 281, 146, 29);
		shoppingCartFrame.getContentPane().add(dashboard);
		
		// PURCHASE HISTORY
		purchase = new JLabel("");
		purchase.addMouseListener(new MouseAdapter() {
			@SuppressWarnings("deprecation")
			@Override
			public void mouseClicked(MouseEvent e) {
				purchaseHistory ph = new purchaseHistory();
				ph.purchaseHistoryFrame.show();
				shoppingCartFrame.dispose();
			}
		});
		purchase.setBounds(181, 342, 131, 29);
		shoppingCartFrame.getContentPane().add(purchase);
		
		// RESTOCK PRODUCT
		restockProduct = new JLabel("");
		restockProduct.addMouseListener(new MouseAdapter() {
			@SuppressWarnings("deprecation")
			@Override
			public void mouseClicked(MouseEvent e) {
				product pd = new product();
				pd.productFrame.show();
				shoppingCartFrame.dispose();
			}
		});
		restockProduct.setBounds(180, 408, 196, 29);
		shoppingCartFrame.getContentPane().add(restockProduct);
		
		// LOGOUT
		logout = new JLabel("");
		restockProduct.addMouseListener(new MouseAdapter() {
			@SuppressWarnings("deprecation")
			@Override
			public void mouseClicked(MouseEvent e) {
				login ln = new login();
				ln.loginFrame.show();
				shoppingCartFrame.dispose();
			}
		});
		logout.setBounds(181, 662, 110, 29);
		shoppingCartFrame.getContentPane().add(logout);

		// BACKGROUND
		JLabel shoppingCartBG = new JLabel("");
		shoppingCartBG.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				shoppingCartBG.grabFocus();
			}
		});
		shoppingCartBG.setBackground(new Color(54, 54, 55));
		shoppingCartBG.setIcon(new ImageIcon(ShoppingCartBGImg));
		shoppingCartBG.setBounds(0, 0, screenWidth, screenHeight);
		shoppingCartBG.setFocusable(true);
		shoppingCartFrame.getContentPane().add(shoppingCartBG);
	}
}
