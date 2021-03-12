package com.stackroute.keepnote.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.stackroute.keepnote.exceptions.UserAlreadyExistsException;
import com.stackroute.keepnote.exceptions.UserNotFoundException;
import com.stackroute.keepnote.model.User;
import com.stackroute.keepnote.service.UserService;
import com.stackroute.keepnote.service.UserServiceImpl;

/*
 * As in this assignment, we are working on creating RESTful web service, hence annotate
 * the class with @RestController annotation. A class annotated with the @Controller annotation
 * has handler methods which return a view. However, if we use @ResponseBody annotation along
 * with @Controller annotation, it will return the data directly in a serialized 
 * format. Starting from Spring 4 and above, we can use @RestController annotation which 
 * is equivalent to using @Controller and @ResposeBody annotation
 */
@RestController
@RequestMapping("/api/v1/user")
public class UserController {

	/*
	 * Autowiring should be implemented for the UserService. (Use Constructor-based
	 * autowiring) Please note that we should not create an object using the new
	 * keyword
	 */
	@Autowired
	UserService userService;
	
	public UserController(UserService userService) {
		this.userService=userService;
	}

	/*
	 * Define a handler method which will create a specific user by reading the
	 * Serialized object from request body and save the user details in the
	 * database. This handler method should return any one of the status messages
	 * basis on different situations:
	 * 1. 201(CREATED) - If the user created successfully. 
	 * 2. 409(CONFLICT) - If the userId conflicts with any existing user
	 * 
	 * This handler method should map to the URL "/user" using HTTP POST method
	 */

	@PostMapping()
	public ResponseEntity<?> registerUser(@RequestBody User user)
	{
		try {
			userService.registerUser(user);
			return new ResponseEntity<>(user,HttpStatus.CREATED);
			
		}catch(UserAlreadyExistsException u)
		{
			return new ResponseEntity<>("user already exists",HttpStatus.CONFLICT);
		}

	}
	
	
	
	/*
	 * Define a handler method which will update a specific user by reading the
	 * Serialized object from request body and save the updated user details in a
	 * database. This handler method should return any one of the status messages
	 * basis on different situations: 
	 * 1. 200(OK) - If the user updated successfully.
	 * 2. 404(NOT FOUND) - If the user with specified userId is not found.
	 * 
	 * This handler method should map to the URL "/api/v1/user/{id}" using HTTP PUT method.
	 */

	@PutMapping("/{userId}")
	public ResponseEntity<?> updateUser(@PathVariable String userId, @RequestBody User user)
	{
		try {
			userService.updateUser(userId,user);
			return new ResponseEntity<>(user,HttpStatus.OK);
		}catch(UserNotFoundException u)
		{
			return new ResponseEntity<>(user,HttpStatus.NOT_FOUND);
		}
	}
	

	
	/*
	 * Define a handler method which will delete a user from a database.
	 * This handler method should return any one of the status messages basis on
	 * different situations: 
	 * 1. 200(OK) - If the user deleted successfully from database. 
	 * 2. 404(NOT FOUND) - If the user with specified userId is not found.
	 *
	 * This handler method should map to the URL "/api/v1/user/{id}" using HTTP Delete
	 * method" where "id" should be replaced by a valid userId without {}
	 */

	@DeleteMapping("/{userId}")
	public ResponseEntity<?> deleteUser(@PathVariable String userId)
	{
		try {
			boolean b=userService.deleteUser(userId);
			return new ResponseEntity<>(b,HttpStatus.OK);
		}catch(UserNotFoundException u)
		{
			return new ResponseEntity<>("not found",HttpStatus.NOT_FOUND);
		}
	}

	
	
	
	/*
	 * Define a handler method which will show details of a specific user. This
	 * handler method should return any one of the status messages basis on
	 * different situations: 
	 * 1. 200(OK) - If the user found successfully. 
	 * 2. 404(NOT FOUND) - If the user with specified userId is not found. 
	 * This handler method should map to the URL "/api/v1/user/{id}" using HTTP GET method where "id" should be
	 * replaced by a valid userId without {}
	 */
	
	
	@GetMapping("/{userId}")
	public ResponseEntity<?> getUserById(@PathVariable String userId)
	{
		try {
			User u=userService.getUserById(userId);
			return new ResponseEntity<>(u,HttpStatus.OK);
		}catch(UserNotFoundException un)
		{
			return new ResponseEntity<>(un.getMessage(),HttpStatus.NOT_FOUND);
		}
	}

}
