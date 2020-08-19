package com.aeronoord.authmongo.repo;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.aeronoord.authmongo.model.Users;

public interface UserRepo extends MongoRepository<Users, String>{
	
	Users findByUsername(String username);

}
