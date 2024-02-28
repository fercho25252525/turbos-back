package co.com.turbos.service;

import java.util.List;

import co.com.turbos.entity.Users;
import co.com.turbos.request.UserRequest;
import co.com.turbos.response.CommandEvent;
import co.com.turbos.response.ResponseEvent;

public interface IUserService {	
	
	ResponseEvent<List<UserRequest>> getUsers();
	
	ResponseEvent<UserRequest> addUsers(CommandEvent<UserRequest> requestEvent);

	ResponseEvent<UserRequest> updateUsers(CommandEvent<UserRequest> requestEvent);

	Users findByUsername(String username);
	
	ResponseEvent<Boolean> deleteUser(CommandEvent<UserRequest> requestEvent);
	
}
