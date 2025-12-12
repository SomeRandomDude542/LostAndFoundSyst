package laf;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * Secure password hashing utility using SHA-256 with salt
 * This protects passwords from being stored in plain text
 */
public class PasswordHasher {
    
    private static final int SALT_LENGTH = 16;
    
    /**
     * Hashes a password with a randomly generated salt
     * @param password The plain text password
     * @return A string containing salt and hash separated by ':'
     */
    public static String hashPassword(String password) {
        try {
            // Generate random salt
            SecureRandom random = new SecureRandom();
            byte[] salt = new byte[SALT_LENGTH];
            random.nextBytes(salt);
            
            // Hash password with salt
            String hash = hashWithSalt(password, salt);
            
            // Return salt + hash (so we can verify later)
            return Base64.getEncoder().encodeToString(salt) + ":" + hash;
            
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error hashing password", e);
        }
    }
    
    /**
     * Verifies if a password matches the stored hash
     * @param password The plain text password to verify
     * @param storedHash The stored hash from database (salt:hash format)
     * @return true if password matches, false otherwise
     */
    public static boolean verifyPassword(String password, String storedHash) {
        try {
            // Split stored hash into salt and hash
            String[] parts = storedHash.split(":");
            if (parts.length != 2) {
                return false;
            }
            
            byte[] salt = Base64.getDecoder().decode(parts[0]);
            String originalHash = parts[1];
            
            // Hash the provided password with the same salt
            String testHash = hashWithSalt(password, salt);
            
            // Compare hashes
            return originalHash.equals(testHash);
            
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Internal method to hash password with given salt
     */
    private static String hashWithSalt(String password, byte[] salt) 
            throws NoSuchAlgorithmException {
        
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(salt);
        byte[] hashedPassword = md.digest(password.getBytes());
        
        return Base64.getEncoder().encodeToString(hashedPassword);
    }
    
    /**
     * Validates password strength
     * @param password The password to validate
     * @return Error message if invalid, null if valid
     */
    public static String validatePasswordStrength(String password) {
        if (password == null || password.length() < 8) {
            return "Password must be at least 8 characters long";
        }
        
        boolean hasUpper = false;
        boolean hasLower = false;
        boolean hasDigit = false;
        boolean hasSpecial = false;
        
        for (char c : password.toCharArray()) {
            if (Character.isUpperCase(c)) hasUpper = true;
            else if (Character.isLowerCase(c)) hasLower = true;
            else if (Character.isDigit(c)) hasDigit = true;
            else hasSpecial = true;
        }
        
        if (!hasUpper) {
            return "Password must contain at least one uppercase letter";
        }
        if (!hasLower) {
            return "Password must contain at least one lowercase letter";
        }
        if (!hasDigit) {
            return "Password must contain at least one number";
        }
        
        // Optional: Uncomment if you want to require special characters
        // if (!hasSpecial) {
        //     return "Password must contain at least one special character";
        // }
        
        return null; // Password is valid
    }
}