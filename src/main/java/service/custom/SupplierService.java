package service.custom;

import dto.Employee;
import dto.Supplier;
import javafx.collections.ObservableList;
import service.SuperService;

public interface SupplierService extends SuperService {
    boolean addSupplier(Supplier supplier);
    boolean deleteSupplier(Supplier supplier);
    ObservableList<Supplier> getAll();
    boolean updateSupplier(Supplier supplier);
    Supplier searchSupplier(String id);
}
