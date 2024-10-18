package controller;

import dto.Employee;
import dto.Order;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import service.ServiceFactory;
import service.custom.EmployeeService;
import service.custom.OrderService;
import util.ServiceType;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class MainDashboardController {
    private static List<Employee> employeeList=new ArrayList<>();

    int empCount=1;
    int orderCount=1;

    @FXML
    void EmpBtnOnAction(ActionEvent event) {
        EmployeeService service= ServiceFactory.getInstance().getServiceType(ServiceType.EMPLOYEE);

        String id=String.format("E%03d",empCount);
        empCount++;

        //service.addEmployee(new Employee(id,"Amal","1","1"));
    }

    @FXML
    void OrderBtnOnAction(ActionEvent event) {
        OrderService service= ServiceFactory.getInstance().getServiceType(ServiceType.ORDER);

        String id=String.format("O%03d",orderCount);
        orderCount++;

        String empId=String.format("E%03d",empCount);
        empCount++;

        LocalDateTime currentDateTime = LocalDateTime.now();

        //service.placeOrder(new Order(id,currentDateTime,empId));
    }
}








