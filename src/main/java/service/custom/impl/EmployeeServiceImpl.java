package service.custom.impl;

import dto.Employee;
import dto.User;
import entity.EmployeeEntity;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.modelmapper.ModelMapper;
import repository.DaoFactory;
import repository.custom.EmployeeDao;
import service.ServiceFactory;
import service.custom.EmployeeService;
import service.custom.UserService;
import util.DaoType;
import util.ServiceType;

public class EmployeeServiceImpl implements EmployeeService {

    @Override
    public boolean addEmployee(Employee employee) {
        EmployeeEntity entity=new ModelMapper().map(employee, EmployeeEntity.class);

        EmployeeDao employeeDao= DaoFactory.getInstance().getDaoType(DaoType.EMPLOYEE);

        employeeDao.save(entity);

        return true;
    }

    @Override
    public boolean deleteEmployee(Employee employee) {
        EmployeeEntity entity = new ModelMapper().map(employee, EmployeeEntity.class);

        EmployeeDao employeeDao = DaoFactory.getInstance().getDaoType(DaoType.EMPLOYEE);

        employeeDao.delete(entity);

        return true;
    }

    @Override
    public ObservableList<Employee> getAll() {
        EmployeeDao dao = DaoFactory.getInstance().getDaoType(DaoType.EMPLOYEE);
        ObservableList<EmployeeEntity> entityList= dao.getAll();
        ObservableList<Employee> employeeList = FXCollections.observableArrayList();

        for (EmployeeEntity entity:entityList){
            employeeList.add(new ModelMapper().map(entity, Employee.class));
        }

        return employeeList;
    }

    @Override
    public boolean updateEmployee(Employee employee) {
        EmployeeEntity entity = new ModelMapper().map(employee, EmployeeEntity.class);

        EmployeeDao employeeDao = DaoFactory.getInstance().getDaoType(DaoType.EMPLOYEE);

        employeeDao.update(entity);

        return true;
    }

    @Override
    public Employee searchEmployee(String id) {
        EmployeeDao employeeDao= DaoFactory.getInstance().getDaoType(DaoType.EMPLOYEE);

        EmployeeEntity entity = employeeDao.search(id);
        Employee employee=new ModelMapper().map(entity, Employee.class);

        return employee;
    }
}
