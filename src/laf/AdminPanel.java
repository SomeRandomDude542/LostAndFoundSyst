package laf;

import java.awt.EventQueue;
import javax.swing.JFrame;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;
import java.awt.Component;
import javax.swing.border.EmptyBorder;
import java.awt.Font;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.awt.Color;
import javax.swing.JTabbedPane;
import java.awt.event.*;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.BoxLayout;
import java.awt.Dimension;
import javax.swing.BorderFactory;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.awt.Frame;
import javax.swing.JDialog;
import javax.swing.JTextField;
import javax.swing.JOptionPane;
import javax.swing.JComboBox;
import javax.swing.JTextArea;
import javax.swing.JPopupMenu;
import javax.swing.JMenuItem;
import java.io.File;
import javax.swing.ImageIcon;
import java.awt.Image;
import java.awt.BorderLayout;
import java.awt.Insets;

public class AdminPanel extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    
    // Original dimensions for scaling (doubled from original 548x358)
    private static final int ORIGINAL_WIDTH = 1096;
    private static final int ORIGINAL_HEIGHT = 716;
    private static final int SIDE_PANEL_WIDTH = 284;

    // Store components for scaling
    private JPanel sidePanel;
    private JTabbedPane tabbedPane;
    private JLabel lblAdminLabel;
    private JButton btnUsers, btnReports, btnViewLogs, btnReturn;
    private JPanel usersPanel, reportsPanel, logsPanel;

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                AdminPanel frame = new AdminPanel();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public AdminPanel() {
        setFont(new Font("Dialog", Font.BOLD, 24));
        setTitle("Admin Panel");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(ORIGINAL_WIDTH, ORIGINAL_HEIGHT);
        setMinimumSize(new Dimension(800, 500));
        setLocationRelativeTo(null);

        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(10, 10, 10, 10));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        // Side Panel
        sidePanel = new JPanel();
        sidePanel.setLayout(null);
        sidePanel.setBackground(new Color(90, 14, 36));
        contentPane.add(sidePanel);

        // Admin Label
        lblAdminLabel = new JLabel("Admin Panel");
        lblAdminLabel.setForeground(new Color(255, 255, 255));
        lblAdminLabel.setBackground(new Color(90, 14, 36));
        lblAdminLabel.setHorizontalAlignment(SwingConstants.CENTER);
        lblAdminLabel.setFont(new Font("Tahoma", Font.BOLD, 40));
        sidePanel.add(lblAdminLabel);

        // TAB PANE
        tabbedPane = new JTabbedPane(JTabbedPane.TOP);
        tabbedPane.setFont(new Font("Tahoma", Font.PLAIN, 22));
        contentPane.add(tabbedPane);

        // TAB 1 - USERS 
        usersPanel = createViewUsersPanel();  
        tabbedPane.addTab("Users", usersPanel);

        // TAB 2 - REPORTS 
        reportsPanel = createReportsPanel();  
        tabbedPane.addTab("Reports", reportsPanel);      

        // TAB 3 - LOGS 
        logsPanel = createViewLogsPanel();  
        tabbedPane.addTab("Logs", logsPanel);

        // SIDE NAVIGATION BUTTONS
        btnUsers = RoundedComponents.createRoundedButtonWithHover(
            "View Users",
            new Color(90, 14, 36),
            new Color(255, 200, 200),
            Color.WHITE,
            2,
            RoundedComponents.ALL_CORNERS
        );
        btnUsers.setFont(new Font("Tahoma", Font.BOLD, 30));
        btnUsers.addActionListener(e -> tabbedPane.setSelectedIndex(0));
        sidePanel.add(btnUsers);

        btnReports = RoundedComponents.createRoundedButtonWithHover(
            "View Reports",
            new Color(90, 14, 36),
            new Color(255, 200, 200),
            Color.WHITE,
            2,
            RoundedComponents.ALL_CORNERS
        );
        btnReports.setFont(new Font("Tahoma", Font.BOLD, 30));
        btnReports.addActionListener(e -> tabbedPane.setSelectedIndex(1));
        sidePanel.add(btnReports);

        btnViewLogs = RoundedComponents.createRoundedButtonWithHover(
            "View Logs",
            new Color(90, 14, 36),
            new Color(255, 200, 200),
            Color.WHITE,
            2,
            RoundedComponents.ALL_CORNERS
        );
        btnViewLogs.setFont(new Font("Tahoma", Font.BOLD, 30));
        btnViewLogs.addActionListener(e -> tabbedPane.setSelectedIndex(2));
        sidePanel.add(btnViewLogs);

        btnReturn = RoundedComponents.createRoundedButtonWithHover(
            "Return",
            new Color(90, 14, 36),
            new Color(255, 200, 200),
            new Color(0, 123, 255),
            2,
            RoundedComponents.ALL_CORNERS
        );
        btnReturn.setFont(new Font("Tahoma", Font.BOLD, 28));
        btnReturn.addActionListener(e -> {
            new AdminDashboardWindow("admin").setVisible(true);
            dispose();
        });
        sidePanel.add(btnReturn);

        // Add component listener for dynamic resizing
        contentPane.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                scaleComponents();
            }
        });

        // Initial scaling
        scaleComponents();
    }

    private void scaleComponents() {
        int currentWidth = contentPane.getWidth();
        int currentHeight = contentPane.getHeight();

        double scaleX = (double) currentWidth / ORIGINAL_WIDTH;
        double scaleY = (double) currentHeight / ORIGINAL_HEIGHT;
        double scale = Math.min(scaleX, scaleY);

        // Scale side panel
        int sidePanelWidth = (int) (SIDE_PANEL_WIDTH * scale);
        sidePanel.setBounds(0, 0, sidePanelWidth, currentHeight);

        // Scale admin label
        lblAdminLabel.setBounds(
            0, scale(22, scale),
            sidePanelWidth, scale(60, scale)
        );
        lblAdminLabel.setFont(new Font("Tahoma", Font.BOLD, scale(40, scale)));

        // Scale side buttons
        btnUsers.setBounds(
            0, scale(160, scale),
            scale(290, scale), scale(60, scale)
        );
        btnUsers.setFont(new Font("Tahoma", Font.BOLD, scale(30, scale)));

        btnReports.setBounds(
            0, scale(300, scale),
            scale(290, scale), scale(60, scale)
        );
        btnReports.setFont(new Font("Tahoma", Font.BOLD, scale(30, scale)));

        btnViewLogs.setBounds(
            0, scale(440, scale),
            scale(290, scale), scale(60, scale)
        );
        btnViewLogs.setFont(new Font("Tahoma", Font.BOLD, scale(30, scale)));

        btnReturn.setBounds(
            0, scale(576, scale),
            scale(290, scale), scale(62, scale)
        );
        btnReturn.setFont(new Font("Tahoma", Font.BOLD, scale(28, scale)));

        // Scale tabbed pane
        tabbedPane.setBounds(
            scale(286, scale), scale(-50, scale),
            currentWidth - sidePanelWidth, 
            currentHeight + scale(50, scale)
        );
        tabbedPane.setFont(new Font("Tahoma", Font.PLAIN, scale(22, scale)));
        
        // NEW: Scale components inside each tab panel
        scaleUsersPanel(scale);
        scaleReportsPanel(scale);
        scaleLogsPanel(scale);
    }
    
    private int scale(int original, double scaleFactor) {
        return (int) (original * scaleFactor);
    }

    private void scaleUsersPanel(double scale) {
        Component[] components = usersPanel.getComponents();
        
        for (Component comp : components) {
            if (comp instanceof JLabel) {
                JLabel lbl = (JLabel) comp;
                if (lbl.getText().equals("User Management")) {
                    lbl.setBounds(scale(200, scale), scale(20, scale), 
                                scale(400, scale), scale(60, scale));
                    lbl.setFont(new Font("Tahoma", Font.BOLD, scale(40, scale)));
                }
            } else if (comp instanceof JScrollPane) {
                comp.setBounds(scale(20, scale), scale(100, scale), 
                             scale(738, scale), scale(480, scale));
            } else if (comp instanceof JButton) {
                JButton btn = (JButton) comp;
                if (btn.getText().equals("Refresh")) {
                    btn.setBounds(scale(20, scale), scale(590, scale), 
                                scale(200, scale), scale(50, scale));
                    btn.setFont(new Font("Tahoma", Font.BOLD, scale(24, scale)));
                }
            }
        }
    }

    private void scaleReportsPanel(double scale) {
        Component[] components = reportsPanel.getComponents();
        
        for (Component comp : components) {
            if (comp instanceof JLabel) {
                JLabel lbl = (JLabel) comp;
                if (lbl.getText().equals("Reports & Items Management")) {
                    lbl.setBounds(scale(200, scale), scale(20, scale), 
                                scale(500, scale), scale(60, scale));
                    lbl.setFont(new Font("Tahoma", Font.BOLD, scale(32, scale)));
                }
            } else if (comp instanceof JScrollPane) {
                comp.setBounds(scale(20, scale), scale(150, scale), 
                             scale(738, scale), scale(480, scale));
            } else if (comp instanceof JButton) {
                JButton btn = (JButton) comp;
                String text = btn.getText();
                
                if (text.equals("All")) {
                    btn.setBounds(scale(20, scale), scale(90, scale), 
                                scale(130, scale), scale(50, scale));
                    btn.setFont(new Font("Tahoma", Font.BOLD, scale(24, scale)));
                } else if (text.equals("Lost")) {
                    btn.setBounds(scale(160, scale), scale(90, scale), 
                                scale(140, scale), scale(50, scale));
                    btn.setFont(new Font("Tahoma", Font.BOLD, scale(24, scale)));
                } else if (text.equals("Found")) {
                    btn.setBounds(scale(310, scale), scale(90, scale), 
                                scale(160, scale), scale(50, scale));
                    btn.setFont(new Font("Tahoma", Font.BOLD, scale(24, scale)));
                } else if (text.equals("")) {
                    // Bin button
                    if (btn.getBounds().x > 400) {
                        btn.setBounds(scale(480, scale), scale(90, scale), 
                                    scale(120, scale), scale(50, scale));
                    } else {
                        // Refresh button
                        btn.setBounds(scale(610, scale), scale(90, scale), 
                                    scale(110, scale), scale(50, scale));
                    }
                }
            }
        }
    }

    private void scaleLogsPanel(double scale) {
        Component[] components = logsPanel.getComponents();
        
        for (Component comp : components) {
            if (comp instanceof JLabel) {
                JLabel lbl = (JLabel) comp;
                String text = lbl.getText();
                
                if (text.equals("Activity Logs")) {
                    lbl.setBounds(scale(240, scale), scale(20, scale), 
                                scale(300, scale), scale(60, scale));
                    lbl.setFont(new Font("Tahoma", Font.BOLD, scale(40, scale)));
                } else if (text.equals("Online Users")) {
                    lbl.setBounds(scale(20, scale), scale(100, scale), 
                                scale(240, scale), scale(40, scale));
                    lbl.setFont(new Font("Tahoma", Font.BOLD, scale(28, scale)));
                } else if (text.equals("System Statistics")) {
                    lbl.setBounds(scale(400, scale), scale(100, scale), 
                                scale(300, scale), scale(40, scale));
                    lbl.setFont(new Font("Tahoma", Font.BOLD, scale(28, scale)));
                } else if (text.equals("Recent Activity")) {
                    lbl.setBounds(scale(20, scale), scale(390, scale), 
                                scale(300, scale), scale(40, scale));
                    lbl.setFont(new Font("Tahoma", Font.BOLD, scale(28, scale)));
                }
            } else if (comp instanceof JScrollPane) {
                JScrollPane scroll = (JScrollPane) comp;
                // Online users scroll
                if (scroll.getBounds().x < 200) {
                    scroll.setBounds(scale(20, scale), scale(150, scale), 
                                   scale(360, scale), scale(220, scale));
                } else {
                    // Activity scroll
                    scroll.setBounds(scale(20, scale), scale(440, scale), 
                                   scale(720, scale), scale(190, scale));
                }
            } else if (comp instanceof JPanel) {
                // Stats panel
                JPanel panel = (JPanel) comp;
                if (panel.getBounds().x > 200) {
                    panel.setBounds(scale(400, scale), scale(150, scale), 
                                  scale(340, scale), scale(220, scale));
                }
            } else if (comp instanceof JButton) {
                JButton btn = (JButton) comp;
                if (btn.getText().equals("Refresh")) {
                    btn.setBounds(scale(560, scale), scale(380, scale), 
                                scale(180, scale), scale(50, scale));
                    btn.setFont(new Font("Tahoma", Font.BOLD, scale(24, scale)));
                }
            }
        }
    }

    private JPanel createViewUsersPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBackground(Color.WHITE);

        JLabel lblTitle = new JLabel("User Management");
        lblTitle.setFont(new Font("Tahoma", Font.BOLD, 40));
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(lblTitle);

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);  
        scrollPane.getVerticalScrollBar().setBlockIncrement(50);
        panel.add(scrollPane);

        JPanel usersListPanel = new JPanel();
        usersListPanel.setLayout(new BoxLayout(usersListPanel, BoxLayout.Y_AXIS));
        usersListPanel.setBackground(Color.WHITE);
        scrollPane.setViewportView(usersListPanel);

        loadAllUsers(usersListPanel);

        JButton btnRefresh = RoundedComponents.createRoundedButton(
            "Refresh",
            new Color(90, 14, 36),
            Color.WHITE,
            Color.BLACK,
            20,
            RoundedComponents.ALL_CORNERS
        );
        btnRefresh.setFont(new Font("Tahoma", Font.BOLD, 24));  
        btnRefresh.addActionListener(e -> loadAllUsers(usersListPanel));
        panel.add(btnRefresh);

        // Add component listener to center content when panel resizes
        panel.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                int panelWidth = panel.getWidth();
                int panelHeight = panel.getHeight();
                
                // Increase content width to 85% of panel width
                int contentWidth = (int)(panelWidth * 0.85);
                int contentHeight = panelHeight - 200; // Leave space for title and button
                int centerX = (panelWidth - contentWidth) / 2;
                
                // Center title
                lblTitle.setBounds(0, 20, panelWidth, 60);
                
                // Center scroll pane with larger size
                scrollPane.setBounds(centerX, 100, contentWidth, contentHeight);
                
                // Center refresh button
                btnRefresh.setBounds(centerX, panelHeight - 80, 200, 50);
            }
        });

        return panel;
    }

    private void loadAllUsers(JPanel containerPanel) {
        containerPanel.removeAll();

        try (Connection conn = MySQLConnection.connect()) {
            String sql = "SELECT id, username, email, role, account_status FROM users ORDER BY id DESC";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            java.sql.ResultSet rs = pstmt.executeQuery();

            boolean hasUsers = false;
            while (rs.next()) {
                hasUsers = true;
                int userId = rs.getInt("id");
                String username = rs.getString("username");
                String email = rs.getString("email");
                String role = rs.getString("role");
                String status = rs.getString("account_status");

                JPanel userPanel = new JPanel();
                userPanel.setLayout(null);
                userPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));
                userPanel.setPreferredSize(new Dimension(680, 100));
                userPanel.setBackground(Color.WHITE);
                userPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY));

                JLabel lblUsername = new JLabel(username);
                lblUsername.setFont(new Font("Tahoma", Font.BOLD, 24));
                userPanel.add(lblUsername);

                JLabel lblEmail = new JLabel(email);
                lblEmail.setFont(new Font("Tahoma", Font.PLAIN, 20));
                userPanel.add(lblEmail);

                JLabel lblRole = RoundedComponents.createRoundedLabel(
                    role.toUpperCase(),
                    Color.WHITE,
                    Color.WHITE,
                    20
                );
                lblRole.setFont(new Font("Tahoma", Font.BOLD, 18));

                if (role.equals("admin")) {
                    lblRole.setBackground(new Color(220, 53, 69));
                    lblRole.setForeground(Color.WHITE);
                } else {
                    lblRole.setBackground(new Color(40, 167, 69));
                    lblRole.setForeground(Color.WHITE);
                }
                userPanel.add(lblRole);

                JLabel lblStatus = new JLabel("●");
                lblStatus.setFont(new Font("Tahoma", Font.BOLD, 32));
                switch (status) {
                    case "active":
                        lblStatus.setForeground(new Color(40, 167, 69));
                        lblStatus.setToolTipText("Active");
                        break;
                    case "suspended":
                        lblStatus.setForeground(new Color(255, 193, 7));
                        lblStatus.setToolTipText("Suspended");
                        break;
                    case "banned":
                        lblStatus.setForeground(new Color(220, 53, 69));
                        lblStatus.setToolTipText("Banned");
                        break;
                }
                userPanel.add(lblStatus);

                JButton btnActions = RoundedComponents.createRoundedButton(
                    "Actions ▼",
                    new Color(90, 14, 36),
                    Color.WHITE,
                    Color.BLACK,
                    20,
                    RoundedComponents.ALL_CORNERS
                );
                btnActions.setFont(new Font("Tahoma", Font.BOLD, 20));  
                
                btnActions.addActionListener(e -> {
                    showUserActionsMenu(btnActions, userId, username, email, role, status, containerPanel);
                });
                userPanel.add(btnActions);

                // Add component listener to make the user panel responsive
                userPanel.addComponentListener(new ComponentAdapter() {
                    @Override
                    public void componentResized(ComponentEvent e) {
                        int panelWidth = userPanel.getWidth();
                        
                        // Position components responsively
                        lblUsername.setBounds(20, 10, (int)(panelWidth * 0.35), 40);
                        lblEmail.setBounds(20, 50, (int)(panelWidth * 0.4), 30);
                        
                        // Role badge - positioned relative to panel width
                        int roleBadgeX = (int)(panelWidth * 0.45);
                        lblRole.setBounds(roleBadgeX, 32, 100, 36);
                        
                        // Status dot
                        int statusX = (int)(panelWidth * 0.60);
                        lblStatus.setBounds(statusX, 26, 40, 40);
                        
                        // Actions button - always on the right
                        int buttonX = panelWidth - 190;
                        btnActions.setBounds(buttonX, 24, 170, 50);
                    }
                });

                containerPanel.add(userPanel);
            }

            if (!hasUsers) {
                JLabel lblNoUsers = new JLabel("No users found");
                lblNoUsers.setFont(new Font("Tahoma", Font.ITALIC, 24));
                lblNoUsers.setForeground(Color.GRAY);
                containerPanel.add(lblNoUsers);
            }

        } catch (Exception ex) {
            System.err.println("Error loading users: " + ex.getMessage());
            ex.printStackTrace();
        }

        containerPanel.revalidate();
        containerPanel.repaint();
    }
    
    private void showUserActionsMenu(JButton sourceButton, int userId, String username, 
                                      String email, String role, String status, JPanel containerPanel) {
        JPopupMenu popup = new JPopupMenu();

        JMenuItem itemBan = new JMenuItem("Ban User");
        itemBan.setFont(new Font("Tahoma", Font.PLAIN, 24));
        itemBan.addActionListener(e -> showBanUserDialog(userId, username, containerPanel));
        popup.add(itemBan);

        JMenuItem itemSuspend = new JMenuItem("Suspend User");
        itemSuspend.setFont(new Font("Tahoma", Font.PLAIN, 24));
        itemSuspend.addActionListener(e -> showSuspendUserDialog(userId, username, containerPanel));
        popup.add(itemSuspend);

        if (!status.equals("active")) {
            JMenuItem itemRestore = new JMenuItem("Restore Account");
            itemRestore.setFont(new Font("Tahoma", Font.PLAIN, 24));
            itemRestore.addActionListener(e -> restoreUserAccount(userId, username, containerPanel));
            popup.add(itemRestore);
        }

        popup.addSeparator();

        JMenuItem itemChangeRole = new JMenuItem("Change Role");
        itemChangeRole.setFont(new Font("Tahoma", Font.PLAIN, 24));
        itemChangeRole.addActionListener(e -> showChangeRoleDialog(userId, username, role, containerPanel));
        popup.add(itemChangeRole);

        popup.addSeparator();

        JMenuItem itemDetails = new JMenuItem("View Details");
        itemDetails.setFont(new Font("Tahoma", Font.PLAIN, 24));
        itemDetails.addActionListener(e -> showUserDetailsDialog(userId, username, email, role, status));
        popup.add(itemDetails);

        popup.show(sourceButton, 0, sourceButton.getHeight());
    }

    // Dialog methods kept at original size (not scaled)
    private void showBanUserDialog(int userId, String username, JPanel containerPanel) {
        JDialog dialog = new JDialog((Frame) null, "Ban User", true);
        dialog.setSize(350, 200);
        dialog.setLocationRelativeTo(null);
        dialog.getContentPane().setLayout(null);

        JLabel lblWarning = new JLabel("<html><b>Ban user: " + username + "?</b></html>");
        lblWarning.setFont(new Font("Tahoma", Font.PLAIN, 14));
        lblWarning.setBounds(20, 20, 300, 25);
        dialog.getContentPane().add(lblWarning);

        JLabel lblReason = new JLabel("Reason:");
        lblReason.setFont(new Font("Tahoma", Font.PLAIN, 12));
        lblReason.setBounds(20, 55, 100, 20);
        dialog.getContentPane().add(lblReason);

        JTextField txtReason = RoundedComponents.createRoundedTextField(
            Color.WHITE, Color.BLACK, Color.BLACK, 10
        );
        txtReason.setBounds(20, 75, 300, 25);
        txtReason.setFont(new Font("Tahoma", Font.PLAIN, 12));
        dialog.getContentPane().add(txtReason);

        JButton btnConfirm = RoundedComponents.createRoundedButton(
            "Confirm Ban", new Color(220, 53, 69), Color.BLACK, Color.BLACK, 10, RoundedComponents.ALL_CORNERS
        );
        btnConfirm.setFont(new Font("Tahoma", Font.BOLD, 12));  
        btnConfirm.setBounds(20, 115, 140, 30);
        btnConfirm.addActionListener(e -> {
            String reason = txtReason.getText().trim();
            if (reason.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Please provide a reason", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            banUser(userId, username, reason, containerPanel);
            dialog.dispose();
        });
        dialog.getContentPane().add(btnConfirm);

        JButton btnCancel = RoundedComponents.createRoundedButton(
            "Cancel", new Color(0, 0, 0), Color.WHITE, Color.BLACK, 10, RoundedComponents.ALL_CORNERS
        );
        btnCancel.setFont(new Font("Tahoma", Font.BOLD, 12));  
        btnCancel.setBounds(180, 115, 140, 30);
        btnCancel.addActionListener(e -> dialog.dispose());
        dialog.getContentPane().add(btnCancel);

        dialog.setVisible(true);
    }

    private void showSuspendUserDialog(int userId, String username, JPanel containerPanel) {
        JDialog dialog = new JDialog((Frame) null, "Suspend User", true);
        dialog.setSize(350, 220);
        dialog.setLocationRelativeTo(null);
        dialog.getContentPane().setLayout(null);

        JLabel lblWarning = new JLabel("<html><b>Suspend user: " + username + "?</b></html>");
        lblWarning.setFont(new Font("Tahoma", Font.PLAIN, 14));
        lblWarning.setBounds(20, 20, 300, 25);
        dialog.getContentPane().add(lblWarning);

        JLabel lblDays = new JLabel("Suspension Days:");
        lblDays.setFont(new Font("Tahoma", Font.PLAIN, 12));
        lblDays.setBounds(20, 55, 120, 20);
        dialog.getContentPane().add(lblDays);

        JComboBox<String> comboDays = new JComboBox<>(new String[]{"1", "3", "7", "14", "30"});
        comboDays.setBounds(140, 55, 80, 25);
        dialog.getContentPane().add(comboDays);

        JLabel lblReason = new JLabel("Reason:");
        lblReason.setFont(new Font("Tahoma", Font.PLAIN, 12));
        lblReason.setBounds(20, 80, 100, 20);
        dialog.getContentPane().add(lblReason);

        JTextField txtReason = RoundedComponents.createRoundedTextField(
            Color.WHITE, Color.BLACK, Color.BLACK, 10
        );
        txtReason.setBounds(20, 100, 300, 25);
        txtReason.setFont(new Font("Tahoma", Font.PLAIN, 12));
        dialog.getContentPane().add(txtReason);

        JButton btnConfirm = RoundedComponents.createRoundedButton(
            "Confirm Suspend", new Color(255, 193, 7), Color.WHITE, Color.BLACK, 10, RoundedComponents.ALL_CORNERS
        );
        btnConfirm.setFont(new Font("Tahoma", Font.BOLD, 12));  
        btnConfirm.setBounds(20, 145, 140, 30);
        btnConfirm.addActionListener(e -> {
            String reason = txtReason.getText().trim();
            if (reason.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Please provide a reason", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            int days = Integer.parseInt((String) comboDays.getSelectedItem());
            suspendUser(userId, username, days, reason, containerPanel);
            dialog.dispose();
        });
        dialog.getContentPane().add(btnConfirm);

        JButton btnCancel = RoundedComponents.createRoundedButton(
            "Cancel", new Color(220, 53, 69), Color.WHITE, Color.BLACK, 10, RoundedComponents.ALL_CORNERS
        );
        btnCancel.setFont(new Font("Tahoma", Font.BOLD, 12));  
        btnCancel.setBounds(180, 145, 140, 30);
        btnCancel.addActionListener(e -> dialog.dispose());
        dialog.getContentPane().add(btnCancel);

        dialog.setVisible(true);
    }

    private void showChangeRoleDialog(int userId, String username, String currentRole, JPanel containerPanel) {
        JDialog dialog = new JDialog((Frame) null, "Change Role", true);
        dialog.setSize(350, 180);
        dialog.setLocationRelativeTo(null);
        dialog.getContentPane().setLayout(null);

        JLabel lblInfo = new JLabel("<html><b>Change role for: " + username + "</b></html>");
        lblInfo.setFont(new Font("Tahoma", Font.PLAIN, 14));
        lblInfo.setBounds(20, 20, 300, 25);
        dialog.getContentPane().add(lblInfo);

        JLabel lblRole = new JLabel("New Role:");
        lblRole.setFont(new Font("Tahoma", Font.PLAIN, 12));
        lblRole.setBounds(20, 60, 100, 20);
        dialog.getContentPane().add(lblRole);

        JComboBox<String> comboRole = new JComboBox<>(new String[]{"user", "admin"});
        comboRole.setSelectedItem(currentRole);
        comboRole.setBounds(120, 60, 100, 25);
        dialog.getContentPane().add(comboRole);

        JButton btnConfirm = RoundedComponents.createRoundedButton(
            "Confirm", new Color(0, 123, 255), Color.WHITE, Color.BLACK, 10, RoundedComponents.ALL_CORNERS
        );
        btnConfirm.setFont(new Font("Tahoma", Font.BOLD, 12));  
        btnConfirm.setBounds(20, 100, 140, 30);
        btnConfirm.addActionListener(e -> {
            String newRole = (String) comboRole.getSelectedItem();
            changeUserRole(userId, username, newRole, containerPanel);
            dialog.dispose();
        });
        dialog.getContentPane().add(btnConfirm);

        JButton btnCancel = RoundedComponents.createRoundedButton(
            "Cancel", new Color(220, 53, 69), Color.BLACK, Color.BLACK, 10, RoundedComponents.ALL_CORNERS
        );
        btnCancel.setFont(new Font("Tahoma", Font.BOLD, 12));  
        btnCancel.setBounds(180, 100, 140, 30);
        btnCancel.addActionListener(e -> dialog.dispose());
        dialog.getContentPane().add(btnCancel);

        dialog.setVisible(true);
    }
    
    private void showUserDetailsDialog(int userId, String username, String email, String role, String status) {
        JDialog dialog = new JDialog((Frame) null, "User Details", true);
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(null);
        dialog.getContentPane().setLayout(null);

        JLabel lblTitle = new JLabel("User Information");
        lblTitle.setFont(new Font("Tahoma", Font.BOLD, 18));
        lblTitle.setBounds(20, 20, 200, 25);
        lblTitle.setForeground(Color.BLACK);
        dialog.getContentPane().add(lblTitle);

        JTextArea textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        textArea.setBounds(20, 60, 350, 150);
        
        StringBuilder details = new StringBuilder();
        details.append("User ID: ").append(userId).append("\n\n");
        details.append("Username: ").append(username).append("\n\n");
        details.append("Email: ").append(email).append("\n\n");
        details.append("Role: ").append(role.toUpperCase()).append("\n\n");
        details.append("Status: ").append(status.toUpperCase()).append("\n\n");
        
        try (Connection conn = MySQLConnection.connect()) {
            String sql = "SELECT ban_reason, suspension_end_date FROM users WHERE id = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, userId);
            java.sql.ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                String banReason = rs.getString("ban_reason");
                String suspensionEnd = rs.getString("suspension_end_date");
                
                if (banReason != null && !banReason.isEmpty()) {
                    details.append("Ban Reason: ").append(banReason).append("\n\n");
                }
                if (suspensionEnd != null && !suspensionEnd.isEmpty()) {
                    details.append("Suspension Ends: ").append(suspensionEnd).append("\n");
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        
        textArea.setText(details.toString());
        dialog.getContentPane().add(textArea);

        JButton btnClose = RoundedComponents.createRoundedButton(
            "Close", new Color(128, 0, 0), Color.WHITE, Color.BLACK, 10, RoundedComponents.ALL_CORNERS
        );
        btnClose.setFont(new Font("Tahoma", Font.BOLD, 15));  
        btnClose.setBounds(140, 220, 100, 30);
        btnClose.addActionListener(e -> dialog.dispose());
        dialog.getContentPane().add(btnClose);

        dialog.setVisible(true);
    }

    // Database operations
    private void banUser(int userId, String username, String reason, JPanel containerPanel) {
        try (Connection conn = MySQLConnection.connect()) {
            String sql = "UPDATE users SET account_status = 'banned', ban_reason = ? WHERE id = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, reason);
            pstmt.setInt(2, userId);
            pstmt.executeUpdate();
            
            JOptionPane.showMessageDialog(null, username + " has been banned.", "Success", JOptionPane.INFORMATION_MESSAGE);
            loadAllUsers(containerPanel);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void suspendUser(int userId, String username, int days, String reason, JPanel containerPanel) {
        try (Connection conn = MySQLConnection.connect()) {
            java.time.LocalDateTime endDate = java.time.LocalDateTime.now().plusDays(days);
            String endDateStr = endDate.toString();
            
            String sql = "UPDATE users SET account_status = 'suspended', ban_reason = ?, suspension_end_date = ? WHERE id = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, reason);
            pstmt.setString(2, endDateStr);
            pstmt.setInt(3, userId);
            pstmt.executeUpdate();
            
            JOptionPane.showMessageDialog(null, username + " has been suspended for " + days + " days.", "Success", JOptionPane.INFORMATION_MESSAGE);
            loadAllUsers(containerPanel);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void restoreUserAccount(int userId, String username, JPanel containerPanel) {
        int confirm = JOptionPane.showConfirmDialog(null, 
            "Restore account for " + username + "?", 
            "Confirm Restore", 
            JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            try (Connection conn = MySQLConnection.connect()) {
                String sql = "UPDATE users SET account_status = 'active', ban_reason = NULL, suspension_end_date = NULL WHERE id = ?";
                PreparedStatement pstmt = conn.prepareStatement(sql);
                pstmt.setInt(1, userId);
                pstmt.executeUpdate();
                
                JOptionPane.showMessageDialog(null, username + "'s account has been restored.", "Success", JOptionPane.INFORMATION_MESSAGE);
                loadAllUsers(containerPanel);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        }
    }

    private void changeUserRole(int userId, String username, String newRole, JPanel containerPanel) {
        int confirm = JOptionPane.showConfirmDialog(null, 
            "Change " + username + "'s role to " + newRole + "?", 
            "Confirm Role Change", 
            JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            try (Connection conn = MySQLConnection.connect()) {
                String sql = "UPDATE users SET role = ? WHERE id = ?";
                PreparedStatement pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, newRole);
                pstmt.setInt(2, userId);
                pstmt.executeUpdate();
                
                JOptionPane.showMessageDialog(null, username + " is now a " + newRole + ".", "Success", JOptionPane.INFORMATION_MESSAGE);
                loadAllUsers(containerPanel);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        }
    }

    // VIEW REPORTS CODE - NO SCALING, uses static doubled sizes
    private JPanel createReportsPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBackground(Color.WHITE);

        JLabel lblTitle = new JLabel("Reports & Items Management");
        lblTitle.setFont(new Font("Tahoma", Font.BOLD, 32));
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(lblTitle);

        // Filter buttons
        JButton btnShowAll = RoundedComponents.createRoundedButton(
            "All",
            new Color(90, 14, 36),
            Color.WHITE,
            Color.BLACK,
            20,
            RoundedComponents.ALL_CORNERS
        );
        btnShowAll.setFont(new Font("Tahoma", Font.BOLD, 24));  
        panel.add(btnShowAll);

        JButton btnShowLost = RoundedComponents.createRoundedButton(
            "Lost",
            new Color(220, 53, 69),
            Color.WHITE,
            Color.BLACK,
            20,
            RoundedComponents.ALL_CORNERS
        );
        btnShowLost.setFont(new Font("Tahoma", Font.BOLD, 24));  
        panel.add(btnShowLost);
                
        JButton btnShowFound = RoundedComponents.createRoundedButton(
            "Found",
            new Color(50, 205, 50),
            Color.WHITE,
            Color.BLACK,
            20,
            RoundedComponents.ALL_CORNERS
        );
        btnShowFound.setFont(new Font("Tahoma", Font.BOLD, 24));  
        panel.add(btnShowFound);

        JButton btnShowDeleted = RoundedComponents.createRoundedButton(
            "",
            new Color(128, 0, 0),
            Color.WHITE,
            Color.BLACK,
            20,
            RoundedComponents.ALL_CORNERS
        );

        // Add image icon
        try {
            ImageIcon icon = new ImageIcon(getClass().getResource("/image/Binlogo.png"));
            Image scaledImage = icon.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
            btnShowDeleted.setIcon(new ImageIcon(scaledImage));
        } catch (Exception e) {
            e.printStackTrace();
        }
        panel.add(btnShowDeleted);

        JButton btnRefresh = RoundedComponents.createRoundedButton(
            "",
            new Color(0, 0, 0),
            Color.WHITE,
            Color.BLACK,
            20,
            RoundedComponents.ALL_CORNERS
        );

        // Add image icon for REFRESH button
        try {
            ImageIcon refreshIcon = new ImageIcon(getClass().getResource("/image/Refreshlogos.png"));
            Image refreshScaledImage = refreshIcon.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
            btnRefresh.setIcon(new ImageIcon(refreshScaledImage));
        } catch (Exception e) {
            e.printStackTrace();
        }
        panel.add(btnRefresh);

        // Scroll pane for items
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);  
        scrollPane.getVerticalScrollBar().setBlockIncrement(50);
        panel.add(scrollPane);
        
        JPanel itemsListPanel = new JPanel();
        itemsListPanel.setLayout(new BoxLayout(itemsListPanel, BoxLayout.Y_AXIS));
        itemsListPanel.setBackground(Color.WHITE);
        scrollPane.setViewportView(itemsListPanel);

        // Load all items by default
        loadAllReports(itemsListPanel, "all");

        // Button actions
        btnShowAll.addActionListener(e -> {
            btnShowAll.setBackground(new Color(0, 123, 255));
            btnShowAll.setForeground(Color.WHITE);
            btnShowLost.setBackground(null);
            btnShowLost.setForeground(Color.BLACK);
            btnShowFound.setBackground(null);
            btnShowFound.setForeground(Color.BLACK);
            btnShowDeleted.setBackground(null);
            btnShowDeleted.setForeground(Color.BLACK);
            loadAllReports(itemsListPanel, "all");
        });

        btnShowLost.addActionListener(e -> {
            btnShowLost.setBackground(new Color(0, 123, 255));
            btnShowLost.setForeground(Color.WHITE);
            btnShowAll.setBackground(null);
            btnShowAll.setForeground(Color.BLACK);
            btnShowFound.setBackground(null);
            btnShowFound.setForeground(Color.BLACK);
            btnShowDeleted.setBackground(null);
            btnShowDeleted.setForeground(Color.BLACK);
            loadAllReports(itemsListPanel, "lost");
        });

        btnShowFound.addActionListener(e -> {
            btnShowFound.setBackground(new Color(0, 123, 255));
            btnShowFound.setForeground(Color.WHITE);
            btnShowAll.setBackground(null);
            btnShowAll.setForeground(Color.BLACK);
            btnShowLost.setBackground(null);
            btnShowLost.setForeground(Color.BLACK);
            btnShowDeleted.setBackground(null);
            btnShowDeleted.setForeground(Color.BLACK);
            loadAllReports(itemsListPanel, "found");
        });

        btnShowDeleted.addActionListener(e -> {
            btnShowDeleted.setBackground(new Color(220, 53, 69));
            btnShowDeleted.setForeground(Color.WHITE);
            btnShowAll.setBackground(null);
            btnShowAll.setForeground(Color.BLACK);
            btnShowLost.setBackground(null);
            btnShowLost.setForeground(Color.BLACK);
            btnShowFound.setBackground(null);
            btnShowFound.setForeground(Color.BLACK);
            loadDeletedItems(itemsListPanel);
        });

        btnRefresh.addActionListener(e -> loadAllReports(itemsListPanel, "all"));

        // Set default button colors
        btnShowAll.setBackground(new Color(0, 123, 255));
        btnShowAll.setForeground(Color.WHITE);

        // Add component listener to center and resize content
        panel.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                int panelWidth = panel.getWidth();
                int panelHeight = panel.getHeight();
                
                // Use 85% of panel width for content
                int contentWidth = (int)(panelWidth * 0.85);
                int centerX = (panelWidth - contentWidth) / 2;
                
                // Center title
                lblTitle.setBounds(0, 20, panelWidth, 60);
                
                // Position filter buttons horizontally centered
                int buttonY = 90;
                int totalButtonWidth = 130 + 140 + 160 + 120 + 110 + 40; // sum of all button widths + gaps
                int buttonStartX = (panelWidth - totalButtonWidth) / 2;
                
                btnShowAll.setBounds(buttonStartX, buttonY, 130, 50);
                btnShowLost.setBounds(buttonStartX + 140, buttonY, 140, 50);
                btnShowFound.setBounds(buttonStartX + 290, buttonY, 160, 50);
                btnShowDeleted.setBounds(buttonStartX + 460, buttonY, 120, 50);
                btnRefresh.setBounds(buttonStartX + 590, buttonY, 110, 50);
                
                // Center and size scroll pane
                int scrollHeight = panelHeight - 180;
                scrollPane.setBounds(centerX, 150, contentWidth, scrollHeight);
            }
        });

        return panel;
    }
    private void loadAllReports(JPanel containerPanel, String filter) {
        containerPanel.removeAll();

        try (Connection conn = MySQLConnection.connect()) {
            // Load lost items
            if (filter.equals("all") || filter.equals("lost")) {
                String sqlLost = "SELECT id, item_name, location_lost as location, date_lost as date, " +
                               "description, image_path, reported_by, status FROM lost_items " +
                               "WHERE (deleted_date IS NULL OR deleted_date = '') AND status != 'Resolved' " +
                               "ORDER BY date_reported DESC";
                PreparedStatement pstmt = conn.prepareStatement(sqlLost);
                java.sql.ResultSet rs = pstmt.executeQuery();

                while (rs.next()) {
                    JPanel itemPanel = createItemPanel(
                        "lost",
                        rs.getInt("id"),
                        rs.getString("item_name"),
                        rs.getString("location"),
                        rs.getString("date"),
                        rs.getString("description"),
                        rs.getString("image_path"),
                        rs.getString("reported_by"),
                        rs.getString("status"),
                        containerPanel
                    );
                    containerPanel.add(itemPanel);
                }
            }

            // Load found items
            if (filter.equals("all") || filter.equals("found")) {
                String sqlFound = "SELECT id, item_name, location_found as location, date_found as date, " +
                                "description, image_path, reported_by, status FROM found_items " +
                                "WHERE (deleted_date IS NULL OR deleted_date = '') AND status != 'Resolved' " +
                                "ORDER BY date_reported DESC";
                PreparedStatement pstmt = conn.prepareStatement(sqlFound);
                java.sql.ResultSet rs = pstmt.executeQuery();

                while (rs.next()) {
                    JPanel itemPanel = createItemPanel(
                        "found",
                        rs.getInt("id"),
                        rs.getString("item_name"),
                        rs.getString("location"),
                        rs.getString("date"),
                        rs.getString("description"),
                        rs.getString("image_path"),
                        rs.getString("reported_by"),
                        rs.getString("status"),
                        containerPanel
                    );
                    containerPanel.add(itemPanel);
                }
            }

            if (containerPanel.getComponentCount() == 0) {
                JLabel lblNoItems = new JLabel("No items found");
                lblNoItems.setFont(new Font("Tahoma", Font.ITALIC, 24));
                lblNoItems.setForeground(Color.GRAY);
                containerPanel.add(lblNoItems);
            }

        } catch (Exception ex) {
            System.err.println("Error loading reports: " + ex.getMessage());
            ex.printStackTrace();
        }

        containerPanel.revalidate();
        containerPanel.repaint();
    }
    
    private void restoreItem(String type, int id, String itemName, JPanel parentPanel) {
        try (Connection conn = MySQLConnection.connect()) {
            String table = type.equals("lost") ? "lost_items" : "found_items";
            String sql = "UPDATE " + table + " SET deleted_date = NULL WHERE id = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, id);
            pstmt.executeUpdate();

            JOptionPane.showMessageDialog(null, "'" + itemName + "' has been restored!", "Success", JOptionPane.INFORMATION_MESSAGE);
            loadDeletedItems(parentPanel);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private JPanel createBinItemPanel(String type, int id, String itemName, String deletedDate, JPanel parentPanel) {
        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));
        panel.setPreferredSize(new Dimension(700, 100));
        panel.setBackground(new Color(255, 240, 240));
        panel.setBorder(BorderFactory.createLineBorder(new Color(220, 53, 69), 1));

        // Type & Name
        JLabel lblInfo = new JLabel(type.toUpperCase() + " - " + itemName);
        lblInfo.setFont(new Font("Tahoma", Font.BOLD, 22));
        panel.add(lblInfo);

        // Deleted date
        JLabel lblExpiry = new JLabel();
        try {
            java.time.LocalDateTime deleted = java.time.LocalDateTime.parse(deletedDate);
            java.time.LocalDateTime expiry = deleted.plusDays(3);
            long daysLeft = java.time.Duration.between(java.time.LocalDateTime.now(), expiry).toDays();
            
            lblExpiry.setText("Expires in " + daysLeft + " days");
            lblExpiry.setFont(new Font("Tahoma", Font.ITALIC, 18));
            lblExpiry.setForeground(Color.RED);
            panel.add(lblExpiry);

            // Auto-delete if expired
            if (daysLeft < 0) {
                permanentlyDeleteItem(type, id);
                return panel;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Restore button
        JButton btnRestore = RoundedComponents.createRoundedButton(
            "Restore",
            new Color(40, 167, 69),       
            new Color(255, 255, 255),    
            new Color(0, 0, 0),
            30,                         
            RoundedComponents.ALL_CORNERS
        );
        btnRestore.setFont(new Font("Tahoma", Font.BOLD, 20));
        btnRestore.addActionListener(e -> restoreItem(type, id, itemName, parentPanel));
        panel.add(btnRestore);

        // Permanent delete button
        JButton btnDeletePerm = RoundedComponents.createRoundedButton(
            "Delete",
            new Color(220, 53, 69),       
            new Color(255, 255, 255),    
            new Color(0, 0, 0),
            30,                         
            RoundedComponents.ALL_CORNERS
        );
        btnDeletePerm.setFont(getEmojiFont("Tahoma", Font.BOLD, 20));
        btnDeletePerm.setToolTipText("Delete Permanently");
        btnDeletePerm.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(null,
                "PERMANENTLY delete '" + itemName + "'?\n\nThis cannot be undone!",
                "Confirm Permanent Delete",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);
            if (confirm == JOptionPane.YES_OPTION) {
                permanentlyDeleteItem(type, id);
                loadDeletedItems(parentPanel);
            }
        });
        panel.add(btnDeletePerm);

        // Add component listener for responsive layout
        panel.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                int panelWidth = panel.getWidth();
                
                lblInfo.setBounds(20, 20, (int)(panelWidth * 0.5), 40);
                lblExpiry.setBounds(20, 60, 300, 30);
                
                // Buttons on the right
                int buttonWidth = 160;
                int deleteButtonX = panelWidth - buttonWidth - 20;
                int restoreButtonX = deleteButtonX - buttonWidth - 10;
                
                btnRestore.setBounds(restoreButtonX, 24, buttonWidth, 50);
                btnDeletePerm.setBounds(deleteButtonX, 24, buttonWidth, 50);
            }
        });

        return panel;
    }

    private void permanentlyDeleteItem(String type, int id) {
        try (Connection conn = MySQLConnection.connect()) {
            String table = type.equals("lost") ? "lost_items" : "found_items";
            String sql = "DELETE FROM " + table + " WHERE id = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
            System.out.println("Permanently deleted " + type + " item #" + id);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private JPanel createViewLogsPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBackground(Color.WHITE);

        JLabel lblTitle = new JLabel("Activity Logs");
        lblTitle.setFont(new Font("Tahoma", Font.BOLD, 40));
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(lblTitle);

        // Online Users Section
        JLabel lblOnlineUsers = new JLabel("Online Users");
        lblOnlineUsers.setFont(new Font("Tahoma", Font.BOLD, 28));
        panel.add(lblOnlineUsers);

        JPanel onlineUsersPanel = new JPanel();
        onlineUsersPanel.setLayout(new BoxLayout(onlineUsersPanel, BoxLayout.Y_AXIS));
        onlineUsersPanel.setBackground(Color.WHITE);

        // Create rounded scroll pane with color
        JScrollPane scrollOnline = RoundedComponents.createRoundedScrollPane(
            onlineUsersPanel,
            Color.WHITE,
            Color.BLACK,
            20
        );
        scrollOnline.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollOnline.getVerticalScrollBar().setUnitIncrement(16);  
        scrollOnline.getVerticalScrollBar().setBlockIncrement(50);
        panel.add(scrollOnline);
        
        // Statistics Section
        JLabel lblStats = new JLabel("System Statistics");
        lblStats.setFont(new Font("Tahoma", Font.BOLD, 28));
        panel.add(lblStats);

        JPanel statsPanel = RoundedComponents.createRoundedPanel(
            Color.WHITE,
            Color.BLACK,
            20,
            RoundedComponents.ALL_CORNERS
        );
        statsPanel.setLayout(null);
        panel.add(statsPanel);

        // Recent Activity Log
        JLabel lblRecentActivity = new JLabel("Recent Activity");
        lblRecentActivity.setFont(new Font("Tahoma", Font.BOLD, 28));
        panel.add(lblRecentActivity);

        JPanel activityPanel = new JPanel();
        activityPanel.setLayout(new BoxLayout(activityPanel, BoxLayout.Y_AXIS));
        activityPanel.setBackground(Color.WHITE);

        // Create rounded scroll pane with the panel
        JScrollPane scrollActivity = RoundedComponents.createRoundedScrollPane(
            activityPanel,
            Color.WHITE,
            Color.GRAY,
            20
        );
        scrollActivity.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollActivity.getVerticalScrollBar().setUnitIncrement(16);  
        scrollActivity.getVerticalScrollBar().setBlockIncrement(50);
        panel.add(scrollActivity);

        // Load data
        loadOnlineUsers(onlineUsersPanel);
        loadSystemStats(statsPanel);
        loadRecentActivity(activityPanel);

        // Refresh button
        JButton btnRefresh = RoundedComponents.createRoundedButton(
            "Refresh",
            new Color(90, 14, 36),
            Color.WHITE,
            Color.BLACK,
            20,
            RoundedComponents.ALL_CORNERS
        );
        btnRefresh.setFont(new Font("Tahoma", Font.BOLD, 24));  
        btnRefresh.addActionListener(e -> {
            loadOnlineUsers(onlineUsersPanel);
            loadSystemStats(statsPanel);
            loadRecentActivity(activityPanel);
        });
        panel.add(btnRefresh);

        // Add component listener for responsive layout
        panel.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                int panelWidth = panel.getWidth();
                int panelHeight = panel.getHeight();
                
                // Use 85% of panel width
                int contentWidth = (int)(panelWidth * 0.85);
                int centerX = (panelWidth - contentWidth) / 2;
                
                // Title centered
                lblTitle.setBounds(0, 20, panelWidth, 60);
                
                // Two column layout for top section
                int columnWidth = (int)(contentWidth * 0.48);
                int gap = (int)(contentWidth * 0.04);
                
                // Left column - Online Users
                lblOnlineUsers.setBounds(centerX, 100, columnWidth, 40);
                scrollOnline.setBounds(centerX, 150, columnWidth, 220);
                
                // Right column - Statistics
                int rightColX = centerX + columnWidth + gap;
                lblStats.setBounds(rightColX, 100, columnWidth, 40);
                statsPanel.setBounds(rightColX, 150, columnWidth, 220);
                
                // Bottom section - Recent Activity (full width)
                int activityY = 390;
                lblRecentActivity.setBounds(centerX, activityY, contentWidth, 40);
                
                int activityHeight = panelHeight - activityY - 100;
                scrollActivity.setBounds(centerX, activityY + 50, contentWidth, activityHeight);
                
                // Refresh button
                btnRefresh.setBounds(panelWidth - 200 - centerX, activityY - 10, 180, 50);
            }
        });

        return panel;
    }

    private void loadOnlineUsers(JPanel containerPanel) {
        containerPanel.removeAll();

        try (Connection conn = MySQLConnection.connect()) {
            String sql = "SELECT username, account_status FROM users ORDER BY username";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            java.sql.ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                String username = rs.getString("username");
                String accountStatus = rs.getString("account_status");
                if (accountStatus == null) accountStatus = "active";

                JPanel userPanel = new JPanel();
                userPanel.setLayout(null);
                userPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
                userPanel.setPreferredSize(new Dimension(320, 50));
                userPanel.setBackground(Color.WHITE);

                JLabel lblStatusDot = new JLabel("●");
                lblStatusDot.setFont(getEmojiFont("Tahoma", Font.PLAIN, 28));
                lblStatusDot.setBounds(10, 10, 30, 30);
                lblStatusDot.setForeground(Color.GRAY); 
                lblStatusDot.setToolTipText("Offline");
                userPanel.add(lblStatusDot);

                // Username
                JLabel lblUsername = new JLabel(username);
                lblUsername.setFont(new Font("Tahoma", Font.PLAIN, 22));
                lblUsername.setBounds(50, 10, 260, 30);
                userPanel.add(lblUsername);

                containerPanel.add(userPanel);
            }

        } catch (Exception ex) {
            System.err.println("Error loading online users: " + ex.getMessage());
            ex.printStackTrace();
        }

        containerPanel.revalidate();
        containerPanel.repaint();
    }

    private void loadSystemStats(JPanel statsPanel) {
        statsPanel.removeAll();

        try (Connection conn = MySQLConnection.connect()) {
            // Total Users
            int totalUsers = 0;
            String sqlUsers = "SELECT COUNT(*) as count FROM users";
            PreparedStatement pstmt = conn.prepareStatement(sqlUsers);
            java.sql.ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                totalUsers = rs.getInt("count");
            }

            // Total Lost Items
            int totalLost = 0;
            String sqlLost = "SELECT COUNT(*) as count FROM lost_items WHERE (deleted_date IS NULL OR deleted_date = '')";
            pstmt = conn.prepareStatement(sqlLost);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                totalLost = rs.getInt("count");
            }

            // Total Found Items
            int totalFound = 0;
            String sqlFound = "SELECT COUNT(*) as count FROM found_items WHERE (deleted_date IS NULL OR deleted_date = '')";
            pstmt = conn.prepareStatement(sqlFound);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                totalFound = rs.getInt("count");
            }

            // Pending Items
            int totalPending = 0;
            String sqlPending = "SELECT " +
                "(SELECT COUNT(*) FROM lost_items WHERE status = 'Pending' AND (deleted_date IS NULL OR deleted_date = '')) + " +
                "(SELECT COUNT(*) FROM found_items WHERE status = 'Pending' AND (deleted_date IS NULL OR deleted_date = '')) as count";
            pstmt = conn.prepareStatement(sqlPending);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                totalPending = rs.getInt("count");
            }

            // Resolved Items
            int totalResolved = 0;
            String sqlResolved = "SELECT " +
                "(SELECT COUNT(*) FROM lost_items WHERE status = 'Resolved') + " +
                "(SELECT COUNT(*) FROM found_items WHERE status = 'Resolved') as count";
            pstmt = conn.prepareStatement(sqlResolved);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                totalResolved = rs.getInt("count");
            }

            JLabel lblTotalUsers = new JLabel("Total Users: " + totalUsers);
            lblTotalUsers.setFont(new Font("Tahoma", Font.PLAIN, 22));
            lblTotalUsers.setBounds(20, 20, 300, 30);
            statsPanel.add(lblTotalUsers);

            JLabel lblLostItems = new JLabel("Lost Items: " + totalLost);
            lblLostItems.setFont(new Font("Tahoma", Font.PLAIN, 22));
            lblLostItems.setBounds(20, 60, 300, 30);
            statsPanel.add(lblLostItems);

            JLabel lblFoundItems = new JLabel("Found Items: " + totalFound);
            lblFoundItems.setFont(new Font("Tahoma", Font.PLAIN, 22));
            lblFoundItems.setBounds(20, 100, 300, 30);
            statsPanel.add(lblFoundItems);

            JLabel lblPending = new JLabel("Pending: " + totalPending);
            lblPending.setFont(new Font("Tahoma", Font.BOLD, 22));
            lblPending.setForeground(new Color(255, 193, 7));
            lblPending.setBounds(20, 140, 300, 30);
            statsPanel.add(lblPending);

            JLabel lblResolved = new JLabel("Resolved: " + totalResolved);
            lblResolved.setFont(new Font("Tahoma", Font.BOLD, 22));
            lblResolved.setForeground(new Color(40, 167, 69));
            lblResolved.setBounds(20, 180, 300, 30);
            statsPanel.add(lblResolved);

        } catch (Exception ex) {
            System.err.println("Error loading stats: " + ex.getMessage());
            ex.printStackTrace();
        }

        statsPanel.revalidate();
        statsPanel.repaint();
    }

    private void loadRecentActivity(JPanel containerPanel) {
        containerPanel.removeAll();

        try (Connection conn = MySQLConnection.connect()) {
            // Get recent lost items submissions
            String sqlLost = "SELECT 'Lost Item' as type, item_name, reported_by, date_reported " +
                            "FROM lost_items ORDER BY date_reported DESC LIMIT 10";
            PreparedStatement pstmt = conn.prepareStatement(sqlLost);
            java.sql.ResultSet rs = pstmt.executeQuery();

            java.util.List<ActivityLog> activities = new java.util.ArrayList<>();
            
            while (rs.next()) {
                activities.add(new ActivityLog(
                    rs.getString("type"),
                    rs.getString("item_name"),
                    rs.getString("reported_by"),
                    rs.getString("date_reported")
                ));
            }

            // Get recent found items submissions
            String sqlFound = "SELECT 'Found Item' as type, item_name, reported_by, date_reported " +
                             "FROM found_items ORDER BY date_reported DESC LIMIT 10";
            pstmt = conn.prepareStatement(sqlFound);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                activities.add(new ActivityLog(
                    rs.getString("type"),
                    rs.getString("item_name"),
                    rs.getString("reported_by"),
                    rs.getString("date_reported")
                ));
            }

            // Sort by date (most recent first)
            activities.sort((a, b) -> b.date.compareTo(a.date));

            // Display activities (limit to 15)
            int count = 0;
            for (ActivityLog activity : activities) {
                if (count >= 15) break;

                JPanel activityPanel = new JPanel();
                activityPanel.setLayout(null);
                activityPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));
                activityPanel.setPreferredSize(new Dimension(680, 60));
                activityPanel.setBackground(Color.WHITE);
                activityPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY));

                // Activity icon
                JLabel lblIcon = new JLabel(activity.type.contains("Lost") ? "🔴" : "🟢");
                lblIcon.setFont(getEmojiFont(Font.PLAIN, 24));
                lblIcon.setBounds(10, 14, 40, 30);
                activityPanel.add(lblIcon);

                // Activity text
                String activityText = activity.user + " reported: " + 
                    (activity.itemName.length() > 20 ? activity.itemName.substring(0, 20) + "..." : activity.itemName);
                JLabel lblActivity = new JLabel(activityText);
                lblActivity.setFont(new Font("Tahoma", Font.PLAIN, 20));
                lblActivity.setBounds(60, 10, 500, 30);
                activityPanel.add(lblActivity);

                // Time
                String timeStr = formatTimeAgo(activity.date);
                JLabel lblTime = new JLabel(timeStr);
                lblTime.setFont(new Font("Tahoma", Font.ITALIC, 18));
                lblTime.setForeground(Color.GRAY);
                lblTime.setBounds(60, 36, 500, 20);
                activityPanel.add(lblTime);

                containerPanel.add(activityPanel);
                count++;
            }

            if (activities.isEmpty()) {
                JLabel lblNoActivity = new JLabel("No recent activity");
                lblNoActivity.setFont(new Font("Tahoma", Font.ITALIC, 24));
                lblNoActivity.setForeground(Color.GRAY);
                containerPanel.add(lblNoActivity);
            }

        } catch (Exception ex) {
            System.err.println("Error loading activity: " + ex.getMessage());
            ex.printStackTrace();
        }

        containerPanel.revalidate();
        containerPanel.repaint();
    }

    private String formatTimeAgo(String dateStr) {
        try {
            java.time.LocalDateTime date = java.time.LocalDateTime.parse(dateStr.replace(" ", "T"));
            java.time.Duration duration = java.time.Duration.between(date, java.time.LocalDateTime.now());
            
            long minutes = duration.toMinutes();
            if (minutes < 1) return "just now";
            if (minutes < 60) return minutes + " min ago";
            
            long hours = duration.toHours();
            if (hours < 24) return hours + " hour" + (hours > 1 ? "s" : "") + " ago";
            
            long days = duration.toDays();
            if (days < 30) return days + " day" + (days > 1 ? "s" : "") + " ago";
            
            return dateStr.substring(0, 10); // Return date
        } catch (Exception e) {
            return dateStr;
        }
    }

    // Helper class for activity logs
    class ActivityLog {
        String type;
        String itemName;
        String user;
        String date;
        
        ActivityLog(String type, String itemName, String user, String date) {
            this.type = type;
            this.itemName = itemName;
            this.user = user;
            this.date = date;
        }
    }

    // Version 1: With base font parameter (3 parameters)
    private Font getEmojiFont(String baseFont, int style, int size) {
        Font font = new Font(baseFont, style, size);
        
        if (font.canDisplayUpTo("📍📅👤🔴⟲●▼🟢") == -1) {
            return font; 
        }
        
        String[] emojiFonts = {
            "Segoe UI Emoji", "Apple Color Emoji", "Noto Color Emoji", "Segoe UI Symbol"
        };
        for (String fontName : emojiFonts) {
            Font emojiFont = new Font(fontName, style, size);
            if (emojiFont.getFamily().equals(fontName)) {
                return emojiFont;
            }
        }
        return new Font("Dialog", style, size);
    }

    private Font getEmojiFont(int style, int size) {
        return getEmojiFont("Tahoma", style, size);
    }

    private Image getHighQualityScaledImage(Image src, int w, int h) {
        BufferedImage resized = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = resized.createGraphics();

        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                            RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        g2.setRenderingHint(RenderingHints.KEY_RENDERING,
                            RenderingHints.VALUE_RENDER_QUALITY);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                            RenderingHints.VALUE_ANTIALIAS_ON);

        g2.drawImage(src, 0, 0, w, h, null);
        g2.dispose();

        return resized;
    }
    
 // Add these methods to your AdminPanel class:

		    private JPanel createItemPanel(String type, int id, String itemName, String location, 
		            String date, String description, String imagePath, 
		            String reportedBy, String status, JPanel parentPanel) {
		JPanel panel = new JPanel();
		panel.setLayout(null);
		panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 240));
		panel.setPreferredSize(new Dimension(738, 240));
		panel.setBackground(Color.WHITE);
		panel.setBorder(BorderFactory.createCompoundBorder(
		BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
		BorderFactory.createEmptyBorder(10, 10, 10, 10)
		));
		
		// Image preview
		JLabel lblImage = new JLabel();
		lblImage.setHorizontalAlignment(SwingConstants.CENTER);
		lblImage.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));
		
		if (imagePath != null && !imagePath.isEmpty()) {
		try {
		File imgFile = new File(imagePath);
		if (imgFile.exists()) {
		ImageIcon icon = new ImageIcon(imagePath);
		Image img = getHighQualityScaledImage(icon.getImage(), 160, 160);
		lblImage.setIcon(new ImageIcon(img));
		} else {
		lblImage.setText("No Image");
		lblImage.setFont(new Font("Tahoma", Font.PLAIN, 18));
		}
		} catch (Exception e) {
		lblImage.setText("Error");
		lblImage.setFont(new Font("Tahoma", Font.PLAIN, 18));
		}
		} else {
		lblImage.setText("No Image");
		lblImage.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblImage.setBackground(new Color(245, 245, 245));
		lblImage.setOpaque(true);
		}
		panel.add(lblImage);
		
		// Type badge
		JLabel lblType = new JLabel(type.toUpperCase());
		lblType.setFont(new Font("Tahoma", Font.BOLD, 18));
		lblType.setHorizontalAlignment(SwingConstants.CENTER);
		lblType.setOpaque(true);
		lblType.setForeground(Color.WHITE);
		if (type.equals("lost")) {
		lblType.setBackground(new Color(220, 53, 69));
		} else {
		lblType.setBackground(new Color(40, 167, 69));
		}
		panel.add(lblType);
		
		// Item name
		JLabel lblItemName = new JLabel(itemName.length() > 20 ? itemName.substring(0, 20) + "..." : itemName);
		lblItemName.setFont(new Font("Tahoma", Font.BOLD, 24));
		lblItemName.setToolTipText(itemName);
		panel.add(lblItemName);
		
		// Location
		JLabel lblLocation = new JLabel("📍 " + (location.length() > 18 ? location.substring(0, 18) + "..." : location));
		lblLocation.setFont(getEmojiFont(Font.PLAIN, 20));
		lblLocation.setToolTipText(location);
		panel.add(lblLocation);
		
		// Date
		JLabel lblDate = new JLabel("📅 " + date);
		lblDate.setFont(getEmojiFont(Font.PLAIN, 20));
		panel.add(lblDate);
		
		// Reporter
		JLabel lblReporter = new JLabel("👤 " + reportedBy);
		lblReporter.setFont(getEmojiFont(Font.PLAIN, 18));
		panel.add(lblReporter);
		
		// View Description button
		JButton btnViewDesc = RoundedComponents.createRoundedButton(
		"Description",
		new Color(90, 14, 36),
		Color.WHITE,
		Color.BLACK,
		16,
		RoundedComponents.ALL_CORNERS
		);
		btnViewDesc.setFont(new Font("Tahoma", Font.BOLD, 22));
		btnViewDesc.setToolTipText("View Description");
		btnViewDesc.addActionListener(e -> showDescriptionDialog(itemName, description));
		panel.add(btnViewDesc);
		
		// Message button
		JButton btnMessage = RoundedComponents.createRoundedButton(
		"Message",
		new Color(0, 123, 255),
		Color.WHITE,
		Color.BLACK,
		16,
		RoundedComponents.ALL_CORNERS
		);
		btnMessage.setFont(new Font("Tahoma", Font.BOLD, 22));  
		btnMessage.setToolTipText("Message Reporter");
		btnMessage.addActionListener(e -> {
		ChatWindow chatWindow = new ChatWindow("admin", id, type);
		chatWindow.setVisible(true);
		});
		panel.add(btnMessage);
		
		// Actions button
		JButton btnActions = RoundedComponents.createRoundedButton(
		"Actions ▼",
		new Color(108, 117, 125),
		Color.WHITE,
		Color.BLACK,
		16,
		RoundedComponents.ALL_CORNERS
		);
		btnActions.setFont(new Font("Tahoma", Font.BOLD, 20));
		btnActions.addActionListener(e -> {
		showItemActionsMenu(btnActions, type, id, itemName, parentPanel);
		});
		panel.add(btnActions);
		
		// Status label
		JLabel lblStatus = new JLabel(status);
		lblStatus.setFont(new Font("Tahoma", Font.BOLD, 26));
		lblStatus.setHorizontalAlignment(SwingConstants.CENTER);
		if (status.equals("Pending")) {
		lblStatus.setForeground(new Color(255, 193, 7));
		} else if (status.equals("Resolved")) {
		lblStatus.setForeground(new Color(40, 167, 69));
		}
		panel.add(lblStatus);
		
		// Add component listener for responsive layout
		panel.addComponentListener(new ComponentAdapter() {
		@Override
		public void componentResized(ComponentEvent e) {
		int panelWidth = panel.getWidth();
		
		// Image stays fixed on left
		lblImage.setBounds(10, 10, 160, 160);
		
		// Calculate info section width
		int infoStartX = 180;
		int infoWidth = (int)(panelWidth * 0.4);
		
		lblType.setBounds(infoStartX, 10, 100, 30);
		lblItemName.setBounds(infoStartX, 44, infoWidth, 36);
		lblLocation.setBounds(infoStartX, 84, infoWidth, 30);
		lblDate.setBounds(infoStartX, 116, infoWidth, 30);
		lblReporter.setBounds(infoStartX, 148, infoWidth, 30);
		
		// Buttons on the right
		int buttonWidth = 200;
		int buttonX = panelWidth - buttonWidth - 20;
		
		btnViewDesc.setBounds(buttonX, 10, buttonWidth, 56);
		btnActions.setBounds(buttonX, 74, buttonWidth, 56);
		btnMessage.setBounds(buttonX, 174, buttonWidth, 56);
		
		// Status label
		lblStatus.setBounds(buttonX + 50, 136, 150, 30);
		}
		});
		
		return panel;
		}
    private void showDescriptionDialog(String itemName, String description) {
        JDialog dialog = new JDialog((Frame) null, "Description - " + itemName, true);
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(null);
        dialog.getContentPane().setLayout(new BorderLayout());

        JTextArea textArea = new JTextArea(description);
        textArea.setEditable(false);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setFont(new Font("Tahoma", Font.PLAIN, 12));
        textArea.setMargin(new Insets(10, 10, 10, 10));

        JScrollPane scrollPane = new JScrollPane(textArea);
        dialog.getContentPane().add(scrollPane, BorderLayout.CENTER);

        JButton btnClose = RoundedComponents.createRoundedButton(
            "Close",
            new Color(220, 53, 69),
            Color.WHITE,
            Color.BLACK,
            10,
            RoundedComponents.ALL_CORNERS
        );
        btnClose.addActionListener(e -> dialog.dispose());
        JPanel bottomPanel = new JPanel();
        bottomPanel.add(btnClose);
        dialog.getContentPane().add(bottomPanel, BorderLayout.SOUTH);

        dialog.setVisible(true);
    }

    private void showItemActionsMenu(JButton sourceButton, String type, int id, String itemName, JPanel parentPanel) {
        JPopupMenu popup = new JPopupMenu();

        // Mark as Resolved
        JMenuItem itemResolve = new JMenuItem("✓ Mark as Resolved");
        itemResolve.setFont(getEmojiFont(Font.PLAIN, 24));
        itemResolve.addActionListener(e -> markItemResolved(type, id, itemName, parentPanel));
        popup.add(itemResolve);

        popup.addSeparator();

        // Delete Report
        JMenuItem itemDelete = new JMenuItem("🗑 Delete Report");
        itemDelete.setFont(getEmojiFont(Font.PLAIN, 24));
        itemDelete.setForeground(new Color(220, 53, 69));
        itemDelete.addActionListener(e -> deleteReport(type, id, itemName, parentPanel));
        popup.add(itemDelete);

        popup.show(sourceButton, 0, sourceButton.getHeight());
    }

    private void markItemResolved(String type, int id, String itemName, JPanel parentPanel) {
        int confirm = JOptionPane.showConfirmDialog(null,
            "Mark '" + itemName + "' as resolved?",
            "Confirm Resolution",
            JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            try (Connection conn = MySQLConnection.connect()) {
                String table = type.equals("lost") ? "lost_items" : "found_items";
                String sql = "UPDATE " + table + " SET status = 'Resolved' WHERE id = ?";
                PreparedStatement pstmt = conn.prepareStatement(sql);
                pstmt.setInt(1, id);
                pstmt.executeUpdate();

                // Get the reporter's username to send notification
                String sqlGetReporter = "SELECT reported_by FROM " + table + " WHERE id = ?";
                PreparedStatement reporterStmt = conn.prepareStatement(sqlGetReporter);
                reporterStmt.setInt(1, id);
                java.sql.ResultSet rsReporter = reporterStmt.executeQuery();

                if (rsReporter.next()) {
                    String reporter = rsReporter.getString("reported_by");
                    
                    // Send notification to reporter
                    NotificationManager.createNotification(
                        reporter,
                        "Item Marked as Resolved",
                        "Your " + type + " item report '" + itemName + "' has been marked as resolved by admin.",
                        "resolved",
                        id
                    );
                }

                JOptionPane.showMessageDialog(null, "Item marked as resolved!", "Success", JOptionPane.INFORMATION_MESSAGE);
                loadAllReports(parentPanel, "all");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        }
    }

    private void deleteReport(String type, int id, String itemName, JPanel parentPanel) {
        int confirm = JOptionPane.showConfirmDialog(null,
            "Delete report for '" + itemName + "'?\n\nThis will move it to the recycle bin for 3 days.",
            "Confirm Delete",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            try (Connection conn = MySQLConnection.connect()) {
                String table = type.equals("lost") ? "lost_items" : "found_items";
                String deletedDate = java.time.LocalDateTime.now().toString();
                String sql = "UPDATE " + table + " SET deleted_date = ? WHERE id = ?";
                PreparedStatement pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, deletedDate);
                pstmt.setInt(2, id);
                pstmt.executeUpdate();

                JOptionPane.showMessageDialog(null, "Report moved to recycle bin!", "Success", JOptionPane.INFORMATION_MESSAGE);
                loadAllReports(parentPanel, "all");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        }
    }

    private void loadDeletedItems(JPanel containerPanel) {
        containerPanel.removeAll();

        try (Connection conn = MySQLConnection.connect()) {
            // Load deleted lost items
            String sqlLost = "SELECT id, item_name, deleted_date FROM lost_items WHERE deleted_date IS NOT NULL AND deleted_date != ''";
            PreparedStatement pstmt = conn.prepareStatement(sqlLost);
            java.sql.ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                JPanel binPanel = createBinItemPanel(
                    "lost",
                    rs.getInt("id"),
                    rs.getString("item_name"),
                    rs.getString("deleted_date"),
                    containerPanel
                );
                containerPanel.add(binPanel);
            }

            // Load deleted found items
            String sqlFound = "SELECT id, item_name, deleted_date FROM found_items WHERE deleted_date IS NOT NULL AND deleted_date != ''";
            pstmt = conn.prepareStatement(sqlFound);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                JPanel binPanel = createBinItemPanel(
                    "found",
                    rs.getInt("id"),
                    rs.getString("item_name"),
                    rs.getString("deleted_date"),
                    containerPanel
                );
                containerPanel.add(binPanel);
            }

            if (containerPanel.getComponentCount() == 0) {
                JLabel lblEmpty = new JLabel("Recycle bin is empty");
                lblEmpty.setFont(new Font("Tahoma", Font.ITALIC, 24));
                lblEmpty.setForeground(Color.GRAY);
                containerPanel.add(lblEmpty);
            }

        } catch (Exception ex) {
            System.err.println("Error loading deleted items: " + ex.getMessage());
            ex.printStackTrace();
        }

        containerPanel.revalidate();
        containerPanel.repaint();
    }
}