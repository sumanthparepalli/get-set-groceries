package com.ecommerce.getsetgroceries.services;

import com.ecommerce.getsetgroceries.models.User;
import com.ecommerce.getsetgroceries.repositories.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;


public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepo userRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepo.getUserByUsername(username);
        if(user==null)
            throw new UsernameNotFoundException("User doesn't exist");
        return new UserDetailsImpl(user);
    }
}
