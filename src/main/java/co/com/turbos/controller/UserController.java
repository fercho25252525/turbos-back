package co.com.turbos.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import co.com.turbos.request.UserRequest;
import co.com.turbos.response.CommandEvent;
import co.com.turbos.response.ResponseEntityUtility;
import co.com.turbos.response.ResponseEvent;
import co.com.turbos.service.IUserService;

@RestController
@RequestMapping("/api/userController/v1/")
public class UserController { 

	private IUserService iUserService;

	@Autowired
	public UserController(IUserService iUserService) {
		this.iUserService = iUserService;
	}	
	
	@GetMapping(value = "getUsers")
	public ResponseEntity<ResponseEvent<List<UserRequest>>> getUser() {
		final ResponseEvent<List<UserRequest>> responseEvent = this.iUserService.getUsers();
		return ResponseEntityUtility.buildHttpResponse(responseEvent);
	}
	
	@PostMapping(value = "addUsers")
	public ResponseEntity<ResponseEvent<UserRequest>> addUser(@RequestBody UserRequest userRequest) {
		final CommandEvent<UserRequest> requestEvent = new CommandEvent<>();
		requestEvent.setRequest(userRequest);
		final ResponseEvent<UserRequest> responseEvent = this.iUserService.addUsers(requestEvent);
		return ResponseEntityUtility.buildHttpResponse(responseEvent);
	}
	
	@PutMapping(value = "UpdateUsers")
	public ResponseEntity<ResponseEvent<UserRequest>> updateUser(@RequestBody UserRequest userRequest) {
		final CommandEvent<UserRequest> requestEvent = new CommandEvent<>();
		requestEvent.setRequest(userRequest);
		final ResponseEvent<UserRequest> responseEvent = this.iUserService.updateUsers(requestEvent);
		return ResponseEntityUtility.buildHttpResponse(responseEvent);
	}
	
	@DeleteMapping(value = "deleteUsers")
	public ResponseEntity<ResponseEvent<Boolean>> deleteUser(@RequestBody UserRequest userRequest) {
		final CommandEvent<UserRequest> requestEvent = new CommandEvent<>();
		requestEvent.setRequest(userRequest);
		final ResponseEvent<Boolean> responseEvent = this.iUserService.deleteUser(requestEvent);
		return ResponseEntityUtility.buildHttpResponse(responseEvent);
	}
}
