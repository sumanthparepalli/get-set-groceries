package com.ecommerce.getsetgroceries.repositories;

import com.ecommerce.getsetgroceries.models.UserAddress;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserAddressRepo extends JpaRepository<UserAddress, Long> {
}
