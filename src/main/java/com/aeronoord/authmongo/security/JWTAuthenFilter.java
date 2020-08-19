package com.aeronoord.authmongo.security;

import static com.aeronoord.authmongo.security.SecurityConstants.*;
import static com.auth0.jwt.algorithms.Algorithm.HMAC512;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.aeronoord.authmongo.model.Users;
import com.auth0.jwt.JWT;
import com.fasterxml.jackson.databind.ObjectMapper;



/**
 * JWTAuthFilter
 */
public class JWTAuthenFilter extends UsernamePasswordAuthenticationFilter{
    
    private AuthenticationManager authenticationManager;
    
    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
    
    public JWTAuthenFilter(AuthenticationManager authenticationManager){
        this.authenticationManager = authenticationManager;
    }
    
    public JWTAuthenFilter(){
        
    }

    public Authentication attemptAuthentication(HttpServletRequest req, HttpServletResponse res) throws AuthenticationException{
		try{
            Users creds = new ObjectMapper()
                    .readValue(req.getInputStream(), Users.class);
            
            return authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    creds.getUsername(), 
                    creds.getPassword(), 
                    new ArrayList<>())
            );
        }catch(IOException e){
            throw new RuntimeException(e);
        }

    }

    @Override
    protected void successfulAuthentication(HttpServletRequest req, 
                                            HttpServletResponse res,
                                            FilterChain chain,
                                            Authentication auth) throws IOException, ServletException{
        redirectStrategy.sendRedirect(req, res, "/username/"+ ((User)auth.getPrincipal()).getUsername());      
        
        
        
        }
    
    public String getJWTToken(String username) {
    	String token = JWT.create()
                .withSubject(username)
                .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .sign(HMAC512(SECRET.getBytes()));
    	
    	return token;
    	
    }

    
}