package service;

import dao.UserDao;
import doanthietkethucdon.BHException;
import entity.UserEntity;
import java.util.ArrayList;
import java.util.List;
import model.User;

/**
 *
 * @author ADMIN
 */
public class UserService {
    private static UserService instance;
    private UserDao userDao;
    private List<User> userList = new ArrayList<>();

    public static UserService getInstance() {
        if (instance == null) {
            instance = new UserService();
        }
        return instance;
    }

    private UserService() {
        this.userDao = new UserDao();
    }

    public void loadAllUsersFromDatabase() {
        userList.clear();
        List<UserEntity> entities = userDao.getAllUsers();
        
        for (UserEntity entity : entities) {
            try {
                userList.add(fromEntity(entity));
            } catch (BHException ex) {
                // Skip invalid users
            }
        }
    }
    

    public List<User> getAllUsers() {
        return userList;
    }

    public User getUserById(int id) {
        for (User user : userList) {
            if (user.getId() == id) {
                return user;
            }
        }

        UserEntity entity = userDao.findById(id);
        if (entity != null) {
            try {
                return fromEntity(entity);
            } catch (BHException ex) {
                return null;
            }
        }
        
        return null;
    }


    public boolean addUser(User user) {
        try {
            UserEntity entity = toEntity(user);
            boolean result = userDao.addUser(entity);
            if (result) {
                loadAllUsersFromDatabase();
            }
            return result;
        } catch (Exception ex) {
            return false;
        }
    }

    public boolean updateUser(User user) {
        try {
            UserEntity entity = toEntity(user);
            boolean result = userDao.updateUser(entity);
            if (result) {
                loadAllUsersFromDatabase();
            }
            return result;
        } catch (Exception ex) {
            return false;
        }
    }

    public boolean deleteUser(int userId) {
        boolean result = userDao.deleteUser(userId);
        if (result) {
            loadAllUsersFromDatabase();
        }
        return result;
    }


    private User fromEntity(UserEntity entity) throws BHException {
        return new User(
                entity.getId(),
                entity.getUsername(),
                entity.getPassword(),
                entity.getFullName(),
                entity.getEmail(),
                entity.getPhone(),
                entity.getRole()
        );
    }

    private UserEntity toEntity(User model) {
        return new UserEntity(
                model.getId(),
                model.getUsername(),
                model.getPassword(),
                model.getFullName(),
                model.getEmail(),
                model.getPhone(),
                model.getRole()
        );
    }
} 