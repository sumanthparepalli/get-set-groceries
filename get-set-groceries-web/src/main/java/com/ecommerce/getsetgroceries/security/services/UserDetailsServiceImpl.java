package com.ecommerce.getsetgroceries.security.services;

import com.ecommerce.getsetgroceries.models.User;
import com.ecommerce.getsetgroceries.repositories.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepo userRepo;

    @Override
    public UserDetailsImpl loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepo.getUserByUsername(username);
        if(user==null)
            throw new UsernameNotFoundException("User doesn't exist");
        return new UserDetailsImpl(user);
    }
}
