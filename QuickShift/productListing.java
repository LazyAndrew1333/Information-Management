import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class productListing{

	JFrame productListingFrame;
	
	private int screenWidth = 1366, screenHeight = 768;
	
	String url = "jdbc:mysql://localhost:3306/quickshift";

	String user = "root";

	String password = "root";
	
	JPanel panel;
	
	@SuppressWarnings("deprecation")
	public void addToCart(int pid) {
		shoppingCart sc = new shoppingCart();
		
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			
			Connection conn = DriverManager.getConnection(url, user, password);
			
			Statement stmt = conn.createStatement();

			ResultSet rs = stmt.executeQuery("SELECT * FROM products WHERE product_code = " + pid);

			while (rs.next()) {
				if (rs.getInt("product_instock") > 0) {
					try {
						Class.forName("com.mysql.cj.jdbc.Driver");
						
						Connection conn2 = DriverManager.getConnection(url, user, password);
						
						Statement stmt2 = conn2.createStatement();
			
						ResultSet rs2 = stmt2.executeQuery("SELECT * FROM products WHERE product_code = " + pid);
			
						while (rs2.next()) {
							Image PID1Src = new ImageIcon(this.getClass().getResource(rs2.getString("product_icon"))).getImage();
							Image PID1Img = PID1Src.getScaledInstance(88, 88, Image.SCALE_DEFAULT);
							sc.productIcon.setIcon(new ImageIcon(PID1Img));
							
							sc.productName.setText(rs2.getString("product_name").toUpperCase());
							
							sc.itemPrice = rs2.getInt("product_price");
							
							sc.product_code = rs2.getInt("product_code");
						}
			
						rs2.close();
						stmt2.close();
						conn2.close();
			
					} catch (ClassNotFoundException e) {
						System.err.println("Could not load JDBC driver: " + e.getMessage());
					} catch (SQLException e) {
						System.err.println("SQL Exception: " + e.getMessage());
					}
					
					sc.shoppingCartFrame.show();
					productListingFrame.dispose();
					sc.totalPrice.setText(Integer.toString(sc.itemPrice));
					sc.itemPrice();
				} else {
					panel.setVisible(true);
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

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					productListing window = new productListing();
					window.productListingFrame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public productListing() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		Image ProductListingBGSrc = new ImageIcon(this.getClass().getResource("/Product Listing BG.png")).getImage();
		Image ProductListingBGImg = ProductListingBGSrc.getScaledInstance(screenWidth, screenHeight,Image.SCALE_DEFAULT);

		Image AddToCartButtonSrc = new ImageIcon(this.getClass().getResource("/Add to cart button.png")).getImage();
		Image AddToCartButtonImg = AddToCartButtonSrc.getScaledInstance(30, 30, Image.SCALE_DEFAULT);

		Image AddToCartButtonInverseSrc = new ImageIcon(this.getClass().getResource("/add to cart button(inverse).png")).getImage();
		Image AddToCartButtonInverseImg = AddToCartButtonInverseSrc.getScaledInstance(30, 30, Image.SCALE_DEFAULT);
		
		Image OutOfStockSrc = new ImageIcon(this.getClass().getResource("/Out of stock.png")).getImage();
		Image OutOfStockImg = OutOfStockSrc.getScaledInstance(499, 281, Image.SCALE_DEFAULT);

		productListingFrame = new JFrame();
		productListingFrame.setBounds(100, 100, screenWidth + 14, screenHeight + 37);
		productListingFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		productListingFrame.getContentPane().setLayout(null);
		
		// OUT OF STOCK
		panel = new JPanel();
		panel.setBounds(573, 264, 499, 281);
		productListingFrame.getContentPane().add(panel);
		panel.setOpaque(false);
		panel.setVisible(false);
		panel.setLayout(null);
		
		JLabel OutOfStockClose = new JLabel("");
		OutOfStockClose.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				panel.setVisible(false);
			}
		});
		OutOfStockClose.setBounds(122, 11, 23, 25);
		panel.add(OutOfStockClose);
		
		JLabel OutOfStockFrame = new JLabel("");
		OutOfStockFrame.setBackground(new Color(54, 54, 55));
		OutOfStockFrame.setIcon(new ImageIcon(OutOfStockImg));
		OutOfStockFrame.setBounds(0, 0, 499, 281);
		OutOfStockFrame.setFocusable(true);
		panel.add(OutOfStockFrame);

		// PID1
		JLabel PID1AddToCart = new JLabel("");
		PID1AddToCart.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				PID1AddToCart.setIcon(new ImageIcon(AddToCartButtonInverseImg));
			}

			@Override
			public void mouseExited(MouseEvent e) {
				PID1AddToCart.setIcon(new ImageIcon(AddToCartButtonImg));
			}

			@Override
			public void mouseClicked(MouseEvent e) {
				addToCart(1);
			}
		});
		PID1AddToCart.setIcon(new ImageIcon(AddToCartButtonImg));
		PID1AddToCart.setBounds(511, 384, 30, 30);
		productListingFrame.getContentPane().add(PID1AddToCart);

		// PID2
		JLabel PID2AddToCart = new JLabel("");
		PID2AddToCart.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				PID2AddToCart.setIcon(new ImageIcon(AddToCartButtonInverseImg));
			}

			@Override
			public void mouseExited(MouseEvent e) {
				PID2AddToCart.setIcon(new ImageIcon(AddToCartButtonImg));
			}

			@Override
			public void mouseClicked(MouseEvent e) {
				addToCart(2);
			}
		});
		PID2AddToCart.setIcon(new ImageIcon(AddToCartButtonImg));
		PID2AddToCart.setBounds(747, 384, 30, 30);
		productListingFrame.getContentPane().add(PID2AddToCart);

		// PID3
		JLabel PID3AddToCart = new JLabel("");
		PID3AddToCart.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				PID3AddToCart.setIcon(new ImageIcon(AddToCartButtonInverseImg));
			}

			@Override
			public void mouseExited(MouseEvent e) {
				PID3AddToCart.setIcon(new ImageIcon(AddToCartButtonImg));
			}

			@Override
			public void mouseClicked(MouseEvent e) {
				addToCart(3);
			}
		});
		PID3AddToCart.setIcon(new ImageIcon(AddToCartButtonImg));
		PID3AddToCart.setBounds(981, 384, 30, 30);
		productListingFrame.getContentPane().add(PID3AddToCart);

		// PID4
		JLabel PID4AddToCart = new JLabel("");
		PID4AddToCart.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				PID4AddToCart.setIcon(new ImageIcon(AddToCartButtonInverseImg));
			}

			@Override
			public void mouseExited(MouseEvent e) {
				PID4AddToCart.setIcon(new ImageIcon(AddToCartButtonImg));
			}

			@Override
			public void mouseClicked(MouseEvent e) {
				addToCart(4);
			}
		});
		PID4AddToCart.setIcon(new ImageIcon(AddToCartButtonImg));
		PID4AddToCart.setBounds(1216, 384, 30, 30);
		productListingFrame.getContentPane().add(PID4AddToCart);

		// PID5
		JLabel PID5AddToCart = new JLabel("");
		PID5AddToCart.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				PID5AddToCart.setIcon(new ImageIcon(AddToCartButtonInverseImg));
			}

			@Override
			public void mouseExited(MouseEvent e) {
				PID5AddToCart.setIcon(new ImageIcon(AddToCartButtonImg));
			}

			@Override
			public void mouseClicked(MouseEvent e) {
				addToCart(5);
			}
		});
		PID5AddToCart.setIcon(new ImageIcon(AddToCartButtonImg));
		PID5AddToCart.setBounds(511, 648, 30, 30);
		productListingFrame.getContentPane().add(PID5AddToCart);

		// PID6
		JLabel PID6AddToCart = new JLabel("");
		PID6AddToCart.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				PID6AddToCart.setIcon(new ImageIcon(AddToCartButtonInverseImg));
			}

			@Override
			public void mouseExited(MouseEvent e) {
				PID6AddToCart.setIcon(new ImageIcon(AddToCartButtonImg));
			}

			@Override
			public void mouseClicked(MouseEvent e) {
				addToCart(6);
			}
		});
		PID6AddToCart.setIcon(new ImageIcon(AddToCartButtonImg));
		PID6AddToCart.setBounds(747, 648, 30, 30);
		productListingFrame.getContentPane().add(PID6AddToCart);

		// PID7
		JLabel PID7AddToCart = new JLabel("");
		PID7AddToCart.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				PID7AddToCart.setIcon(new ImageIcon(AddToCartButtonInverseImg));
			}

			@Override
			public void mouseExited(MouseEvent e) {
				PID7AddToCart.setIcon(new ImageIcon(AddToCartButtonImg));
			}

			@Override
			public void mouseClicked(MouseEvent e) {
				addToCart(7);
			}
		});
		PID7AddToCart.setIcon(new ImageIcon(AddToCartButtonImg));
		PID7AddToCart.setBounds(981, 648, 30, 30);
		productListingFrame.getContentPane().add(PID7AddToCart);

		// PID8
		JLabel PID8AddToCart = new JLabel("");
		PID8AddToCart.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				PID8AddToCart.setIcon(new ImageIcon(AddToCartButtonInverseImg));
			}

			@Override
			public void mouseExited(MouseEvent e) {
				PID8AddToCart.setIcon(new ImageIcon(AddToCartButtonImg));
			}

			@Override
			public void mouseClicked(MouseEvent e) {
				addToCart(8);
			}
		});
		PID8AddToCart.setIcon(new ImageIcon(AddToCartButtonImg));
		PID8AddToCart.setBounds(1216, 648, 30, 30);
		productListingFrame.getContentPane().add(PID8AddToCart);

		// DASHBOARD
		JLabel dashboard = new JLabel("");
		dashboard.addMouseListener(new MouseAdapter() {
			@SuppressWarnings("deprecation")
			@Override
			public void mouseClicked(MouseEvent e) {
				dashboard db = new dashboard();
				db.dashboardFrame.show();
				productListingFrame.dispose();
			}
		});
		dashboard.setBounds(111, 285, 143, 26);
		productListingFrame.getContentPane().add(dashboard);
		
		JLabel purchase = new JLabel("");
		purchase.addMouseListener(new MouseAdapter() {
			@SuppressWarnings("deprecation")
			@Override
			public void mouseClicked(MouseEvent e) {
				purchaseHistory ph = new purchaseHistory();
				ph.purchaseHistoryFrame.show();
				productListingFrame.dispose();
			}
		});
		purchase.setBounds(110, 347, 143, 26);
		productListingFrame.getContentPane().add(purchase);
		
		JLabel restockProduct = new JLabel("");
		restockProduct.addMouseListener(new MouseAdapter() {
			@SuppressWarnings("deprecation")
			@Override
			public void mouseClicked(MouseEvent e) {
				product pd = new product();
				pd.productFrame.show();
				productListingFrame.dispose();
			}
		});
		restockProduct.setBounds(111, 410, 197, 28);
		productListingFrame.getContentPane().add(restockProduct);
		
		// LOGOUT
		JLabel logout = new JLabel("");
		logout.addMouseListener(new MouseAdapter() {
			@SuppressWarnings("deprecation")
			@Override
			public void mouseClicked(MouseEvent e) {
				login ln = new login();
				ln.loginFrame.show();
				productListingFrame.dispose();
			}
		});
		logout.setBounds(111, 661, 115, 34);
		productListingFrame.getContentPane().add(logout);
		
		// BACKGROUND
		JLabel productListingBG = new JLabel("");
		productListingBG.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				productListingBG.grabFocus();
			}
		});
		productListingBG.setBackground(new Color(54, 54, 55));
		productListingBG.setIcon(new ImageIcon(ProductListingBGImg));
		productListingBG.setBounds(0, 0, screenWidth, screenHeight);
		productListingBG.setFocusable(true);
		productListingFrame.getContentPane().add(productListingBG);
	}
}
