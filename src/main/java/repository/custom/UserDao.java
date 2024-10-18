package repository.custom;

import dto.User;
import entity.UserEntity;
import repository.CrudDao;

public interface UserDao extends CrudDao<UserEntity> {
    UserEntity getUserByEmail(String emailAddress);
}
