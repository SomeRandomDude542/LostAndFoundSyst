package laf;

import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class SignupFrame extends JFrame {

    private static final long serialVersionUID = 1L;
    
    private JPanel signupFrame;
    private JTextField txfUsername, txfEmail;
    private JPasswordField txfPassword, txfConfirmPassword;
    private JLabel lblSignup, lblPasswordStrength;
    private JButton btnSignup, btnBack;
    private JCheckBox checkSignup;
    private JPanel panel;
    
    // Original dimensions for scaling
    private static final int ORIGINAL_WIDTH = 734;
    private static final int ORIGINAL_HEIGHT = 520;
    private static final int PANEL_LEFT_WIDTH = 282;
    private static final int PANEL_RIGHT_WIDTH = 322;
    private static final int PANEL_HEIGHT = 410;

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                SignupFrame frame = new SignupFrame();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public SignupFrame() {
        setTitle("Lost And Found");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(ORIGINAL_WIDTH, ORIGINAL_HEIGHT);
        setMinimumSize(new Dimension(550, 450));
        setLocationRelativeTo(null);
        
        signupFrame = new JPanel();
        signupFrame.setBackground(new Color(64, 0, 0));
        signupFrame.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(signupFrame);
        signupFrame.setLayout(null);
        
        // Left Panel (Image)
        JPanel panel_1 = RoundedComponents.createRoundedPanelWithBackground(
            "/image/Signup Design.png",           
            Color.BLACK,
            1,
            15,
            RoundedComponents.LEFT_CORNERS
        );
        panel_1.setLayout(null);        
        signupFrame.add(panel_1);
        
        // Right Panel (Signup Form)
        panel = RoundedComponents.createRoundedPanel(
            Color.WHITE,
            Color.BLACK,
            1,
            15,  
            RoundedComponents.RIGHT_CORNERS
        );
        panel.setBackground(new Color(255, 255, 255));
        panel.setLayout(null);
        signupFrame.add(panel);

        // Title
        lblSignup = new JLabel("Sign up");
        lblSignup.setBackground(new Color(255, 255, 255));
        lblSignup.setForeground(new Color(64, 0, 0));
        lblSignup.setFont(new Font("Tahoma", Font.BOLD, 26));
        lblSignup.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(lblSignup);
        
        // Username input
        txfUsername = RoundedComponents.createRoundedTextField(
            Color.WHITE,
            Color.BLACK,
            Color.BLACK,
            10,
            "Username"
        );
        txfUsername.setFont(new Font("Tahoma", Font.PLAIN, 18));
        panel.add(txfUsername);
        
        // Email input
        txfEmail = RoundedComponents.createRoundedTextField(
            Color.WHITE,
            Color.BLACK,
            Color.BLACK,
            10,
            "Email"
        );
        txfEmail.setFont(new Font("Tahoma", Font.PLAIN, 18));
        panel.add(txfEmail);
                
        // Password input
        txfPassword = RoundedComponents.createRoundedPasswordField(
            Color.WHITE,
            Color.BLACK,
            Color.BLACK,
            10,
            "Password"
        );
        txfPassword.setFont(new Font("Tahoma", Font.PLAIN, 18));
        panel.add(txfPassword);
        
        // Confirm Password input
        txfConfirmPassword = RoundedComponents.createRoundedPasswordField(
            Color.WHITE,
            Color.BLACK,
            Color.BLACK,
            10,
            "Confirm Password"
        );
        txfConfirmPassword.setFont(new Font("Tahoma", Font.PLAIN, 18));
        panel.add(txfConfirmPassword);
        
        // Password strength indicator
        lblPasswordStrength = new JLabel("");
        lblPasswordStrength.setFont(new Font("Tahoma", Font.ITALIC, 10));
        panel.add(lblPasswordStrength);
        
        // Real-time password strength checker
        txfPassword.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                String password = new String(txfPassword.getPassword());
                String strengthError = PasswordHasher.validatePasswordStrength(password);
                if (password.isEmpty()) {
                    lblPasswordStrength.setText("");
                    lblPasswordStrength.setForeground(Color.BLACK);
                } else if (strengthError == null) {
                    lblPasswordStrength.setText("✓ Strong password");
                    lblPasswordStrength.setFont(getEmojiFont(Font.ITALIC, 10));
                    lblPasswordStrength.setForeground(new Color(0, 128, 0));
                } else {
                    lblPasswordStrength.setText("⚠ " + strengthError);
                    lblPasswordStrength.setFont(getEmojiFont(Font.ITALIC, 10));
                    lblPasswordStrength.setForeground(new Color(220, 53, 69));
                }
            }
        });
                        
        // Eye toggle for password
        JButton eyeToggle = RoundedComponents.createEyeToggleButton(Color.WHITE, 24);
        panel.add(eyeToggle);

        eyeToggle.addActionListener(e -> {
            eyeToggle.setSelected(!eyeToggle.isSelected());
            if(eyeToggle.isSelected()) {
                txfPassword.setEchoChar((char) 0);
                txfConfirmPassword.setEchoChar((char) 0);
            } else {
                txfPassword.setEchoChar('•');
                txfConfirmPassword.setEchoChar('•');
            }
        });
        
        // Back button
        btnBack = RoundedComponents.createRoundedButton(
            "Return",  
            new Color(0, 0, 0),
            Color.WHITE,
            Color.BLACK,
            7,
            RoundedComponents.ALL_CORNERS
        );  
        btnBack.setFont(new Font("Tahoma", Font.BOLD, 16));
        btnBack.addActionListener(e -> {
            SignupFrame.this.dispose();
            new LoginWindow().setVisible(true);
        });
        panel.add(btnBack);
        
        // Sign up button
        btnSignup = RoundedComponents.createRoundedButton(
            "Sign Up",
            new Color(122, 14, 26),
            new Color(255, 255, 255),
            Color.BLACK,
            7,
            RoundedComponents.ALL_CORNERS
        );
        btnSignup.setFont(new Font("Tahoma", Font.BOLD, 16));
        btnSignup.addActionListener(e -> handleSignup());
        panel.add(btnSignup);
        
        // Icons
        JLabel lblEmailIcon = new JLabel();
        lblEmailIcon.setHorizontalAlignment(SwingConstants.CENTER);
        lblEmailIcon.setBackground(new Color(255, 255, 255));
        try {
            ImageIcon originalIcon = new ImageIcon(LoginWindow.class.getResource("/image/Email Icon.png"));
            Image scaledImage = originalIcon.getImage().getScaledInstance(25, 25, Image.SCALE_SMOOTH);
            lblEmailIcon.setIcon(new ImageIcon(scaledImage));
        } catch (Exception e) {
            e.printStackTrace();
            lblEmailIcon.setText("📧");
        }
        panel.add(lblEmailIcon);
        
        JLabel lblUsernameIcon = new JLabel();
        lblUsernameIcon.setHorizontalAlignment(SwingConstants.CENTER);
        lblUsernameIcon.setBackground(new Color(255, 255, 255));
        try {
            ImageIcon originalIcon = new ImageIcon(LoginWindow.class.getResource("/image/UsernameLogo.png"));
            Image scaledImage = originalIcon.getImage().getScaledInstance(25, 25, Image.SCALE_SMOOTH);
            lblUsernameIcon.setIcon(new ImageIcon(scaledImage));
        } catch (Exception e) {
            e.printStackTrace();
            lblUsernameIcon.setText("👤");
        }
        panel.add(lblUsernameIcon);
        
        JLabel lblPasswordIcon = new JLabel();
        lblPasswordIcon.setHorizontalAlignment(SwingConstants.CENTER);
        lblPasswordIcon.setBackground(new Color(255, 255, 255));
        try {
            ImageIcon originalIcon = new ImageIcon(LoginWindow.class.getResource("/image/Password Logo.png"));
            Image scaledImage = originalIcon.getImage().getScaledInstance(25, 25, Image.SCALE_SMOOTH);
            lblPasswordIcon.setIcon(new ImageIcon(scaledImage));
        } catch (Exception e) {
            e.printStackTrace();
            lblPasswordIcon.setText("🔒");
        }
        panel.add(lblPasswordIcon);

        // Add component listener for dynamic resizing
        signupFrame.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                scaleComponents(panel_1, panel, lblSignup, txfUsername, txfEmail, 
                               txfPassword, txfConfirmPassword, lblPasswordStrength,
                               eyeToggle, btnBack, btnSignup, 
                               lblUsernameIcon, lblEmailIcon, lblPasswordIcon);
            }
        });
        
        // Initial scaling
        scaleComponents(panel_1, panel, lblSignup, txfUsername, txfEmail, 
                       txfPassword, txfConfirmPassword, lblPasswordStrength,
                       eyeToggle, btnBack, btnSignup, 
                       lblUsernameIcon, lblEmailIcon, lblPasswordIcon);
        
        panel.setFocusable(true);
        panel.requestFocusInWindow();
    }
    
    private void scaleComponents(JPanel panel_1, JPanel panel, JLabel lblSignup,
                                 JTextField txfUsername, JTextField txfEmail,
                                 JPasswordField txfPassword, JPasswordField txfConfirmPassword,
                                 JLabel lblPasswordStrength, JButton eyeToggle,
                                 JButton btnBack, JButton btnSignup,
                                 JLabel lblUsernameIcon, JLabel lblEmailIcon, JLabel lblPasswordIcon) {
        
        // Get current window size
        int currentWidth = signupFrame.getWidth();
        int currentHeight = signupFrame.getHeight();
        
        // Calculate scale factors
        double scaleX = (double) currentWidth / ORIGINAL_WIDTH;
        double scaleY = (double) currentHeight / ORIGINAL_HEIGHT;
        double scale = Math.min(scaleX, scaleY);
        
        // Calculate centered position
        int totalPanelWidth = (int) ((PANEL_LEFT_WIDTH + PANEL_RIGHT_WIDTH) * scale);
        int totalPanelHeight = (int) (PANEL_HEIGHT * scale);
        int startX = (currentWidth - totalPanelWidth) / 2;
        int startY = (currentHeight - totalPanelHeight) / 2;
        
        // Scale and position left panel (image)
        int leftPanelWidth = (int) (PANEL_LEFT_WIDTH * scale);
        int leftPanelHeight = (int) (PANEL_HEIGHT * scale);
        panel_1.setBounds(startX, startY, leftPanelWidth, leftPanelHeight);
        
        // Scale and position right panel (form)
        int rightPanelWidth = (int) (PANEL_RIGHT_WIDTH * scale);
        int rightPanelHeight = (int) (PANEL_HEIGHT * scale);
        panel.setBounds(startX + leftPanelWidth, startY, rightPanelWidth, rightPanelHeight);
        
        // Scale components inside right panel
        
        // Title
        lblSignup.setBounds(
            scale(109, scale), scale(20, scale),
            scale(104, scale), scale(39, scale)
        );
        lblSignup.setFont(new Font("Tahoma", Font.BOLD, scale(26, scale)));
        
        // Username icon
        lblUsernameIcon.setBounds(
            scale(20, scale), scale(82, scale),
            scale(25, scale), scale(25, scale)
        );
        scaleIcon(lblUsernameIcon, "/image/UsernameLogo.png", scale(25, scale));
        
        // Username input
        txfUsername.setBounds(
            scale(47, scale), scale(80, scale),
            scale(227, scale), scale(30, scale)
        );
        txfUsername.setFont(new Font("Tahoma", Font.PLAIN, scale(18, scale)));
        
        // Email icon
        lblEmailIcon.setBounds(
            scale(20, scale), scale(137, scale),
            scale(25, scale), scale(25, scale)
        );
        scaleIcon(lblEmailIcon, "/image/Email Icon.png", scale(25, scale));
        
        // Email input
        txfEmail.setBounds(
            scale(47, scale), scale(135, scale),
            scale(227, scale), scale(30, scale)
        );
        txfEmail.setFont(new Font("Tahoma", Font.PLAIN, scale(18, scale)));
        
        // Password icon
        lblPasswordIcon.setBounds(
            scale(20, scale), scale(192, scale),
            scale(25, scale), scale(25, scale)
        );
        scaleIcon(lblPasswordIcon, "/image/Password Logo.png", scale(25, scale));
        
        // Password input
        txfPassword.setBounds(
            scale(47, scale), scale(190, scale),
            scale(227, scale), scale(30, scale)
        );
        txfPassword.setFont(new Font("Tahoma", Font.PLAIN, scale(18, scale)));
        
        // Password strength label
        lblPasswordStrength.setBounds(
            scale(47, scale), scale(223, scale),
            scale(227, scale), scale(15, scale)
        );
        lblPasswordStrength.setFont(getEmojiFont(Font.ITALIC, scale(10, scale)));
        
        // Confirm Password input
        txfConfirmPassword.setBounds(
            scale(47, scale), scale(245, scale),
            scale(227, scale), scale(30, scale)
        );
        txfConfirmPassword.setFont(new Font("Tahoma", Font.PLAIN, scale(18, scale)));
        
        // Eye toggle
        eyeToggle.setBounds(
            scale(280, scale), scale(191, scale),
            scale(24, scale), scale(26, scale)
        );
        
        // Back button
        btnBack.setBounds(
            scale(57, scale), scale(325, scale),
            scale(100, scale), scale(28, scale)
        );
        btnBack.setFont(new Font("Tahoma", Font.BOLD, scale(16, scale)));
        
        // Sign up button
        btnSignup.setBounds(
            scale(167, scale), scale(325, scale),
            scale(100, scale), scale(28, scale)
        );
        btnSignup.setFont(new Font("Tahoma", Font.BOLD, scale(16, scale)));
    }
    
    private int scale(int original, double scaleFactor) {
        return (int) (original * scaleFactor);
    }
    
    private void scaleIcon(JLabel label, String iconPath, int size) {
        try {
            ImageIcon originalIcon = new ImageIcon(LoginWindow.class.getResource(iconPath));
            Image scaledImage = originalIcon.getImage().getScaledInstance(size, size, Image.SCALE_SMOOTH);
            label.setIcon(new ImageIcon(scaledImage));
        } catch (Exception e) {
            // Keep existing icon or text
        }
    }

    private void handleSignup() {
        String username = txfUsername.getText().trim();
        String email = txfEmail.getText().trim();
        String password = new String(txfPassword.getPassword()).trim();
        String confirmPassword = new String(txfConfirmPassword.getPassword()).trim();

        // 1. Check empty fields
        if (username.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            JOptionPane.showMessageDialog(null,
                    "Please fill in all fields.",
                    "Signup Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        // 2. Validate username
        if (!isValidUsername(username)) {
            JOptionPane.showMessageDialog(null,
                    "Username can only contain letters, numbers, and underscores.\n" +
                    "Must be 3-20 characters long.",
                    "Invalid Username",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        // 3. Validate email format
        if (!isValidEmail(email)) {
            JOptionPane.showMessageDialog(null,
                    "Please enter a valid email address.\nExample: user@gmail.com, user@yahoo.com",
                    "Invalid Email",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        // 4. Validate password strength
        String strengthError = PasswordHasher.validatePasswordStrength(password);
        if (strengthError != null) {
            JOptionPane.showMessageDialog(null,
                    strengthError + "\n\nPassword Requirements:\n" +
                    "• At least 8 characters\n" +
                    "• One uppercase letter\n" +
                    "• One lowercase letter\n" +
                    "• One number",
                    "Weak Password",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        // 5. Check if passwords match
        if (!password.equals(confirmPassword)) {
            JOptionPane.showMessageDialog(null,
                    "Passwords do not match!",
                    "Password Mismatch",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        // 6. Check if username already exists
        try (Connection conn = MySQLConnection.connect()) {
            String checkSql = "SELECT username FROM users WHERE username = ?";
            PreparedStatement checkStmt = conn.prepareStatement(checkSql);
            checkStmt.setString(1, username);
            ResultSet rs = checkStmt.executeQuery();
            
            if (rs.next()) {
                JOptionPane.showMessageDialog(null,
                        "Username already exists. Please choose another one.",
                        "Username Taken",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null,
                    "Error checking username: " + ex.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        // 7. Hash password and INSERT INTO DATABASE
        try (Connection conn = MySQLConnection.connect()) {
            String hashedPassword = PasswordHasher.hashPassword(password);
            
            String sql = "INSERT INTO users(username, email, password, account_status) VALUES(?, ?, ?, ?)";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, username);
            pst.setString(2, email);
            pst.setString(3, hashedPassword);
            pst.setString(4, "active");
            pst.executeUpdate();

            System.out.println("✅ USER ADDED SECURELY: " + username);

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null,
                    "Error creating account: " + ex.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        // SUCCESS POPUP
        String[] options = {"Go to Login", "Create Another Account"};
        int choice = JOptionPane.showOptionDialog(
                null,
                "Account created successfully!\nYour password is securely encrypted.",
                "Signup Success",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,
                options,
                options[0]
        );

        if (choice == 0) {
            SignupFrame.this.dispose();
            new LoginWindow().setVisible(true);
        } else if (choice == 1) {
            txfUsername.setText("");
            txfEmail.setText("");
            txfPassword.setText("");
            txfConfirmPassword.setText("");
            lblPasswordStrength.setText("");
        }
    }

    private boolean isValidUsername(String username) {
        return username != null && 
               username.matches("^[a-zA-Z0-9_]{3,20}$");
    }

    private boolean isValidEmail(String email) {
        if (email == null || !email.contains("@")) {
            return false;
        }
        
        String[] validDomains = {
            "@gmail.com",
            "@yahoo.com",
            "@outlook.com",
            "@hotmail.com",
            "@icloud.com",
            "@mail.com"
        };
        
        String emailLower = email.toLowerCase();
        for (String domain : validDomains) {
            if (emailLower.endsWith(domain)) {
                String beforeAt = email.substring(0, email.indexOf("@"));
                return !beforeAt.isEmpty() && beforeAt.length() >= 3;
            }
        }
        
        return false;
    }
    
    private Font getEmojiFont(int style, int size) {
        String[] emojiFont = {
            "Segoe UI Emoji", "Apple Color Emoji", "Noto Color Emoji", "Segoe UI Symbol"
        };
        for (String fontName : emojiFont) {
            Font font = new Font(fontName, style, size);
            if (font.getFamily().equals(fontName)) {
                return font;
            }
        }
        return new Font("Dialog", style, size);
    }
}