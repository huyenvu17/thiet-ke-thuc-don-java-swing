package service;

import entity.UserEntity;
import org.mindrot.jbcrypt.BCrypt;

/**
 *
 * @author ADMIN
 */
public class AuthService {
    private static UserEntity currentUserEntity = null;

    public static boolean isAuthenticated() {
        return currentUserEntity != null;
    }

    public static UserEntity getCurrentUser() {
        return currentUserEntity;
    }

    public static void setCurrentUser(UserEntity userEntity) {
        currentUserEntity = userEntity;
    }

    public static void logout() {
        currentUserEntity = null;
    }

    public static String hashPassword(String plainPassword) {
        return BCrypt.hashpw(plainPassword, BCrypt.gensalt());
    }

    public static boolean checkPassword(String plainPassword, String storedPassword) {
        // Kiểm tra nếu mật khẩu được lưu trữ là mật khẩu BCrypt
        if (isBCryptHash(storedPassword)) {
            try {
                return BCrypt.checkpw(plainPassword, storedPassword);
            } catch (Exception e) {
                return plainPassword.equals(storedPassword);
            }
        } else {
            return plainPassword.equals(storedPassword);
        }
    }

    private static boolean isBCryptHash(String password) {
        return password != null && 
               password.length() > 4 && 
               (password.startsWith("$2a$") || 
                password.startsWith("$2b$") || 
                password.startsWith("$2y$"));
    }
} 