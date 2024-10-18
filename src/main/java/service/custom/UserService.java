package service.custom;

import dto.User;
import service.SuperService;

public interface UserService extends SuperService {
    boolean registerUser(User user);
    User loginUser(String email, String password);
}
