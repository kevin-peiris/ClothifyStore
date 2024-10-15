package service.custom.impl;

import dto.Employee;
import dto.Item;
import entity.EmployeeEntity;
import entity.ItemEntity;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.modelmapper.ModelMapper;
import repository.DaoFactory;
import repository.custom.EmployeeDao;
import repository.custom.ItemDao;
import service.custom.ItemService;
import util.DaoType;

public class ItemServiceImpl implements ItemService {
    @Override
    public boolean addItem(Item item) {
        ItemEntity entity=new ModelMapper().map(item, ItemEntity.class);

        ItemDao itemDao= DaoFactory.getInstance().getDaoType(DaoType.ITEM);

        itemDao.save(entity);

        return true;
    }

    @Override
    public boolean deleteItem(Item item) {
        ItemEntity entity = new ModelMapper().map(item, ItemEntity.class);

        ItemDao itemDao = DaoFactory.getInstance().getDaoType(DaoType.ITEM);

        itemDao.delete(entity);

        return true;
    }

    @Override
    public ObservableList<Item> getAll() {
        ItemDao dao = DaoFactory.getInstance().getDaoType(DaoType.ITEM);
        ObservableList<ItemEntity> entityList= dao.getAll();
        ObservableList<Item> itemList = FXCollections.observableArrayList();

        for (ItemEntity entity:entityList){
            itemList.add(new ModelMapper().map(entity, Item.class));
        }

        return itemList;
    }

    @Override
    public boolean updateItem(Item item) {
        ItemEntity entity = new ModelMapper().map(item, ItemEntity.class);

        ItemDao itemDao = DaoFactory.getInstance().getDaoType(DaoType.ITEM);

        itemDao.update(entity);

        return true;
    }

    @Override
    public Item searchItem(String id) {
        ItemDao itemDao= DaoFactory.getInstance().getDaoType(DaoType.ITEM);

        ItemEntity entity = itemDao.search(id);
        Item item=new ModelMapper().map(entity, Item.class);

        return item;
    }
}
