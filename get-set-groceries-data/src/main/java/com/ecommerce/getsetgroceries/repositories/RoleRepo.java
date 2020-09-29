package com.ecommerce.getsetgroceries.repositories;

import com.ecommerce.getsetgroceries.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepo extends JpaRepository<Role,Long> {
}
