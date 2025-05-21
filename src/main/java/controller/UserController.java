package controller;

import dto.UserDTO;
import entity.UserEntity;
import model.User;
import service.AuthService;
import service.UserService;
import java.util.List;
import java.util.ArrayList;
import doanthietkethucdon.BHException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ADMIN
 */
public class UserController implements IUserController {
    private static UserController instance;
    private final UserService userService;

    public static UserController getInstance() {
        if (instance == null) {
            instance = new UserController();
        }
        return instance;
    }

    private UserController() {
        this.userService = UserService.getInstance();
        this.userService.loadAllUsersFromDatabase();
    }
    
    @Override
    public List<UserEntity> getAllUsers() {
        List<User> userModels = userService.getAllUsers();
        List<UserEntity> userEntities = new ArrayList<>();
        
        for (User model : userModels) {
            userEntities.add(new UserEntity(
                model.getId(),
                model.getUsername(),
                model.getPassword(),
                model.getFullName(),
                model.getEmail(),
                model.getPhone(),
                model.getRole()
            ));
        }
        
        return userEntities;
    }
    
    @Override
    public UserEntity getUserById(int id) {
        User user = userService.getUserById(id);
        if (user != null) {
            return new UserEntity(
                user.getId(),
                user.getUsername(),
                user.getPassword(),
                user.getFullName(),
                user.getEmail(),
                user.getPhone(),
                user.getRole()
            );
        }
        return null;
    }
    
    @Override
    public boolean addUser(UserDTO userDto) {
        if (userDto == null || userDto.getUsername() == null || userDto.getPassword() == null 
                || userDto.getFullName() == null) {
            return false;
        }
        
        try {
            User user = new User(
                0,
                userDto.getUsername(),
                userDto.getPassword(),
                userDto.getFullName(),
                userDto.getEmail(),
                userDto.getPhone(),
                userDto.getRole() != null ? userDto.getRole() : "user" // Default
            );
            
            return userService.addUser(user);
        } catch (BHException ex) {
            Logger.getLogger(UserController.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }
    
    @Override
    public boolean updateUserProfile(UserDTO userDto) {
        if (userDto == null || userDto.getId() <= 0) {
            return false;
        }

        User existingUser = userService.getUserById(userDto.getId());
        if (existingUser == null) {
            return false;
        }
        
        try {
            User updatedUser = new User(
                existingUser.getId(),
                existingUser.getUsername(),
                userDto.getPassword() != null ? userDto.getPassword() : existingUser.getPassword(),
                userDto.getFullName(),
                userDto.getEmail(),
                userDto.getPhone(),
                userDto.getRole() != null ? userDto.getRole() : existingUser.getRole()
            );
            
            return userService.updateUser(updatedUser);
        } catch (BHException ex) {
            Logger.getLogger(UserController.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }
    
    @Override
    public boolean changePassword(int userId, String currentPassword, String newPassword) {
        if (userId <= 0 || currentPassword == null || newPassword == null) {
            return false;
        }

        User existingUser = userService.getUserById(userId);
        if (existingUser == null) {
            return false;
        }

        if (!AuthService.checkPassword(currentPassword, existingUser.getPassword())) {
            return false;
        }
        
        try {
            User updatedUser = new User(
                existingUser.getId(),
                existingUser.getUsername(),
                newPassword,
                existingUser.getFullName(),
                existingUser.getEmail(),
                existingUser.getPhone(),
                existingUser.getRole()
            );
            
            return userService.updateUser(updatedUser);
        } catch (BHException ex) {
            Logger.getLogger(UserController.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }
    
    @Override
    public boolean deleteUser(int userId) {
        if (userId <= 0) {
            return false;
        }
        
        return userService.deleteUser(userId);
    }
    
    @Override
    public boolean isValidEmail(String email) {
        return User.isValidEmail(email);
    }
    
    @Override
    public boolean isValidPhoneNumber(String phoneNumber) {
        return User.isValidPhoneNumber(phoneNumber);
    }
} 