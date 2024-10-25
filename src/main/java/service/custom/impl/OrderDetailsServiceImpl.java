package service.custom.impl;

import dto.OrderDetails;
import entity.OrderDetailsEntity;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.modelmapper.ModelMapper;
import repository.DaoFactory;
import repository.custom.OrderDetailsDao;
import service.custom.OrderDetailsService;
import util.DaoType;

public class OrderDetailsServiceImpl implements OrderDetailsService {

    @Override
    public boolean addOrderDetails(OrderDetails orderDetails) {
        OrderDetailsEntity entity = new ModelMapper().map(orderDetails, OrderDetailsEntity.class);

        OrderDetailsDao orderDetailsDao = DaoFactory.getInstance().getDaoType(DaoType.ORDER_DETAILS);

        orderDetailsDao.save(entity);

        return true;
    }

    @Override
    public boolean deleteOrderDetails(OrderDetails orderDetails) {
        OrderDetailsEntity entity = new ModelMapper().map(orderDetails, OrderDetailsEntity.class);

        OrderDetailsDao orderDetailsDao = DaoFactory.getInstance().getDaoType(DaoType.ORDER_DETAILS);

        orderDetailsDao.delete(entity);

        return true;
    }

    @Override
    public ObservableList<OrderDetails> getAll() {
        OrderDetailsDao orderDetailsDao = DaoFactory.getInstance().getDaoType(DaoType.ORDER_DETAILS);
        ObservableList<OrderDetailsEntity> entityList = orderDetailsDao.getAll();
        ObservableList<OrderDetails> orderDetailsList = FXCollections.observableArrayList();

        for (OrderDetailsEntity entity : entityList) {
            OrderDetails dto = new OrderDetails();
            dto.setId(entity.getId());
            dto.setOrderId(entity.getOrder().getOrderId());
            dto.setItemId(entity.getItem().getItemId());
            dto.setQty(entity.getQty());
            dto.setPrice(entity.getPrice());
            dto.setSize(entity.getSize());
            dto.setTotal(entity.getTotal());
            orderDetailsList.add(dto);
        }

        return orderDetailsList;
    }

    @Override
    public boolean updateOrderDetails(OrderDetails orderDetails) {
        OrderDetailsEntity entity = new ModelMapper().map(orderDetails, OrderDetailsEntity.class);

        OrderDetailsDao orderDetailsDao = DaoFactory.getInstance().getDaoType(DaoType.ORDER_DETAILS);

        orderDetailsDao.update(entity);

        return true;
    }

    @Override
    public OrderDetails searchOrderDetails(String id) {
        OrderDetailsDao orderDetailsDao = DaoFactory.getInstance().getDaoType(DaoType.ORDER_DETAILS);

        OrderDetailsEntity entity = orderDetailsDao.search(id);
        OrderDetails orderDetails = new ModelMapper().map(entity, OrderDetails.class);

        return orderDetails;
    }


}
