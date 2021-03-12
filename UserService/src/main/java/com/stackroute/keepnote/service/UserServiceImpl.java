package com.stackroute.keepnote.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.stackroute.keepnote.exceptions.UserAlreadyExistsException;
import com.stackroute.keepnote.exceptions.UserNotFoundException;
import com.stackroute.keepnote.model.User;
import com.stackroute.keepnote.repository.UserRepository;

/*
* Service classes are used here to implement additional business logic/validation 
* This class has to be annotated with @Service annotation.
* @Service - It is a specialization of the component annotation. It doesn't currently 
* provide any additional behavior over the @Component annotation, but it's a good idea 
* to use @Service over @Component in service-layer classes because it specifies intent 
* better. Additionally, tool support and additional behavior might rely on it in the 
* future.
* */
@Service
public class UserServiceImpl implements UserService {

	/*
	 * Autowiring should be implemented for the UserRepository. (Use
	 * Constructor-based autowiring) Please note that we should not create any
	 * object using the new keyword.
	 */

	@Autowired
	UserRepository userRepository;
	public UserServiceImpl(UserRepository userRepository)
	{
		this.userRepository=userRepository;
	}
	/*
	 * This method should be used to save a new user.Call the corresponding method
	 * of Respository interface.
	 */

	public User registerUser(User user) throws UserAlreadyExistsException {

		User newUser;
		if(userRepository.existsById(user.getUserId()))
		 {	
			throw new UserAlreadyExistsException("User already exists");
		 } 
		else {
			newUser=userRepository.insert(user);
			if(newUser==null)
			{
				throw new UserAlreadyExistsException("user already exists");
			}
			
		}
		 return newUser;

	}

	/*
	 * This method should be used to update a existing user.Call the corresponding
	 * method of Respository interface.
	 */

	public User updateUser(String userId,User user) throws UserNotFoundException {

		User updatedUser=userRepository.findById(userId).get();
		updatedUser.setUserId(user.getUserId());
		updatedUser.setUserName(user.getUserName());
		updatedUser.setUserPassword(user.getUserPassword());
		updatedUser.setUserMobile(user.getUserMobile());
		updatedUser.setUserAddedDate(new Date());
		if(userRepository.findById(userId)==null)
		{
			throw new UserNotFoundException("user not found");
		}
		userRepository.save(updatedUser);
		return updatedUser;

	}

	/*
	 * This method should be used to delete an existing user. Call the corresponding
	 * method of Respository interface.
	 */

	public boolean deleteUser(String userId) throws UserNotFoundException {

		if(userRepository.findById(userId)==null)
		{
			throw new UserNotFoundException("user not exists");
		}
		User foundUser=userRepository.findById(userId).get();
		userRepository.delete(foundUser);
		return true;

	}

	/*
	 * This method should be used to get a user by userId.Call the corresponding
	 * method of Respository interface.
	 */

	public User getUserById(String userId) throws UserNotFoundException {

		User foundUser;
		if(userRepository.findById(userId)==null)
		{
			throw new UserNotFoundException("User not found");
		}
		foundUser=userRepository.findById(userId).get();
		return foundUser;

	}

}
