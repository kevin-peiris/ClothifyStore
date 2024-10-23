package service.custom.impl;

import dto.Admin;
import dto.User;
import entity.AdminEntity;
import entity.UserEntity;
import org.mindrot.jbcrypt.BCrypt;
import org.modelmapper.ModelMapper;
import repository.DaoFactory;
import repository.custom.AdminDao;
import repository.custom.UserDao;
import service.custom.AdminService;
import util.DaoType;

public class AdminServiceImpl implements AdminService {
    @Override
    public boolean registerUser(Admin admin) {
        String hashedPassword = BCrypt.hashpw(admin.getPassword(), BCrypt.gensalt());
        admin.setPassword(hashedPassword);
        AdminEntity entity = new ModelMapper().map(admin, AdminEntity.class);
        AdminDao adminDao = DaoFactory.getInstance().getDaoType(DaoType.ADMIN);
        adminDao.save(entity);
        return true;
    }

    @Override
    public Admin loginAdmin(String email, String password) {
        AdminDao adminDao = DaoFactory.getInstance().getDaoType(DaoType.ADMIN);
        AdminEntity entity = adminDao.getAdminByEmail(email);
        if (entity == null || !BCrypt.checkpw(password, entity.getPassword())) {
            return null;
        }else {
            return new ModelMapper().map(entity, Admin.class);
        }
    }

    @Override
    public Admin getAdminByEmail(String email) {
        AdminDao adminDao = DaoFactory.getInstance().getDaoType(DaoType.ADMIN);
        AdminEntity entity = adminDao.getAdminByEmail(email);
        if (entity == null) {
            return null;
        }else {
            return new ModelMapper().map(entity, Admin.class);
        }
    }
}
