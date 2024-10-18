package repository;

import repository.custom.impl.*;
import util.DaoType;

public class DaoFactory {
    private static DaoFactory instance;

    private DaoFactory(){}

    public static DaoFactory getInstance(){
        return instance==null? instance=new DaoFactory(): instance;
    }

    public <T extends SuperDao>T getDaoType(DaoType type){
        switch (type){
            case EMPLOYEE:return (T) new EmployeeDaoImpl();
            case ORDER:return (T) new OrderDaoImpl();
            case ITEM:return (T) new ItemDaoImpl();
            case SUPPLIER:return (T) new SupplierDaoImpl();
            case ORDER_DETAILS:return (T) new OrderDetailsDaoImpl();
            case USER:return (T) new UserDaoImpl();
            case ADMIN:return (T) new AdminDaoImpl();
        }
        return null;
    }
}












