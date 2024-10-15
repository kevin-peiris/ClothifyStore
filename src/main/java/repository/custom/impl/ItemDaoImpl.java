package repository.custom.impl;

import entity.EmployeeEntity;
import entity.ItemEntity;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.hibernate.Session;
import repository.custom.ItemDao;
import util.HibernateUtil;

public class ItemDaoImpl implements ItemDao {
    @Override
    public boolean save(ItemEntity item) {
        Session session = HibernateUtil.getSession();
        session.beginTransaction();
        session.persist(item);
        session.getTransaction().commit();
        session.close();

        return false;
    }

    @Override
    public boolean delete(ItemEntity item) {
        Session session = HibernateUtil.getSession();
        session.beginTransaction();
        session.remove(item);
        session.getTransaction().commit();
        session.close();

        return true;
    }

    @Override
    public ObservableList<ItemEntity> getAll() {
        ObservableList<ItemEntity> itemList = FXCollections.observableArrayList();

        try (Session session = HibernateUtil.getSession()) {
            itemList.addAll(session.createQuery("FROM ItemEntity", ItemEntity.class).list());
        }
        return itemList;
    }

    @Override
    public boolean update(ItemEntity item) {
        Session session = HibernateUtil.getSession();
        session.beginTransaction();
        session.update(item);
        session.getTransaction().commit();
        session.close();

        return true;
    }

    @Override
    public ItemEntity search(String id) {
        Session session = HibernateUtil.getSession();
        session.beginTransaction();
        ItemEntity item=session.get(ItemEntity.class, id);
        session.getTransaction().commit();
        session.close();

        return item;
    }
}
