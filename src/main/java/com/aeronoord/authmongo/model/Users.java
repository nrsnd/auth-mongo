package com.aeronoord.authmongo.model;

import java.io.Serializable;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@Document(collection = "users")
public class Users implements Serializable{
	
	private static final long serialVersionUID = 1393967289094137515L;
	@Id
	private ObjectId _id;
	private String username;
	private String password;
	
	public Users() {}
	
	
	public Users(String username, String password) {
		super();
		this.username = username;
		this.password = password;
	}

	public ObjectId get_id() {
		return _id;
	}

	public void set_id(ObjectId _id) {
		this._id = _id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	
	
	
	
	
	

}
