package service.custom.impl;

import dto.Order;
import dto.OrderDetails;
import entity.OrderEntity;
import entity.OrderDetailsEntity;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.modelmapper.ModelMapper;
import repository.DaoFactory;
import repository.custom.OrderDao;

import service.custom.OrderService;
import util.DaoType;
import java.util.HashSet;
import java.util.Set;

public class OrderServiceImpl implements OrderService {

    private final ModelMapper modelMapper = new ModelMapper();

    @Override
    public boolean placeOrder(Order order) {
        OrderEntity orderEntity = modelMapper.map(order, OrderEntity.class);

        Set<OrderDetailsEntity> orderDetailsEntities = new HashSet<>();
        for (OrderDetails detailsDTO : order.getOrderDetailsList()) {
            OrderDetailsEntity orderDetailsEntity = modelMapper.map(detailsDTO, OrderDetailsEntity.class);
            orderDetailsEntities.add(orderDetailsEntity);
        }
        orderEntity.setOrderDetails(orderDetailsEntities);

        OrderDao orderDao = DaoFactory.getInstance().getDaoType(DaoType.ORDER);
        orderDao.save(orderEntity);

        return true;
    }

    @Override
    public boolean deleteOrder(Order order) {
        OrderEntity entity = modelMapper.map(order, OrderEntity.class);

        OrderDao orderDao = DaoFactory.getInstance().getDaoType(DaoType.ORDER);
        orderDao.delete(entity);

        return true;
    }

    @Override
    public ObservableList<Order> getAll() {
        OrderDao dao = DaoFactory.getInstance().getDaoType(DaoType.ORDER);
        ObservableList<OrderEntity> entityList = dao.getAll();
        ObservableList<Order> orderList = FXCollections.observableArrayList();

        for (OrderEntity entity : entityList) {
            Order orderDTO = modelMapper.map(entity, Order.class);
            orderList.add(orderDTO);
        }

        return orderList;
    }

    @Override
    public boolean updateOrder(Order order) {
        OrderEntity entity = modelMapper.map(order, OrderEntity.class);

        OrderDao orderDao = DaoFactory.getInstance().getDaoType(DaoType.ORDER);
        orderDao.update(entity);

        return true;
    }

    @Override
    public Order searchOrder(String id) {
        OrderDao orderDao = DaoFactory.getInstance().getDaoType(DaoType.ORDER);
        OrderEntity entity = orderDao.search(id);

        Order order = modelMapper.map(entity, Order.class);

        return order;
    }
}
