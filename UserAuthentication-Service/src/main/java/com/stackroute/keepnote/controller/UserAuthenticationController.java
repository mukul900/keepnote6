package com.stackroute.keepnote.controller;


import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.stackroute.keepnote.exception.UserAlreadyExistsException;
import com.stackroute.keepnote.exception.UserNotFoundException;
import com.stackroute.keepnote.model.User;
import com.stackroute.keepnote.service.UserAuthenticationService;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

/*
 * As in this assignment, we are working on creating RESTful web service, hence annotate
 * the class with @RestController annotation. A class annotated with the @Controller annotation
 * has handler methods which return a view. However, if we use @ResponseBody annotation along
 * with @Controller annotation, it will return the data directly in a serialized 
 * format. Starting from Spring 4 and above, we can use @RestController annotation which 
 * is equivalent to using @Controller and @ResposeBody annotation
 */
@RestController
@RequestMapping("api/v1/auth")
public class UserAuthenticationController {

    /*
	 * Autowiring should be implemented for the UserAuthenticationService. (Use Constructor-based
	 * autowiring) Please note that we should not create an object using the new
	 * keyword
	 */
	@Autowired
	UserAuthenticationService userAuthenticationService;
    public UserAuthenticationController(UserAuthenticationService authicationService) {
    	this.userAuthenticationService=authicationService;
	}

/*
	 * Define a handler method which will create a specific user by reading the
	 * Serialized object from request body and save the user details in the
	 * database. This handler method should return any one of the status messages
	 * basis on different situations:
	 * 1. 201(CREATED) - If the user created successfully. 
	 * 2. 409(CONFLICT) - If the userId conflicts with any existing user
	 * 
	 * This handler method should map to the URL "/api/v1/auth/register" using HTTP POST method
	 */
    @PostMapping("/register")
   public ResponseEntity<?>  registerUser(@RequestBody User user)
    {
    	try {
    		userAuthenticationService.saveUser(user);
    		return new ResponseEntity<>("user added",HttpStatus.CREATED);
    	}
    	catch(UserAlreadyExistsException u)
    	{
    		return new ResponseEntity<>("user already exists",HttpStatus.CONFLICT);
    	}
    }



	/* Define a handler method which will authenticate a user by reading the Serialized user
	 * object from request body containing the username and password. The username and password should be validated 
	 * before proceeding ahead with JWT token generation. The user credentials will be validated against the database entries. 
	 * The error should be return if validation is not successful. If credentials are validated successfully, then JWT
	 * token will be generated. The token should be returned back to the caller along with the API response.
	 * This handler method should return any one of the status messages basis on different
	 * situations:
	 * 1. 200(OK) - If login is successful
	 * 2. 401(UNAUTHORIZED) - If login is not successful
	 * 
	 * This handler method should map to the URL "/api/v1/auth/login" using HTTP POST method
	*/



  //  @PostMapping("/login")
    //public ResponseEntity<?> login(@RequestBody User user)
    //{
//    	try {
//			User u=userAuthenticationService.findByUserIdAndPassword(user.getUserId(), user.getUserPassword());
//			return new ResponseEntity<>("user login successfull",HttpStatus.OK);
//    	} catch (UserNotFoundException e) {
//			return new ResponseEntity<>("user login failed",HttpStatus.UNAUTHORIZED);
//		}
//    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User user){
		String jwtToken ="";
		Map<String,String> map = new HashMap<>();
		try {
			jwtToken = getToken(user.getUserId(), user.getUserPassword());
			map.put("token", jwtToken);
			map.put("message", "user logged in successfully");
			return new ResponseEntity<>(map,HttpStatus.OK);
		}catch(Exception e) {
			map.put("token", "");
			map.put("message", "user NOT logged in ");
			return new ResponseEntity<>(map,HttpStatus.UNAUTHORIZED);
		}
		
	}
	
    

// Generate JWT token
	public String getToken(String username, String password) throws Exception {
			
		String jwtToken ="";
		
		if(username == null || password ==null) {
			throw new ServletException(" Username - Password cannot be blank");
		}
		
		boolean status = (userAuthenticationService.findByUserIdAndPassword(username, password)!=null);
		if(!status) {
			throw new ServletException(" Invalid credentials");
		}
		
		jwtToken = Jwts.builder()
					   .setSubject("Demo")
					   .setIssuer("sam")
					   .setExpiration(new Date(System.currentTimeMillis()+300000))
					   .signWith(SignatureAlgorithm.HS256, "ibmwave8")
					   .compact();
		return jwtToken;
        
}


}
