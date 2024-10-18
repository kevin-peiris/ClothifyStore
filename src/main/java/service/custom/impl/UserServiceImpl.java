package service.custom.impl;

import dto.Supplier;
import dto.User;
import entity.SupplierEntity;
import entity.UserEntity;
import org.mindrot.jbcrypt.BCrypt;
import org.modelmapper.ModelMapper;
import repository.DaoFactory;
import repository.custom.SupplierDao;
import repository.custom.UserDao;
import service.custom.UserService;
import util.DaoType;

public class UserServiceImpl implements UserService {

    @Override
    public boolean registerUser(User user) {
        String hashedPassword = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());
        user.setPassword(hashedPassword);
        UserEntity entity = new ModelMapper().map(user, UserEntity.class);
        UserDao userDao = DaoFactory.getInstance().getDaoType(DaoType.USER);
        userDao.save(entity);
        return true;
    }

    @Override
    public User loginUser(String email, String password) {
        UserDao userDao = DaoFactory.getInstance().getDaoType(DaoType.USER);
        UserEntity entity = userDao.getUserByEmail(email);
        if (entity == null || !BCrypt.checkpw(password, entity.getPassword())) {
            return null;
        }else {
            return new ModelMapper().map(entity, User.class);
        }
    }
}
