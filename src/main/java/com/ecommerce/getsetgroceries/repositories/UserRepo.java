package com.ecommerce.getsetgroceries.repositories;

import com.ecommerce.getsetgroceries.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo extends JpaRepository<User, Long> {
    public User getUserByUsername(String username);
}
