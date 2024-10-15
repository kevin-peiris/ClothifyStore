package service.custom;

import dto.Employee;
import dto.Order;
import javafx.collections.ObservableList;
import service.SuperService;

public interface OrderService extends SuperService {
    boolean placeOrder(Order order);
    boolean deleteOrder(Order order);
    ObservableList<Order> getAll();
    boolean updateOrder(Order order);
    Order searchOrder(String id);
}
