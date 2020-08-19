package com.aeronoord.authmongo.controller;

import static com.aeronoord.authmongo.security.SecurityConstants.*;


import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.aeronoord.authmongo.exception.ResourceNotFoundException;
import com.aeronoord.authmongo.model.Response;
import com.aeronoord.authmongo.model.Users;
import com.aeronoord.authmongo.repo.UserRepo;
import com.aeronoord.authmongo.security.JWTAuthenFilter;






@CrossOrigin(maxAge = 3600)
@RestController
public class UserController {
	
	@Autowired
	private UserRepo userRepo;
	
	private Response response;
	
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	public UserController(UserRepo userRepo, BCryptPasswordEncoder bCryptPasswordEncoder) {
		this.userRepo = userRepo;
		this.bCryptPasswordEncoder = bCryptPasswordEncoder;
	}
	
	@PostMapping("/user")
	public Response signUp(@RequestBody Users users, final HttpServletRequest req) {
		Users checkExistUsername = userRepo.findByUsername(users.getUsername());
		if (null != checkExistUsername) {
			return new Response(new Date(), HttpStatus.CONFLICT.value(), null, HttpStatus.CONFLICT.getReasonPhrase(),
					req.getRequestURI(), checkExistUsername);
		}
		users.setPassword(bCryptPasswordEncoder.encode(users.getPassword()));
		Users appUser = userRepo.save(users);
		return new Response(new Date(), HttpStatus.OK.value(), null, "New User Has Been Created Successfully",
				req.getRequestURI(), appUser);
	}
	
	@GetMapping("/username/{username}")
	public Response getUserByName(final HttpServletRequest req, final HttpServletResponse res, @PathVariable("username") String userName) throws ResourceNotFoundException{
		Users users = userRepo.findByUsername(userName);
		JWTAuthenFilter filter = new JWTAuthenFilter();
		 res.addHeader("Access-Control-Expose-Headers", "Authorization");
	     res.addHeader("Access-Control-Allow-Headers", "Authorization, X-PINGOTHER, Origin, X-Requested-With, Content-Type, Accept, X-Custom-header");  
		 res.setHeader(HEADER_STRING, TOKEN_PREFIX + filter.getJWTToken(userName));
		return new Response(new Date(), HttpStatus.OK.value(), null, HttpStatus.OK.getReasonPhrase(),
				req.getRequestURI(), users) ;
		
	}
	
	@PostMapping("/user/reset")
	public Response resetPassword(@RequestBody Users users, final HttpServletRequest req) {

		Users user = userRepo.findByUsername(users.getUsername());
		if (null != user) {
			if (users.getPassword().length() < 6) {
				response = new Response(new Date(), HttpStatus.NOT_ACCEPTABLE.value(), null,
						"Password min. 6 char", req.getRequestURI(), user);
			} else {
				user.setPassword(bCryptPasswordEncoder.encode(users.getPassword()));
				Users appUsers = userRepo.save(user);
				response = new Response(new Date(), HttpStatus.OK.value(), null, HttpStatus.OK.getReasonPhrase(),
						req.getRequestURI(), appUsers);
			}
		} else {
			response = new Response(new Date(), HttpStatus.NOT_FOUND.value(), null,
					HttpStatus.NOT_FOUND.getReasonPhrase(), req.getRequestURI(), user);

		}

		return response;

	}

}
