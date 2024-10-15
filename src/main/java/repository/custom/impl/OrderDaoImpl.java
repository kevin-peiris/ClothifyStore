package repository.custom.impl;

import entity.EmployeeEntity;
import entity.OrderEntity;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import repository.custom.OrderDao;
import util.HibernateUtil;

import java.util.List;

public class OrderDaoImpl implements OrderDao {
    @Override
    public boolean save(OrderEntity order) {
        Session session = HibernateUtil.getSession();
        session.beginTransaction();
        session.persist(order);
        session.getTransaction().commit();
        session.close();

        return false;
    }

    @Override
    public boolean delete(OrderEntity order) {
        Session session = HibernateUtil.getSession();
        session.beginTransaction();
        session.remove(order);
        session.getTransaction().commit();
        session.close();

        return true;
    }

    @Override
    public ObservableList<OrderEntity> getAll() {
        ObservableList<OrderEntity> orderList = FXCollections.observableArrayList();

        try (Session session = HibernateUtil.getSession()) {
            List<OrderEntity> orders = session.createQuery("FROM OrderEntity", OrderEntity.class).list();
            for (OrderEntity order : orders) {
                Hibernate.initialize(order.getOrderDetails());
            }
            orderList.addAll(orders);
        }

        return orderList;
    }


    @Override
    public boolean update(OrderEntity order) {
        Session session = HibernateUtil.getSession();
        session.beginTransaction();
        session.update(order);
        session.getTransaction().commit();
        session.close();

        return true;
    }

    @Override
    public OrderEntity search(String id) {
        Session session = HibernateUtil.getSession();
        session.beginTransaction();
        OrderEntity order=session.get(OrderEntity.class, id);
        session.getTransaction().commit();
        session.close();

        return order;
    }
}
