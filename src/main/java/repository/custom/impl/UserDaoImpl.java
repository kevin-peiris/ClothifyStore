package repository.custom.impl;

import dto.User;
import entity.SupplierEntity;
import entity.UserEntity;
import javafx.collections.ObservableList;
import org.hibernate.Session;
import repository.custom.UserDao;
import util.HibernateUtil;

public class UserDaoImpl implements UserDao {
    @Override
    public boolean save(UserEntity userEntity) {
        Session session = HibernateUtil.getSession();
        session.beginTransaction();
        session.persist(userEntity);
        session.getTransaction().commit();
        session.close();

        return true;
    }

    @Override
    public boolean delete(UserEntity userEntity) {
        return false;
    }

    @Override
    public ObservableList<UserEntity> getAll() {
        return null;
    }

    @Override
    public boolean update(UserEntity userEntity) {
        return false;
    }

    @Override
    public UserEntity search(String id) {
        return null;
    }

    @Override
    public UserEntity getUserByEmail(String emailAddress) {
        Session session = HibernateUtil.getSession();
        session.beginTransaction();
        UserEntity user = session.createQuery("FROM UserEntity WHERE email = :email", UserEntity.class)
                .setParameter("email", emailAddress)
                .uniqueResult();

        session.getTransaction().commit();
        session.close();

        return user;
    }
}
