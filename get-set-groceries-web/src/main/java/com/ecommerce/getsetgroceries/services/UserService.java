package com.ecommerce.getsetgroceries.services;

import com.ecommerce.getsetgroceries.models.Seller;
import com.ecommerce.getsetgroceries.models.User;
import com.ecommerce.getsetgroceries.repositories.RoleRepo;
import com.ecommerce.getsetgroceries.repositories.SellerRepo;
import com.ecommerce.getsetgroceries.repositories.UserRepo;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;

@Service
@Transactional
public class UserService {

    private final UserRepo userRepo;
    private final RoleRepo roleRepo;
    private final SellerRepo sellerRepo;

    public UserService(UserRepo userRepo, RoleRepo roleRepo, SellerRepo sellerRepo) {
        this.userRepo = userRepo;
        this.roleRepo = roleRepo;
        this.sellerRepo = sellerRepo;
    }


    public void addNewUser(User user) {
        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        user.setCreatedAt(new Date(System.currentTimeMillis()));
        user.setIsMerchant(false);
        user.setEnabled(true);
        user.roles.add(roleRepo.findRoleByRoleName("USER"));
        userRepo.saveAndFlush(user);
    }

    public void becomeSeller(String username, Seller seller)
    {
        User user=userRepo.getUserByUsername(username);
        user.setIsMerchant(true);
        seller.user=user;
        sellerRepo.save(seller);
    }
}
