package repository.custom.impl;

import entity.EmployeeEntity;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.hibernate.Session;
import repository.custom.EmployeeDao;
import util.HibernateUtil;

public class EmployeeDaoImpl implements EmployeeDao {

    @Override
    public boolean save(EmployeeEntity employee) {
        Session session = HibernateUtil.getSession();
        session.beginTransaction();
        session.persist(employee);
        session.getTransaction().commit();
        session.close();

        return false;
    }

    @Override
    public boolean delete(EmployeeEntity employee) {

        Session session = HibernateUtil.getSession();
        session.beginTransaction();
        session.remove(employee);
        session.getTransaction().commit();
        session.close();

        return true;
    }

    @Override
    public ObservableList<EmployeeEntity> getAll() {
        ObservableList<EmployeeEntity> employeeList = FXCollections.observableArrayList();

        try (Session session = HibernateUtil.getSession()) {
            employeeList.addAll(session.createQuery("FROM EmployeeEntity", EmployeeEntity.class).list());
        }

        return employeeList;
    }

    @Override
    public boolean update(EmployeeEntity employee) {
        Session session = HibernateUtil.getSession();
        session.beginTransaction();
        session.update(employee);
        session.getTransaction().commit();
        session.close();

        return true;
    }

    @Override
    public EmployeeEntity search(String id) {
        Session session = HibernateUtil.getSession();
        session.beginTransaction();
        EmployeeEntity employee=session.get(EmployeeEntity.class, id);
        session.getTransaction().commit();
        session.close();

        return employee;
    }
}
