package service.custom;

import dto.Employee;
import dto.Item;
import javafx.collections.ObservableList;
import service.SuperService;

public interface ItemService extends SuperService {
    boolean addItem(Item item);
    boolean deleteItem(Item item);
    ObservableList<Item> getAll();
    boolean updateItem(Item item);
    Item searchItem(String id);
}
