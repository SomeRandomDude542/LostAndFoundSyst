package laf;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.time.LocalDateTime;

public class NotificationManager {
    
    public static void createNotification(String username, String title, String message, 
                                         String type, int relatedId) {
        try (Connection conn = MySQLConnection.connect()) {
            String sql = "INSERT INTO notifications (username, title, message, type, timestamp, related_id) " +
                        "VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, username);
            pstmt.setString(2, title);
            pstmt.setString(3, message);
            pstmt.setString(4, type);
            pstmt.setString(5, LocalDateTime.now().toString());
            pstmt.setInt(6, relatedId);
            pstmt.executeUpdate();
            
            System.out.println("Notification created for: " + username);
            
        } catch (Exception ex) {
            System.err.println("Error creating notification: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
}