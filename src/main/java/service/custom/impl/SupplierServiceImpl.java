package service.custom.impl;


import dto.Supplier;
import entity.SupplierEntity;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.modelmapper.ModelMapper;
import repository.DaoFactory;

import repository.custom.SupplierDao;
import service.custom.SupplierService;
import util.DaoType;

public class SupplierServiceImpl implements SupplierService {
    @Override
    public boolean addSupplier(Supplier supplier) {
        SupplierEntity entity = new ModelMapper().map(supplier, SupplierEntity.class);

        SupplierDao supplierDao = DaoFactory.getInstance().getDaoType(DaoType.SUPPLIER);

        supplierDao.save(entity);

        return true;
    }

    @Override
    public boolean deleteSupplier(Supplier supplier) {
        SupplierEntity entity = new ModelMapper().map(supplier, SupplierEntity.class);

        SupplierDao supplierDao = DaoFactory.getInstance().getDaoType(DaoType.SUPPLIER);

        supplierDao.delete(entity);

        return true;
    }

    @Override
    public ObservableList<Supplier> getAll() {
        SupplierDao supplierDao = DaoFactory.getInstance().getDaoType(DaoType.SUPPLIER);
        ObservableList<SupplierEntity> entityList = supplierDao.getAll();
        ObservableList<Supplier> supplierList = FXCollections.observableArrayList();

        for (SupplierEntity entity : entityList) {
            supplierList.add(new ModelMapper().map(entity, Supplier.class));
        }

        return supplierList;
    }

    @Override
    public boolean updateSupplier(Supplier supplier) {
        SupplierEntity entity = new ModelMapper().map(supplier, SupplierEntity.class);

        SupplierDao supplierDao = DaoFactory.getInstance().getDaoType(DaoType.SUPPLIER);

        supplierDao.update(entity);

        return true;
    }

    @Override
    public Supplier searchSupplier(String id) {
        SupplierDao supplierDao = DaoFactory.getInstance().getDaoType(DaoType.SUPPLIER);

        SupplierEntity entity = supplierDao.search(id);
        Supplier supplier = new ModelMapper().map(entity, Supplier.class);

        return supplier;
    }
}
