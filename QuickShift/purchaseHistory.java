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
import javax.swing.SwingConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JPanel;

public class purchaseHistory {

	JFrame purchaseHistoryFrame;
	
	private int screenWidth = 1366, screenHeight = 768;
	
	int startingYPos = 11, currentYPos = 11;
	
	JPanel panel, DeletePanel;
	
	JLabel Update;
	
	private String url = "jdbc:mysql://localhost:3306/quickshift", user = "root", password = "root";
	private JTextField DeleteInput;
	
	public void refresh() {
		currentYPos = startingYPos;
		panel.removeAll();
		loadData();
	}
	
	public void createDatabase() {
		int transactionID = 0;
		
		try (Connection conn = DriverManager.getConnection(url, user, password);
				PreparedStatement preparedStmt = conn.prepareStatement("INSERT INTO sales(customer_id, product_code, products_purchased, quantity_sold, total_amount, discount_code, promotion_code, purchase_date)"
						+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?)")) {
			
			preparedStmt.setInt(1, 1);
			preparedStmt.setInt(2, 0);
			preparedStmt.setString(3, "N/A");
			preparedStmt.setInt(4, 0);
			preparedStmt.setInt(5, 0);
			preparedStmt.setString(6, "N/A");
			preparedStmt.setString(7, "N/A");
			preparedStmt.setString(8, LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
			
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT MAX(transaction_id) FROM sales");
			while (rs.next()) {
				transactionID = rs.getInt("MAX(transaction_id)") + 1;
			}
			stmt.close();
			rs.close();
			
			preparedStmt.executeUpdate();
		} catch (SQLException f) {
			f.printStackTrace();
		}
		
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");

			Connection conn = DriverManager.getConnection(url, user, password);

			String sql = "INSERT INTO purchase_history(transaction_id, promotion_code, discount_code, customer_id, product_code, quantity_sold, total_amount, purchase_date)"
					+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

			PreparedStatement preparedStmt = conn.prepareStatement(sql);
			
			preparedStmt.setInt(1, transactionID);
			preparedStmt.setString(2, "N/A");
			preparedStmt.setString(3, "N/A");
			preparedStmt.setInt(4, 1);
			preparedStmt.setInt(5, 0);
			preparedStmt.setInt(6, 0);
			preparedStmt.setInt(7, 0);
			preparedStmt.setString(8, LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
			
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
			preparedStmt.setInt(2, 1);
			preparedStmt.setInt(3, 1);
			preparedStmt.setString(4, LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
			
			preparedStmt.execute();
			preparedStmt.close();
			conn.close();
		} catch (Exception f) {
			System.err.println("Got an exception!");
			f.printStackTrace();
			System.out.println(f);
		}
		
		refresh();
	}
	
	private String getProductName(int productCode) {
		switch(productCode) {
			case 1:
				return "Border";
			case 2:
				return "Gymkhana";
			case 3:
				return "Revolt LM";
			case 4:
				return "Revolt V2";
			case 5:
				return "Instinct";
			case 6:
				return "Sec Surge";
			case 7:
				return "Revolt";
			case 8:
				return "Challenger V3";
			default:
				return "N/A";
		}
	}
	
	private void deleteDatabase(int input) {
	    try (Connection conn = DriverManager.getConnection(url, user, password)) {
	        // Delete from sales
	        try (PreparedStatement salesStmt = conn.prepareStatement("DELETE FROM sales WHERE transaction_id = ?")) {
	            salesStmt.setInt(1, input);
	            salesStmt.executeUpdate();
	        }

	        // Delete from purchase_history
	        try (PreparedStatement purchaseStmt = conn.prepareStatement("DELETE FROM purchase_history WHERE transaction_id = ?")) {
	            purchaseStmt.setInt(1, input);
	            purchaseStmt.executeUpdate();
	        }

	        // Delete from sales_transaction
	        try (PreparedStatement salesTransStmt = conn.prepareStatement("DELETE FROM sales_transaction WHERE transaction_id = ?")) {
	            salesTransStmt.setInt(1, input);
	            salesTransStmt.executeUpdate();
	        }

	    } catch (SQLException f) {
	        f.printStackTrace();
	    }

	    refresh();
	    DeleteInput.setText("");
	    DeletePanel.setVisible(false);
	}
	
	private void updateOtherDatabase(String transactionID, String columnName, String newValue) {
		try (Connection conn = DriverManager.getConnection(url, user, password);
				PreparedStatement stmt = conn.prepareStatement("UPDATE sales SET " + columnName + " = ?  WHERE transaction_id = ?")) {

			stmt.setString(1, newValue);
			stmt.setString(2, transactionID);

			stmt.executeUpdate();
		} catch (SQLException f) {
			f.printStackTrace();
		}
	}
	
	private void updateDatabase(String transactionID, String columnName, String newValue) {		
		Update.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (!newValue.isEmpty()) {
					// UPDATE sales
					try (Connection conn = DriverManager.getConnection(url, user, password);
							PreparedStatement stmt = conn.prepareStatement("UPDATE sales SET " + columnName + " = ?  WHERE transaction_id = ?")) {

						if (columnName.equalsIgnoreCase("promotion_code") || columnName.equalsIgnoreCase("discount_code") || columnName.equalsIgnoreCase("purchase_date")) {
							stmt.setString(1, newValue);
							stmt.setString(2, transactionID);
						} else if (columnName.equalsIgnoreCase("product_code")) {
							stmt.setInt(1, Integer.parseInt(newValue));
							stmt.setString(2, transactionID);
							updateOtherDatabase(transactionID, "products_purchased", getProductName(Integer.parseInt(newValue)));
						} else {
							stmt.setInt(1, Integer.parseInt(newValue.trim()));
							stmt.setString(2, transactionID);
						}

						stmt.executeUpdate();
					} catch (SQLException f) {
						f.printStackTrace();
					}
					
					// UPDATE purchase_history
					try (Connection conn = DriverManager.getConnection(url, user, password);
							PreparedStatement stmt = conn.prepareStatement("UPDATE purchase_history SET " + columnName + " = ?  WHERE transaction_id = ?")) {

						if (columnName.equalsIgnoreCase("promotion_code") || columnName.equalsIgnoreCase("discount_code") || columnName.equalsIgnoreCase("purchase_date")) {
							stmt.setString(1, newValue);
							stmt.setString(2, transactionID);
						} else {
							stmt.setInt(1, Integer.parseInt(newValue));
							stmt.setString(2, transactionID);
						}

						stmt.executeUpdate();
					} catch (SQLException f) {
						f.printStackTrace();
					}
					
					// UPDATE sales_transaction
					if (columnName.equalsIgnoreCase("customer_id") || columnName.equalsIgnoreCase("purchase_date")) {
						try (Connection conn = DriverManager.getConnection(url, user, password);
								PreparedStatement stmt = conn.prepareStatement("UPDATE sales_transaction SET " + columnName + " = ?  WHERE transaction_id = ?")) {

							stmt.setString(1, newValue);
							stmt.setString(2, transactionID);

							stmt.executeUpdate();
						} catch (SQLException f) {
							f.printStackTrace();
						}
					}
				}
			}
		});
	}
	
	public void loadData() {
		Image backDropSrc = new ImageIcon(this.getClass().getResource("/Transaction History Backdrop.png")).getImage();
		Image backDropImg = backDropSrc.getScaledInstance(915, 915, Image.SCALE_DEFAULT);
		
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			
			Connection conn = DriverManager.getConnection(url, user, password);
			
			Statement stmt = conn.createStatement();
			
			ResultSet rs = stmt.executeQuery("SELECT * FROM purchase_history");
			
			while (rs.next()) {
				// TRANSACTION ID
				JLabel transactionID = new JLabel("");
				transactionID.setForeground(new Color(255, 255, 255));
				transactionID.setHorizontalAlignment(SwingConstants.CENTER);
				transactionID.setBounds(30, currentYPos, 80, 70);
				transactionID.setText(Integer.toString(rs.getInt("transaction_id")));
				panel.add(transactionID);
				
				// CUSTOMER ID
				JTextField customerID = new JTextField("");
				customerID.getDocument().addDocumentListener(new DocumentListener() {
				    public void changedUpdate(DocumentEvent e) {
				        updateDatabase(transactionID.getText(), "customer_id", customerID.getText());
				    }
				    public void removeUpdate(DocumentEvent e) {
				        updateDatabase(transactionID.getText(), "customer_id", customerID.getText());
				    }
				    public void insertUpdate(DocumentEvent e) {
				        updateDatabase(transactionID.getText(), "customer_id", customerID.getText());
				    }
				});
				customerID.setForeground(new Color(255, 255, 255));
				customerID.setHorizontalAlignment(SwingConstants.CENTER);
				customerID.setBounds(177, currentYPos, 80, 62);
				customerID.setText(rs.getString("customer_id"));
				customerID.setOpaque(false);
				customerID.setBorder(javax.swing.BorderFactory.createEmptyBorder());
				panel.add(customerID);
				
				// PRODUCT CODE
				JTextField productCode = new JTextField("");
				productCode.getDocument().addDocumentListener(new DocumentListener() {
				    public void changedUpdate(DocumentEvent e) {
				        updateDatabase(transactionID.getText(), "product_code", productCode.getText());
				    }
				    public void removeUpdate(DocumentEvent e) {
				        updateDatabase(transactionID.getText(), "product_code", productCode.getText());
				    }
				    public void insertUpdate(DocumentEvent e) {
				        updateDatabase(transactionID.getText(), "product_code", productCode.getText());
				    }
				});
				productCode.setForeground(new Color(255, 255, 255));
				productCode.setHorizontalAlignment(SwingConstants.CENTER);
				productCode.setBounds(291, currentYPos, 80, 62);
				productCode.setText(rs.getString("product_code"));
				productCode.setOpaque(false);
				productCode.setBorder(javax.swing.BorderFactory.createEmptyBorder());
				panel.add(productCode);
				
				// DISCOUNT CODE
				JTextField discountCode = new JTextField("");
				discountCode.getDocument().addDocumentListener(new DocumentListener() {
				    public void changedUpdate(DocumentEvent e) {
				        updateDatabase(transactionID.getText(), "discount_code", discountCode.getText());
				    }
				    public void removeUpdate(DocumentEvent e) {
				        updateDatabase(transactionID.getText(), "discount_code", discountCode.getText());
				    }
				    public void insertUpdate(DocumentEvent e) {
				        updateDatabase(transactionID.getText(), "discount_code", discountCode.getText());
				    }
				});
				discountCode.setForeground(new Color(255, 255, 255));
				discountCode.setHorizontalAlignment(SwingConstants.CENTER);
				discountCode.setBounds(401, currentYPos, 80, 62);
				discountCode.setText(rs.getString("discount_code"));
				discountCode.setOpaque(false);
				discountCode.setBorder(javax.swing.BorderFactory.createEmptyBorder());
				panel.add(discountCode);
				
				// PROMOTION CODE
				JTextField promotionCode = new JTextField("");
				promotionCode.getDocument().addDocumentListener(new DocumentListener() {
				    public void changedUpdate(DocumentEvent e) {
				        updateDatabase(transactionID.getText(), "promotion_code", promotionCode.getText());
				    }
				    public void removeUpdate(DocumentEvent e) {
				        updateDatabase(transactionID.getText(), "promotion_code", promotionCode.getText());
				    }
				    public void insertUpdate(DocumentEvent e) {
				        updateDatabase(transactionID.getText(), "promotion_code", promotionCode.getText());
				    }
				});
				promotionCode.setForeground(new Color(255, 255, 255));
				promotionCode.setHorizontalAlignment(SwingConstants.CENTER);
				promotionCode.setBounds(520, currentYPos, 80, 62);
				promotionCode.setText(rs.getString("promotion_code"));
				promotionCode.setOpaque(false);
				promotionCode.setBorder(javax.swing.BorderFactory.createEmptyBorder());
				panel.add(promotionCode);
				
				// DATE
				JTextField date = new JTextField("");
				date.getDocument().addDocumentListener(new DocumentListener() {
				    public void changedUpdate(DocumentEvent e) {
				        updateDatabase(transactionID.getText(), "purchase_date", date.getText());
				    }
				    public void removeUpdate(DocumentEvent e) {
				        updateDatabase(transactionID.getText(), "purchase_date", date.getText());
				    }
				    public void insertUpdate(DocumentEvent e) {
				        updateDatabase(transactionID.getText(), "purchase_date", date.getText());
				    }
				});
				date.setForeground(new Color(255, 255, 255));
				date.setHorizontalAlignment(SwingConstants.CENTER);
				date.setBounds(625, currentYPos, 80, 62);
				date.setText(rs.getString("purchase_date"));
				date.setOpaque(false);
				date.setBorder(javax.swing.BorderFactory.createEmptyBorder());
				panel.add(date);
				
				// QUANTITY
				JTextField quantity = new JTextField("");
				quantity.getDocument().addDocumentListener(new DocumentListener() {
				    public void changedUpdate(DocumentEvent e) {
				        updateDatabase(transactionID.getText(), "quantity_sold", quantity.getText());
				    }
				    public void removeUpdate(DocumentEvent e) {
				        updateDatabase(transactionID.getText(), "quantity_sold", quantity.getText());
				    }
				    public void insertUpdate(DocumentEvent e) {
				        updateDatabase(transactionID.getText(), "quantity_sold", quantity.getText());
				    }
				});
				quantity.setForeground(new Color(255, 255, 255));
				quantity.setHorizontalAlignment(SwingConstants.CENTER);
				quantity.setBounds(725, currentYPos, 80, 62);
				quantity.setText(rs.getString("quantity_sold"));
				quantity.setOpaque(false);
				quantity.setBorder(javax.swing.BorderFactory.createEmptyBorder());
				panel.add(quantity);
				
				JLabel pesoSign = new JLabel("");
				pesoSign.setForeground(new Color(255, 255, 255));
				pesoSign.setHorizontalAlignment(SwingConstants.CENTER);
				pesoSign.setBounds(790, currentYPos, 80, 62);
				pesoSign.setText("P");
				panel.add(pesoSign);
				
				// TOTAL AMOUNT
				JTextField amount = new JTextField("");
				amount.getDocument().addDocumentListener(new DocumentListener() {
				    public void changedUpdate(DocumentEvent e) {
				        updateDatabase(transactionID.getText(), "total_amount", amount.getText());
				    }
				    public void removeUpdate(DocumentEvent e) {
				        updateDatabase(transactionID.getText(), "total_amount", amount.getText());
				    }
				    public void insertUpdate(DocumentEvent e) {
				        updateDatabase(transactionID.getText(), "total_amount", amount.getText());
				    }
				});
				amount.setForeground(new Color(255, 255, 255));
				amount.setHorizontalAlignment(SwingConstants.CENTER);
				amount.setBounds(830, currentYPos, 80, 62);
				amount.setText(rs.getString("total_amount"));
				amount.setOpaque(false);
				amount.setBorder(javax.swing.BorderFactory.createEmptyBorder());
				panel.add(amount);
				
				// BACKDROP
				JLabel backdrop = new JLabel("");
				backdrop.setBounds(0, currentYPos, 915, 70);
				backdrop.setIcon(new ImageIcon(backDropImg));
				panel.add(backdrop);
				
				currentYPos += 73;
				
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
					purchaseHistory window = new purchaseHistory();
					window.purchaseHistoryFrame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public purchaseHistory() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		
		Image PurchaseHistoryBGSrc = new ImageIcon(this.getClass().getResource("/Purchase History BG.png")).getImage();
		Image PurchaseHistoryBGImg = PurchaseHistoryBGSrc.getScaledInstance(screenWidth, screenHeight, Image.SCALE_DEFAULT);
		
		Image CreateButtonSrc = new ImageIcon(this.getClass().getResource("/Create Button.png")).getImage();
		Image CreateButtonImg = CreateButtonSrc.getScaledInstance(199, 199, Image.SCALE_DEFAULT);
		
		Image CreateButtonInverseSrc = new ImageIcon(this.getClass().getResource("/Create Button(Inverse).png")).getImage();
		Image CreateButtonInverseImg = CreateButtonInverseSrc.getScaledInstance(199, 199, Image.SCALE_DEFAULT);
		
		Image UpdateButtonSrc = new ImageIcon(this.getClass().getResource("/Update Button.png")).getImage();
		Image UpdateButtonImg = UpdateButtonSrc.getScaledInstance(199, 199, Image.SCALE_DEFAULT);
		
		Image UpdateButtonInverseSrc = new ImageIcon(this.getClass().getResource("/Update Button(Inverse).png")).getImage();
		Image UpdateButtonInverseImg = UpdateButtonInverseSrc.getScaledInstance(199, 199, Image.SCALE_DEFAULT);
		
		Image DeleteButtonSrc = new ImageIcon(this.getClass().getResource("/Delete Button.png")).getImage();
		Image DeleteButtonImg = DeleteButtonSrc.getScaledInstance(199, 199, Image.SCALE_DEFAULT);
		
		Image DeleteButtonInverseSrc = new ImageIcon(this.getClass().getResource("/Delete Button(Inverse).png")).getImage();
		Image DeleteButtonInverseImg = DeleteButtonInverseSrc.getScaledInstance(199, 199, Image.SCALE_DEFAULT);
		
		Image RefreshButtonSrc = new ImageIcon(this.getClass().getResource("/Refresh Button.png")).getImage();
		Image RefreshButtonImg = RefreshButtonSrc.getScaledInstance(30, 30, Image.SCALE_DEFAULT);
		
		Image RefreshButtonInverseSrc = new ImageIcon(this.getClass().getResource("/Refresh Button(Inverse).png")).getImage();
		Image RefreshButtonInverseImg = RefreshButtonInverseSrc.getScaledInstance(30, 30, Image.SCALE_DEFAULT);
		
		Image DeleteScreenSrc = new ImageIcon(this.getClass().getResource("/Delete Screen.png")).getImage();
		Image DeleteScreenImg = DeleteScreenSrc.getScaledInstance(499, 281, Image.SCALE_DEFAULT);
		
		purchaseHistoryFrame = new JFrame();
		purchaseHistoryFrame.setBounds(100, 100, screenWidth + 14, screenHeight + 37);
		purchaseHistoryFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		purchaseHistoryFrame.getContentPane().setLayout(null);
		
		// DELETE SCREEN VIEW
		DeletePanel = new JPanel();
		DeletePanel.setBounds(581, 339, 499, 281);
		DeletePanel.setOpaque(false);
		DeletePanel.setLayout(null);
		DeletePanel.setVisible(false);
		purchaseHistoryFrame.getContentPane().add(DeletePanel);
		
		JLabel DeleteInputClose = new JLabel("");
		DeleteInputClose.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				DeletePanel.setVisible(false);
			}
		});
		DeleteInputClose.setBounds(27, 36, 28, 30);
		DeletePanel.add(DeleteInputClose);
		
		DeleteInput = new JTextField();
		DeleteInput.setBounds(66, 141, 240, 45);
		DeletePanel.add(DeleteInput);
		DeleteInput.setColumns(10);
		
		JLabel ConfirmDelete = new JLabel("");
		ConfirmDelete.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				deleteDatabase(Integer.parseInt(DeleteInput.getText()));
			}
		});
		ConfirmDelete.setBounds(311, 141, 121, 45);
		DeletePanel.add(ConfirmDelete);
		
		JLabel DeleteScreen = new JLabel("");
		DeleteScreen.setBackground(new Color(54, 54, 55));
		DeleteScreen.setIcon(new ImageIcon(DeleteScreenImg));
		DeleteScreen.setBounds(0, 0, 499, 281);
		DeleteScreen.setFocusable(true);
		DeletePanel.add(DeleteScreen);
		
		// PURCHASE HISTORY SCROLLVIEW
		JScrollPane purchaseHistoryPanel = new JScrollPane();
		purchaseHistoryPanel.setBounds(360, 270, 915, 450);
		purchaseHistoryPanel.setOpaque(false);
		purchaseHistoryPanel.getViewport().setOpaque(false);
		purchaseHistoryFrame.getContentPane().setLayout(null);
		purchaseHistoryPanel.setBorder(BorderFactory.createEmptyBorder());
		purchaseHistoryPanel.setViewportBorder(BorderFactory.createEmptyBorder());
		purchaseHistoryPanel.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		purchaseHistoryPanel.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
		purchaseHistoryFrame.getContentPane().add(purchaseHistoryPanel);

		panel = new JPanel();
		purchaseHistoryPanel.setViewportView(panel);
		panel.setOpaque(false);
		panel.setLayout(null);
		
		// CREATE BUTTON
		JLabel Create = new JLabel("");
		Create.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				Create.setIcon(new ImageIcon(CreateButtonInverseImg));
			}
			@Override
			public void mouseReleased(MouseEvent e) {
				Create.setIcon(new ImageIcon(CreateButtonImg));
			}
			@Override
			public void mouseClicked(MouseEvent e) {
				createDatabase();
			}
		});
		Create.setIcon(new ImageIcon(CreateButtonImg));
		Create.setBounds(482, 131, 199, 63);
		purchaseHistoryFrame.getContentPane().add(Create);
		
		// UPDATE BUTTON
		Update = new JLabel("");
		Update.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				Update.setIcon(new ImageIcon(UpdateButtonInverseImg));
			}
			@Override
			public void mouseReleased(MouseEvent e) {
				Update.setIcon(new ImageIcon(UpdateButtonImg));
			}
		});
		Update.setIcon(new ImageIcon(UpdateButtonImg));
		Update.setBounds(723, 131, 199, 63);
		purchaseHistoryFrame.getContentPane().add(Update);
		
		// DELETE BUTTON
		JLabel Delete = new JLabel("");
		Delete.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				Delete.setIcon(new ImageIcon(DeleteButtonInverseImg));
			}
			@Override
			public void mouseReleased(MouseEvent e) {
				Delete.setIcon(new ImageIcon(DeleteButtonImg));
			}
			@Override
			public void mouseClicked(MouseEvent e) {
				DeletePanel.setVisible(true);
			}
		});
		Delete.setIcon(new ImageIcon(DeleteButtonImg));
		Delete.setBounds(964, 131, 199, 63);
		purchaseHistoryFrame.getContentPane().add(Delete);
		
		// REFRESH
		JLabel Refresh = new JLabel("");
		Refresh.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				Refresh.setIcon(new ImageIcon(RefreshButtonInverseImg));
			}
			@Override
			public void mouseReleased(MouseEvent e) {
				Refresh.setIcon(new ImageIcon(RefreshButtonImg));
			}
			@Override
			public void mouseClicked(MouseEvent e) {
				refresh();
			}
		});
		Refresh.setIcon(new ImageIcon(RefreshButtonImg));
		Refresh.setBounds(1223, 149, 30, 30);
		purchaseHistoryFrame.getContentPane().add(Refresh);
		
		// LOAD PURCHASE HISTORY
		loadData();
		
		// PRODUCT LISTING
		JLabel product = new JLabel("");
		product.addMouseListener(new MouseAdapter() {
			@SuppressWarnings("deprecation")
			@Override
			public void mouseClicked(MouseEvent e) {
				productListing pl = new productListing();
				pl.productListingFrame.show();
				purchaseHistoryFrame.dispose();
			}
		});
		product.setBounds(112, 222, 120, 28);
		purchaseHistoryFrame.getContentPane().add(product);
		
		// DASHBOARD
		JLabel dashboard = new JLabel("");
		dashboard.addMouseListener(new MouseAdapter() {
			@SuppressWarnings("deprecation")
			@Override
			public void mouseClicked(MouseEvent e) {
				dashboard db = new dashboard();
				db.dashboardFrame.show();
				purchaseHistoryFrame.dispose();
			}
		});
		dashboard.setBounds(112, 283, 145, 28);
		purchaseHistoryFrame.getContentPane().add(dashboard);
		
		// RESTOCK PRODUCT
		JLabel restockProduct = new JLabel("");
		restockProduct.addMouseListener(new MouseAdapter() {
			@SuppressWarnings("deprecation")
			@Override
			public void mouseClicked(MouseEvent e) {
				product pd = new product();
				pd.productFrame.show();
				purchaseHistoryFrame.dispose();
			}
		});
		restockProduct.setBounds(110, 410, 201, 31);
		purchaseHistoryFrame.getContentPane().add(restockProduct);
		
		// LOGOUT
		JLabel logout = new JLabel("");
		logout.addMouseListener(new MouseAdapter() {
			@SuppressWarnings("deprecation")
			@Override
			public void mouseClicked(MouseEvent e) {
				login ln = new login();
				ln.loginFrame.show();
				purchaseHistoryFrame.dispose();
			}
		});
		logout.setBounds(111, 690, 113, 31);
		purchaseHistoryFrame.getContentPane().add(logout);
		
		// BACKGROUND
		JLabel purchaseHistoryBG = new JLabel("");
		purchaseHistoryBG.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				purchaseHistoryBG.grabFocus();
			}
		});
		purchaseHistoryBG.setBackground(new Color(54, 54, 55));
		purchaseHistoryBG.setIcon(new ImageIcon(PurchaseHistoryBGImg));
		purchaseHistoryBG.setBounds(0, 0, screenWidth, screenHeight);
		purchaseHistoryBG.setFocusable(true);
		purchaseHistoryFrame.getContentPane().add(purchaseHistoryBG);
	}
}
