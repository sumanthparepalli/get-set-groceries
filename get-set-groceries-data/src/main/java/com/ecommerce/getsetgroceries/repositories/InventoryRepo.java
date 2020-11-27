package com.ecommerce.getsetgroceries.repositories;

import com.ecommerce.getsetgroceries.models.Inventory;
import com.ecommerce.getsetgroceries.models.InventoryID;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InventoryRepo extends JpaRepository<Inventory,InventoryID> {
    List<Inventory> getInventoryBySeller_Id(Long sellerId);
}
