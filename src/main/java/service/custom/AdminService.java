package service.custom;

import dto.Admin;
import dto.User;
import service.SuperService;

public interface AdminService extends SuperService {
    boolean registerUser(Admin admin);
    Admin loginAdmin(String email, String password);
}
