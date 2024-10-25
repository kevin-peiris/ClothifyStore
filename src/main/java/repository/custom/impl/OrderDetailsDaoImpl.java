package repository.custom.impl;

import dto.OrderDetails;
import entity.OrderDetailsEntity;
import jakarta.persistence.TypedQuery;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.hibernate.Session;
import repository.custom.OrderDetailsDao;
import util.HibernateUtil;

import java.util.List;


public class OrderDetailsDaoImpl implements OrderDetailsDao {

    @Override
    public boolean save(OrderDetailsEntity orderDetailsEntity) {
        Session session = HibernateUtil.getSession();
        session.beginTransaction();
        session.persist(orderDetailsEntity);
        session.getTransaction().commit();
        session.close();

        return true;
    }

    @Override
    public boolean delete(OrderDetailsEntity orderDetailsEntity) {
        Session session = HibernateUtil.getSession();
        session.beginTransaction();
        session.remove(orderDetailsEntity);
        session.getTransaction().commit();
        session.close();

        return true;
    }

    @Override
    public ObservableList<OrderDetailsEntity> getAll() {
        ObservableList<OrderDetailsEntity> orderDetailsList = FXCollections.observableArrayList();

        try (Session session = HibernateUtil.getSession()) {
            orderDetailsList.addAll(session.createQuery("FROM OrderDetailsEntity", OrderDetailsEntity.class).list());
        }

        return orderDetailsList;
    }

    @Override
    public boolean update(OrderDetailsEntity orderDetailsEntity) {
        Session session = HibernateUtil.getSession();
        session.beginTransaction();
        session.update(orderDetailsEntity);
        session.getTransaction().commit();
        session.close();

        return true;
    }

    @Override
    public OrderDetailsEntity search(String id) {
        Session session = HibernateUtil.getSession();
        session.beginTransaction();
        OrderDetailsEntity orderDetails = session.get(OrderDetailsEntity.class, id);
        session.getTransaction().commit();
        session.close();

        return orderDetails;
    }


}
