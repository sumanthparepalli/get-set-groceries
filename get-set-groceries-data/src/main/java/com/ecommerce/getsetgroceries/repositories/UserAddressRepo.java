package com.ecommerce.getsetgroceries.repositories;

import com.ecommerce.getsetgroceries.models.User;
import com.ecommerce.getsetgroceries.models.UserAddress;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserAddressRepo extends JpaRepository<UserAddress, Long> {

    Optional<UserAddress> findFirstByUser(User user);
}
