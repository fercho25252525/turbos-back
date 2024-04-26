package co.com.turbos.service;

import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import co.com.turbos.entity.Users;
import co.com.turbos.request.RoleRequest;
import co.com.turbos.request.SendImageRequest;
import co.com.turbos.request.UserRequest;
import co.com.turbos.response.CommandEvent;
import co.com.turbos.response.ResponseEvent;

public interface IUserService {	
	
	ResponseEvent<List<UserRequest>> getUsers();	
	
	ResponseEvent<UserRequest> addUsers(CommandEvent<UserRequest> requestEvent);
	
	ResponseEvent<UserRequest> updateUsers(CommandEvent<UserRequest> requestEvent);

	Users findByUsername(String username);
	
	ResponseEvent<Boolean> deleteUser(CommandEvent<UserRequest> requestEvent);
	
	ResponseEvent<List<RoleRequest>> getRole();

	ResponseEvent<String> uploadImage(MultipartFile file, String username) throws IOException;
	
	SendImageRequest sendImage(String userName) throws IOException;
	
	ResponseEvent<List<UserRequest>> getMecanic();
}
