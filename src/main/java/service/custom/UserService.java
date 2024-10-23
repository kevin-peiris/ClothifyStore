package service.custom;

import dto.Employee;
import dto.User;
import entity.UserEntity;
import service.SuperService;

public interface UserService extends SuperService {
    boolean registerUser(User user);
    User loginUser(String email, String password);
    User getUserByEmail(String emailAddress);
    boolean updateUser(User user);
    boolean deleteUser(User user);
}
