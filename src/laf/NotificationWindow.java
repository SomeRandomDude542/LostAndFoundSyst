package laf;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.time.LocalDateTime;

/**
 * NotificationWindow - Shows notifications and messages for users
 * Create this as a new file: NotificationWindow.java
 */
public class NotificationWindow extends JFrame {
    private String currentUsername;
    private JPanel notificationsPanel;
    private JPanel messagesPanel;
    
    
    public NotificationWindow(String username) {
        this.currentUsername = username;
        
        setTitle("Notifications & Messages");
        setSize(500, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        JTabbedPane tabbedPane = new JTabbedPane();
        
        // Tab 1: Notifications
        JPanel notifTab = createNotificationsTab();
        tabbedPane.addTab("Notifications", notifTab);
        
        // Tab 2: Messages
        JPanel messagesTab = createMessagesTab();
        tabbedPane.addTab("Messages", messagesTab);
        
        add(tabbedPane);
        
        // Load data
        loadNotifications();
        loadMessages();
    }
    
    private JPanel createNotificationsTab() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        
        JLabel lblTitle = new JLabel("Your Notifications");
        lblTitle.setFont(new Font("Tahoma", Font.BOLD, 16));
        lblTitle.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.add(lblTitle, BorderLayout.NORTH);
        
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        
        notificationsPanel = new JPanel();
        notificationsPanel.setLayout(new BoxLayout(notificationsPanel, BoxLayout.Y_AXIS));
        notificationsPanel.setBackground(Color.WHITE);
        scrollPane.setViewportView(notificationsPanel);
        
        panel.add(scrollPane, BorderLayout.CENTER);
        
        // Refresh button
        JButton btnRefresh = RoundedComponents.createRoundedButton(
          	     "Refresh",
           	     new Color(135, 206, 235),    // background
           	     Color.BLACK,                // text
           	     Color.BLACK,            // border
           	     10,                         // radius
           	     RoundedComponents.ALL_CORNERS
           	 );
        btnRefresh.addActionListener(e -> loadNotifications());
        JPanel bottomPanel = new JPanel();
        bottomPanel.add(btnRefresh);
        panel.add(bottomPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private JPanel createMessagesTab() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        
        JLabel lblTitle = new JLabel("Your Messages");
        lblTitle.setFont(new Font("Tahoma", Font.BOLD, 16));
        lblTitle.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.add(lblTitle, BorderLayout.NORTH);
        
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        
        messagesPanel = new JPanel();
        messagesPanel.setLayout(new BoxLayout(messagesPanel, BoxLayout.Y_AXIS));
        messagesPanel.setBackground(Color.WHITE);
        scrollPane.setViewportView(messagesPanel);
        
        panel.add(scrollPane, BorderLayout.CENTER);
        
        // Refresh button
        JButton btnRefresh = RoundedComponents.createRoundedButton(
          	     "Refresh",
           	     new Color(135, 206, 235),    // background
           	     Color.BLACK,                // text
           	     Color.BLACK,            // border
           	     10,                         // radius
           	     RoundedComponents.ALL_CORNERS
           	 );
        btnRefresh.addActionListener(e -> loadMessages());
        JPanel bottomPanel = new JPanel();
        bottomPanel.add(btnRefresh);
        panel.add(bottomPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private void loadNotifications() {
        notificationsPanel.removeAll();
        
        try (Connection conn = MySQLConnection.connect()) {
            String sql = "SELECT id, title, message, type, timestamp, is_read, related_id " +
                        "FROM notifications WHERE username = ? ORDER BY timestamp DESC LIMIT 50";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, currentUsername);
            ResultSet rs = pstmt.executeQuery();
            
            boolean hasNotifications = false;
            while (rs.next()) {
                hasNotifications = true;
                int id = rs.getInt("id");
                String title = rs.getString("title");
                String message = rs.getString("message");
                String type = rs.getString("type");
                String timestamp = rs.getString("timestamp");
                boolean isRead = rs.getInt("is_read") == 1;
                int relatedId = rs.getInt("related_id");
                
                JPanel notifPanel = createNotificationPanel(id, title, message, type, timestamp, isRead, relatedId);
                notificationsPanel.add(notifPanel);
            }
            
            if (!hasNotifications) {
                JLabel lblEmpty = new JLabel("No notifications");
                lblEmpty.setFont(new Font("Tahoma", Font.ITALIC, 12));
                lblEmpty.setForeground(Color.GRAY);
                notificationsPanel.add(lblEmpty);
            }
            
        } catch (Exception ex) {
            System.err.println("Error loading notifications: " + ex.getMessage());
            ex.printStackTrace();
        }
        
        notificationsPanel.revalidate();
        notificationsPanel.repaint();
    }
    
    private JPanel createNotificationPanel(int id, String title, String message, String type, 
                                          String timestamp, boolean isRead, int relatedId) {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout(10, 5));
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));
        panel.setPreferredSize(new Dimension(450, 80));
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        
        if (!isRead) {
            panel.setBackground(new Color(230, 240, 255));
        } else {
            panel.setBackground(Color.WHITE);
        }
        
        // Icon based on type
        String icon = "🔔";
        Color iconColor = Color.BLUE;
        if (type.equals("resolved")) {
            icon = "✅";
            iconColor = new Color(40, 167, 69);
        } else if (type.equals("message")) {
            icon = "💬";
            iconColor = new Color(0, 123, 255);
        } else if (type.equals("status_change")) {
            icon = "📝";
            iconColor = new Color(255, 193, 7);
        }
        
        JLabel lblIcon = new JLabel(icon);
        lblIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 24));
        lblIcon.setForeground(iconColor);
        panel.add(lblIcon, BorderLayout.WEST);
        
        // Content
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setOpaque(false);
        
        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(new Font("Tahoma", Font.BOLD, 12));
        contentPanel.add(lblTitle);
        
        JLabel lblMessage = new JLabel("<html>" + message + "</html>");
        lblMessage.setFont(new Font("Tahoma", Font.PLAIN, 11));
        contentPanel.add(lblMessage);
        
        JLabel lblTime = new JLabel(formatTimeAgo(timestamp));
        lblTime.setFont(new Font("Tahoma", Font.ITALIC, 9));
        lblTime.setForeground(Color.GRAY);
        contentPanel.add(lblTime);
        
        panel.add(contentPanel, BorderLayout.CENTER);
        
        // Mark as read button
        if (!isRead) {
            JButton btnMarkRead = RoundedComponents.createRoundedButton(
         		    "",
	            	    new Color(199, 255, 206),       
	                    new Color(0, 0, 0),    
	                    new Color(0, 0, 0),
	            	    10,                         
	            	     RoundedComponents.ALL_CORNERS
	            	     );
         btnMarkRead.setPreferredSize(new Dimension(40, 30));
         btnMarkRead.setToolTipText("Mark as read");
         ImageIcon icon1 = new ImageIcon(getClass().getResource("/image/Checklogo.png"));
         Image scaledImage = icon1.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
         btnMarkRead.setIcon(new ImageIcon(scaledImage));                                    
         btnMarkRead.addActionListener(e -> {
         markNotificationAsRead(id);
         loadNotifications();
         });
         panel.add(btnMarkRead, BorderLayout.EAST);
     }
        // Click to open related item (if it's a message)
        if (type.equals("message") && relatedId > 0) {
            panel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            panel.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseClicked(java.awt.event.MouseEvent e) {
                    openMessageThread(relatedId);
                }
            });
        }
        
        return panel;
    }
    
    private void loadMessages() {
        messagesPanel.removeAll();
        
        try (Connection conn = MySQLConnection.connect()) {
            // Get unique conversations
            String sql = "SELECT DISTINCT report_type, report_id FROM messages " +
                        "WHERE sender = ? OR receiver = ? ORDER BY timestamp DESC";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, currentUsername);
            pstmt.setString(2, currentUsername);
            ResultSet rs = pstmt.executeQuery();
            
            boolean hasMessages = false;
            while (rs.next()) {
                hasMessages = true;
                String reportType = rs.getString("report_type");
                int reportId = rs.getInt("report_id");
                
                JPanel msgPanel = createMessageThreadPanel(reportType, reportId);
                messagesPanel.add(msgPanel);
            }
            
            if (!hasMessages) {
                JLabel lblEmpty = new JLabel("No messages");
                lblEmpty.setFont(new Font("Tahoma", Font.ITALIC, 12));
                lblEmpty.setForeground(Color.GRAY);
                messagesPanel.add(lblEmpty);
            }
            
        } catch (Exception ex) {
            System.err.println("Error loading messages: " + ex.getMessage());
            ex.printStackTrace();
        }
        
        messagesPanel.revalidate();
        messagesPanel.repaint();
    }
    
    private JPanel createMessageThreadPanel(String reportType, int reportId) {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout(10, 5));
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 70));
        panel.setPreferredSize(new Dimension(450, 70));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        
        // Get report details and last message
        try (Connection conn = MySQLConnection.connect()) {
            String table = reportType.equals("lost") ? "lost_items" : "found_items";
            String locationCol = reportType.equals("lost") ? "location_lost" : "location_found";
            
            String sql = "SELECT item_name, " + locationCol + " FROM " + table + " WHERE id = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, reportId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                String itemName = rs.getString("item_name");
                
                // Get unread count
                String unreadSql = "SELECT COUNT(*) FROM messages WHERE report_id = ? AND report_type = ? " +
                                  "AND receiver = ? AND is_read = 0";
                PreparedStatement unreadStmt = conn.prepareStatement(unreadSql);
                unreadStmt.setInt(1, reportId);
                unreadStmt.setString(2, reportType);
                unreadStmt.setString(3, currentUsername);
                ResultSet unreadRs = unreadStmt.executeQuery();
                int unreadCount = unreadRs.getInt(1);
                
                // Icon
                JLabel lblIcon = new JLabel("💬");
                lblIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 24));
                panel.add(lblIcon, BorderLayout.WEST);
                
                // Content
                JPanel contentPanel = new JPanel();
                contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
                contentPanel.setOpaque(false);
                
                String title = reportType.toUpperCase() + " Item: " + itemName;
                if (unreadCount > 0) {
                    title += " (" + unreadCount + " unread)";
                }
                
                JLabel lblTitle = new JLabel(title);
                lblTitle.setFont(new Font("Tahoma", Font.BOLD, 12));
                if (unreadCount > 0) {
                    lblTitle.setForeground(new Color(0, 123, 255));
                }
                contentPanel.add(lblTitle);
                
                JLabel lblSubtext = new JLabel("Report ID: #" + reportId);
                lblSubtext.setFont(new Font("Tahoma", Font.PLAIN, 10));
                lblSubtext.setForeground(Color.GRAY);
                contentPanel.add(lblSubtext);
                
                panel.add(contentPanel, BorderLayout.CENTER);
                
                // Open button
                JButton btnOpen = RoundedComponents.createRoundedButton(
		            	    "Open",
		            	    new Color(64, 0, 0),       
		                    new Color(255, 255 ,255),    
		                    new Color(0, 0, 0),
		            	    10,                         
		            	     RoundedComponents.ALL_CORNERS
		            	     );
                btnOpen.setFont(new Font("Tahoma", Font.BOLD, 14));
                btnOpen.addActionListener(e -> openMessageThread(reportId, reportType));
                panel.add(btnOpen, BorderLayout.EAST);
            }
            
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        
        return panel;
    }
    
    private void openMessageThread(int messageId) {
        // Get report details from message ID
        try (Connection conn = MySQLConnection.connect()) {
            String sql = "SELECT report_type, report_id FROM messages WHERE id = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, messageId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                String reportType = rs.getString("report_type");
                int reportId = rs.getInt("report_id");
                openMessageThread(reportId, reportType);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void openMessageThread(int reportId, String reportType) {
        // ✅ CLOSE THIS WINDOW FIRST
        this.dispose();
        
        // ✅ PASS 'this' REFERENCE TO ChatWindow
        ChatWindow chatWindow = new ChatWindow(currentUsername, reportId, reportType, this);
        chatWindow.setVisible(true);
    }
    
    private void markNotificationAsRead(int notificationId) {
        try (Connection conn = MySQLConnection.connect()) {
            String sql = "UPDATE notifications SET is_read = 1 WHERE id = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, notificationId);
            pstmt.executeUpdate();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    private String formatTimeAgo(String timestamp) {
        try {
            LocalDateTime dateTime = LocalDateTime.parse(timestamp);
            LocalDateTime now = LocalDateTime.now();
            long minutes = java.time.Duration.between(dateTime, now).toMinutes();
            
            if (minutes < 1) return "Just now";
            if (minutes < 60) return minutes + " min ago";
            
            long hours = minutes / 60;
            if (hours < 24) return hours + " hour" + (hours > 1 ? "s" : "") + " ago";
            
            long days = hours / 24;
            if (days < 7) return days + " day" + (days > 1 ? "s" : "") + " ago";
            
            return timestamp.substring(0, 10);
        } catch (Exception e) {
            return timestamp;
        }
    }
    
    // Static method to get unread count for badge
    public static int getUnreadCount(String username) {
        int count = 0;

        try (Connection conn = MySQLConnection.connect()) {

            // Count unread notifications
            String sql = "SELECT COUNT(*) FROM notifications WHERE username = ? AND is_read = 0";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {  // <-- FIX
                count += rs.getInt(1);
            }

            // Count unread messages
            sql = "SELECT COUNT(*) FROM messages WHERE receiver = ? AND is_read = 0";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, username);
            rs = pstmt.executeQuery();

            if (rs.next()) {  // <-- FIX
                count += rs.getInt(1);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return count;
    }
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
}