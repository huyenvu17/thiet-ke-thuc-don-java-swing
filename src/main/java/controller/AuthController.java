package controller;

import dao.UserDao;
import dto.UserDTO;
import entity.UserEntity;
import service.AuthService;

/**
 *
 * @author ADMIN
 */
public class AuthController implements IAuthController {
    private static AuthController instance;
    private final UserDao userDao;
    
    /**
     * Get singleton instance
     */
    public static AuthController getInstance() {
        if (instance == null) {
            instance = new AuthController();
        }
        return instance;
    }

    private AuthController() {
        this.userDao = new UserDao();
    }
    
    @Override
    public UserEntity login(UserDTO loginDto) {
        if (loginDto == null || loginDto.getUsername() == null || loginDto.getPassword() == null) {
            return null;
        }
        UserEntity userEntity = userDao.login(loginDto.getUsername(), loginDto.getPassword());
        if (userEntity != null) {
            AuthService.setCurrentUser(userEntity);
        }
        
        return userEntity;
    }
    
    @Override
    public boolean register(UserDTO registerDto) {
        if (registerDto == null || registerDto.getUsername() == null || registerDto.getPassword() == null
                || registerDto.getFullName() == null) {
            return false;
        }
        
        if (usernameExists(registerDto.getUsername())) {
            return false;
        }
        
        UserEntity userEntity = new UserEntity(
                0,
                registerDto.getUsername(),
                registerDto.getPassword(),
                registerDto.getFullName(),
                registerDto.getEmail(),
                registerDto.getPhone(),
                registerDto.getRole() != null ? registerDto.getRole() : "user" // Default
        );
        
        return userDao.addUser(userEntity);
    }
    
    @Override
    public boolean usernameExists(String username) {
        if (username == null || username.trim().isEmpty()) {
            return false;
        }
        return userDao.findByUsername(username) != null;
    }
    
    @Override
    public UserEntity getCurrentUser() {
        return AuthService.getCurrentUser();
    }
    
    @Override
    public void logout() {
        AuthService.logout();
    }
    
    @Override
    public boolean isValidEmail(String email) {
        if (email == null || email.isEmpty()) {
            return false;
        }
        String emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        return email.matches(emailRegex);
    }
    
    @Override
    public boolean isValidPhoneNumber(String phoneNumber) {
        if (phoneNumber == null || phoneNumber.isEmpty()) {
            return false;
        }

        String phoneRegex = "^[0-9]+$";
        return phoneNumber.matches(phoneRegex);
    }
} 