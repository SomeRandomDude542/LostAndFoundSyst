package laf;

import java.awt.EventQueue;


import java.awt.GraphicsEnvironment;
import java.awt.Point;
import javax.swing.SwingUtilities;
import java.awt.Font;
import java.awt.Toolkit;
import javax.imageio.ImageIO;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import javax.swing.JDialog;
import javax.swing.JTextArea;
import javax.swing.JPasswordField;
import javax.swing.Box;
import java.awt.BorderLayout;
import java.awt.Component;
import java.sql.Connection;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Insets;
import java.io.File;
import javax.swing.JFileChooser;
import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.BorderFactory;
import javax.swing.JOptionPane;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.Color;
import javax.swing.JLayeredPane;
import javax.swing.JSplitPane;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.SwingConstants;
import javax.swing.JButton;
import javax.swing.JTabbedPane;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JTextField;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.ScrollPaneConstants;
import javax.swing.BoxLayout;
import java.sql.PreparedStatement;
import java.sql.Connection;
import java.io.File;




public class DashboardWindow extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTabbedPane Dashboardtab;
	private JPanel LostItemPan;
	private JPanel ProgressPan;
	private JTextField textField;
	private JTextField textField_1;
	private JTextField textField_2;
	private JTextField textField_3;
	private JTextField textField_4;
	private JTextField textField_5;
	private JTextField textField_6;
	private JTextField textField_7;
	private String currentUser;
	private File selectedImageFile = null;
	private String savedImagePath = null;
	private JLabel lblImagePreview;
    private String currentUsername;
    private JLabel lblImagePreview_1; 
    private JButton btnImageIns_1; 
    private JPanel pendingLostList;
    private JLabel lblFoundItemsUnclaimed;  
    private JLabel lblLostItemPending; 
    private JScrollPane scrollPaneforLost;
    private JScrollPane scrollPaneforFound; 
    private JPanel pendingFoundListPanel; 
    private JPanel SettingsPanel;
    private JPanel GuidePanel;
    private JLabel FirstPanellbl;
    private JButton btnSetting;     
    private JButton btnGuide;
    
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					AdminDashboardWindow frame = new AdminDashboardWindow();
					 frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	public DashboardWindow() {
	    this("Admin");
	}

	private String loggedUser;

	public DashboardWindow(String username) {
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int width = screenSize.width;
		int height = screenSize.height;

		setSize(width, height);
		setLocationRelativeTo(null);

		setExtendedState(JFrame.MAXIMIZED_BOTH);

		ResponsiveScaler.initializeScale(width, height, 548, 358);
		
	    this.loggedUser = username;
	    this.currentUser = username;
	    this.currentUsername = username;  
	    setTitle("Lost And Found");
	    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    setSize(width, height);
	    setExtendedState(JFrame.MAXIMIZED_BOTH); // ✅ This maximizes the window
	    setLocationRelativeTo(null);

	    // Initialize scaling AFTER setting window size
	    ResponsiveScaler.initializeScale(width, height, 548, 358);
	
	  
	  

	    contentPane = new JPanel();
	    contentPane.setBackground(new Color(255, 255, 255));
	    contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
	    setContentPane(contentPane);
	    contentPane.setLayout(null);
	    
	    SessionManager sessionManager = new SessionManager(this, username);
	    System.out.println("✅ Session manager started for: " + username);

	    JPanel panel = new JPanel();
	    panel.setBounds(0, 0, 142, 329);
	    panel.setBackground(new Color(90, 14, 36));
	    contentPane.add(panel);
	    panel.setLayout(null);

	    JLabel lblNewLabel_5_1 = new JLabel() {
	        private Image originalImage;
	        
	        {
	            try {
	                ImageIcon icon = new ImageIcon(DashboardWindow.class.getResource("/image/Crimson Qcu Logo.png"));
	                originalImage = icon.getImage();
	            } catch (Exception e) {
	                e.printStackTrace();
	            }
	        }
	        
	        @Override
	        protected void paintComponent(Graphics g) {
	            super.paintComponent(g);
	            if (originalImage != null) {
	                Graphics2D g2 = (Graphics2D) g.create();
	                g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, 
	                                   RenderingHints.VALUE_INTERPOLATION_BILINEAR);
	                g2.setRenderingHint(RenderingHints.KEY_RENDERING, 
	                                   RenderingHints.VALUE_RENDER_QUALITY);
	                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, 
	                                   RenderingHints.VALUE_ANTIALIAS_ON);
	                
	                // Calculate scaling to fit within label bounds while maintaining aspect ratio
	                int labelWidth = getWidth();
	                int labelHeight = getHeight();
	                int imgWidth = originalImage.getWidth(this);
	                int imgHeight = originalImage.getHeight(this);
	                
	                double scaleX = (double) labelWidth / imgWidth;
	                double scaleY = (double) labelHeight / imgHeight;
	                double scale = Math.min(scaleX, scaleY); // Maintain aspect ratio
	                
	                int scaledWidth = (int) (imgWidth * scale);
	                int scaledHeight = (int) (imgHeight * scale);
	                
	                // Center the image
	                int x = (labelWidth - scaledWidth) / 2;
	                int y = (labelHeight - scaledHeight) / 2;
	                
	                g2.drawImage(originalImage, x, y, scaledWidth, scaledHeight, this);
	                g2.dispose();
	            }
	        }
	    };
	    lblNewLabel_5_1.setBounds(10, 5, 55, 55);
	    panel.add(lblNewLabel_5_1);
	    
	    JLabel lblNewLabel_5_1_1 = new JLabel() {
	        private Image originalImage;
	        
	        {
	            try {
	                ImageIcon icon = new ImageIcon(DashboardWindow.class.getResource("/image/Crimson Logo.png"));
	                originalImage = icon.getImage();
	            } catch (Exception e) {
	                e.printStackTrace();
	            }
	        }
	        
	        @Override
	        protected void paintComponent(Graphics g) {
	            super.paintComponent(g);
	            if (originalImage != null) {
	                Graphics2D g2 = (Graphics2D) g.create();
	                g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, 
	                                   RenderingHints.VALUE_INTERPOLATION_BILINEAR);
	                g2.setRenderingHint(RenderingHints.KEY_RENDERING, 
	                                   RenderingHints.VALUE_RENDER_QUALITY);
	                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, 
	                                   RenderingHints.VALUE_ANTIALIAS_ON);
	                
	                // Calculate scaling to fit within label bounds while maintaining aspect ratio
	                int labelWidth = getWidth();
	                int labelHeight = getHeight();
	                int imgWidth = originalImage.getWidth(this);
	                int imgHeight = originalImage.getHeight(this);
	                
	                double scaleX = (double) labelWidth / imgWidth;
	                double scaleY = (double) labelHeight / imgHeight;
	                double scale = Math.min(scaleX, scaleY); // Maintain aspect ratio
	                
	                int scaledWidth = (int) (imgWidth * scale);
	                int scaledHeight = (int) (imgHeight * scale);
	                
	                // Center the image
	                int x = (labelWidth - scaledWidth) / 2;
	                int y = (labelHeight - scaledHeight) / 2;
	                
	                g2.drawImage(originalImage, x, y, scaledWidth, scaledHeight, this);
	                g2.dispose();
	            }
	        }
	    };	    	    	    
	    lblNewLabel_5_1_1.setBounds(77, 5, 55, 55);
	    panel.add(lblNewLabel_5_1_1);
	    

	    JButton btnHome = RoundedComponents.createRoundedButtonWithHover(
	    	    "Home",
	    	    new Color(90, 14, 36),       // background
	    	    new Color(255, 200, 200),       // hover color
	    	    Color.WHITE,                    // text color
	    	    1,                              // radius
	    	    RoundedComponents.ALL_CORNERS
	    	);
	    	btnHome.setFont(FontLoader.loadPoppins("Bold", 15f));
	    	btnHome.setBorder(BorderFactory.createLineBorder(Color.WHITE, 1)); 
	    	btnHome.setBounds(0, 174, 142, 30);
	    btnHome.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent e) {
	    	}
	    });
	    panel.add(btnHome);
	   
	    JButton btnLogout = RoundedComponents.createRoundedButtonWithHover(
            	    "Logout",
            	    new Color(90, 14, 36),       // background
    	    	    new Color(255, 200, 200),       // hover color
    	    	    Color.WHITE,                    // text color
    	    	    1,                              // radius
    	    	    RoundedComponents.ALL_CORNERS
    	    	);
	    btnLogout.setFont(FontLoader.loadPoppins("Bold", 15f));
   	    btnLogout.setBounds(0, 229, 142, 30);
   	    panel.add(btnLogout);


	    JButton btnLostItem = RoundedComponents.createRoundedButtonWithHover(
            	    "Lost Item",
            	    new Color(90, 14, 36),       // background
    	    	    new Color(255, 200, 200),       // hover color
    	    	    Color.WHITE,                    // text color
    	    	    1,                              // radius
    	    	    RoundedComponents.ALL_CORNERS
    	    	);
    	    	btnHome.setFont(FontLoader.loadPoppins("Bold", 15f));
	    btnLostItem.setBounds(0, 64, 142, 30);
	    panel.add(btnLostItem);

	    JButton btnProgress = RoundedComponents.createRoundedButtonWithHover(
        	    "Progress",
        	    new Color(90, 14, 36),       // background
	    	    new Color(255, 200, 200),       // hover color
	    	    Color.WHITE,                    // text color
	    	    1,                              // radius
	    	    RoundedComponents.ALL_CORNERS
	    	);
	    	btnHome.setFont(FontLoader.loadPoppins("Bold", 15f));
	    btnProgress.setBounds(0, 119, 142, 30);
	    panel.add(btnProgress);
	    
	    	   	   	   	    
	    Dashboardtab = new JTabbedPane(JTabbedPane.TOP);
	    Dashboardtab.setBounds(142, -2, 406, 360);  // 548 total - 142 sidebar = 406 remaining
	    contentPane.add(Dashboardtab);
	    
	    Dashboardtab.setUI(new javax.swing.plaf.basic.BasicTabbedPaneUI() {
	        @Override
	        protected int calculateTabAreaHeight(int tabPlacement, int runCount, int maxTabHeight) {
	            return 0; 
	        }

	        @Override
	        protected int calculateTabAreaWidth(int tabPlacement, int vertRunCount, int maxTabWidth) {
	            return 0;
	        }
	    });

	    contentPane.add(Dashboardtab);
	    	    
	    	    int lostItemsCount = countUserLostItems(currentUsername);
	    
	    	    LostItemPan = new JPanel();
	    	    LostItemPan.setBackground(new Color(255, 255, 255));
	    	    Dashboardtab.addTab("Lost Item", LostItemPan);
	    	    LostItemPan.setLayout(null);
	    	    
	    	    JLabel LostItemSelectLabel = new JLabel("Select an Action");
	    	    LostItemSelectLabel.setBounds(98, 32, 177, 51);
	    	    LostItemSelectLabel.setHorizontalAlignment(SwingConstants.CENTER);
	    	    LostItemSelectLabel.setFont(new Font("Tahoma", Font.BOLD, 20));
	    	    LostItemPan.add(LostItemSelectLabel);
	    	    
	    	    JButton btnReportLost = RoundedComponents.createRoundedButton(
	    	    	    "",
	    	    	    new Color(90, 14, 36),       // background
	    	    	    Color.WHITE,                 // text
	    	    	    Color.BLACK,                 // border
	    	    	    30,                          // radius
	    	    	    RoundedComponents.ALL_CORNERS
	    	    	);
	    	    	btnReportLost.setFont(new Font("Tahoma", Font.BOLD, 28));

	    	    	// Store the original image
	    	    	ImageIcon originalIcon = new ImageIcon(getClass().getResource("/image/Submit.png"));
	    	    	Image originalImage = originalIcon.getImage();

	    	    	// Add a component listener to handle resizing
	    	    	btnReportLost.addComponentListener(new java.awt.event.ComponentAdapter() {
	    	    	    @Override
	    	    	    public void componentResized(java.awt.event.ComponentEvent e) {
	    	    	        // Calculate new icon size based on button size (70% of smallest dimension)
	    	    	        int buttonSize = Math.min(btnReportLost.getWidth(), btnReportLost.getHeight());
	    	    	        int iconSize = (int) (buttonSize * 0.7); // Adjust 0.7 to make icon bigger/smaller
	    	    	        
	    	    	        if (iconSize > 0) {
	    	    	            Image scaledImage = originalImage.getScaledInstance(
	    	    	                iconSize, iconSize, Image.SCALE_SMOOTH);
	    	    	            btnReportLost.setIcon(new ImageIcon(scaledImage));
	    	    	        }
	    	    	    }
	    	    	});

	    	    	// Set initial icon size
	    	    	int initialSize = (int) (Math.min(147, 178) * 0.7); // Based on initial bounds
	    	    	Image scaledImage = originalImage.getScaledInstance(initialSize, initialSize, Image.SCALE_SMOOTH);
	    	    	btnReportLost.setIcon(new ImageIcon(scaledImage));

	    	    	btnReportLost.setBounds(30, 99, 147, 178);
	    	    	LostItemPan.add(btnReportLost);
	        	 
	    	    
	    	    JButton btnFoundLost = RoundedComponents.createRoundedButton(
        			    "",
        			    new Color(90, 14, 36),       // background
        			    Color.WHITE,                 // text
        			    Color.BLACK,                 // border
        			    30,                          // radius
        			    RoundedComponents.ALL_CORNERS
        			);
        			btnFoundLost.setFont(new Font("Tahoma", Font.BOLD, 28));

        			// Store the original image
        			ImageIcon originalIcon1 = new ImageIcon(getClass().getResource("/image/Lost.png"));
        			Image originalImage1 = originalIcon1.getImage();

        			// Add a component listener to handle resizing
        			btnFoundLost.addComponentListener(new java.awt.event.ComponentAdapter() {
        			    @Override
        			    public void componentResized(java.awt.event.ComponentEvent e) {
        			        int buttonSize = Math.min(btnFoundLost.getWidth(), btnFoundLost.getHeight());
        			        int iconSize = (int) (buttonSize * 0.7); // Adjust 0.7 to make icon bigger/smaller
        			        
        			        if (iconSize > 0) {
        			            Image scaledImage1 = originalImage1.getScaledInstance(
        			                iconSize, iconSize, Image.SCALE_SMOOTH);
        			            btnFoundLost.setIcon(new ImageIcon(scaledImage1));
        			        }
        			    }
        			});

        			// Set initial icon size
        			int initialSize1 = (int) (Math.min(147, 178) * 0.7); // Based on initial bounds
        			Image scaledImage1 = originalImage1.getScaledInstance(initialSize1, initialSize1, Image.SCALE_SMOOTH);
        			btnFoundLost.setIcon(new ImageIcon(scaledImage1));

        			btnFoundLost.setBounds(213, 99, 147, 178);
        			LostItemPan.add(btnFoundLost);
	    	    
	    	    JPanel LogoutPan = new JPanel();
	    	    LogoutPan.setBackground(new Color(255, 255, 255));
	    	    Dashboardtab.addTab("New tab", null, LogoutPan, null);
	    	    LogoutPan.setLayout(null);
	    	    
	    	    JLabel lblNewLabel_3 = new JLabel("<html><div style='text-align:center;'>Are you sure you want<br>to <span style='color:#800000;'> logout?</span></div></html>\"");
	    	    lblNewLabel_3.setHorizontalAlignment(SwingConstants.CENTER);
	    	    lblNewLabel_3.setFont(new Font("Tahoma", Font.BOLD, 20));
	    	    lblNewLabel_3.setBackground(new Color(255, 255, 255));
	    	    lblNewLabel_3.setBounds(73, 79, 239, 55);
	    	    LogoutPan.add(lblNewLabel_3);
	    	    
	    	    JLabel lblNewLabel_4 = new JLabel("You’ll need to sign in again to access your dashboard.");
	    	    lblNewLabel_4.setFont(new Font("Tahoma", Font.ITALIC, 11));
	    	    lblNewLabel_4.setBounds(64, 134, 257, 14);
	    	    LogoutPan.add(lblNewLabel_4);
	    	    
	    	    JButton btnLogoutCancel = RoundedComponents.createRoundedButton(
	            	    "Cancel",
	                    new Color(90, 14, 36),       // background
	                    Color.RED,    // text
	            	    Color.BLACK,            // border
	            	     15,                         // radius
	            	     RoundedComponents.ALL_CORNERS
	            	     );
	    	    btnLogoutCancel.addActionListener(new ActionListener() {
	    	    	public void actionPerformed(ActionEvent e) {
	    	    	}
	    	    });
	    	    btnLogoutCancel.setFont(new Font("Tahoma", Font.BOLD, 16));
	    	    btnLogoutCancel.setBounds(64, 189, 115, 30);
	    	    LogoutPan.add(btnLogoutCancel);
	    	    
	    	    JButton btnLogoutConfirm = RoundedComponents.createRoundedButton(
	    	    	    "Confirm",
	    	    	    new Color(90, 14, 36),      
	    	    	    new Color(255, 255, 255),   
	    	    	    Color.BLACK,            
	    	    	    15,                        
	    	    	    RoundedComponents.ALL_CORNERS
	    	    	);
	    	    	btnLogoutConfirm.addActionListener(new ActionListener() {
	    	    	    public void actionPerformed(ActionEvent e) {
	    	    	        // ✅ PROPER LOGOUT - Stops session timer and returns to login
	    	    	        sessionManager.stopSession();  // Stop the timer
	    	    	        System.out.println("🔒 User logged out: " + currentUsername);
	    	    	        
	    	    	        DashboardWindow.this.dispose();	    	           
	    	    	        LoginWindow loginWindow = new LoginWindow();
	    	    	        loginWindow.setVisible(true);
	    	    	    }
	    	    	});
	    	    	btnLogoutConfirm.setFont(new Font("Tahoma", Font.BOLD, 16));
	    	    	btnLogoutConfirm.setBackground(new Color(192, 192, 192));
	    	    	btnLogoutConfirm.setBounds(206, 189, 115, 30);
	    	    	LogoutPan.add(btnLogoutConfirm);
	    	    
	    	    
	    	    	    ProgressPan = new JPanel();
	    	    	    ProgressPan.setBackground(new Color(255, 255, 255));
	    	    	    Dashboardtab.addTab("Progress", ProgressPan);
	    	    	    ProgressPan.setLayout(null);
	    	    	    
	    	    	    JLabel lblNewLabel_1 = new JLabel("Check Your Progress");
	    	    	    lblNewLabel_1.setFont(new Font("Tahoma", Font.BOLD, 20));
	    	    	    lblNewLabel_1.setHorizontalAlignment(SwingConstants.CENTER);
	    	    	    lblNewLabel_1.setBounds(86, 32, 214, 51);
	    	    	    ProgressPan.add(lblNewLabel_1);
	    	    	    
	    	    	    JButton btnLostItemProg = RoundedComponents.createRoundedButton(
			            	    "<html><b>Lost Item<br>Report <br> Progress</b></html>",
			                    new Color(90, 14, 36),       
			                    new Color(255, 255, 255),  
			            	    Color.BLACK,           
			            	    30,                        
			            	    RoundedComponents.ALL_CORNERS
			            	     );
	    	    	    btnLostItemProg.addActionListener(new ActionListener() {
	    	    	    	public void actionPerformed(ActionEvent e) {
	    	    	    	}
	    	    	    });	    	    	   
	    	    	    btnLostItemProg.setFont(new Font("Tahoma", Font.PLAIN, 19));	    	    	   
	    	    	    btnLostItemProg.setBounds(36, 99, 147, 178);
	    	    	    ProgressPan.add(btnLostItemProg);
	    	    	    
	    	    	    JButton btnFoundItemProfg = RoundedComponents.createRoundedButton(
			            	    "<html><b>Found Item<br>Report <br> Progress</b></html>",
			                    new Color(90, 14, 36),       
			                    new Color(255, 255, 255),    
			            	    Color.BLACK,            
			            	    30,                         
			            	     RoundedComponents.ALL_CORNERS
			            	     );
	    	    	  	    	    	    btnFoundItemProfg.setFont(new Font("Tahoma", Font.BOLD, 19));
	    	    	    btnFoundItemProfg.setBounds(213, 99, 147, 178);
	    	    	    ProgressPan.add(btnFoundItemProfg);
	    	    	    
	    	    	    JPanel ItemLostForm = new JPanel();
	    	    	    ItemLostForm.setBackground(new Color(255, 255, 255));
	    	    	    Dashboardtab.addTab("New tab", null, ItemLostForm, null);
	    	    	    ItemLostForm.setLayout(null);
	    	    	    
	    	    	    JLabel lblItemLostForm = new JLabel("Item Lost");
	    	    	    lblItemLostForm.setBounds(126, 0, 134, 29);
	    	    	    lblItemLostForm.setFont(new Font("Tahoma", Font.BOLD, 22));
	    	    	    lblItemLostForm.setHorizontalAlignment(SwingConstants.CENTER);
	    	    	    ItemLostForm.add(lblItemLostForm);
	    	    	    
	    	    	    JLabel lblNewLabel_2 = new JLabel("Item Name");
	    	    	    lblNewLabel_2.setBounds(30, 32, 84, 17);
	    	    	    lblNewLabel_2.setFont(new Font("Tahoma", Font.BOLD, 14));
	    	    	    ItemLostForm.add(lblNewLabel_2);
	    	    	    
	    	    	    textField = RoundedComponents.createRoundedTextField(
      			        	     Color.WHITE,               // background
      			        	     Color.BLACK,               // text
      			        	     Color.BLACK,                // border
      			        	     8
      			        	     );// radius      			        	     
      		    	    	    textField.setBounds(30, 49, 332, 25);
      		    	    	    
      		    	    	    ItemLostForm.add(textField);
      		    	    	    textField.setColumns(10);
	    	    	    
	    	    	    JLabel lblNewLabel_2_1 = new JLabel("Location Lost");
	    	    	    lblNewLabel_2_1.setBounds(30, 77, 93, 17);
	    	    	    lblNewLabel_2_1.setFont(new Font("Tahoma", Font.BOLD, 14));
	    	    	    ItemLostForm.add(lblNewLabel_2_1);
	    	    	    
	    	    	    textField_1 = RoundedComponents.createRoundedTextField(
     			        	     Color.WHITE,               // background
     			        	     Color.BLACK,               // text
     			        	     Color.BLACK,                // border
     			        	     8
     			        	     );// radius    
	    	    	    textField_1.setBounds(30, 94, 332, 25);
	    	    	    textField_1.setColumns(10);
	    	    	    ItemLostForm.add(textField_1);
	    	    	    
	    	    	    JLabel lblNewLabel_2_2 = new JLabel("Date Lost");
	    	    	    lblNewLabel_2_2.setBounds(30, 122, 66, 17);
	    	    	    lblNewLabel_2_2.setFont(new Font("Tahoma", Font.BOLD, 14));
	    	    	    ItemLostForm.add(lblNewLabel_2_2);
	    	    	    
	    	    	    textField_2 = RoundedComponents.createRoundedTextField(
    			        	     Color.WHITE,               // background
    			        	     Color.BLACK,               // text
    			        	     Color.BLACK,                // border
    			        	     8
    			        	     );// radius
	    	    	    textField_2.setBounds(30, 139, 332, 25);
	    	    	    textField_2.setColumns(10);
	    	    	    ItemLostForm.add(textField_2);
	    	    	    
	    	    	    JLabel lblNewLabel_2_3 = new JLabel("Description");
	    	    	    lblNewLabel_2_3.setBounds(30, 167, 77, 17);
	    	    	    lblNewLabel_2_3.setFont(new Font("Tahoma", Font.BOLD, 14));
	    	    	    ItemLostForm.add(lblNewLabel_2_3);
	    	    	    
	    	    	    textField_3 = RoundedComponents.createRoundedTextField(
    			        	     Color.WHITE,               // background
    			        	     Color.BLACK,               // text
    			        	     Color.BLACK,                // border
    			        	     8
    			        	     );// radius
	    	    	    textField_3.setBounds(30, 184, 332, 30);
	    	    	    textField_3.setColumns(10);
	    	    	    ItemLostForm.add(textField_3);
	    	    	    
	    	    	    JButton btnNewButton_1 = RoundedComponents.createRoundedButton(
 			            	    "Submit",
 			            	    new Color(90, 14, 36),       
 			                    new Color(255, 255, 255),    
 			                    new Color(0, 0, 0),
 			            	    20,                         
 			            	     RoundedComponents.ALL_CORNERS
 			            	     );
	    	    	    btnNewButton_1.setBounds(134, 287, 118, 29);
	    	    	    btnNewButton_1.addActionListener(new ActionListener() {
	    	    	        public void actionPerformed(ActionEvent e) {
	    	    	            // Get form values
	    	    	            String itemName = textField.getText().trim();
	    	    	            String locationLost = textField_1.getText().trim();
	    	    	            String dateLost = textField_2.getText().trim();
	    	    	            String description = textField_3.getText().trim();

	    	    	            // Validate required fields
	    	    	            if (itemName.isEmpty() || locationLost.isEmpty() || dateLost.isEmpty() || description.isEmpty()) {
	    	    	                JOptionPane.showMessageDialog(null,
	    	    	                    "Please fill in all required fields.",
	    	    	                    "Validation Error",
	    	    	                    JOptionPane.ERROR_MESSAGE);
	    	    	                return;
	    	    	            }

	    	    	            // Save image if selected
	    	    	            String imagePath = null;
	    	    	            if (selectedImageFile != null) {
	    	    	                try {
	    	    	                    File imagesDir = new File("images/items");
	    	    	                    if (!imagesDir.exists()) {
	    	    	                        imagesDir.mkdirs();
	    	    	                    }

	    	    	                    // Read the original image
	    	    	                    BufferedImage originalImage = ImageIO.read(selectedImageFile);
	    	    	                    
	    	    	                    // Calculate scaled dimensions (maintain aspect ratio)
	    	    	                    int maxSize = 70;
	    	    	                    int originalWidth = originalImage.getWidth();
	    	    	                    int originalHeight = originalImage.getHeight();
	    	    	                    
	    	    	                    int newWidth, newHeight;
	    	    	                    if (originalWidth > originalHeight) {
	    	    	                        newWidth = maxSize;
	    	    	                        newHeight = (int) ((double) originalHeight / originalWidth * maxSize);
	    	    	                    } else {
	    	    	                        newHeight = maxSize;
	    	    	                        newWidth = (int) ((double) originalWidth / originalHeight * maxSize);
	    	    	                    }
	    	    	                    
	    	    	                    // Resize the image with high quality
	    	    	                    BufferedImage resizedImage = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_ARGB);
	    	    	                    Graphics2D g2d = resizedImage.createGraphics();
	    	    	                    g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
	    	    	                    g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
	    	    	                    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
	    	    	                    g2d.drawImage(originalImage, 0, 0, newWidth, newHeight, null);
	    	    	                    g2d.dispose();

	    	    	                    // Save the resized image
	    	    	                    String extension = selectedImageFile.getName().substring(
	    	    	                        selectedImageFile.getName().lastIndexOf('.') + 1).toLowerCase();
	    	    	                    String newFileName = System.currentTimeMillis() + "_" +
	    	    	                        itemName.replaceAll("[^a-zA-Z0-9]", "_") + "." + extension;
	    	    	                    File destFile = new File(imagesDir, newFileName);

	    	    	                    // Use PNG for transparency support, otherwise use the original format
	    	    	                    String outputFormat = extension.equals("png") ? "png" : "jpg";
	    	    	                    ImageIO.write(resizedImage, outputFormat, destFile);

	    	    	                    imagePath = destFile.getPath();
	    	    	                    System.out.println("Image saved to: " + imagePath);

	    	    	                } catch (Exception ex) {
	    	    	                    JOptionPane.showMessageDialog(null,
	    	    	                        "Error saving image: " + ex.getMessage(),
	    	    	                        "File Error",
	    	    	                        JOptionPane.ERROR_MESSAGE);
	    	    	                    return;
	    	    	                }
	    	    	                
	    	    	            }
	    	    	            // Save to database
	    	    	            try (Connection conn = MySQLConnection.connect()) {
	    	    	                String sql = "INSERT INTO lost_items (item_name, location_lost, date_lost, description, image_path, reported_by) " +
	    	    	                             "VALUES (?, ?, ?, ?, ?, ?)";
	    	    	                
	    	    	                java.sql.PreparedStatement pstmt = conn.prepareStatement(sql);
	    	    	                pstmt.setString(1, itemName);
	    	    	                pstmt.setString(2, locationLost);
	    	    	                pstmt.setString(3, dateLost);
	    	    	                pstmt.setString(4, description);
	    	    	                pstmt.setString(5, imagePath); // Can be null if no image
	    	    	                pstmt.setString(6, currentUsername); // The logged-in user
	    	    	                
	    	    	                pstmt.executeUpdate();
	    	    	                
	    	    	                System.out.println("Lost item report saved!");
	    	    	                
	    	    	                NotificationManager.createNotification(
	    	    	                	    "admin",
	    	    	                	    "New Lost Item Report",
	    	    	                	    currentUsername + " reported a lost item: " + itemName,
	    	    	                	    "status_change",
	    	    	                	    0
	    	    	                	);
	    	    	                
	    	    	                JOptionPane.showMessageDialog(null,
	    	    	                    "Lost item report submitted successfully!",
	    	    	                    "Success",
	    	    	                    JOptionPane.INFORMATION_MESSAGE);
	    	    	                	    	    	 
	    	    	                // Clear form
	    	    	                textField.setText("");
	    	    	                textField_1.setText("");
	    	    	                textField_2.setText("");
	    	    	                textField_3.setText("");
	    	    	                lblImagePreview_1.setIcon(null);
	    	    	                lblImagePreview_1.setText("No Image");
	    	    	                btnImageIns_1.setText("Choose Image");
	    	    	                btnImageIns_1.setForeground(Color.BLACK);
	    	    	                selectedImageFile = null;
	    	    	                
	    	    	                int count = countUserLostItems(currentUsername);
	    	    	                lblLostItemPending.setText(String.valueOf(count));

	    	    	                // Refresh pending tickets list
	    	    	                loadPendingLostItems(pendingLostList, currentUsername);


	    	    	            } catch (Exception ex) {
	    	    	                ex.printStackTrace();
	    	    	                JOptionPane.showMessageDialog(null,
	    	    	                    "Database Error: " + ex.getMessage(),
	    	    	                    "Error",
	    	    	                    JOptionPane.ERROR_MESSAGE);
	    	    	            }
	    	    	        }
	    	    	    });	    	    	   
	    	    	    btnNewButton_1.setFont(new Font("Tahoma", Font.BOLD, 18));
	    	    	    ItemLostForm.add(btnNewButton_1);
	    	    	    
	    	    	    lblImagePreview_1 = new JLabel("No Image");
	    	    	    lblImagePreview_1.setHorizontalAlignment(SwingConstants.CENTER);
	    	    	    lblImagePreview_1.setBorder(BorderFactory.createLineBorder(new Color(255, 193, 193), 2));
	    	    	    lblImagePreview_1.setBounds(190, 215, 70, 70); // Made taller for image
	    	    	    ItemLostForm.add(lblImagePreview_1);

	    	    	    // Then create button
	    	    	    btnImageIns_1 = RoundedComponents.createRoundedButton(
			            	    "Choose Image",
			                    new Color(90, 14, 36),       
			                    new Color(255, 255, 255),    
			                    new Color(0, 0, 0),    
			            	    20,                         
			            	    RoundedComponents.ALL_CORNERS
			            	     );
	    	    	    btnImageIns_1.setFont(new Font("Tahoma", Font.BOLD, 13));
	    	    	    btnImageIns_1.addActionListener(new ActionListener() {
	    	    	        public void actionPerformed(ActionEvent e) {
	    	    	            JFileChooser fileChooser = new JFileChooser();
	    	    	            fileChooser.setDialogTitle("Select Item Image");

	    	    	            javax.swing.filechooser.FileNameExtensionFilter filter =
	    	    	                new javax.swing.filechooser.FileNameExtensionFilter(
	    	    	                    "Image Files", "jpg", "jpeg", "png", "gif", "bmp", "jfif");
	    	    	            fileChooser.setFileFilter(filter);

	    	    	            int result = fileChooser.showOpenDialog(null);

	    	    	            if (result == JFileChooser.APPROVE_OPTION) {
	    	    	                File tempFile = fileChooser.getSelectedFile();
	    	    	                
	    	    	                // Validate file type FIRST
	    	    	                String fileName = tempFile.getName().toLowerCase();
	    	    	                String[] supportedTypes = {".jpg", ".jpeg", ".png", ".gif", ".bmp"};
	    	    	                boolean isSupported = false;
	    	    	                
	    	    	                for (String type : supportedTypes) {
	    	    	                    if (fileName.endsWith(type)) {
	    	    	                        isSupported = true;
	    	    	                        break;
	    	    	                    }
	    	    	                }
	    	    	                
	    	    	                if (!isSupported) {
	    	    	                    JOptionPane.showMessageDialog(null,
	    	    	                        "Unsupported file type!\n\nSupported formats:\n" +
	    	    	                        "JPG, JPEG, PNG, GIF, BMP\n\n" +
	    	    	                        "Your file: " + fileName,
	    	    	                        "Invalid Image Format",
	    	    	                        JOptionPane.ERROR_MESSAGE);
	    	    	                    // DON'T set selectedImageFile - just return
	    	    	                    return;
	    	    	                }
	    	    	                
	    	    	                // Only set the file if it's supported
	    	    	                selectedImageFile = tempFile;

	    	    	                try {
	    	    	                    ImageIcon imageIcon = new ImageIcon(selectedImageFile.getAbsolutePath());
	    	    	                    Image image = imageIcon.getImage();
	    	    	                    Image scaledImage = image.getScaledInstance(70, 70, Image.SCALE_SMOOTH);
	    	    	                    lblImagePreview_1.setIcon(new ImageIcon(scaledImage));
	    	    	                    lblImagePreview_1.setText("");
	    	    	                    
	    	    	                    btnImageIns_1.setText("✓ Selected");
	    	    	                    btnImageIns_1.setForeground(new Color(0, 128, 0));
	    	    	                    
	    	    	                } catch (Exception ex) {
	    	    	                    lblImagePreview_1.setText("Error");
	    	    	                    selectedImageFile = null; // Reset on error
	    	    	                    JOptionPane.showMessageDialog(null,
	    	    	                        "Error loading image preview",
	    	    	                        "Image Error",
	    	    	                        JOptionPane.ERROR_MESSAGE);
	    	    	                }
	    	    	            }
	    	    	        }
	    	    	    });
	    	    	    btnImageIns_1.setBounds(30, 235, 150, 29);
	    	    	    ItemLostForm.add(btnImageIns_1);
	    	    	
	    	    	    
	    	    	    JPanel FoundItemForm = new JPanel();
	    	    	    FoundItemForm.setBackground(new Color(255, 255, 255));
	    	    	    Dashboardtab.addTab("New tab", null, FoundItemForm, null);
	    	    	    FoundItemForm.setLayout(null);
	    	    	    
	    	    	    JLabel lblFoundItem = new JLabel("Found Item");
	    	    	    lblFoundItem.setBounds(126, 2, 134, 29);
	    	    	    lblFoundItem.setHorizontalAlignment(SwingConstants.CENTER);
	    	    	    lblFoundItem.setFont(new Font("Tahoma", Font.BOLD, 22));
	    	    	    FoundItemForm.add(lblFoundItem);
	    	    	    
	    	    	    JLabel lblNewLabel_2_4 = new JLabel("Item Name");
	    	    	    lblNewLabel_2_4.setBounds(30, 32, 84, 17);
	    	    	    lblNewLabel_2_4.setFont(new Font("Tahoma", Font.BOLD, 14));
	    	    	    FoundItemForm.add(lblNewLabel_2_4);
	    	    	    
	    	    	    textField_4 = RoundedComponents.createRoundedTextField(
      			        	     Color.WHITE,               // background
      			        	     Color.BLACK,               // text
      			        	     Color.BLACK,                // border
      			        	     7
      			        	     );// radius
	    	    	    textField_4.setBounds(30, 49, 332, 25);
	    	    	    textField_4.setColumns(10);
	    	    	    FoundItemForm.add(textField_4);
	    	    	    
	    	    	    JLabel lblNewLabel_2_1_1 = new JLabel("Location Lost");
	    	    	    lblNewLabel_2_1_1.setBounds(30, 77, 93, 17);
	    	    	    lblNewLabel_2_1_1.setFont(new Font("Tahoma", Font.BOLD, 14));
	    	    	    FoundItemForm.add(lblNewLabel_2_1_1);
	    	    	    
	    	    	    textField_5 = RoundedComponents.createRoundedTextField(
     			        	     Color.WHITE,               // background
     			        	     Color.BLACK,               // text
     			        	     Color.BLACK,                // border
     			        	     7
     			        	     );// radius
	    	    	    textField_5.setBounds(30, 94, 332, 25);
	    	    	    textField_5.setColumns(10);
	    	    	    FoundItemForm.add(textField_5);
	    	    	    
	    	    	    JLabel lblNewLabel_2_2_1 = new JLabel("Date Lost");
	    	    	    lblNewLabel_2_2_1.setBounds(30, 122, 66, 17);
	    	    	    lblNewLabel_2_2_1.setFont(new Font("Tahoma", Font.BOLD, 14));
	    	    	    FoundItemForm.add(lblNewLabel_2_2_1);
	    	    	    
	    	    	    textField_6 = RoundedComponents.createRoundedTextField(
     			        	     Color.WHITE,               // background
     			        	     Color.BLACK,               // text
     			        	     Color.BLACK,                // border
     			        	     7
     			        	     );// radius
	    	    	    textField_6.setBounds(30, 139, 332, 25);
	    	    	    textField_6.setColumns(10);
	    	    	    FoundItemForm.add(textField_6);
	    	    	    
	    	    	    JLabel lblNewLabel_2_3_1 = new JLabel("Description");
	    	    	    lblNewLabel_2_3_1.setBounds(30, 167, 77, 17);
	    	    	    lblNewLabel_2_3_1.setFont(new Font("Tahoma", Font.BOLD, 14));
	    	    	    FoundItemForm.add(lblNewLabel_2_3_1);
	    	    	    
	    	    	    textField_7 = RoundedComponents.createRoundedTextField(
     			        	     Color.WHITE,               // background
     			        	     Color.BLACK,               // text
     			        	     Color.BLACK,                // border
     			        	     7
     			        	     );// radius
	    	    	    textField_7.setBounds(30, 184, 332, 29);
	    	    	    textField_7.setColumns(10);
	    	    	    FoundItemForm.add(textField_7);
	    	    	    
	    	    	    
	   
	    	    	    
	    	    	    JButton btnNewButton_1_1 = RoundedComponents.createRoundedButton(
 			            	    "Submit",
 			            	    new Color(90, 14, 36),       
 			                    new Color(255, 255, 255),    
 			                    new Color(0, 0, 0),
 			            	    20,                         
			            	     RoundedComponents.ALL_CORNERS
 			            	     );
	    	    	    btnNewButton_1_1.setBounds(134, 287, 118, 29);
	    	    	    btnNewButton_1_1.addActionListener(new ActionListener() {
	    	    	        public void actionPerformed(ActionEvent e) {
	    	    	            // Get form values
	    	    	            String itemName = textField_4.getText().trim();
	    	    	            String locationFound = textField_5.getText().trim();
	    	    	            String dateFound = textField_6.getText().trim();
	    	    	            String description = textField_7.getText().trim();

	    	    	            // Validate required fields
	    	    	            if (itemName.isEmpty() || locationFound.isEmpty() || dateFound.isEmpty() || description.isEmpty()) {
	    	    	                JOptionPane.showMessageDialog(null,
	    	    	                    "Please fill in all required fields.",
	    	    	                    "Validation Error",
	    	    	                    JOptionPane.ERROR_MESSAGE);
	    	    	                return;
	    	    	            }

	    	    	            // Save image if selected
	    	    	            String imagePath = null;
	    	    	            if (selectedImageFile != null) {
	    	    	                try {
	    	    	                    File imagesDir = new File("images/items");
	    	    	                    if (!imagesDir.exists()) {
	    	    	                        imagesDir.mkdirs();
	    	    	                    }

	    	    	                    // Read the original image
	    	    	                    BufferedImage originalImage = ImageIO.read(selectedImageFile);
	    	    	                    
	    	    	                    // Calculate scaled dimensions (maintain aspect ratio)
	    	    	                    int maxSize = 70;
	    	    	                    int originalWidth = originalImage.getWidth();
	    	    	                    int originalHeight = originalImage.getHeight();
	    	    	                    
	    	    	                    int newWidth, newHeight;
	    	    	                    if (originalWidth > originalHeight) {
	    	    	                        newWidth = maxSize;
	    	    	                        newHeight = (int) ((double) originalHeight / originalWidth * maxSize);
	    	    	                    } else {
	    	    	                        newHeight = maxSize;
	    	    	                        newWidth = (int) ((double) originalWidth / originalHeight * maxSize);
	    	    	                    }
	    	    	                    
	    	    	                    // Resize the image with high quality
	    	    	                    BufferedImage resizedImage = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_ARGB);
	    	    	                    Graphics2D g2d = resizedImage.createGraphics();
	    	    	                    g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
	    	    	                    g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
	    	    	                    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
	    	    	                    g2d.drawImage(originalImage, 0, 0, newWidth, newHeight, null);
	    	    	                    g2d.dispose();

	    	    	                    // Save the resized image
	    	    	                    String extension = selectedImageFile.getName().substring(
	    	    	                        selectedImageFile.getName().lastIndexOf('.') + 1).toLowerCase();
	    	    	                    String newFileName = System.currentTimeMillis() + "_" +
	    	    	                        itemName.replaceAll("[^a-zA-Z0-9]", "_") + "." + extension;
	    	    	                    File destFile = new File(imagesDir, newFileName);

	    	    	                    // Use PNG for transparency support, otherwise use the original format
	    	    	                    String outputFormat = extension.equals("png") ? "png" : "jpg";
	    	    	                    ImageIO.write(resizedImage, outputFormat, destFile);

	    	    	                    imagePath = destFile.getPath();
	    	    	                    System.out.println("Image saved to: " + imagePath);

	    	    	                } catch (Exception ex) {
	    	    	                    JOptionPane.showMessageDialog(null,
	    	    	                        "Error saving image: " + ex.getMessage(),
	    	    	                        "File Error",
	    	    	                        JOptionPane.ERROR_MESSAGE);
	    	    	                    return;
	    	    	                }
	    	    	            }

	    	    	            // Save to database
	    	    	            try (Connection conn = MySQLConnection.connect()) {
	    	    	                String sql = "INSERT INTO found_items (item_name, location_found, date_found, description, image_path, reported_by) " +
	    	    	                             "VALUES (?, ?, ?, ?, ?, ?)";
	    	    	                
	    	    	                PreparedStatement pstmt = conn.prepareStatement(sql);
	    	    	                pstmt.setString(1, itemName);
	    	    	                pstmt.setString(2, locationFound);
	    	    	                pstmt.setString(3, dateFound);
	    	    	                pstmt.setString(4, description);
	    	    	                pstmt.setString(5, imagePath); // Can be null if no image
	    	    	                pstmt.setString(6, currentUsername); // The logged-in user
	    	    	                
	    	    	                pstmt.executeUpdate();
	    	    	                
	    	    	                System.out.println("Found item report saved!");
	    	    	                
	    	    	                NotificationManager.createNotification(
	    	    	                	    "admin",
	    	    	                	    "New Found Item Report",
	    	    	                	    currentUsername + " reported a found item: " + itemName,
	    	    	                	    "status_change",
	    	    	                	    0
	    	    	                	);
	    	    	                
	    	    	                JOptionPane.showMessageDialog(null,
	    	    	                    "Found item report submitted successfully!",
	    	    	                    "Success",
	    	    	                    JOptionPane.INFORMATION_MESSAGE);

	    	    	                // Clear form
	    	    	                textField_4.setText("");
	    	    	                textField_5.setText("");
	    	    	                textField_6.setText("");
	    	    	                textField_7.setText("");
	    	    	                lblImagePreview.setIcon(null);
	    	    	                lblImagePreview.setText("No Image");
	    	    	                selectedImageFile = null;

	    	    	                // Refresh count
	    	    	                int count = countUserFoundItems(currentUsername);
	    	    	                lblFoundItemsUnclaimed.setText(String.valueOf(count));
	    	    	                
	    	    	                // Refresh pending tickets list
	    	    	                loadPendingFoundItems(pendingFoundListPanel, currentUsername);

	    	    	            } catch (Exception ex) {
	    	    	                ex.printStackTrace();
	    	    	                JOptionPane.showMessageDialog(null,
	    	    	                    "Database Error: " + ex.getMessage(),
	    	    	                    "Error",
	    	    	                    JOptionPane.ERROR_MESSAGE);
	    	    	            }
	    	    	        }
	    	    	    });	    	    	   
	    	    	    btnNewButton_1_1.setFont(new Font("Tahoma", Font.BOLD, 18));
	    	    	    FoundItemForm.add(btnNewButton_1_1);
	    	    	    
	    	    	 // Create label FIRST
	    	    	    lblImagePreview = new JLabel("No Image");
	    	    	    lblImagePreview.setHorizontalAlignment(SwingConstants.CENTER);
	    	    	    lblImagePreview.setBorder(BorderFactory.createLineBorder(new Color(183, 204, 255), 2));
	    	    	    lblImagePreview.setBounds(190, 215, 70, 70); 
	    	    	    FoundItemForm.add(lblImagePreview);

	    	    	    // Then create button
	    	    	    JButton btnImageIns = RoundedComponents.createRoundedButton(
 			            	    "Choose Image",
 			            	    new Color(90, 14, 36),       
 			                    new Color(255, 255, 255),    
 			                    new Color(0, 0, 0),
 			            	    20,                         
 			            	     RoundedComponents.ALL_CORNERS
 			            	     );
	    	    	    btnImageIns.setFont(new Font("Tahoma", Font.BOLD, 14));
	    	    	    btnImageIns.addActionListener(new ActionListener() {
	    	    	        public void actionPerformed(ActionEvent e) {
	    	    	            JFileChooser fileChooser = new JFileChooser();
	    	    	            fileChooser.setDialogTitle("Select Item Image");

	    	    	            javax.swing.filechooser.FileNameExtensionFilter filter =
	    	    	                new javax.swing.filechooser.FileNameExtensionFilter(
	    	    	                    "Image Files", "jpg", "jpeg", "png", "gif", "bmp", "jfif");
	    	    	            fileChooser.setFileFilter(filter);

	    	    	            int result = fileChooser.showOpenDialog(null);

	    	    	            if (result == JFileChooser.APPROVE_OPTION) {
	    	    	                selectedImageFile = fileChooser.getSelectedFile();
	    	    	                
	    	    	                // Validate file type
	    	    	                String fileName = selectedImageFile.getName().toLowerCase();
	    	    	                String[] supportedTypes = {".jpg", ".jpeg", ".png", ".gif", ".bmp"};
	    	    	                boolean isSupported = false;
	    	    	                
	    	    	                for (String type : supportedTypes) {
	    	    	                    if (fileName.endsWith(type)) {
	    	    	                        isSupported = true;
	    	    	                        break;
	    	    	                    }
	    	    	                }
	    	    	                
	    	    	                if (!isSupported) {
	    	    	                    JOptionPane.showMessageDialog(null,
	    	    	                        "Unsupported file type!\n\nSupported formats:\n" +
	    	    	                        "JPG, JPEG, PNG, GIF, BMP\n\n" +
	    	    	                        "Your file: " + fileName,
	    	    	                        "Invalid Image Format",
	    	    	                        JOptionPane.ERROR_MESSAGE);
	    	    	                    selectedImageFile = null;
	    	    	                    return;
	    	    	                }

	    	    	                try {
	    	    	                    ImageIcon imageIcon = new ImageIcon(selectedImageFile.getAbsolutePath());
	    	    	                    Image image = imageIcon.getImage();
	    	    	                    Image scaledImage = image.getScaledInstance(70, 70, Image.SCALE_SMOOTH);
	    	    	                    lblImagePreview.setIcon(new ImageIcon(scaledImage)); // or lblImagePreview for Found form
	    	    	                    lblImagePreview.setText("");
	    	    	                } catch (Exception ex) {
	    	    	                    lblImagePreview.setText("Error");
	    	    	                    JOptionPane.showMessageDialog(null,
	    	    	                        "Error loading image preview",
	    	    	                        "Image Error",
	    	    	                        JOptionPane.ERROR_MESSAGE);
	    	    	                    selectedImageFile = null;
	    	    	                }

	    	    	                btnImageIns.setText("✓ Selected"); // or btnImageIns for Found form
	    	    	                btnImageIns.setForeground(new Color(0, 128, 0));
	    	    	            }
	    	    	        }
	    	    	    });
	    	    	    btnImageIns.setBounds(30, 235, 150, 29);
	    	    	    FoundItemForm.add(btnImageIns);
	    	    	    
	    	    	    JPanel ItemLostStats = new JPanel();
	    	    	    ItemLostStats.setBackground(new Color(255, 255, 255));
	    	    	    Dashboardtab.addTab("New tab", null, ItemLostStats, null);
	    	    	    ItemLostStats.setLayout(null);
	    	    	    
	    	    	    JLabel lblWellcomeforLostStats = new JLabel("Welcome, " + currentUser);
	    	    	    lblWellcomeforLostStats.setBounds(10, 11, 179, 32);
	    	    	    lblWellcomeforLostStats.setForeground(new Color(90, 14, 36));
	    	    	    lblWellcomeforLostStats.setFont(new Font("Tahoma", Font.BOLD, 20));
	    	    	    ItemLostStats.add(lblWellcomeforLostStats);
	    	    	    
	    	    	    JPanel panel_1 = RoundedComponents.createRoundedPanel(
       		    	    		new Color(90, 14, 36),           // background
       		    	       	    Color.BLACK,                   // border
       		    	       	    1,
       		    	       	    15,  
       		    	            RoundedComponents.ALL_CORNERS
       		    	        );
	    	    	    panel_1.setBounds(10, 54, 187, 95);
	    	    	    ItemLostStats.add(panel_1);
	    	    	    panel_1.setLayout(null);
	    	    	    
	    	    	    JLabel lblMyLost = new JLabel("My Lost Items");
	    	    	    lblMyLost.setForeground(new Color(255, 255, 255));
	    	    	    lblMyLost.setBounds(10, 5, 111, 20);
	    	    	    lblMyLost.setFont(new Font("Tahoma", Font.BOLD, 15));
	    	    	    panel_1.add(lblMyLost);
	    	    	    
	    	    	    lblLostItemPending = new JLabel("New label");
	    	    	    lblLostItemPending.setFont(new Font("Tahoma", Font.BOLD, 20));
	    	    	    lblLostItemPending.setForeground(Color.WHITE);
	    	    	    lblLostItemPending.setBounds(10, 29, 46, 37);
	    	    	    panel_1.add(lblLostItemPending);
	    	    	    lblLostItemPending.setText(String.valueOf(lostItemsCount));  
	    	    	    
	    	    	    JPanel panel_2_1_1 = RoundedComponents.createRoundedPanel(
       		    	    		new Color(90, 14, 36),           // background
       		    	       	    Color.BLACK,                   // border
       		    	       	    1,
       		    	       	    15,  
       		    	            RoundedComponents.TOP_CORNERS
       		    	        );

	    	    	    panel_2_1_1.setLayout(null);
	    	    	    panel_2_1_1.setBounds(10, 159, 366, 28);
	    	    	    ItemLostStats.add(panel_2_1_1);
	    	    	    
	    	    	    JLabel lblPendings = new JLabel("Pendings");
	    	    	    lblPendings.setForeground(new Color(255, 255, 255));
	    	    	    lblPendings.setFont(new Font("Tahoma", Font.BOLD, 15));
	    	    	    lblPendings.setBounds(10, 64, 111, 20);
	    	    	    panel_1.add(lblPendings);
	    	    	    
	    	 // For Lost Items Stats
	    	    	    scrollPaneforLost = new JScrollPane(pendingLostList);
       		    		scrollPaneforLost.setBackground(new Color(240, 240, 240));
       		    		scrollPaneforLost.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
       		    		scrollPaneforLost.getViewport().setBackground(new Color(240, 240, 240));
       		    		scrollPaneforLost.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
       		    		scrollPaneforLost.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
       		    		scrollPaneforLost.getVerticalScrollBar().setUnitIncrement(16);  
       		    		scrollPaneforLost.getVerticalScrollBar().setBlockIncrement(50);
       		    		scrollPaneforLost.setBounds(10, 186, 366, 118);
       		    		ItemLostStats.add(scrollPaneforLost);

	    	    	    
	    	    	    	    	    pendingLostList = RoundedComponents.createRoundedPanel(
	    	    	    	    	    		new Color(240, 240, 240),           // background
                       		    	       	    Color.BLACK,                   // border
                       		    	       	    0,
                       		    	       	    15,  
                       		    	            RoundedComponents.ALL_CORNERS
                       		    	            );
	
	    	    	    	    	    pendingLostList.setLayout(new BoxLayout(pendingLostList, BoxLayout.Y_AXIS));
	    	    	    	    	    scrollPaneforLost.setViewportView(pendingLostList);  
	    	    	    	    	    
	    	    	    	    	    	    	    // Load pending tickets
	    	    	    	    	    	    	    loadPendingLostItems(pendingLostList, currentUsername);
	    	    	    	    	    	    	    
	    	    	    	    	    	    	    
	    	    	    	    	    	    	    
	    	    	    	    	    	    	    
	    	    	    	    	    	    	    
	    	    	    	    	    	    	    JLabel lblPendingTicketsLost = new JLabel("Pending Tickets");
	    	    	    	    	    	    	    lblPendingTicketsLost.setHorizontalAlignment(SwingConstants.CENTER);
	    	    	    	    	    	    	    lblPendingTicketsLost.setForeground(new Color(255, 255, 255));
	    	    	    	    	    	    	    lblPendingTicketsLost.setBounds(99, 0, 169, 28);
	    	    	    	    	    	    	    panel_2_1_1.add(lblPendingTicketsLost);
	    	    	    	    	    	    	    lblPendingTicketsLost.setFont(new Font("Tahoma", Font.BOLD, 16));
	    	    	    	    	    	    	    lblWellcomeforLostStats.setText("Welcome, " + currentUser);
	    	    	    	    	    	    	    
	    	    	    	    	    	    	    JButton btnNotification = RoundedComponents.createRoundedButton(
	                             			            	    "Notification",
	                             			            	    new Color(90, 14, 36),       
	                             			                    new Color(255, 255, 255),    
	                             			                    new Color(0, 0, 0),
	                             			            	    25,                         
	                             			            	     RoundedComponents.ALL_CORNERS
	                             			            	     );
	    	    	    	    	    	    	    btnNotification.setFont(new Font("Tahoma", Font.BOLD, 14));
	    	    	    	    	    	    	    btnNotification.addActionListener(new ActionListener() {
	    	    	    	    	    	    	        public void actionPerformed(ActionEvent e) {
	    	    	    	    	    	    	            // Open notification window
	    	    	    	    	    	    	            NotificationWindow notifWindow = new NotificationWindow(currentUsername);
	    	    	    	    	    	    	            notifWindow.setVisible(true);
	    	    	    	    	    	    	        }
	    	    	    	    	    	    	    });

	    	    	    	    	    	    	    // Add badge showing unread count
	    	    	    	    	    	    	    int unreadCount = NotificationWindow.getUnreadCount(currentUsername);
	    	    	    	    	    	    	    if (unreadCount > 0) {
	    	    	    	    	    	    	        btnNotification.setText("Notification (" + unreadCount + ")");
	    	    	    	    	    	    	        btnNotification.setForeground(new Color(220, 53, 69)); // Red color for unread
	    	    	    	    	    	    	    }

	    	    	    	    	    	    	    btnNotification.setBounds(229, 55, 135, 32);
	    	    	    	    	    	    	    ItemLostStats.add(btnNotification);
	    	    	    	    	    	    	    
	    	    	    	    	    	    	    JButton btnMessages = RoundedComponents.createRoundedButton(
	                             			            	    "Messages",
	                             			            	    new Color(90, 14, 36),       
	                             			                    new Color(0, 123, 255),    
	                             			                    new Color(0, 0, 0),
	                             			            	    25,                         
	                             			            	     RoundedComponents.ALL_CORNERS
	                             			            	     );
	    	    	    	    	    	    	    btnMessages.setFont(new Font("Tahoma", Font.BOLD, 16));
	    	    	    	    	    	    	    btnMessages.addActionListener(new ActionListener() {
	    	    	    	    	    	    	        public void actionPerformed(ActionEvent e) {
	    	    	    	    	    	    	            MessagesListWindow msgWindow = new MessagesListWindow(currentUsername, "lost");
	    	    	    	    	    	    	            msgWindow.setVisible(true);
	    	    	    	    	    	    	        }
	    	    	    	    	    	    	    });
	    	    	    	    	    	    	    btnMessages.setBounds(229, 98, 135, 32); // Position below notification button
	    	    	    	    	    	    	    ItemLostStats.add(btnMessages);
	    	    	    
	    	    	    JPanel ItemFoundStats = new JPanel();
	    	    	    Dashboardtab.addTab("New tab", null, ItemFoundStats, null);
	    	    	    ItemFoundStats.setLayout(null);
	    	    	    
	    	    	    JLabel lblWellcomeforFoundStats = new JLabel("Welcome, " + currentUser);
	    	    	    lblWellcomeforFoundStats.setForeground(new Color(90, 14, 36));
	    	    	    lblWellcomeforFoundStats.setFont(new Font("Tahoma", Font.BOLD, 20));
	    	    	    lblWellcomeforFoundStats.setBounds(10, 11, 179, 32);
	    	    	    ItemFoundStats.add(lblWellcomeforFoundStats);
	    	    	    
	    	    	    
	    	    	    
	    	    	    JPanel panel_1_2 = RoundedComponents.createRoundedPanel(
       		    	    		new Color(90, 14, 36),           // background
       		    	       	    Color.BLACK,                   // border
       		    	       	    1,
       		    	       	    15,  
       		    	            RoundedComponents.ALL_CORNERS
       		    	        );

	    	    	    panel_1_2.setLayout(null);
	    	    	    panel_1_2.setBounds(10, 54, 187, 95);
	    	    	    ItemFoundStats.add(panel_1_2);
	    	    	    
	    	    	    JLabel lblFoundItems = new JLabel("Found Items");
	    	    	    lblFoundItems.setForeground(Color.WHITE);
	    	    	    lblFoundItems.setFont(new Font("Tahoma", Font.BOLD, 15));
	    	    	    lblFoundItems.setBounds(10, 5, 111, 20);
	    	    	    panel_1_2.add(lblFoundItems);
	    	    	    
	    	    	    lblFoundItemsUnclaimed = new JLabel("New label");
	    	    	    lblFoundItemsUnclaimed.setFont(new Font("Tahoma", Font.BOLD, 20));
	    	    	    lblFoundItemsUnclaimed.setForeground(Color.WHITE);
	    	    	    lblFoundItemsUnclaimed.setBounds(10, 29, 46, 37);
	    	    	    panel_1_2.add(lblFoundItemsUnclaimed);
	    	    	    
	    	    	    int foundItemsCount = countUserFoundItems(currentUsername);
	    	    	    lblFoundItemsUnclaimed.setText(String.valueOf(foundItemsCount));
	    	    	    
	    	    	    JLabel lblUnclaimed = new JLabel("Unclaimed");
	    	    	    lblUnclaimed.setForeground(Color.WHITE);
	    	    	    lblUnclaimed.setFont(new Font("Tahoma", Font.BOLD, 15));
	    	    	    lblUnclaimed.setBounds(10, 64, 111, 20);
	    	    	    panel_1_2.add(lblUnclaimed);
	    	    	    
	    	    	    JPanel panel_2_1 = RoundedComponents.createRoundedPanel(
       		    	    		new Color(90, 14, 36),           // background
       		    	       	    Color.BLACK,                   // border
       		    	       	    1,
       		    	       	    15,  
       		    	            RoundedComponents.TOP_CORNERS
       		    	        );

	    	    	    panel_2_1.setLayout(null);
	    	    	    panel_2_1.setBounds(10, 159, 366, 28);
	    	    	    ItemFoundStats.add(panel_2_1);
	    	    	    
	    	    	    JLabel lblPendingTicketsFound = new JLabel("Pending Tickets");
	    	    	    lblPendingTicketsFound.setHorizontalAlignment(SwingConstants.CENTER);
	    	    	    lblPendingTicketsFound.setForeground(Color.WHITE);
	    	    	    lblPendingTicketsFound.setFont(new Font("Tahoma", Font.BOLD, 16));
	    	    	    lblPendingTicketsFound.setBounds(99, 0, 169, 28);
	    	    	    panel_2_1.add(lblPendingTicketsFound);
	    	    	    
	    	    	    scrollPaneforFound = new JScrollPane(pendingFoundListPanel);
	    	    	    scrollPaneforFound.setBackground(new Color(240, 240, 240));
	    	    	    scrollPaneforFound.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
	    	    	    scrollPaneforFound.getViewport().setBackground(new Color(240, 240, 240));
	    	    	    scrollPaneforFound.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
	    	    	    scrollPaneforFound.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
	    	    	    scrollPaneforFound.getVerticalScrollBar().setUnitIncrement(16);  
	    	    	    scrollPaneforFound.getVerticalScrollBar().setBlockIncrement(50);
	    	    	    scrollPaneforFound.setBounds(10, 186, 366, 118);
	    	    	    ItemFoundStats.add(scrollPaneforFound);

	    	    	    pendingFoundListPanel = RoundedComponents.createRoundedPanel(
	    	    	    		new Color(240, 240, 240),           // background
       		    	       	    Color.WHITE,                   // border
       		    	       	    1,
       		    	       	    15 
       		    	        );
	    	    	    pendingFoundListPanel.setLayout(new BoxLayout(pendingFoundListPanel, BoxLayout.Y_AXIS));
	    	    	    scrollPaneforFound.setViewportView(pendingFoundListPanel);  // ✅ CORRECT

	    	    	    // Load pending tickets
	    	    	    loadPendingFoundItems(pendingFoundListPanel, currentUsername);
	    	    	    
	    	    	    JButton btnNewButton_2 = RoundedComponents.createRoundedButton(
 			            	    "Notification",
 			            	    new Color(90, 14, 36),       
 			                    new Color(255, 255, 255),    
 			                    new Color(0, 0, 0),
 			            	    25,                         
 			            	     RoundedComponents.ALL_CORNERS
 			            	     );
	    	    	    btnNewButton_2.setFont(new Font("Tahoma", Font.BOLD, 14));
	    	    	    btnNewButton_2.addActionListener(new ActionListener() {
	    	    	        public void actionPerformed(ActionEvent e) {
	    	    	            // Open notification window
	    	    	            NotificationWindow notifWindow = new NotificationWindow(currentUsername);
	    	    	            notifWindow.setVisible(true);
	    	    	        }
	    	    	    });

	    	    	    // Add badge showing unread count
	    	    	    int unreadCountFound = NotificationWindow.getUnreadCount(currentUsername);
	    	    	    if (unreadCountFound > 0) {
	    	    	        btnNewButton_2.setText("Notification (" + unreadCountFound + ")");
	    	    	        btnNewButton_2.setForeground(new Color(220, 53, 69)); // Red color for unread
	    	    	    }

	    	    	    btnNewButton_2.setBounds(229, 54, 135, 32);
	    	    	    ItemFoundStats.add(btnNewButton_2);
	    	    	    
	    	    	    JButton btnMessages_1 = RoundedComponents.createRoundedButton(
 			            	    "Messages",
 			            	    new Color(90, 14, 36),       
 			                    new Color(0, 123, 255),    
 			                    new Color(0, 0, 0),
 			            	    25,                         
 			            	     RoundedComponents.ALL_CORNERS
 			            	     );
	    	    	    btnMessages_1.setFont(new Font("Tahoma", Font.BOLD, 16));
	    	    	    btnMessages_1.addActionListener(new ActionListener() {
	    	    	        public void actionPerformed(ActionEvent e) {
	    	    	            MessagesListWindow msgWindow = new MessagesListWindow(currentUsername, "found");
	    	    	            msgWindow.setVisible(true);
	    	    	        }
	    	    	    });
	    	    	    btnMessages_1.setBounds(229, 97, 135, 32);
	    	    	    ItemFoundStats.add(btnMessages_1);

	    	    	    
	    	    	    JPanel HomePanel = new JPanel() {
	    	    	        private Image backgroundImage;
	    	    	        
	    	    	        {
	    	    	            try {
	    	    	                ImageIcon icon = new ImageIcon(getClass().getResource("/image/Landscape Background.png"));
	    	    	                backgroundImage = icon.getImage();
	    	    	            } catch (Exception e) {
	    	    	                e.printStackTrace();
	    	    	            }
	    	    	        }
	    	    	        
	    	    	        @Override
	    	    	        protected void paintComponent(Graphics g) {
	    	    	            super.paintComponent(g);
	    	    	            if (backgroundImage != null) {
	    	    	                Graphics2D g2 = (Graphics2D) g.create();
	    	    	                g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, 
	    	    	                                   RenderingHints.VALUE_INTERPOLATION_BILINEAR);
	    	    	                g2.setRenderingHint(RenderingHints.KEY_RENDERING, 
	    	    	                                   RenderingHints.VALUE_RENDER_QUALITY);
	    	    	                
	    	    	                // Draw image scaled to panel size
	    	    	                g2.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
	    	    	                g2.dispose();
	    	    	            }
	    	    	        }
	    	    	    };

	    	    	    HomePanel.setBackground(new Color(255, 255, 255));
	    	    	    Dashboardtab.addTab("New tab", null, HomePanel, null);
	    	    	    HomePanel.setLayout(null);
	    	    	    
	    	    	    FirstPanellbl = new JLabel("Welcome, " + currentUser);
	    	    	    FirstPanellbl.setForeground(new Color(0, 0, 0));

	    	    	    FirstPanellbl.setHorizontalAlignment(SwingConstants.CENTER);
	    	    	    FirstPanellbl.setFont(new Font("Tahoma", Font.BOLD, 22));
	    	    	    FirstPanellbl.setBounds(53, 38, 280, 45);
	    	    	    HomePanel.add(FirstPanellbl);
	    	    	    
	    	    	   btnSetting =  RoundedComponents.createRoundedButton(
			            	    "Settings",
			                    new Color(90, 14, 36),       // background
			                    new Color(255, 255, 255	),    // text
			            	    Color.BLACK,            // border
			            	    30,                         // radius
			            	    RoundedComponents.ALL_CORNERS
			            	     );
	    	    	    btnSetting.setFont(new Font("Tahoma", Font.BOLD, 18));
	    	    	    btnSetting.addActionListener(new ActionListener() {
	    	    	        public void actionPerformed(ActionEvent e) {
	    	    	            // Hide home components
	    	    	            FirstPanellbl.setVisible(false);
	    	    	            btnSetting.setVisible(false);
	    	    	            btnGuide.setVisible(false);
	    	    	            
	    	    	            // Show settings
	    	    	            SettingsPanel.setVisible(true);
	    	    	        }
	    	    	    });
	    	    	    btnSetting.setBounds(60, 166, 117, 45);
	    	    	    HomePanel.add(btnSetting);
	    	    	    
	    	    	    btnGuide =  RoundedComponents.createRoundedButton(
			            	    "Guide",
			                    new Color(90, 14, 36),       // background
			                    new Color(135, 206, 235),    // text
			            	    Color.BLACK,            // border
			            	    30,                         // radius
			            	    RoundedComponents.ALL_CORNERS
			            	     );
	    	    	    btnGuide.setFont(new Font("Tahoma", Font.BOLD, 18));
	    	    	    btnGuide.setBounds(199, 166, 127, 45);
	    	    	    HomePanel.add(btnGuide);
	    	    	    btnGuide.addActionListener(new ActionListener() {
	    	    	        public void actionPerformed(ActionEvent e) {
	    	    	            // Hide home components
	    	    	            FirstPanellbl.setVisible(false);
	    	    	            btnSetting.setVisible(false);
	    	    	            btnGuide.setVisible(false);
	    	    	            
	    	    	            // Show guide
	    	    	            GuidePanel.setVisible(true);
	    	    	        }
	    	    	    });
	    	    	    
	    	    	    SettingsPanel = new JPanel();  
	    	    	    SettingsPanel.setBounds(0, 0, 406, 358);  
	    	    	    HomePanel.add(SettingsPanel);
	    	    	    initializeSettingsPanel(SettingsPanel, HomePanel);

	    	    	    GuidePanel = new JPanel();  
	    	    	    GuidePanel.setBounds(0, 0, 406, 358);  
	    	    	    HomePanel.add(GuidePanel);
	    	    	    initializeGuidePanel(GuidePanel, HomePanel);

	    	    	    	    	    	 

	    btnLostItem.addActionListener(e -> Dashboardtab.setSelectedComponent(LostItemPan));
	    btnProgress.addActionListener(e -> Dashboardtab.setSelectedComponent(ProgressPan));
   	    btnLogout.addActionListener(e -> Dashboardtab.setSelectedComponent(LogoutPan));
	    btnFoundLost.addActionListener(e -> Dashboardtab.setSelectedComponent(ItemLostForm));
	    btnReportLost.addActionListener(e -> Dashboardtab.setSelectedComponent(FoundItemForm));
	    btnLostItemProg.addActionListener(e -> Dashboardtab.setSelectedComponent(ItemLostStats));
	    btnFoundItemProfg.addActionListener(e -> Dashboardtab.setSelectedComponent(ItemFoundStats));
	    btnHome.addActionListener(e -> Dashboardtab.setSelectedComponent(HomePanel));
	    btnLogoutCancel.addActionListener(e -> Dashboardtab.setSelectedIndex(0)); 

	    Dashboardtab.setSelectedComponent(HomePanel); 

	    ResponsiveScaler.scaleAllComponents(this); 
	    setLocationRelativeTo(null);  
	   
	       
	    }
	private int countUserLostItems(String username) {
	    int count = 0;
	    try (Connection conn = MySQLConnection.connect()) {
	        String sql = "SELECT COUNT(*) FROM lost_items WHERE reported_by = ?";
	        PreparedStatement pstmt = conn.prepareStatement(sql);
	        pstmt.setString(1, username);
	        
	        java.sql.ResultSet rs = pstmt.executeQuery();
	        if (rs.next()) {
	            count = rs.getInt(1);
	        }
	    } catch (Exception ex) {
	        System.err.println("Error counting lost items: " + ex.getMessage());
	    }
	    return count;
	}

	private int countUserFoundItems(String username) {
	    int count = 0;
	    try (Connection conn = MySQLConnection.connect()) {
	        // We'll create this table next
	        String sql = "SELECT COUNT(*) FROM found_items WHERE reported_by = ?";
	        PreparedStatement pstmt = conn.prepareStatement(sql);
	        pstmt.setString(1, username);
	        
	        java.sql.ResultSet rs = pstmt.executeQuery();
	        if (rs.next()) {
	            count = rs.getInt(1);
	        }
	    } catch (Exception ex) {
	        System.err.println("Error counting found items: " + ex.getMessage());
	    }
	    return count;
	}
	private void loadPendingLostItems(JPanel containerPanel, String username) {
	    containerPanel.removeAll();
	    
	    try (Connection conn = MySQLConnection.connect()) {
	        String sql = "SELECT id, item_name, description, date_lost, status FROM lost_items " +
	                     "WHERE reported_by = ? AND status = 'Pending' ORDER BY date_reported DESC";
	        PreparedStatement pstmt = conn.prepareStatement(sql);
	        pstmt.setString(1, username);
	        
	        java.sql.ResultSet rs = pstmt.executeQuery();
	        
	        boolean hasItems = false;
	        while (rs.next()) {
	            hasItems = true;
	            int ticketId = rs.getInt("id");
	            String itemName = rs.getString("item_name");
	            String description = rs.getString("description");
	            String dateLost = rs.getString("date_lost");
	            String status = rs.getString("status");
	            
	            // Create a panel for each ticket
	            JPanel ticketPanel = new JPanel();
	            ticketPanel.setLayout(null);
	            ticketPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60)); // Increased height
	            ticketPanel.setPreferredSize(new Dimension(350, 60)); // Increased height
	            ticketPanel.setBackground(Color.WHITE);
	            ticketPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY));
	            
	            // Ticket ID
	            JLabel lblTicketId = new JLabel("#" + ticketId);
	            lblTicketId.setFont(new Font("Tahoma", Font.BOLD, 12));
	            lblTicketId.setBounds(10, 5, 50, 20);
	            ticketPanel.add(lblTicketId);
	            
	            // Type
	            JLabel lblType = new JLabel("Lost");
	            lblType.setFont(new Font("Tahoma", Font.PLAIN, 11));
	            lblType.setForeground(new Color(100, 100, 100));
	            lblType.setBounds(70, 5, 40, 20);
	            ticketPanel.add(lblType);
	            
	            // Status
	            JLabel lblStatus = new JLabel(status);
	            lblStatus.setFont(new Font("Tahoma", Font.BOLD, 11));
	            lblStatus.setForeground(new Color(255, 165, 0));
	            lblStatus.setBounds(280, 5, 70, 20);
	            ticketPanel.add(lblStatus);
	            
	            // Description
	            String shortDesc = description.length() > 30 ? description.substring(0, 30) + "..." : description;
	            JLabel lblDesc = new JLabel(shortDesc);
	            lblDesc.setFont(new Font("Tahoma", Font.PLAIN, 11));
	            lblDesc.setBounds(10, 27, 250, 20);
	            lblDesc.setToolTipText(description);
	            ticketPanel.add(lblDesc);
	            
	            // Date
	            JLabel lblDate = new JLabel("📅 " + dateLost);
	            lblDate.setFont(getEmojiFont(Font.PLAIN, 9));
	            lblDate.setForeground(new Color(120, 120, 120));
	            lblDate.setBounds(265, 27, 90, 20);
	            ticketPanel.add(lblDate);
	            
	            // Make panel clickable
	            ticketPanel.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
	            ticketPanel.addMouseListener(new MouseAdapter() {
	                @Override
	                public void mouseClicked(MouseEvent e) {
	                    TicketDetailWindow detailWindow = new TicketDetailWindow(
	                        (java.awt.Frame) javax.swing.SwingUtilities.getWindowAncestor(ticketPanel),
	                        "lost",
	                        ticketId
	                    );
	                    detailWindow.setVisible(true);
	                }
	                
	                @Override
	                public void mouseEntered(MouseEvent e) {
	                    ticketPanel.setBackground(new Color(245, 245, 245));
	                }
	                
	                @Override
	                public void mouseExited(MouseEvent e) {
	                    ticketPanel.setBackground(Color.WHITE);
	                }
	            });
	            
	            containerPanel.add(ticketPanel);
	        }
	        
	        if (!hasItems) {
	            JLabel lblNoItems = new JLabel("No pending tickets");
	            lblNoItems.setFont(new Font("Tahoma", Font.ITALIC, 12));
	            lblNoItems.setForeground(Color.GRAY);
	            containerPanel.add(lblNoItems);
	        }
	        
	    } catch (Exception ex) {
	        System.err.println("Error loading pending items: " + ex.getMessage());
	        ex.printStackTrace();
	    }
	    
	    containerPanel.revalidate();
	    containerPanel.repaint();
	}
	
	private void loadPendingFoundItems(JPanel containerPanel, String username) {
	    containerPanel.removeAll();
	    
	    try (Connection conn = MySQLConnection.connect()) {
	        String sql = "SELECT id, item_name, description, date_found, status FROM found_items " +
	                     "WHERE reported_by = ? AND status = 'Pending' ORDER BY date_reported DESC";
	        PreparedStatement pstmt = conn.prepareStatement(sql);
	        pstmt.setString(1, username);
	        
	        java.sql.ResultSet rs = pstmt.executeQuery();
	        
	        boolean hasItems = false;
	        while (rs.next()) {
	            hasItems = true;
	            int ticketId = rs.getInt("id");
	            String itemName = rs.getString("item_name");
	            String description = rs.getString("description");
	            String dateFound = rs.getString("date_found");
	            String status = rs.getString("status");
	            
	            // Create a panel for each ticket
	            JPanel ticketPanel = new JPanel();
	            ticketPanel.setLayout(null);
	            ticketPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60)); // Increased height
	            ticketPanel.setPreferredSize(new Dimension(350, 60)); // Increased height
	            ticketPanel.setBackground(Color.WHITE);
	            ticketPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY));
	            
	            // Ticket ID
	            JLabel lblTicketId = new JLabel("#" + ticketId);
	            lblTicketId.setFont(new Font("Tahoma", Font.BOLD, 12));
	            lblTicketId.setBounds(10, 5, 50, 20);
	            ticketPanel.add(lblTicketId);
	            
	            // Type
	            JLabel lblType = new JLabel("Found");
	            lblType.setFont(new Font("Tahoma", Font.PLAIN, 11));
	            lblType.setForeground(new Color(100, 100, 100));
	            lblType.setBounds(70, 5, 45, 20);
	            ticketPanel.add(lblType);
	            
	            // Status
	            JLabel lblStatus = new JLabel(status);
	            lblStatus.setFont(new Font("Tahoma", Font.BOLD, 11));
	            lblStatus.setForeground(new Color(255, 165, 0));
	            lblStatus.setBounds(280, 5, 70, 20);
	            ticketPanel.add(lblStatus);
	            
	            // Description
	            String shortDesc = description.length() > 30 ? description.substring(0, 30) + "..." : description;
	            JLabel lblDesc = new JLabel(shortDesc);
	            lblDesc.setFont(new Font("Tahoma", Font.PLAIN, 11));
	            lblDesc.setBounds(10, 27, 250, 20);
	            lblDesc.setToolTipText(description);
	            ticketPanel.add(lblDesc);
	            
	            // Date
	            JLabel lblDate = new JLabel("📅 " + dateFound);
	            lblDate.setFont(getEmojiFont(Font.PLAIN, 9));
	            lblDate.setForeground(new Color(120, 120, 120));
	            lblDate.setBounds(265, 27, 90, 20);
	            ticketPanel.add(lblDate);
	            
	            // Make panel clickable
	            ticketPanel.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
	            ticketPanel.addMouseListener(new MouseAdapter() {
	                @Override
	                public void mouseClicked(MouseEvent e) {
	                    TicketDetailWindow detailWindow = new TicketDetailWindow(
	                        (java.awt.Frame) javax.swing.SwingUtilities.getWindowAncestor(ticketPanel),
	                        "found",
	                        ticketId
	                    );
	                    detailWindow.setVisible(true);
	                }
	                
	                @Override
	                public void mouseEntered(MouseEvent e) {
	                    ticketPanel.setBackground(new Color(245, 245, 245));
	                }
	                
	                @Override
	                public void mouseExited(MouseEvent e) {
	                    ticketPanel.setBackground(Color.WHITE);
	                }
	            });
	            
	            containerPanel.add(ticketPanel);
	        }
	        
	        if (!hasItems) {
	            JLabel lblNoItems = new JLabel("No pending tickets");
	            lblNoItems.setFont(new Font("Tahoma", Font.ITALIC, 12));
	            lblNoItems.setForeground(Color.GRAY);
	            containerPanel.add(lblNoItems);
	        }
	        
	    } catch (Exception ex) {
	        System.err.println("Error loading pending found items: " + ex.getMessage());
	        ex.printStackTrace();
	    }
	    
	    containerPanel.revalidate();
	    containerPanel.repaint();
	}
	
	private void initializeSettingsPanel(JPanel SettingsPanel, JPanel HomePanel) {
	    SettingsPanel.setLayout(null);
	    SettingsPanel.setBackground(Color.WHITE);
	    SettingsPanel.setVisible(false); // Hidden by default
	    
	    // Back button
	    JButton btnBackToHome = RoundedComponents.createRoundedButton(
         	    "← Return",
         	    new Color(90, 14, 36),       
                 new Color(255, 255, 255),    
                 new Color(0, 0, 0),
         	    25,                         
         	     RoundedComponents.ALL_CORNERS
         	     );
 
	    btnBackToHome.setBounds(10, 10, 100, 30);
	    btnBackToHome.addActionListener(e -> {
	        SettingsPanel.setVisible(false);
	        
	        FirstPanellbl.setVisible(true);
	        btnSetting.setVisible(true);
	        btnGuide.setVisible(true);
	    });
	    SettingsPanel.add(btnBackToHome);
	    
	    // Title
	    JLabel lblSettingsTitle = new JLabel("Settings");
	    lblSettingsTitle.setFont(new Font("Tahoma", Font.BOLD, 24));
	    lblSettingsTitle.setHorizontalAlignment(SwingConstants.CENTER);
	    lblSettingsTitle.setBounds(10, 50, 366, 40);
	    SettingsPanel.add(lblSettingsTitle);
	    
	    // Account Settings Section
	    JPanel accountPanel = RoundedComponents.createRoundedPanel(
   	    		new Color(240, 240, 240),           // background
   	       	    Color.BLACK,                   // border
   	       	    1,
   	       	    15,  
   	            RoundedComponents.ALL_CORNERS
   	        );	    accountPanel.setBounds(20, 100, 346, 80);
	    accountPanel.setLayout(null);
	    SettingsPanel.add(accountPanel);
	    
	    JLabel lblAccount = new JLabel("Account Settings");
	    lblAccount.setFont(new Font("Tahoma", Font.BOLD, 14));
	    lblAccount.setBounds(15, 10, 200, 20);
	    accountPanel.add(lblAccount);
	    
	    JButton btnChangePassword = RoundedComponents.createRoundedButton(
         	    "Change Password",
         	    new Color(64, 0, 0),       
                 new Color(255, 255, 255),    
                 new Color(0, 0, 0),
         	    25,                         
         	     RoundedComponents.ALL_CORNERS
         	     );
	    
	    btnChangePassword.setBounds(15, 40, 150, 30);
	    btnChangePassword.addActionListener(e -> showChangePasswordDialog());
	    accountPanel.add(btnChangePassword);
	    
	    JButton btnEditProfile = RoundedComponents.createRoundedButton(
         	    "Edit Profile",
         	     new Color(100, 100, 100),       
                 new Color(255, 255, 255),    
                 Color.BLACK,
         	    25,                         
         	     RoundedComponents.ALL_CORNERS
         	     );
	    btnEditProfile.setBounds(180, 40, 150, 30);
	    btnEditProfile.addActionListener(e -> showEditProfileDialog());
	    accountPanel.add(btnEditProfile);
	    
	    // Appearance Settings Section
	    JPanel appearancePanel = RoundedComponents.createRoundedPanel(
   	    		new Color(240, 240, 240),           // background
   	       	    Color.BLACK,                   // border
   	       	    1,
   	       	    15,  
   	            RoundedComponents.ALL_CORNERS
   	        );
	    appearancePanel.setBounds(20, 190, 346, 80);
	    appearancePanel.setLayout(null);
	    SettingsPanel.add(appearancePanel);
	    
	    JLabel lblAppearance = new JLabel("Appearance");
	    lblAppearance.setFont(new Font("Tahoma", Font.BOLD, 14));
	    lblAppearance.setBounds(15, 10, 200, 20);
	    appearancePanel.add(lblAppearance);
	    
	    JLabel lblTheme = new JLabel("Theme: Light Mode");
	    lblTheme.setFont(new Font("Tahoma", Font.PLAIN, 12));
	    lblTheme.setBounds(15, 40, 150, 20);
	    appearancePanel.add(lblTheme);
	    
	    JLabel lblNote = new JLabel("(Coming Soon)");
	    lblNote.setFont(new Font("Tahoma", Font.ITALIC, 10));
	    lblNote.setForeground(new Color(0, 123, 255));
	    lblNote.setBounds(15, 58, 150, 15);
	    appearancePanel.add(lblNote);
	    
	    // About Section (Easter Egg Hidden Here!)
	    JPanel aboutPanel = RoundedComponents.createRoundedPanel(
	        new Color(255, 255, 255), 
	        15
	    );
	    aboutPanel.setBounds(20, 280, 346, 30);
	    aboutPanel.setLayout(null);
	    SettingsPanel.add(aboutPanel);
	    
	    // Easter Egg: Click "About" 5 times rapidly to reveal Hall of Fame
	    JLabel lblAbout = new JLabel("About Lost & Found v1.0");
	    lblAbout.setFont(new Font("Tahoma", Font.PLAIN, 12));
	    lblAbout.setHorizontalAlignment(SwingConstants.CENTER);
	    lblAbout.setBounds(0, 0, 346, 30);
	    lblAbout.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
	    aboutPanel.add(lblAbout);
	    
	    // Easter egg click counter
	    final int[] clickCount = {0};
	    final long[] lastClickTime = {0};
	    
	    lblAbout.addMouseListener(new java.awt.event.MouseAdapter() {
	        @Override
	        public void mouseClicked(java.awt.event.MouseEvent e) {
	            long currentTime = System.currentTimeMillis();
	            if (currentTime - lastClickTime[0] < 500) { // Within 500ms
	                clickCount[0]++;
	                if (clickCount[0] >= 5) {
	                    showHallOfFame(); // Easter egg revealed!
	                    clickCount[0] = 0;
	                }
	            } else {
	                clickCount[0] = 1;
	            }
	            lastClickTime[0] = currentTime;
	        }
	    });
	}
	
	private void initializeGuidePanel(JPanel GuidePanel, JPanel HomePanel) {
		
		
	    GuidePanel.setLayout(null);
	    GuidePanel.setBackground(Color.WHITE);
	    GuidePanel.setVisible(false); // Hidden by default
	    
	    // Back button
	    JButton btnBackToHome = RoundedComponents.createRoundedButton(
	        "← Return", 
	        new Color(90, 14, 36), 
	        Color.WHITE, 
	        15
	    );
	    btnBackToHome.setBounds(10, 10, 100, 30);
	    btnBackToHome.addActionListener(e -> {
	        GuidePanel.setVisible(false);
	        
	        FirstPanellbl.setVisible(true);
	        btnSetting.setVisible(true);
	        btnGuide.setVisible(true);
	    });
	    GuidePanel.add(btnBackToHome);
	    
	    // Title
	    JLabel lblGuideTitle = new JLabel("User Guide");
	    lblGuideTitle.setFont(new Font("Tahoma", Font.BOLD, 24));
	    lblGuideTitle.setHorizontalAlignment(SwingConstants.CENTER);
	    lblGuideTitle.setBounds(10, 50, 366, 40);
	    GuidePanel.add(lblGuideTitle);
	    
	    // Scrollable guide content
	    JScrollPane scrollPane = new JScrollPane();
	    scrollPane.setBounds(20, 100, 346, 200);
	    scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
	    scrollPane.setBorder(null);
	    scrollPane.getVerticalScrollBar().setUnitIncrement(16);  
	    scrollPane.getVerticalScrollBar().setBlockIncrement(50);
	    GuidePanel.add(scrollPane);
	    
	    JPanel guideContent = new JPanel();
	    guideContent.setLayout(new BoxLayout(guideContent, BoxLayout.Y_AXIS));
	    guideContent.setBackground(Color.WHITE);
	    scrollPane.setViewportView(guideContent);
	    
	    // Add guide sections
	    addGuideSection(guideContent, "📝 Report Lost Item", 
	        "Click 'Report a Lost Item' to submit details about an item you've lost. " +
	        "Include the item name, location, date, and description. You can also upload a photo.");
	    
	    addGuideSection(guideContent, "🔍 Submit Found Item", 
	        "Found something? Click 'Submit a Found Item' to report it. " +
	        "This helps others find their lost belongings!");
	    
	    addGuideSection(guideContent, "📊 Check Progress", 
	        "View your submitted reports under 'Progress'. " +
	        "Track the status of your lost/found items (Pending, Approved, Resolved).");
	    
	    addGuideSection(guideContent, "🎫 Pending Tickets", 
	        "Click on any pending ticket to view full details including images and descriptions.");
	    
	    addGuideSection(guideContent, "🔔 Notifications", 
	        "Click the Notification button to see updates on your reports. " +
	        "Admin will notify you when your item is approved or resolved.");
	    
	    addGuideSection(guideContent, "💬 Messages", 
	        "Communicate with admin about your reports through the Messages feature. " +
	        "Admin can ask for clarifications or provide updates.");
	    
	    addGuideSection(guideContent, "⚙️ Settings", 
	        "Access Settings from the Home tab to change your password or edit your profile.");
	    
	    SwingUtilities.invokeLater(() -> {
	        scrollPane.getVerticalScrollBar().setValue(0);
	        scrollPane.getViewport().setViewPosition(new Point(0, 0));
	    });

	}

	// Helper method to add guide sections
	private void addGuideSection(JPanel parent, String title, String content) {
	    JPanel section = RoundedComponents.createRoundedPanel(
   	    		new Color(240, 240, 240),           // background
   	       	    Color.BLACK,                   // border
   	       	    1,
   	       	    15,  
   	            RoundedComponents.ALL_CORNERS
   	        );
	    section.setLayout(new BorderLayout(10, 10));
	    section.setBorder(BorderFactory.createEmptyBorder(0, 15, 20, 15)); // Increased from 15 to 20
	    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	    int scaledWidth = (int) (300 * ((double) screenSize.width / 548));
	    int scaledHeight = (int) (100 * ((double) screenSize.height / 358));
	    section.setPreferredSize(new Dimension(scaledWidth, scaledHeight));
	    section.setMaximumSize(new Dimension(Integer.MAX_VALUE, scaledHeight)); // Allow width to expand
	    section.setAlignmentX(Component.LEFT_ALIGNMENT);
	    
	    JLabel lblTitle = new JLabel(title);
	    lblTitle.setFont(getEmojiFont(Font.BOLD, 13));
	    lblTitle.setBorder(BorderFactory.createEmptyBorder(20, 5, 20, 15));
	    section.add(lblTitle, BorderLayout.NORTH);
	    
	    JTextArea txtContent = new JTextArea(content);
	    txtContent.setFont(new Font("Tahoma", Font.PLAIN, 12));
	    txtContent.setLineWrap(true);
	    txtContent.setWrapStyleWord(true);
	    txtContent.setEditable(false);
	    txtContent.setOpaque(false);
	    txtContent.setBorder(BorderFactory.createEmptyBorder(0, 0, 5, 0)); // Add top/bottom padding
	    section.add(txtContent, BorderLayout.CENTER);
	    
	    parent.add(section);
	    parent.add(Box.createVerticalStrut(10));
	}
	
	
	private void showChangePasswordDialog() {
	    JDialog dialog = new JDialog((java.awt.Frame) null, "Change Password", true);
	    dialog.setSize(400, 350);  // ✅ Fixed size - back to original
	    dialog.setLocationRelativeTo(null);
	    dialog.getContentPane().setLayout(null);
	    dialog.getContentPane().setBackground(Color.WHITE);
	    
	    JLabel lblTitle = new JLabel("Change Your Password");
	    lblTitle.setFont(new Font("Tahoma", Font.BOLD, 16));
	    lblTitle.setBounds(20, 20, 300, 30);
	    dialog.getContentPane().add(lblTitle);
	    
	    JLabel lblCurrent = new JLabel("Current Password:");
	    lblCurrent.setFont(new Font("Tahoma", Font.PLAIN, 12));
	    lblCurrent.setBounds(20, 70, 150, 20);
	    dialog.getContentPane().add(lblCurrent);
	    
	    JPasswordField txtCurrent = RoundedComponents.createRoundedPasswordField(
	    	    new Color(245, 245, 245), Color.BLACK, Color.BLACK, 10
	    	);
	    	txtCurrent.setBounds(20, 95, 320, 30);
	    	dialog.getContentPane().add(txtCurrent);

	    
	    JLabel lblNew = new JLabel("New Password:");
	    lblNew.setFont(new Font("Tahoma", Font.PLAIN, 12));
	    lblNew.setBounds(20, 135, 150, 20);
	    dialog.getContentPane().add(lblNew);
	    
	    JPasswordField txtNew = RoundedComponents.createRoundedPasswordField(
	    	    new Color(245, 245, 245), Color.BLACK, Color.BLACK, 10
	    	);
	    	txtNew.setBounds(20, 160, 320, 30);
	    	dialog.getContentPane().add(txtNew);
	    
	    JLabel lblConfirm = new JLabel("Confirm New Password:");
	    lblConfirm.setFont(new Font("Tahoma", Font.PLAIN, 12));
	    lblConfirm.setBounds(20, 200, 150, 20);
	    dialog.getContentPane().add(lblConfirm);
	    
	    JPasswordField txtConfirm = RoundedComponents.createRoundedPasswordField(
	    	    new Color(245, 245, 245), Color.BLACK, Color.BLACK, 10
	    	);
	    	txtConfirm.setBounds(20, 225, 320, 30);
	    	dialog.getContentPane().add(txtConfirm);
	    	
	    	JButton eyeToggle = RoundedComponents.createEyeToggleButton(
	    		    new Color(255, 255, 255),
	    		    24
	    		);
	    		eyeToggle.setBounds(345, 95, 24, 30);
	    		dialog.getContentPane().add(eyeToggle);

	    		// Eye toggle controls ALL THREE password fields
	    		eyeToggle.addActionListener(e -> {
	    		    eyeToggle.setSelected(!eyeToggle.isSelected());
	    		    if(eyeToggle.isSelected()) {
	    		        // Show all passwords
	    		        txtCurrent.setEchoChar((char) 0);
	    		        txtNew.setEchoChar((char) 0);
	    		        txtConfirm.setEchoChar((char) 0);
	    		    } else {
	    		        // Hide all passwords
	    		        txtCurrent.setEchoChar('•');
	    		        txtNew.setEchoChar('•');
	    		        txtConfirm.setEchoChar('•');
	    		    }
	    		});
	    
	    // Password strength indicator
	    JLabel lblStrength = new JLabel("");
	    lblStrength.setFont(new Font("Tahoma", Font.ITALIC, 10));
	    lblStrength.setBounds(20, 258, 340, 15);
	    dialog.getContentPane().add(lblStrength);
	    
	    JButton btnConfirm = RoundedComponents.createRoundedButton(
		            	    "Change Password",
		            	    new Color(90, 14, 36),       
		                    new Color(255, 255, 255),    
		                    new Color(0, 0, 0),
		            	    15,                         
		            	    RoundedComponents.ALL_CORNERS		            	     
	    );
	    btnConfirm.setBounds(20, 265, 160, 35);
	    btnConfirm.addActionListener(e -> {
	        String currentPass = new String(txtCurrent.getPassword());
	        String newPass = new String(txtNew.getPassword());
	        String confirmPass = new String(txtConfirm.getPassword());
	        
	        // Validation
	        if (currentPass.isEmpty() || newPass.isEmpty() || confirmPass.isEmpty()) {
	            JOptionPane.showMessageDialog(dialog, 
	                "Please fill all fields", 
	                "Error", JOptionPane.ERROR_MESSAGE);
	            return;
	        }
	        
	        // Check if new passwords match
	        if (!newPass.equals(confirmPass)) {
	            JOptionPane.showMessageDialog(dialog, 
	                "New passwords do not match", 
	                "Error", JOptionPane.ERROR_MESSAGE);
	            return;
	        }
	        
	        // Validate password strength
	        String strengthError = PasswordHasher.validatePasswordStrength(newPass);
	        if (strengthError != null) {
	            JOptionPane.showMessageDialog(dialog, 
	                strengthError, 
	                "Weak Password", JOptionPane.WARNING_MESSAGE);
	            return;
	        }
	        
	        // Verify current password and update
	        try (Connection conn = MySQLConnection.connect()) {
	            String checkSql = "SELECT password FROM users WHERE username = ?";
	            PreparedStatement checkStmt = conn.prepareStatement(checkSql);
	            checkStmt.setString(1, currentUsername);
	            java.sql.ResultSet rs = checkStmt.executeQuery();
	            
	            if (rs.next()) {
	                String storedHash = rs.getString("password");
	                
	                // Verify current password (handles both old plain text and new hashed)
	                boolean isCorrect = false;
	                if (storedHash.contains(":")) {
	                    // New hashed password
	                    isCorrect = PasswordHasher.verifyPassword(currentPass, storedHash);
	                } else {
	                    // Old plain text password (for migration)
	                    isCorrect = storedHash.equals(currentPass);
	                }
	                
	                if (isCorrect) {
	                    // Hash the new password
	                    String newHashedPassword = PasswordHasher.hashPassword(newPass);
	                    
	                    String updateSql = "UPDATE users SET password = ? WHERE username = ?";
	                    PreparedStatement updateStmt = conn.prepareStatement(updateSql);
	                    updateStmt.setString(1, newHashedPassword);
	                    updateStmt.setString(2, currentUsername);
	                    updateStmt.executeUpdate();
	                    
	                    // Log the security event
	                    System.out.println("Password changed for user: " + currentUsername);
	                    
	                    JOptionPane.showMessageDialog(dialog, 
	                        "Password changed successfully!\nYour password is now securely encrypted.", 
	                        "Success", JOptionPane.INFORMATION_MESSAGE);
	                    dialog.dispose();
	                } else {
	                    JOptionPane.showMessageDialog(dialog, 
	                        "Current password is incorrect", 
	                        "Error", JOptionPane.ERROR_MESSAGE);
	                }
	            } else {
	                JOptionPane.showMessageDialog(dialog, 
	                    "User not found", 
	                    "Error", JOptionPane.ERROR_MESSAGE);
	            }
	        } catch (Exception ex) {
	            ex.printStackTrace();
	            JOptionPane.showMessageDialog(dialog, 
	                "Database error: " + ex.getMessage(), 
	                "Error", JOptionPane.ERROR_MESSAGE);
	        }
	    });
	    dialog.getContentPane().add(btnConfirm);
	    
	    JButton btnCancel = RoundedComponents.createRoundedButton(
         	    "Cancel",
         	     new Color(90, 14, 36),       
                 Color.RED,    
                 new Color(0, 0, 0),
         	    15,                         
         	     RoundedComponents.ALL_CORNERS
         	     );
	    btnCancel.setBounds(200, 265, 160, 35);
	    btnCancel.addActionListener(e -> dialog.dispose());
	    dialog.getContentPane().add(btnCancel);
	    
	    
	    dialog.setVisible(true);
	}

	private void showEditProfileDialog() {
	    JDialog dialog = new JDialog((java.awt.Frame) null, "Edit Profile", true);
	    dialog.setSize(400, 250);  // ✅ Fixed size - back to original
	    dialog.setLocationRelativeTo(null);
	    dialog.getContentPane().setLayout(null);
	    dialog.getContentPane().setBackground(Color.WHITE);
	    
	    JLabel lblTitle = new JLabel("Edit Your Profile");
	    lblTitle.setFont(new Font("Tahoma", Font.BOLD, 16));
	    lblTitle.setBounds(20, 20, 300, 30);
	    dialog.getContentPane().add(lblTitle);	    
	    
	    JLabel lblEmail = new JLabel("Email:");
	    lblEmail.setFont(new Font("Tahoma", Font.PLAIN, 12));
	    lblEmail.setBounds(20, 70, 100, 20);
	    dialog.getContentPane().add(lblEmail);
	    
	    JTextField txtEmail = RoundedComponents.createRoundedTextField(
	        new Color(245, 245, 245), Color.BLACK, Color.BLACK, 10
	    );
	    txtEmail.setBounds(20, 95, 340, 30);
	    dialog.getContentPane().add(txtEmail);
	    
	    // Load current email
	    try (Connection conn = MySQLConnection.connect()) {
	        String sql = "SELECT email FROM users WHERE username = ?";
	        PreparedStatement pstmt = conn.prepareStatement(sql);
	        pstmt.setString(1, currentUsername);
	        java.sql.ResultSet rs = pstmt.executeQuery();
	        if (rs.next()) {
	            txtEmail.setText(rs.getString("email"));
	        }
	    } catch (Exception ex) {
	        ex.printStackTrace();
	    }
	    
	    JButton btnSave = RoundedComponents.createRoundedButton(
         	    "Save Changes",
         	    new Color(90, 14, 36),       
                 new Color(255, 255, 255),    
                 new Color(0, 0, 0),
         	    15,                         
         	     RoundedComponents.ALL_CORNERS
         	     );
	    
	    btnSave.setBounds(20, 145, 160, 35);
	    btnSave.addActionListener(e -> {
	        String newEmail = txtEmail.getText().trim();
	        if (newEmail.isEmpty()) {
	            JOptionPane.showMessageDialog(dialog, "Email cannot be empty", 
	                "Error", JOptionPane.ERROR_MESSAGE);
	            return;
	        }
	        
	        try (Connection conn = MySQLConnection.connect()) {
	            String sql = "UPDATE users SET email = ? WHERE username = ?";
	            PreparedStatement pstmt = conn.prepareStatement(sql);
	            pstmt.setString(1, newEmail);
	            pstmt.setString(2, currentUsername);
	            pstmt.executeUpdate();
	            
	            JOptionPane.showMessageDialog(dialog, "Profile updated successfully!", 
	                "Success", JOptionPane.INFORMATION_MESSAGE);
	            dialog.dispose();
	        } catch (Exception ex) {
	            ex.printStackTrace();
	            JOptionPane.showMessageDialog(dialog, "Database error: " + ex.getMessage(), 
	                "Error", JOptionPane.ERROR_MESSAGE);
	        }
	    });
	    dialog.getContentPane().add(btnSave);
	    
	    JButton btnCancel = RoundedComponents.createRoundedButton(
         	    "Cancel",
         	    new Color(90, 14, 36),       
                Color.RED,    
                 new Color(0, 0, 0),
         	    15,                         
         	     RoundedComponents.ALL_CORNERS
         	     );
	    btnCancel.setBounds(200, 145, 160, 35);
	    btnCancel.addActionListener(e -> dialog.dispose());
	    dialog.getContentPane().add(btnCancel);
	    
	    dialog.setVisible(true);
	}

	// REPLACE your entire showHallOfFame() method with this:

	private void showHallOfFame() {
	    JDialog dialog = new JDialog((java.awt.Frame) null, "🏆 Hall of Fame", true);
	    dialog.setSize(500, 625);
	    dialog.setLocationRelativeTo(null);
	    dialog.getContentPane().setLayout(null);
	    dialog.getContentPane().setBackground(new Color(240, 240, 240));
	    
	    // Title
	    JLabel lblTitle = new JLabel("🏆 HALL OF FAME 🏆");
	    lblTitle.setFont(getEmojiFont(Font.BOLD, 22));
	    lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
	    lblTitle.setBounds(0, 15, 500, 40);
	    dialog.getContentPane().add(lblTitle);
	    
	    // Subtitle
	    JLabel lblSubtitle = new JLabel("The Amazing Team Behind This Project");
	    lblSubtitle.setFont(new Font("Tahoma", Font.ITALIC, 12));
	    lblSubtitle.setHorizontalAlignment(SwingConstants.CENTER);
	    lblSubtitle.setForeground(Color.GRAY);
	    lblSubtitle.setBounds(0, 50, 500, 20);
	    dialog.getContentPane().add(lblSubtitle);
	    
	    // Scrollable content panel
	    JScrollPane scrollPane = new JScrollPane();
	    scrollPane.setBounds(20, 85, 460, 450);
	    scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
	    scrollPane.setBorder(null);
	    scrollPane.getViewport().setBackground(new Color(240, 240, 240));
	    scrollPane.getVerticalScrollBar().setUnitIncrement(16);  
	    scrollPane.getVerticalScrollBar().setBlockIncrement(50);
	    dialog.getContentPane().add(scrollPane);
	    
	    JPanel contentPanel = new JPanel();
	    contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
	    contentPanel.setBackground(new Color(240, 240, 240));
	    scrollPane.setViewportView(contentPanel);
	    
	    // Add team members
	    addTeamMember(contentPanel, "Ryan Francis Baquilar", "Project Documenter", "/image/Baquilar.png");
	    contentPanel.add(Box.createVerticalStrut(20));
	    
	    addTeamMember(contentPanel, "Ian Francis Partosa", "Developer, Design", "/image/Partosa.png");
	    contentPanel.add(Box.createVerticalStrut(20));

	    addTeamMember(contentPanel, "Elias Glenn Genova", "Designer, Project Documenter", "/image/Elias.jpg");
	    contentPanel.add(Box.createVerticalStrut(20));

	    addTeamMember(contentPanel, "Alexis Prado", "Project Manager", "/image/Prado.jpg");
	    contentPanel.add(Box.createVerticalStrut(20));

	    addTeamMember(contentPanel, "Karlo Matthew Agustin", "Quality Assurance/Tester", "/image/Agustin.jpg");
	    contentPanel.add(Box.createVerticalStrut(20));

	    // Close button
	    JButton btnClose = RoundedComponents.createRoundedButton(
         	    "Close",
         	    new Color(64, 0, 0),       
                 new Color(255, 255, 255),    
                 new Color(0, 0, 0),
         	    10,                         
         	     RoundedComponents.ALL_CORNERS
         	     );
	    btnClose.setBounds(190, 545, 120, 35);
	    btnClose.addActionListener(e -> dialog.dispose());
	    dialog.getContentPane().add(btnClose);
	    
	    dialog.setVisible(true);
	}

	// Helper method to add a team member with photo
	private void addTeamMember(JPanel parent, String name, String role, String imagePath) {
	    JPanel memberPanel = RoundedComponents.createRoundedPanel(
	        Color.WHITE, 
	        15
	    );
	    memberPanel.setLayout(null);
	    memberPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));
	    memberPanel.setPreferredSize(new Dimension(440, 120));
	    memberPanel.setBorder(BorderFactory.createCompoundBorder(
	        BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
	        BorderFactory.createEmptyBorder(10, 10, 10, 10)
	    ));
	    
	    // Profile picture
	    JLabel lblPhoto = new JLabel();
	    lblPhoto.setBounds(15, 10, 100, 100);
	    lblPhoto.setHorizontalAlignment(SwingConstants.CENTER);
	    lblPhoto.setBorder(BorderFactory.createLineBorder(new Color(64, 0, 0), 2));
	    
	    try {
	        ImageIcon icon = new ImageIcon(getClass().getResource(imagePath));
	        Image img = icon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
	        lblPhoto.setIcon(new ImageIcon(img));
	    } catch (Exception e) {
	        lblPhoto.setText("No Photo");
	        lblPhoto.setFont(new Font("Tahoma", Font.ITALIC, 10));
	        lblPhoto.setBackground(new Color(245, 245, 245));
	        lblPhoto.setOpaque(true);
	        System.err.println("Error loading image: " + imagePath);
	        e.printStackTrace();
	    }
	    memberPanel.add(lblPhoto);
	    
	    // Name
	    JLabel lblName = new JLabel(name);
	    lblName.setFont(new Font("Tahoma", Font.BOLD, 16));
	    lblName.setBounds(130, 20, 290, 25);
	    memberPanel.add(lblName);
	    
	    // Role
	    JLabel lblRole = new JLabel(role);
	    lblRole.setFont(new Font("Tahoma", Font.PLAIN, 13));
	    lblRole.setForeground(new Color(100, 100, 100));
	    lblRole.setBounds(130, 50, 290, 20);
	    memberPanel.add(lblRole);
	    
	    // Decorative star
	    JLabel lblStar = new JLabel("⭐");
	    lblStar.setFont(getEmojiFont(Font.PLAIN, 20));
	    lblStar.setForeground(new Color(255, 215, 0));
	    lblStar.setBounds(130, 75, 30, 30);
	    memberPanel.add(lblStar);
	    
	    // Achievement label
	    JLabel lblAchievement = new JLabel("Core Team Member");
	    lblAchievement.setFont(new Font("Tahoma", Font.ITALIC, 11));
	    lblAchievement.setForeground(new Color(64, 0, 0));
	    lblAchievement.setBounds(160, 76, 250, 20);
	    memberPanel.add(lblAchievement);
	    
	    parent.add(memberPanel);
	}

	// Helper method to add a placeholder for missing members
	private void addPlaceholderMember(JPanel parent, String name, String role) {
	    JPanel memberPanel = RoundedComponents.createRoundedPanel(
	        new Color(250, 250, 250), 
	        15
	    );
	    memberPanel.setLayout(null);
	    memberPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));
	    memberPanel.setPreferredSize(new Dimension(440, 120));
	    memberPanel.setBorder(BorderFactory.createCompoundBorder(
	        BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
	        BorderFactory.createEmptyBorder(10, 10, 10, 10)
	    ));
	    
	    // Placeholder icon
	    JLabel lblPhoto = new JLabel("?");
	    lblPhoto.setBounds(15, 10, 100, 100);
	    lblPhoto.setHorizontalAlignment(SwingConstants.CENTER);
	    lblPhoto.setVerticalAlignment(SwingConstants.CENTER);
	    lblPhoto.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 2));
	    lblPhoto.setFont(new Font("Tahoma", Font.BOLD, 48));
	    lblPhoto.setForeground(new Color(180, 180, 180));
	    lblPhoto.setBackground(new Color(240, 240, 240));
	    lblPhoto.setOpaque(true);
	    memberPanel.add(lblPhoto);
	    
	    // Name
	    JLabel lblName = new JLabel(name);
	    lblName.setFont(new Font("Tahoma", Font.BOLD, 16));
	    lblName.setForeground(new Color(150, 150, 150));
	    lblName.setBounds(130, 30, 290, 25);
	    memberPanel.add(lblName);
	    
	    // Role
	    JLabel lblRole = new JLabel(role);
	    lblRole.setFont(new Font("Tahoma", Font.ITALIC, 12));
	    lblRole.setForeground(new Color(180, 180, 180));
	    lblRole.setBounds(130, 60, 290, 20);
	    memberPanel.add(lblRole);
	    
	    parent.add(memberPanel);
	}


	// Helper method to add a placeholder for missing members

	
	private Font getEmojiFont(int style, int size) {
	    // Try different emoji fonts based on OS
	    String[] emojiFont = {
	        "Segoe UI Emoji",      // Windows 10/11
	        "Apple Color Emoji",   // macOS
	        "Noto Color Emoji",    // Linux
	        "Segoe UI Symbol",     // Windows fallback
	        "Arial Unicode MS"     // Universal fallback
	    };
	    
	    for (String fontName : emojiFont) {
	        Font font = new Font(fontName, style, size);
	        if (font.getFamily().equals(fontName)) {
	            return font;
	        }
	    }
	    
	    // If no emoji font found, return default
	    return new Font("Dialog", style, size);
	}
	
	public class FontLoader {
	    public static Font loadPoppins(String weight, float size) {
	        try {
	            Font font = Font.createFont(Font.TRUETYPE_FONT, 
	                FontLoader.class.getResourceAsStream("/fonts/Poppins-" + weight + ".ttf"))
	                .deriveFont(size);
	            GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(font);
	            return font;
	        } catch (Exception e) {
	            e.printStackTrace();
	            return new Font("Tahoma", Font.PLAIN, (int)size);
	        }
	    }
	}
	
 }