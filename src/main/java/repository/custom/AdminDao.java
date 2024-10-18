package repository.custom;

import entity.AdminEntity;
import entity.UserEntity;
import repository.CrudDao;

public interface AdminDao extends CrudDao<AdminEntity> {
    AdminEntity getAdminByEmail(String emailAddress);
}
