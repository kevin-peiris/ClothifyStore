package service.custom;

import dto.Employee;
import javafx.collections.ObservableList;
import service.SuperService;

public interface EmployeeService extends SuperService {
    boolean addEmployee(Employee employee);
    boolean deleteEmployee(Employee employee);
    ObservableList<Employee> getAll();
    boolean updateEmployee(Employee employee);
    Employee searchEmployee(String id);
}
