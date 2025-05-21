package controller;

import dto.UserDTO;
import entity.UserEntity;

/**
 *
 * @author ADMIN
 */
public interface IAuthController {

    UserEntity login(UserDTO loginDto);
    boolean register(UserDTO registerDto);
    boolean usernameExists(String username);
    UserEntity getCurrentUser();
    void logout();
} 