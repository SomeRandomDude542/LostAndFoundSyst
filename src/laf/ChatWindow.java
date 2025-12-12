package laf;

import javax.swing.*;
import java.io.File;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.*;
import java.time.LocalDateTime;

/**
 * ChatWindow - Real-time messaging between users and admins about reports
 */
public class ChatWindow extends JFrame {
    private String currentUsername;
    private int reportId;
    private String reportType;
    private JPanel messagesPanel;
    private JTextArea txtMessage;
    private String otherUser;
    private NotificationWindow parentNotificationWindow;
    private Timer messageRefreshTimer;
    private int lastMessageCount = 0;

    
    public ChatWindow(String username, int reportId, String reportType) {
        this(username, reportId, reportType, null);
    }
    
    public ChatWindow(String username, int reportId, String reportType, NotificationWindow parentWindow) {
        this.currentUsername = username;
        this.reportId = reportId;
        this.reportType = reportType;
        this.parentNotificationWindow = parentWindow;
        
        setTitle("Chat - Report #" + reportId);
        setSize(500, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        initUI();
        loadMessages();
        markMessagesAsRead();
        
        startMessageRefreshTimer();

     // Stop timer when window closes
     addWindowListener(new java.awt.event.WindowAdapter() {
         @Override
         public void windowClosing(java.awt.event.WindowEvent e) {
             stopMessageRefreshTimer();
         }
     });
    }
    
    private void initUI() {
        setLayout(new BorderLayout());

        // Top panel - Report info
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(new Color(64, 0, 0));
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Rounded Back button
        JButton btnBack = RoundedComponents.createRoundedButton(
         	    "← Return",
         	    new Color(100, 0, 0),       
                 new Color(255, 255, 255),    
                 new Color(255, 255, 255),
         	    7,                         
         	     RoundedComponents.ALL_CORNERS
         	     );
        btnBack.setFont(new Font("Tahoma", Font.BOLD, 12));
        btnBack.addActionListener(e -> {
            stopMessageRefreshTimer(); // ADD THIS LINE
            dispose();
            if (parentNotificationWindow != null) {
                NotificationWindow newNotifWindow = new NotificationWindow(currentUsername);
                newNotifWindow.setVisible(true);
            }
        });
        topPanel.add(btnBack, BorderLayout.WEST);

        JLabel lblReportInfo = new JLabel();
        lblReportInfo.setForeground(Color.WHITE);
        lblReportInfo.setFont(new Font("Tahoma", Font.BOLD, 14));
        lblReportInfo.setHorizontalAlignment(SwingConstants.CENTER);
        lblReportInfo.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 10));

        // Get report details
        try (Connection conn = MySQLConnection.connect()) {
            String table = reportType.equals("lost") ? "lost_items" : "found_items";
            String sql = "SELECT item_name, reported_by FROM " + table + " WHERE id = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, reportId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                String itemName = rs.getString("item_name");
                String reportedBy = rs.getString("reported_by");
                otherUser = reportedBy.equals(currentUsername) ? "Admin" : reportedBy;
                String shortItemName = itemName.length() > 15 ? itemName.substring(0, 15) + "..." : itemName;
                lblReportInfo.setText(reportType.toUpperCase() + " Item: " + shortItemName + " | By: " + reportedBy);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        topPanel.add(lblReportInfo, BorderLayout.CENTER);
        add(topPanel, BorderLayout.NORTH);
             
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.getVerticalScrollBar().setBlockIncrement(50);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        
        messagesPanel = new JPanel();
        messagesPanel.setLayout(new BoxLayout(messagesPanel, BoxLayout.Y_AXIS));
        messagesPanel.setBackground(new Color(240, 240, 240));
        messagesPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        scrollPane.setViewportView(messagesPanel);
        
        add(scrollPane, BorderLayout.CENTER);
        
     // Bottom panel - Input
        JPanel bottomPanel = new JPanel(new BorderLayout(5, 5));
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        txtMessage = new JTextArea(3, 40);
        txtMessage.setLineWrap(true);
        txtMessage.setWrapStyleWord(true);
        txtMessage.setFont(new Font("Tahoma", Font.PLAIN, 12));
        JScrollPane txtScrollPane = new JScrollPane(txtMessage);
        bottomPanel.add(txtScrollPane, BorderLayout.CENTER);

        // Button panel for Send and Image buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));

        // Image button
        JButton btnImage = RoundedComponents.createRoundedButton(
         	    "Image",
         	    new Color(108, 117, 125),       
                 new Color(255, 255, 255),    
                 new Color(0, 0, 0),
         	    7,                         
         	     RoundedComponents.ALL_CORNERS
         	     );
        btnImage.setFont(new Font("Tahoma", Font.BOLD, 12));
        btnImage.setPreferredSize(new Dimension(80, 25));
        btnImage.setMaximumSize(new Dimension(80, 25));
        btnImage.addActionListener(e -> selectAndSendImage());
        buttonPanel.add(btnImage);

        buttonPanel.add(Box.createVerticalStrut(5));

        // Send button
        JButton btnSend =  RoundedComponents.createRoundedButton(
         	    "Send",
         	    new Color(64, 0, 0),       
                 new Color(255, 255, 255),    
                 new Color(0, 0, 0),
         	    7,                         
         	     RoundedComponents.ALL_CORNERS
         	     );
        btnSend.setFont(new Font("Tahoma", Font.BOLD, 14));
        btnSend.setPreferredSize(new Dimension(80, 25));
        btnSend.setMaximumSize(new Dimension(80, 25));
        btnSend.addActionListener(e -> sendMessage());
        buttonPanel.add(btnSend);

        bottomPanel.add(buttonPanel, BorderLayout.EAST);

        // Enter key to send (keep this)
        txtMessage.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent e) {
                if (e.getKeyCode() == java.awt.event.KeyEvent.VK_ENTER && !e.isShiftDown()) {
                    e.consume();
                    sendMessage();
                }
            }
        });

        add(bottomPanel, BorderLayout.SOUTH);
    }
    
    private void loadMessages() {
        messagesPanel.removeAll();
        
        try (Connection conn = MySQLConnection.connect()) {
            String sql = "SELECT id, sender, receiver, message, image_path, timestamp FROM messages " +
                        "WHERE report_id = ? AND report_type = ? ORDER BY timestamp ASC";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, reportId);
            pstmt.setString(2, reportType);
            ResultSet rs = pstmt.executeQuery();
            
            int count = 0;
            while (rs.next()) {
                count++;
                String sender = rs.getString("sender");
                String message = rs.getString("message");
                String imagePath = rs.getString("image_path");
                String timestamp = rs.getString("timestamp");
                
                boolean isSentByMe = sender.equals(currentUsername);
                JPanel msgBubble = createMessageBubble(sender, message, timestamp, isSentByMe, imagePath);
                messagesPanel.add(msgBubble);
                messagesPanel.add(Box.createVerticalStrut(5));
                
                lastMessageCount = count;

            }
            
        } catch (Exception ex) {
            System.err.println("Error loading messages: " + ex.getMessage());
            ex.printStackTrace();
        }
        
        messagesPanel.revalidate();
        messagesPanel.repaint();
        
        // Scroll to bottom
        SwingUtilities.invokeLater(() -> {
            JScrollBar vertical = ((JScrollPane)messagesPanel.getParent().getParent()).getVerticalScrollBar();
            vertical.setValue(vertical.getMaximum());
        });
    }
    
    private JPanel createMessageBubble(String sender, String message, String timestamp, boolean isSentByMe) {
        return createMessageBubble(sender, message, timestamp, isSentByMe, null);
    }

    private JPanel createMessageBubble(String sender, String message, String timestamp, boolean isSentByMe, String imagePath) {
        JPanel containerPanel = new JPanel();
        containerPanel.setLayout(new BoxLayout(containerPanel, BoxLayout.X_AXIS));
        containerPanel.setOpaque(false);
        containerPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // Create rounded panel with border
        JPanel bubble = RoundedComponents.createRoundedPanel(
            isSentByMe ? new Color(0, 123, 255) : Color.WHITE,
            Color.BLACK,  // Black border
            1,            // Border width
            15,           // Radius
            RoundedComponents.ALL_CORNERS
        );
        bubble.setLayout(new BoxLayout(bubble, BoxLayout.Y_AXIS));
        bubble.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
        
        if (isSentByMe) {
            // Add spacing on the left to push bubble to the right
            containerPanel.add(Box.createHorizontalStrut(80)); // Minimum left spacing
            containerPanel.add(Box.createHorizontalGlue());
        }
        
        // Sender name (if not sent by me)
        if (!isSentByMe) {
            JLabel lblSender = new JLabel(sender);
            lblSender.setFont(new Font("Tahoma", Font.BOLD, 10));
            lblSender.setForeground(new Color(64, 0, 0));
            lblSender.setAlignmentX(Component.LEFT_ALIGNMENT);
            bubble.add(lblSender);
            bubble.add(Box.createVerticalStrut(3));
        }
        
        // Show image if present
        if (imagePath != null && !imagePath.isEmpty()) {
            try {
                File imgFile = new File(imagePath);
                if (imgFile.exists()) {
                    ImageIcon icon = new ImageIcon(imagePath);
                    Image img = icon.getImage().getScaledInstance(200, 200, Image.SCALE_SMOOTH);
                    JLabel lblImage = new JLabel(new ImageIcon(img));
                    lblImage.setAlignmentX(Component.LEFT_ALIGNMENT);
                    lblImage.setBorder(BorderFactory.createLineBorder(
                        isSentByMe ? new Color(0, 100, 200) : Color.LIGHT_GRAY, 2));
                    bubble.add(lblImage);
                    bubble.add(Box.createVerticalStrut(5));
                }
            } catch (Exception e) {
                System.err.println("Error loading image in bubble: " + e.getMessage());
            }
        }
        
        // Message text with proper wrapping - NO FIXED WIDTH
        if (!message.equals("[Image]")) {
            // Use JTextArea for better text wrapping
            JTextArea txtMessage = new JTextArea(message);
            txtMessage.setFont(new Font("Tahoma", Font.PLAIN, 12));
            txtMessage.setForeground(isSentByMe ? Color.WHITE : Color.BLACK);
            txtMessage.setBackground(isSentByMe ? new Color(0, 123, 255) : Color.WHITE);
            txtMessage.setOpaque(false);
            txtMessage.setEditable(false);
            txtMessage.setFocusable(false);
            txtMessage.setLineWrap(true);
            txtMessage.setWrapStyleWord(true);
            txtMessage.setAlignmentX(Component.LEFT_ALIGNMENT);
            
            // Set maximum width but allow natural shrinking
            txtMessage.setColumns(30); // Maximum ~30 characters wide
            txtMessage.setRows(0); // Auto-calculate rows
            
            bubble.add(txtMessage);
            bubble.add(Box.createVerticalStrut(3));
        }
        
        // Timestamp
        JLabel lblTime = new JLabel(formatTime(timestamp));
        lblTime.setFont(new Font("Tahoma", Font.ITALIC, 9));
        lblTime.setForeground(isSentByMe ? new Color(230, 230, 230) : Color.GRAY);
        lblTime.setAlignmentX(Component.LEFT_ALIGNMENT);
        bubble.add(lblTime);
        
        containerPanel.add(bubble);
        
        if (!isSentByMe) {
            // Add spacing on the right for received messages
            containerPanel.add(Box.createHorizontalGlue());
            containerPanel.add(Box.createHorizontalStrut(80)); // Minimum right spacing
        }
        
        // Let container adjust to content height
        containerPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, Short.MAX_VALUE));
        
        return containerPanel;
    }
    
    private void sendMessage() {
        String message = txtMessage.getText().trim();
        if (message.isEmpty()) {
            return;
        }
        
        try (Connection conn = MySQLConnection.connect()) {
            String table = reportType.equals("lost") ? "lost_items" : "found_items";
            String sql = "SELECT reported_by FROM " + table + " WHERE id = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, reportId);
            ResultSet rs = pstmt.executeQuery();
            
            String receiver = "";
            if (rs.next()) {
                String reportOwner = rs.getString("reported_by");
                receiver = reportOwner.equals(currentUsername) ? "admin" : reportOwner;
            }
            
            String insertSql = "INSERT INTO messages (report_type, report_id, sender, receiver, message, timestamp) " +
                              "VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement insertStmt = conn.prepareStatement(insertSql);
            insertStmt.setString(1, reportType);
            insertStmt.setInt(2, reportId);
            insertStmt.setString(3, currentUsername);
            insertStmt.setString(4, receiver);
            insertStmt.setString(5, message);
            insertStmt.setString(6, LocalDateTime.now().toString());
            insertStmt.executeUpdate();
            
            NotificationManager.createNotification(
                receiver,
                "New message from " + currentUsername,
                "About report #" + reportId + ": " + message.substring(0, Math.min(50, message.length())) + "...",
                "message",
                0
            );
            
            txtMessage.setText("");

         // Add message immediately to UI without reloading from database
         boolean isSentByMe = true;
         JPanel msgBubble = createMessageBubble(currentUsername, message, LocalDateTime.now().toString(), isSentByMe);
         messagesPanel.add(msgBubble);
         messagesPanel.add(Box.createVerticalStrut(5));
         messagesPanel.revalidate();
         messagesPanel.repaint();

         // Scroll to bottom
         SwingUtilities.invokeLater(() -> {
             JScrollBar vertical = ((JScrollPane)messagesPanel.getParent().getParent()).getVerticalScrollBar();
             vertical.setValue(vertical.getMaximum());
         });
            
        } catch (Exception ex) {
            System.err.println("Error sending message: " + ex.getMessage());
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to send message", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void markMessagesAsRead() {
        try (Connection conn = MySQLConnection.connect()) {
            String sql = "UPDATE messages SET is_read = 1 WHERE report_id = ? AND report_type = ? AND receiver = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, reportId);
            pstmt.setString(2, reportType);
            pstmt.setString(3, currentUsername);
            pstmt.executeUpdate();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    private String formatTime(String timestamp) {
        try {
            LocalDateTime dateTime = LocalDateTime.parse(timestamp);
            LocalDateTime now = LocalDateTime.now();
            
            if (dateTime.toLocalDate().equals(now.toLocalDate())) {
                return dateTime.toLocalTime().toString().substring(0, 5);
            } else {
                return dateTime.toLocalDate().toString() + " " + 
                       dateTime.toLocalTime().toString().substring(0, 5);
            }
        } catch (Exception e) {
            return timestamp;
        }
    }
    
    private void selectAndSendImage() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Select Image to Send");
        
        javax.swing.filechooser.FileNameExtensionFilter filter =
            new javax.swing.filechooser.FileNameExtensionFilter(
                "Image Files", "jpg", "jpeg", "png", "gif", "bmp");
        fileChooser.setFileFilter(filter);
        
        int result = fileChooser.showOpenDialog(this);
        
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            sendImageMessage(selectedFile);
        }
    }

    private void sendImageMessage(File imageFile) {
        try {
            // Save image to messages folder
            File messagesDir = new File("images/messages");
            if (!messagesDir.exists()) {
                messagesDir.mkdirs();
            }
            
            String extension = imageFile.getName().substring(imageFile.getName().lastIndexOf('.') + 1);
            String newFileName = System.currentTimeMillis() + "_" + reportId + "." + extension;
            File destFile = new File(messagesDir, newFileName);
            
            java.nio.file.Files.copy(imageFile.toPath(), destFile.toPath(), 
                java.nio.file.StandardCopyOption.REPLACE_EXISTING);
            
            String imagePath = destFile.getPath();
            
            // Send to database
            try (Connection conn = MySQLConnection.connect()) {
                String table = reportType.equals("lost") ? "lost_items" : "found_items";
                String sql = "SELECT reported_by FROM " + table + " WHERE id = ?";
                PreparedStatement pstmt = conn.prepareStatement(sql);
                pstmt.setInt(1, reportId);
                ResultSet rs = pstmt.executeQuery();
                
                String receiver = "";
                if (rs.next()) {
                    String reportOwner = rs.getString("reported_by");
                    receiver = reportOwner.equals(currentUsername) ? "admin" : reportOwner;
                }
                
                String insertSql = "INSERT INTO messages (report_type, report_id, sender, receiver, message, image_path, timestamp) " +
                                  "VALUES (?, ?, ?, ?, ?, ?, ?)";
                PreparedStatement insertStmt = conn.prepareStatement(insertSql);
                insertStmt.setString(1, reportType);
                insertStmt.setInt(2, reportId);
                insertStmt.setString(3, currentUsername);
                insertStmt.setString(4, receiver);
                insertStmt.setString(5, "[Image]");
                insertStmt.setString(6, imagePath);
                insertStmt.setString(7, LocalDateTime.now().toString());
                insertStmt.executeUpdate();
                
                // Create notification
                NotificationManager.createNotification(
                    receiver,
                    "New image from " + currentUsername,
                    "Sent an image about report #" + reportId,
                    "message",
                    0
                );
                
                // Add to UI immediately
                JPanel msgBubble = createMessageBubble(currentUsername, "[Image]", LocalDateTime.now().toString(), true, imagePath);
                messagesPanel.add(msgBubble);
                messagesPanel.add(Box.createVerticalStrut(5));
                messagesPanel.revalidate();
                messagesPanel.repaint();
                
                SwingUtilities.invokeLater(() -> {
                    JScrollBar vertical = ((JScrollPane)messagesPanel.getParent().getParent()).getVerticalScrollBar();
                    vertical.setValue(vertical.getMaximum());
                });
                
            } catch (Exception ex) {
                System.err.println("Error sending image: " + ex.getMessage());
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Failed to send image", "Error", JOptionPane.ERROR_MESSAGE);
            }
            
        } catch (Exception ex) {
            System.err.println("Error saving image: " + ex.getMessage());
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to save image", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void startMessageRefreshTimer() {
        messageRefreshTimer = new Timer(2000, e -> checkForNewMessages()); // Check every 2 seconds
        messageRefreshTimer.start();
    }

    private void stopMessageRefreshTimer() {
        if (messageRefreshTimer != null) {
            messageRefreshTimer.stop();
        }
    }

    private void checkForNewMessages() {
        try (Connection conn = MySQLConnection.connect()) {
            String sql = "SELECT COUNT(*) FROM messages WHERE report_id = ? AND report_type = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, reportId);
            pstmt.setString(2, reportType);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                int currentCount = rs.getInt(1);
                
                // Only reload if there are new messages
                if (currentCount > lastMessageCount) {
                    lastMessageCount = currentCount;
                    loadMessages();
                    markMessagesAsRead();
                }
            }
        } catch (Exception ex) {
            System.err.println("Error checking for new messages: " + ex.getMessage());
        }
    }
}


class MessagesListWindow extends JFrame {
    private String currentUsername;
    private String reportType;
    private JPanel ticketsPanel;
    
    public MessagesListWindow(String username, String reportType) {
        this.currentUsername = username;
        this.reportType = reportType;
        
        setTitle("Select Ticket to Message");
        setSize(500, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        initUI();
        loadTickets();
    }
    
    private void initUI() {
        setLayout(new BorderLayout());
        
        // Top panel - Header
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(new Color(64, 0, 0));
        topPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        // Rounded Back button
        JButton btnBack = RoundedComponents.createRoundedButton(
         	    "Return",
         	    new Color(122, 14, 26),       
                 new Color(255, 255, 255),    
                 new Color(255, 255, 255),
         	    15,                         
         	     RoundedComponents.ALL_CORNERS
         	     );
        btnBack.setFont(new Font("Tahoma", Font.BOLD, 12));
        btnBack.addActionListener(e -> dispose());
        topPanel.add(btnBack, BorderLayout.WEST);
        
        // Header label
        JLabel lblHeader = new JLabel("Select a " + reportType.toUpperCase() + " item ticket to message");
        lblHeader.setFont(new Font("Tahoma", Font.BOLD, 16));
        lblHeader.setForeground(Color.WHITE);
        lblHeader.setHorizontalAlignment(SwingConstants.CENTER);
        lblHeader.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 10));
        topPanel.add(lblHeader, BorderLayout.CENTER);
        
        add(topPanel, BorderLayout.NORTH);
        
        // Center panel - Tickets list
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16); 
        scrollPane.getVerticalScrollBar().setBlockIncrement(50);
        
        ticketsPanel = new JPanel();
        ticketsPanel.setLayout(new BoxLayout(ticketsPanel, BoxLayout.Y_AXIS));
        ticketsPanel.setBackground(Color.WHITE);
        ticketsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        scrollPane.setViewportView(ticketsPanel);
        
        add(scrollPane, BorderLayout.CENTER);
    }
    
    private void loadTickets() {
        ticketsPanel.removeAll();
        
        try (Connection conn = MySQLConnection.connect()) {
            String table = reportType.equals("lost") ? "lost_items" : "found_items";
            String dateCol = reportType.equals("lost") ? "date_lost" : "date_found";
            
            String sql = "SELECT id, item_name, description, " + dateCol + " as date, status FROM " + table + 
                        " WHERE reported_by = ? ORDER BY date_reported DESC";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, currentUsername);
            ResultSet rs = pstmt.executeQuery();
            
            boolean hasTickets = false;
            while (rs.next()) {
                hasTickets = true;
                int ticketId = rs.getInt("id");
                String itemName = rs.getString("item_name");
                String description = rs.getString("description");
                String date = rs.getString("date");
                String status = rs.getString("status");
                
                int unreadCount = getUnreadMessageCount(ticketId);
                
                JPanel ticketCard = createTicketCard(ticketId, itemName, description, date, status, unreadCount);
                ticketsPanel.add(ticketCard);
                ticketsPanel.add(Box.createVerticalStrut(10));
            }
            
            if (!hasTickets) {
                JLabel lblNoTickets = new JLabel("No " + reportType + " item reports found");
                lblNoTickets.setFont(new Font("Tahoma", Font.ITALIC, 14));
                lblNoTickets.setForeground(Color.GRAY);
                lblNoTickets.setAlignmentX(Component.CENTER_ALIGNMENT);
                ticketsPanel.add(Box.createVerticalStrut(50));
                ticketsPanel.add(lblNoTickets);
            }
            
        } catch (Exception ex) {
            System.err.println("Error loading tickets: " + ex.getMessage());
            ex.printStackTrace();
        }
        
        ticketsPanel.revalidate();
        ticketsPanel.repaint();
    }
    
    private JPanel createTicketCard(int ticketId, String itemName, String description, 
                                    String date, String status, int unreadCount) {
        // Rounded ticket card
        JPanel card = RoundedComponents.createRoundedPanel(
            Color.WHITE,
            15,
            RoundedComponents.ALL_CORNERS
        );
        card.setLayout(new BorderLayout(10, 10));
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Left section - Ticket info
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setOpaque(false);
        
        JLabel lblTicketId = new JLabel("Ticket #" + ticketId);
        lblTicketId.setFont(new Font("Tahoma", Font.BOLD, 14));
        lblTicketId.setForeground(new Color(64, 0, 0));
        leftPanel.add(lblTicketId);
        
        leftPanel.add(Box.createVerticalStrut(5));
        
        JLabel lblItemName = new JLabel(itemName);
        lblItemName.setFont(new Font("Tahoma", Font.BOLD, 13));
        leftPanel.add(lblItemName);
        
        leftPanel.add(Box.createVerticalStrut(3));
        
        String shortDesc = description.length() > 50 ? description.substring(0, 50) + "..." : description;
        JLabel lblDesc = new JLabel(shortDesc);
        lblDesc.setFont(new Font("Tahoma", Font.PLAIN, 11));
        lblDesc.setForeground(Color.GRAY);
        leftPanel.add(lblDesc);
        
        leftPanel.add(Box.createVerticalStrut(3));
        
        JLabel lblDate = new JLabel("📅 " + date);
        lblDate.setFont(new Font("Tahoma", Font.PLAIN, 10));
        lblDate.setForeground(Color.GRAY);
        leftPanel.add(lblDate);
        
        card.add(leftPanel, BorderLayout.CENTER);
        
        // Right section - Status and unread badge
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.setOpaque(false);
        
        JLabel lblStatus = new JLabel(status);
        lblStatus.setFont(new Font("Tahoma", Font.BOLD, 12));
        if (status.equals("Pending")) {
            lblStatus.setForeground(new Color(255, 165, 0));
        } else if (status.equals("Resolved")) {
            lblStatus.setForeground(new Color(40, 167, 69));
        }
        rightPanel.add(lblStatus);
        
        rightPanel.add(Box.createVerticalStrut(10));
        
        // Unread message badge
        if (unreadCount > 0) {
            JLabel lblUnread = new JLabel(unreadCount + " new");
            lblUnread.setFont(new Font("Tahoma", Font.BOLD, 11));
            lblUnread.setForeground(Color.WHITE);
            lblUnread.setOpaque(true);
            lblUnread.setBackground(new Color(220, 53, 69));
            lblUnread.setBorder(BorderFactory.createEmptyBorder(3, 8, 3, 8));
            rightPanel.add(lblUnread);
        }
        
        rightPanel.add(Box.createVerticalGlue());
        
        JLabel lblClickHint = new JLabel("Click to message →");
        lblClickHint.setFont(new Font("Tahoma", Font.ITALIC, 10));
        lblClickHint.setForeground(new Color(0, 123, 255));
        rightPanel.add(lblClickHint);
        
        card.add(rightPanel, BorderLayout.EAST);
        
        // Click to open chat
        card.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                ChatWindow chatWindow = new ChatWindow(currentUsername, ticketId, reportType);
                chatWindow.setVisible(true);
                dispose();
            }
            
            @Override
            public void mouseEntered(MouseEvent e) {
                card.setBackground(new Color(245, 245, 245));
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                card.setBackground(Color.WHITE);
            }
        });
        
        return card;
    }
    
    private int getUnreadMessageCount(int reportId) {
        int count = 0;
        try (Connection conn = MySQLConnection.connect()) {
            String sql = "SELECT COUNT(*) FROM messages WHERE report_id = ? AND report_type = ? " +
                        "AND receiver = ? AND is_read = 0";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, reportId);
            pstmt.setString(2, reportType);
            pstmt.setString(3, currentUsername);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                count = rs.getInt(1);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return count;
    }
    
    
    
}