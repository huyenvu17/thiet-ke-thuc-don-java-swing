package controller;

import dto.UserDTO;
import entity.UserEntity;
import java.util.List;

/**
 *
 * @author ADMIN
 */
public interface IUserController {

    List<UserEntity> getAllUsers();
    UserEntity getUserById(int id);
    boolean addUser(UserDTO userDto);
    boolean updateUserProfile(UserDTO userDto);
    boolean changePassword(int userId, String currentPassword, String newPassword);
    boolean deleteUser(int userId);
    boolean isValidEmail(String email);
    boolean isValidPhoneNumber(String phoneNumber);
} 