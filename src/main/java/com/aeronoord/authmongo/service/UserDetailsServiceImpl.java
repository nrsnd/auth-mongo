package com.aeronoord.authmongo.service;

import static java.util.Collections.emptyList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.aeronoord.authmongo.model.Users;
import com.aeronoord.authmongo.repo.UserRepo;


/**
 * UserDetailsServiceImpl
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
	
	@Autowired
    private UserRepo userRepo;
	
    public UserDetailsServiceImpl(UserRepo userRepo){
        this.userRepo = userRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException{
        Users appUser = userRepo.findByUsername(username);
        if(appUser == null){
            throw new UsernameNotFoundException(username);
        }
        return new User(appUser.getUsername(), appUser.getPassword(), emptyList());
    }
    
    
    
    
    
    

    
}