package laf;

import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class LoginWindow extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel LoginFrame;
    private JTextField UsernameInput;
    private JPasswordField PasswordInput;
    
    // Security: Login attempt tracking (prevents brute force)
    private int loginAttempts = 0;
    private static final int MAX_ATTEMPTS = 5;
    private long lockoutEndTime = 0;
    
    // Original dimensions for scaling
    private static final int ORIGINAL_WIDTH = 734;
    private static final int ORIGINAL_HEIGHT = 470;
    private static final int PANEL_LEFT_WIDTH = 282;
    private static final int PANEL_RIGHT_WIDTH = 322;
    private static final int PANEL_HEIGHT = 361;

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                LoginWindow frame = new LoginWindow();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public LoginWindow() {
        setForeground(new Color(0, 0, 0));
        setTitle("Lost And Found");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(ORIGINAL_WIDTH, ORIGINAL_HEIGHT);
        setMinimumSize(new Dimension(550, 400));
        setLocationRelativeTo(null);
        
        LoginFrame = new JPanel();
        LoginFrame.setBackground(new Color(64, 0, 0));
        LoginFrame.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(LoginFrame);
        LoginFrame.setLayout(null);
        
        // Left Panel (Image)
        JPanel panel_1 = RoundedComponents.createRoundedPanelWithBackground(
            "/image/Log in Page.png",
            Color.BLACK,
            1,
            15,
            RoundedComponents.LEFT_CORNERS
        );
        panel_1.setLayout(null);
        LoginFrame.add(panel_1);
        
        // Right Panel (Login Form)
        JPanel panel = RoundedComponents.createRoundedPanel(
            Color.WHITE,
            Color.BLACK,
            1,
            15,
            RoundedComponents.RIGHT_CORNERS
        );
        panel.setLayout(null);
        LoginFrame.add(panel);
        
        // Username Icon
        JLabel lblNewLabel = new JLabel();
        lblNewLabel.setBackground(new Color(255, 255, 255));
        try {
            ImageIcon originalIcon = new ImageIcon(LoginWindow.class.getResource("/image/UsernameLogo.png"));
            Image scaledImage = originalIcon.getImage().getScaledInstance(25, 25, Image.SCALE_SMOOTH);
            lblNewLabel.setIcon(new ImageIcon(scaledImage));
        } catch (Exception e) {
            e.printStackTrace();
            lblNewLabel.setText("👤");
        }
        panel.add(lblNewLabel);
        
        // Password Icon
        JLabel lblNewLabel_1 = new JLabel();
        lblNewLabel_1.setHorizontalAlignment(SwingConstants.CENTER);
        lblNewLabel_1.setBackground(new Color(255, 255, 255));
        try {
            ImageIcon originalIcon = new ImageIcon(LoginWindow.class.getResource("/image/Password Logo.png"));
            Image scaledImage = originalIcon.getImage().getScaledInstance(25, 25, Image.SCALE_SMOOTH);
            lblNewLabel_1.setIcon(new ImageIcon(scaledImage));
        } catch (Exception e) {
            e.printStackTrace();
            lblNewLabel_1.setText("🔒");
        }
        panel.add(lblNewLabel_1);

        // Password Input
        PasswordInput = RoundedComponents.createRoundedPasswordField(
            Color.WHITE,
            Color.BLACK,
            Color.BLACK,
            10,
            "Password"
        );
        PasswordInput.setFont(new Font("Tahoma", Font.PLAIN, 18));
        panel.add(PasswordInput);
        
        // Login Button
        JButton Loginbtn = RoundedComponents.createRoundedButton(
            "Log In",
            new Color(122, 14, 26),
            Color.WHITE,
            Color.BLACK,
            7,
            RoundedComponents.ALL_CORNERS
        );
        Loginbtn.setFont(new Font("Tahoma", Font.BOLD, 16));
        Loginbtn.addActionListener(e -> handleLogin());
        panel.add(Loginbtn);
        
        // Username Input
        UsernameInput = RoundedComponents.createRoundedTextField(
            Color.WHITE,
            Color.BLACK,
            Color.BLACK,
            10,
            "Username"
        );
        UsernameInput.setFont(new Font("Tahoma", Font.PLAIN, 18));
        panel.add(UsernameInput);
        
        // Login Title
        JLabel Login = new JLabel("Log In");
        Login.setBackground(new Color(255, 255, 255));
        Login.setForeground(new Color(125, 0, 0));
        Login.setHorizontalAlignment(SwingConstants.CENTER);
        Login.setFont(new Font("Tahoma", Font.BOLD, 26));
        panel.add(Login);
        
        // Sign Up Button
        JButton CreateAccbtn = RoundedComponents.createRoundedButton(
            "Sign Up",
            new Color(0, 0, 0),
            Color.WHITE,
            Color.BLACK,
            7,
            RoundedComponents.ALL_CORNERS
        );
        CreateAccbtn.setFont(new Font("Tahoma", Font.BOLD, 16));
        CreateAccbtn.addActionListener(e -> {
            SignupFrame signup = new SignupFrame();
            signup.setVisible(true);
            LoginWindow.this.dispose();
        });
        panel.add(CreateAccbtn);
        
        // Eye Toggle Button
        JButton eyeToggle = RoundedComponents.createEyeToggleButton(Color.WHITE, 24);
        panel.add(eyeToggle);

        eyeToggle.addActionListener(e -> {
            eyeToggle.setSelected(!eyeToggle.isSelected());
            if(eyeToggle.isSelected()) {
                PasswordInput.setEchoChar((char) 0);
            } else {
                PasswordInput.setEchoChar('•');
            }
        });
        
        // Add component listener for dynamic resizing
        LoginFrame.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                scaleComponents(panel_1, panel, lblNewLabel, lblNewLabel_1, UsernameInput, 
                               PasswordInput, Loginbtn, CreateAccbtn, Login, eyeToggle);
            }
        });
        
        // Initial scaling
        scaleComponents(panel_1, panel, lblNewLabel, lblNewLabel_1, UsernameInput, 
                       PasswordInput, Loginbtn, CreateAccbtn, Login, eyeToggle);
        
        panel.setFocusable(true);
        panel.requestFocusInWindow();
    }
    
    private void scaleComponents(JPanel panel_1, JPanel panel, JLabel lblNewLabel, 
                                 JLabel lblNewLabel_1, JTextField UsernameInput,
                                 JPasswordField PasswordInput, JButton Loginbtn, 
                                 JButton CreateAccbtn, JLabel Login, JButton eyeToggle) {
        
        // Get current window size
        int currentWidth = LoginFrame.getWidth();
        int currentHeight = LoginFrame.getHeight();
        
        // Calculate scale factors
        double scaleX = (double) currentWidth / ORIGINAL_WIDTH;
        double scaleY = (double) currentHeight / ORIGINAL_HEIGHT;
        double scale = Math.min(scaleX, scaleY); // Use uniform scaling
        
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
        int s = (int) (scale * 100); // Convert to percentage for easier calculation
        
        // Username icon
        lblNewLabel.setBounds(
            scale(34, scale), scale(126, scale), 
            scale(25, scale), scale(25, scale)
        );
        scaleIcon(lblNewLabel, "/image/UsernameLogo.png", scale(25, scale));
        
        // Password icon
        lblNewLabel_1.setBounds(
            scale(34, scale), scale(193, scale), 
            scale(25, scale), scale(25, scale)
        );
        scaleIcon(lblNewLabel_1, "/image/Password Logo.png", scale(25, scale));
        
        // Password input
        PasswordInput.setBounds(
            scale(61, scale), scale(191, scale), 
            scale(227, scale), scale(30, scale)
        );
        PasswordInput.setFont(new Font("Tahoma", Font.PLAIN, scale(18, scale)));
        
        // Login button
        Loginbtn.setBounds(
            scale(55, scale), scale(245, scale), 
            scale(100, scale), scale(28, scale)
        );
        Loginbtn.setFont(new Font("Tahoma", Font.BOLD, scale(16, scale)));
        
        // Username input
        UsernameInput.setBounds(
            scale(61, scale), scale(124, scale), 
            scale(227, scale), scale(30, scale)
        );
        UsernameInput.setFont(new Font("Tahoma", Font.PLAIN, scale(18, scale)));
        
        // Login title
        Login.setBounds(
            scale(109, scale), scale(51, scale), 
            scale(104, scale), scale(39, scale)
        );
        Login.setFont(new Font("Tahoma", Font.BOLD, scale(26, scale)));
        
        // Sign up button
        CreateAccbtn.setBounds(
            scale(174, scale), scale(245, scale), 
            scale(100, scale), scale(28, scale)
        );
        CreateAccbtn.setFont(new Font("Tahoma", Font.BOLD, scale(16, scale)));
        
        // Eye toggle
        eyeToggle.setBounds(
            scale(290, scale), scale(192, scale), 
            scale(24, scale), scale(26, scale)
        );
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
    
    private void handleLogin() {
        // Check if account is locked due to too many failed attempts
        if (System.currentTimeMillis() < lockoutEndTime) {
            long remainingSeconds = (lockoutEndTime - System.currentTimeMillis()) / 1000;
            JOptionPane.showMessageDialog(null, 
                "⚠️ Too many failed login attempts!\n" +
                "Please wait " + remainingSeconds + " seconds before trying again.",
                "Account Temporarily Locked", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        String username = UsernameInput.getText().trim();
        String password = new String(PasswordInput.getPassword()).trim();

        // Basic validation
        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(null, 
                "Please enter both username and password", 
                "Login Error", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        try (Connection conn = MySQLConnection.connect()) {
            String sql = "SELECT password, role, account_status, ban_reason, suspension_end_date FROM users WHERE username = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, username);

            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                String storedPassword = rs.getString("password");
                String role = rs.getString("role");
                String accountStatus = rs.getString("account_status");
                String banReason = rs.getString("ban_reason");
                String suspensionEndDate = rs.getString("suspension_end_date");

                // 1. Check if account is banned
                if ("banned".equalsIgnoreCase(accountStatus)) {
                    JOptionPane.showMessageDialog(null, 
                        "❌ Your account has been permanently banned.\n\n" +
                        "Reason: " + (banReason != null ? banReason : "Violation of terms"),
                        "Account Banned", 
                        JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // 2. Check if account is suspended
                if ("suspended".equalsIgnoreCase(accountStatus)) {
                    if (suspensionEndDate != null) {
                        try {
                            LocalDateTime suspensionEnd = LocalDateTime.parse(
                                suspensionEndDate, 
                                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                            );
                            
                            if (LocalDateTime.now().isBefore(suspensionEnd)) {
                                JOptionPane.showMessageDialog(null, 
                                    "⏸️ Your account is temporarily suspended.\n\n" +
                                    "Reason: " + (banReason != null ? banReason : "Violation of terms") + "\n" +
                                    "Suspension ends: " + suspensionEndDate,
                                    "Account Suspended", 
                                    JOptionPane.WARNING_MESSAGE);
                                return;
                            } else {
                                String updateSql = "UPDATE users SET account_status = 'active', ban_reason = NULL, suspension_end_date = NULL WHERE username = ?";
                                PreparedStatement updateStmt = conn.prepareStatement(updateSql);
                                updateStmt.setString(1, username);
                                updateStmt.executeUpdate();
                                System.out.println("✅ Suspension expired, account reactivated: " + username);
                            }
                        } catch (Exception ex) {
                            System.err.println("Error parsing suspension date: " + ex.getMessage());
                        }
                    }
                }

                // 3. Verify password
                boolean isPasswordCorrect = false;
                
                if (storedPassword.contains(":")) {
                    isPasswordCorrect = PasswordHasher.verifyPassword(password, storedPassword);
                } else {
                    isPasswordCorrect = storedPassword.equals(password);
                    
                    if (isPasswordCorrect) {
                        String hashedPassword = PasswordHasher.hashPassword(password);
                        String updateSql = "UPDATE users SET password = ? WHERE username = ?";
                        PreparedStatement updateStmt = conn.prepareStatement(updateSql);
                        updateStmt.setString(1, hashedPassword);
                        updateStmt.setString(2, username);
                        updateStmt.executeUpdate();
                        System.out.println("✅ Password migrated to secure hash for: " + username);
                    }
                }

                if (isPasswordCorrect) {
                    loginAttempts = 0;
                    lockoutEndTime = 0;
                    
                    System.out.println("✅ LOGIN SUCCESS: " + username + " (Role: " + role + ")");

                    if (role.equalsIgnoreCase("admin")) {
                        AdminDashboardWindow adminDashboard = new AdminDashboardWindow(username);
                        adminDashboard.setVisible(true);
                    } else {
                        DashboardWindow dashboard = new DashboardWindow(username);
                        dashboard.setVisible(true);
                    }

                    LoginWindow.this.dispose();
                } else {
                    handleFailedLogin();
                }
            } else {
                handleFailedLogin();
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, 
                "Database Error: " + ex.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleFailedLogin() {
        loginAttempts++;
        int remainingAttempts = MAX_ATTEMPTS - loginAttempts;

        if (loginAttempts >= MAX_ATTEMPTS) {
            lockoutEndTime = System.currentTimeMillis() + (5 * 60 * 1000);
            JOptionPane.showMessageDialog(null, 
                "❌ Too many failed login attempts!\n" +
                "Your account is temporarily locked for 5 minutes.\n\n" +
                "This is for your security.",
                "Account Locked", 
                JOptionPane.ERROR_MESSAGE);
            
            UsernameInput.setText("");
            PasswordInput.setText("");
        } else {
            JOptionPane.showMessageDialog(null, 
                "❌ Invalid username or password\n\n" +
                "Remaining attempts: " + remainingAttempts,
                "Login Error", 
                JOptionPane.ERROR_MESSAGE);
            
            PasswordInput.setText("");
            PasswordInput.requestFocus();
        }
        
        System.out.println("⚠️ FAILED LOGIN ATTEMPT " + loginAttempts + "/" + MAX_ATTEMPTS);
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