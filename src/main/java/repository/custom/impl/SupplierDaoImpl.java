package repository.custom.impl;

import entity.SupplierEntity;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.hibernate.Session;
import repository.custom.SupplierDao;
import util.HibernateUtil;

public class SupplierDaoImpl implements SupplierDao {
    @Override
    public boolean save(SupplierEntity supplierEntity) {
        Session session = HibernateUtil.getSession();
        session.beginTransaction();
        session.persist(supplierEntity);
        session.getTransaction().commit();
        session.close();

        return true;
    }

    @Override
    public boolean delete(SupplierEntity supplierEntity) {
        Session session = HibernateUtil.getSession();
        session.beginTransaction();
        session.remove(supplierEntity);
        session.getTransaction().commit();
        session.close();

        return true;
    }

    @Override
    public ObservableList<SupplierEntity> getAll() {
        ObservableList<SupplierEntity> supplierList = FXCollections.observableArrayList();

        try (Session session = HibernateUtil.getSession()) {
            supplierList.addAll(session.createQuery("FROM SupplierEntity", SupplierEntity.class).list());
        }

        return supplierList;
    }

    @Override
    public boolean update(SupplierEntity supplierEntity) {
        Session session = HibernateUtil.getSession();
        session.beginTransaction();
        session.update(supplierEntity);
        session.getTransaction().commit();
        session.close();

        return true;
    }

    @Override
    public SupplierEntity search(String id) {
        Session session = HibernateUtil.getSession();
        session.beginTransaction();
        SupplierEntity supplier = session.get(SupplierEntity.class, id);
        session.getTransaction().commit();
        session.close();

        return supplier;
    }
}
















