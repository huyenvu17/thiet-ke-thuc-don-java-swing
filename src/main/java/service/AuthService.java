package service;

import entity.UserEntity;
import org.mindrot.jbcrypt.BCrypt;

/**
 *
 * @author ADMIN
 */
public class AuthService {
    private static UserEntity currentUserEntity = null;
    
    /**
     * Check if a user is authenticated
     * @return true if a user is logged in, false otherwise
     */
    public static boolean isAuthenticated() {
        return currentUserEntity != null;
    }
    
    /**
     * Get the currently logged in user
     * @return the current user or null if no user is logged in
     */
    public static UserEntity getCurrentUser() {
        return currentUserEntity;
    }
    
    /**
     * Set the current logged in user
     * @param userEntity The user to set as currently logged in
     */
    public static void setCurrentUser(UserEntity userEntity) {
        currentUserEntity = userEntity;
    }
    
    /**
     * Logout the current user
     */
    public static void logout() {
        currentUserEntity = null;
    }
    
    /**
     * Hash a password using BCrypt
     * @param plainPassword The plain text password to hash
     * @return The hashed password
     */
    public static String hashPassword(String plainPassword) {
        return BCrypt.hashpw(plainPassword, BCrypt.gensalt());
    }
    
    /**
     * Check if a plain text password matches a stored password
     * @param plainPassword The plain text password to check
     * @param storedPassword The stored password to compare against, may be hashed or plaintext
     * @return true if the passwords match, false otherwise
     */
    public static boolean checkPassword(String plainPassword, String storedPassword) {
        // Kiểm tra nếu mật khẩu được lưu trữ là mật khẩu BCrypt
        if (isBCryptHash(storedPassword)) {
            try {
                return BCrypt.checkpw(plainPassword, storedPassword);
            } catch (Exception e) {
                // Trong trường hợp có lỗi (có thể là do salt không hợp lệ)
                // Thực hiện so sánh văn bản thuần túy
                return plainPassword.equals(storedPassword);
            }
        } else {
            // Nếu không phải là hash BCrypt, so sánh trực tiếp
            return plainPassword.equals(storedPassword);
        }
    }
    
    /**
     * Check if a password is hashed with BCrypt
     * @param password The password string to check
     * @return true if it appears to be a BCrypt hash, false otherwise
     */
    private static boolean isBCryptHash(String password) {
        // BCrypt hash thường bắt đầu với $2a$, $2b$ hoặc $2y$
        return password != null && 
               password.length() > 4 && 
               (password.startsWith("$2a$") || 
                password.startsWith("$2b$") || 
                password.startsWith("$2y$"));
    }
} 