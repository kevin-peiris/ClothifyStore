package repository.custom.impl;

import entity.AdminEntity;
import entity.UserEntity;
import javafx.collections.ObservableList;
import org.hibernate.Session;
import repository.custom.AdminDao;
import util.HibernateUtil;

public class AdminDaoImpl implements AdminDao {

    @Override
    public boolean save(AdminEntity adminEntity) {
        Session session = HibernateUtil.getSession();
        session.beginTransaction();
        session.persist(adminEntity);
        session.getTransaction().commit();
        session.close();

        return true;
    }

    @Override
    public boolean delete(AdminEntity adminEntity) {
        return false;
    }

    @Override
    public ObservableList<AdminEntity> getAll() {
        return null;
    }

    @Override
    public boolean update(AdminEntity adminEntity) {
        return false;
    }

    @Override
    public AdminEntity search(String id) {
        return null;
    }

    @Override
    public AdminEntity getAdminByEmail(String emailAddress) {
        Session session = HibernateUtil.getSession();
        session.beginTransaction();
        AdminEntity admin = session.createQuery("FROM AdminEntity WHERE email = :email", AdminEntity.class)
                .setParameter("email", emailAddress)
                .uniqueResult();


        session.getTransaction().commit();
        session.close();

        return admin;
    }
}
