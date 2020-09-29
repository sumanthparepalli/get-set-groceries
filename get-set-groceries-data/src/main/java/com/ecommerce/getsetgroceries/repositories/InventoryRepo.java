package com.ecommerce.getsetgroceries.repositories;

import com.ecommerce.getsetgroceries.models.Inventory;
import com.ecommerce.getsetgroceries.models.InventoryID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InventoryRepo extends JpaRepository<Inventory,InventoryID> {
}
