package service.custom;

import dto.Employee;
import dto.OrderDetails;
import javafx.collections.ObservableList;
import service.SuperService;

public interface OrderDetailsService extends SuperService {
    boolean addOrderDetails(OrderDetails orderDetails);
    boolean deleteOrderDetails(OrderDetails orderDetails);
    ObservableList<OrderDetails> getAll();
    boolean updateOrderDetails(OrderDetails orderDetails);
    OrderDetails searchOrderDetails(String id);
}
