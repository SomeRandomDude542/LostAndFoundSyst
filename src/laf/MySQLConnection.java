package laf;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class MySQLConnection {

    // ⚙️ CONFIGURE THESE FOR YOUR SETUP
    private static final String DB_HOST = "localhost";
    private static final String DB_PORT = "3307";
    private static final String DB_NAME = "Lostandfound";
    private static final String DB_USER = "root";
    private static final String DB_PASS = "!@partosaian12345@!";

    public static Connection connect() {
        Connection conn = null;

        try {
            // Load MySQL JDBC Driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Build connection URL
            String url = "jdbc:mysql://" + DB_HOST + ":" + DB_PORT + "/" + DB_NAME +
                    "?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";

            System.out.println("Connecting to MySQL: " + url);
            conn = DriverManager.getConnection(url, DB_USER, DB_PASS);

            System.out.println("✅ Connected to MySQL database!");

            // Create tables if they don't exist
            createTablesIfNeeded(conn);

        } catch (ClassNotFoundException e) {
            System.err.println("❌ MySQL JDBC Driver not found!");
            System.err.println("Add mysql-connector-java to your project!");
            e.printStackTrace();

        } catch (SQLException e) {
            System.err.println("❌ MySQL Connection Error: " + e.getMessage());
            e.printStackTrace();
        }

        return conn;
    }

    private static void createTablesIfNeeded(Connection conn) {
        try {
            Statement stmt = conn.createStatement();

            // ✅ Users table
            String createUsersTable =
                    "CREATE TABLE IF NOT EXISTS users (" +
                            "id INT AUTO_INCREMENT PRIMARY KEY, " +
                            "username VARCHAR(255) NOT NULL UNIQUE, " +
                            "email VARCHAR(255) NOT NULL, " +
                            "password VARCHAR(255) NOT NULL, " +
                            "role VARCHAR(50) DEFAULT 'user', " +
                            "account_status VARCHAR(50) DEFAULT 'active', " +
                            "ban_reason TEXT, " +
                            "suspension_end_date VARCHAR(50)" +
                    ");";
            stmt.execute(createUsersTable);
            System.out.println("✅ Users table ready");

            // ✅ Lost items table
            String createLostItemsTable =
                    "CREATE TABLE IF NOT EXISTS lost_items (" +
                            "id INT AUTO_INCREMENT PRIMARY KEY, " +
                            "item_name VARCHAR(255) NOT NULL, " +
                            "location_lost VARCHAR(255) NOT NULL, " +
                            "date_lost VARCHAR(50) NOT NULL, " +
                            "description TEXT, " +
                            "image_path VARCHAR(500), " +
                            "reported_by VARCHAR(255) NOT NULL, " +
                            "status VARCHAR(50) DEFAULT 'Pending', " +
                            "date_reported TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                            "deleted_date VARCHAR(50)" +
                    ");";
            stmt.execute(createLostItemsTable);
            System.out.println("✅ Lost items table ready");

            // ✅ Found items table
            String createFoundItemsTable =
                    "CREATE TABLE IF NOT EXISTS found_items (" +
                            "id INT AUTO_INCREMENT PRIMARY KEY, " +
                            "item_name VARCHAR(255) NOT NULL, " +
                            "location_found VARCHAR(255) NOT NULL, " +
                            "date_found VARCHAR(50) NOT NULL, " +
                            "description TEXT, " +
                            "image_path VARCHAR(500), " +
                            "reported_by VARCHAR(255) NOT NULL, " +
                            "status VARCHAR(50) DEFAULT 'Pending', " +
                            "date_reported TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                            "deleted_date VARCHAR(50)" +
                    ");";
            stmt.execute(createFoundItemsTable);
            System.out.println("✅ Found items table ready");

            // ✅ Messages table
            String createMessagesTable =
                    "CREATE TABLE IF NOT EXISTS messages (" +
                            "id INT AUTO_INCREMENT PRIMARY KEY, " +
                            "report_type VARCHAR(50) NOT NULL, " +
                            "report_id INT NOT NULL, " +
                            "sender VARCHAR(255) NOT NULL, " +
                            "receiver VARCHAR(255) NOT NULL, " +
                            "message TEXT NOT NULL, " +
                            "timestamp VARCHAR(50) NOT NULL, " +
                            "is_read INT DEFAULT 0, " +
                            "image_path VARCHAR(500)" +
                    ");";
            stmt.execute(createMessagesTable);
            System.out.println("✅ Messages table ready");

            // ✅ Notifications table
            String createNotificationsTable =
                    "CREATE TABLE IF NOT EXISTS notifications (" +
                            "id INT AUTO_INCREMENT PRIMARY KEY, " +
                            "username VARCHAR(255) NOT NULL, " +
                            "title VARCHAR(255) NOT NULL, " +
                            "message TEXT NOT NULL, " +
                            "type VARCHAR(50) NOT NULL, " +
                            "timestamp VARCHAR(50) NOT NULL, " +
                            "is_read INT DEFAULT 0, " +
                            "related_id INT" +
                    ");";
            stmt.execute(createNotificationsTable);
            System.out.println("✅ Notifications table ready");

            stmt.close();
            System.out.println("✅ All tables created successfully!");

        } catch (SQLException e) {
            System.err.println("❌ Table creation error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
