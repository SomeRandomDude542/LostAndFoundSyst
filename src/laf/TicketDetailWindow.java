package laf;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.sql.*;
import javax.imageio.ImageIO;

/**
 * TicketDetailWindow - Shows full ticket details with image
 */
public class TicketDetailWindow extends JDialog {
    
    // Define base dimensions for scaling
    private static final int BASE_WIDTH = 600;
    private static final int BASE_HEIGHT = 500;
    
    public TicketDetailWindow(Frame parent, String type, int ticketId) {
        super(parent, "Ticket Details - #" + ticketId, true);
        
        // Get screen size for responsive scaling
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int targetWidth = Math.min((int)(screenSize.width * 0.5), 800);  // 50% of screen or max 800px
        int targetHeight = Math.min((int)(screenSize.height * 0.7), 700); // 70% of screen or max 700px
        
        // Initialize scaling for this window
        ResponsiveScaler.initializeScale(targetWidth, targetHeight, BASE_WIDTH, BASE_HEIGHT);
        
        setSize(targetWidth, targetHeight);
        setLocationRelativeTo(parent);
        
        initUI(type, ticketId);
    }
    
    /**
     * High-quality image scaling method
     */
    private BufferedImage getScaledImage(BufferedImage src, int w, int h) {
        BufferedImage resized = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = resized.createGraphics();
        
        // Enable high-quality rendering
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        g2.drawImage(src, 0, 0, w, h, null);
        g2.dispose();
        
        return resized;
    }
    
    private void initUI(String type, int ticketId) {
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(Color.WHITE);
        
        try (Connection conn = MySQLConnection.connect()) {
            String table = type.equals("lost") ? "lost_items" : "found_items";
            String locationCol = type.equals("lost") ? "location_lost" : "location_found";
            String dateCol = type.equals("lost") ? "date_lost" : "date_found";
            
            String sql = "SELECT item_name, " + locationCol + " as location, " + dateCol + " as date, " +
                        "description, image_path, reported_by, status, date_reported FROM " + table + 
                        " WHERE id = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, ticketId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                String itemName = rs.getString("item_name");
                String location = rs.getString("location");
                String date = rs.getString("date");
                String description = rs.getString("description");
                String imagePath = rs.getString("image_path");
                String reportedBy = rs.getString("reported_by");
                String status = rs.getString("status");
                String dateReported = rs.getString("date_reported");
                
                // Top panel - Header
                JPanel headerPanel = new JPanel(new BorderLayout());
                headerPanel.setBackground(new Color(64, 0, 0));
                
                // Scale the padding
                int padding = (int)(15 * Math.min(ResponsiveScaler.scaleX, ResponsiveScaler.scaleY));
                headerPanel.setBorder(BorderFactory.createEmptyBorder(padding, padding, padding, padding));
                
                JLabel lblHeader = new JLabel(type.toUpperCase() + " ITEM REPORT");
                Font headerFont = new Font("Tahoma", Font.BOLD, 18);
                lblHeader.setFont(ResponsiveScaler.scaleFont(headerFont));
                lblHeader.setForeground(Color.WHITE);
                headerPanel.add(lblHeader, BorderLayout.WEST);
                
                JLabel lblStatus = new JLabel(status.toUpperCase());
                Font statusFont = new Font("Tahoma", Font.BOLD, 14);
                lblStatus.setFont(ResponsiveScaler.scaleFont(statusFont));
                if (status.equals("Pending")) {
                    lblStatus.setForeground(new Color(255, 193, 7));
                } else if (status.equals("Resolved")) {
                    lblStatus.setForeground(new Color(40, 167, 69));
                }
                headerPanel.add(lblStatus, BorderLayout.EAST);
                
                add(headerPanel, BorderLayout.NORTH);
                
                // Center panel - Content
                JPanel contentPanel = new JPanel();
                contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
                contentPanel.setBackground(Color.WHITE);
                
                int contentPadding = (int)(20 * Math.min(ResponsiveScaler.scaleX, ResponsiveScaler.scaleY));
                contentPanel.setBorder(BorderFactory.createEmptyBorder(contentPadding, contentPadding, contentPadding, contentPadding));
                
                // Image section with HIGH-QUALITY scaling
                if (imagePath != null && !imagePath.isEmpty()) {
                    try {
                        File imgFile = new File(imagePath);
                        if (imgFile.exists()) {
                            // Load image using ImageIO for better quality
                            BufferedImage originalImage = ImageIO.read(imgFile);
                            
                            if (originalImage != null) {
                                // Calculate scaled size while maintaining aspect ratio
                                int maxSize = (int)(350 * Math.min(ResponsiveScaler.scaleX, ResponsiveScaler.scaleY));
                                int imgWidth = originalImage.getWidth();
                                int imgHeight = originalImage.getHeight();
                                
                                double aspectRatio = (double) imgWidth / imgHeight;
                                int scaledWidth, scaledHeight;
                                
                                if (imgWidth > imgHeight) {
                                    scaledWidth = maxSize;
                                    scaledHeight = (int)(maxSize / aspectRatio);
                                } else {
                                    scaledHeight = maxSize;
                                    scaledWidth = (int)(maxSize * aspectRatio);
                                }
                                
                                // Use high-quality scaling
                                BufferedImage scaledImage = getScaledImage(originalImage, scaledWidth, scaledHeight);
                                
                                JLabel lblImage = new JLabel(new ImageIcon(scaledImage));
                                lblImage.setAlignmentX(Component.CENTER_ALIGNMENT);
                                lblImage.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 2));
                                contentPanel.add(lblImage);
                                contentPanel.add(Box.createVerticalStrut((int)(20 * ResponsiveScaler.scaleY)));
                            } else {
                                addNoImageLabel(contentPanel);
                            }
                        } else {
                            addNoImageLabel(contentPanel);
                        }
                    } catch (Exception ex) {
                        System.err.println("Error loading image: " + ex.getMessage());
                        ex.printStackTrace();
                        addNoImageLabel(contentPanel);
                    }
                } else {
                    addNoImageLabel(contentPanel);
                }            
                
                // Details
                addDetailRow(contentPanel, "Item Name:", itemName);
                addDetailRow(contentPanel, "Location " + (type.equals("lost") ? "Lost:" : "Found:"), location);
                addDetailRow(contentPanel, "Date " + (type.equals("lost") ? "Lost:" : "Found:"), date);
                addDetailRow(contentPanel, "Reported By:", reportedBy);
                addDetailRow(contentPanel, "Date Reported:", dateReported != null ? dateReported.substring(0, 10) : "N/A");
                
                contentPanel.add(Box.createVerticalStrut((int)(10 * ResponsiveScaler.scaleY)));
                
                // Description
                JLabel lblDescTitle = new JLabel("Description:");
                Font descTitleFont = new Font("Tahoma", Font.BOLD, 13);
                lblDescTitle.setFont(ResponsiveScaler.scaleFont(descTitleFont));
                lblDescTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
                contentPanel.add(lblDescTitle);
                
                contentPanel.add(Box.createVerticalStrut((int)(5 * ResponsiveScaler.scaleY)));
                
                JTextArea txtDescription = RoundedComponents.createRoundedTextArea(
                    new Color(255, 255, 255),
                    Color.BLACK,
                    Color.WHITE,
                    15,
                    5,
                    40
                );
                txtDescription.setText(description);
                txtDescription.setEditable(false);
                Font descFont = new Font("Tahoma", Font.PLAIN, 12);
                txtDescription.setFont(ResponsiveScaler.scaleFont(descFont));
                
                JScrollPane descScroll = RoundedComponents.createRoundedScrollPane(
                    txtDescription, 
                    Color.WHITE,
                    15
                );
                
                // Scale scroll pane size
                int scrollWidth = (int)(500 * ResponsiveScaler.scaleX);
                int scrollHeight = (int)(100 * ResponsiveScaler.scaleY);
                descScroll.setPreferredSize(new Dimension(scrollWidth, scrollHeight));
                descScroll.getVerticalScrollBar().setUnitIncrement(16); 
                descScroll.getVerticalScrollBar().setBlockIncrement(50);
                descScroll.setAlignmentX(Component.LEFT_ALIGNMENT);
                contentPanel.add(descScroll);
                
                JScrollPane scrollPane = new JScrollPane(contentPanel);
                scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
                scrollPane.getVerticalScrollBar().setUnitIncrement(16); 
                scrollPane.getVerticalScrollBar().setBlockIncrement(50);
                add(scrollPane, BorderLayout.CENTER);
                
                // Bottom panel - Close button
                JPanel bottomPanel = new JPanel();
                int bottomPadding = (int)(10 * Math.min(ResponsiveScaler.scaleX, ResponsiveScaler.scaleY));
                bottomPanel.setBorder(BorderFactory.createEmptyBorder(bottomPadding, bottomPadding, bottomPadding, bottomPadding));
                
                JButton btnClose = new JButton("Close");
                Font btnFont = new Font("Tahoma", Font.BOLD, 12);
                btnClose.setFont(ResponsiveScaler.scaleFont(btnFont));
                btnClose.addActionListener(e -> dispose());
                
                bottomPanel.add(btnClose);          
                add(bottomPanel, BorderLayout.SOUTH);
            }
            
        } catch (Exception ex) {
            System.err.println("Error loading ticket details: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
    
    private void addNoImageLabel(JPanel contentPanel) {
        JLabel lblNoImage = new JLabel("No image available");
        lblNoImage.setAlignmentX(Component.CENTER_ALIGNMENT);
        Font noImageFont = new Font("Tahoma", Font.ITALIC, 12);
        lblNoImage.setFont(ResponsiveScaler.scaleFont(noImageFont));
        lblNoImage.setForeground(Color.GRAY);
        contentPanel.add(lblNoImage);
        contentPanel.add(Box.createVerticalStrut((int)(20 * ResponsiveScaler.scaleY)));
    }
    
    private void addDetailRow(JPanel parent, String label, String value) {
        JPanel rowPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 5));
        rowPanel.setBackground(Color.WHITE);
        rowPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        rowPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, (int)(30 * ResponsiveScaler.scaleY)));
        
        JLabel lblLabel = new JLabel(label);
        Font labelFont = new Font("Tahoma", Font.BOLD, 13);
        lblLabel.setFont(ResponsiveScaler.scaleFont(labelFont));
        lblLabel.setPreferredSize(new Dimension((int)(150 * ResponsiveScaler.scaleX), (int)(20 * ResponsiveScaler.scaleY)));
        rowPanel.add(lblLabel);
        
        JLabel lblValue = new JLabel(value);
        Font valueFont = new Font("Tahoma", Font.PLAIN, 13);
        lblValue.setFont(ResponsiveScaler.scaleFont(valueFont));
        rowPanel.add(lblValue);
        
        parent.add(rowPanel);
    }
}