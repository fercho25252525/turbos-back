package co.com.turbos.controller;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import co.com.turbos.request.RoleRequest;
import co.com.turbos.request.SendImageRequest;
import co.com.turbos.request.UploadFile;
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
	
	@DeleteMapping(value = "deleteUser")
	public ResponseEntity<ResponseEvent<Boolean>> deleteUser(@RequestBody UserRequest userRequest) {
		final CommandEvent<UserRequest> requestEvent = new CommandEvent<>();
		requestEvent.setRequest(userRequest);
		final ResponseEvent<Boolean> responseEvent = this.iUserService.deleteUser(requestEvent);
		return ResponseEntityUtility.buildHttpResponse(responseEvent);
	}
	
	@GetMapping(value = "getRoles") 
	public ResponseEntity<ResponseEvent<List<RoleRequest>>> getRole() {
		final ResponseEvent<List<RoleRequest>> responseEvent = this.iUserService.getRole();
		return ResponseEntityUtility.buildHttpResponse(responseEvent);
	} 
		
	
	@PostMapping("/upload")
    public ResponseEntity<ResponseEvent<String>> uploadImage(@RequestParam("file") MultipartFile file, @RequestParam("userName") String username) throws IOException {
		final CommandEvent<UploadFile> requestEvent = new CommandEvent<>();
		UploadFile uploadFile = UploadFile.builder().file(file).userName(username).build();
		requestEvent.setRequest(uploadFile);
		final ResponseEvent<String> responseEvent = this.iUserService.uploadImage(file, username);
		return ResponseEntityUtility.buildHttpResponse(responseEvent);
    }
	
	
    @GetMapping("/sendImage/{userName}")
    public ResponseEntity<ByteArrayResource> downloadImage(@PathVariable String userName) throws IOException {
    	SendImageRequest imageRequest = this.iUserService.sendImage(userName);
    	
    	if(Objects.isNull(imageRequest.getHeaders()) || Objects.isNull(imageRequest.getResource())) {
    		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    	}

        return new ResponseEntity<>(imageRequest.getResource(), imageRequest.getHeaders(), HttpStatus.OK);
    }
    
    @GetMapping(value = "getMecanic")
	public ResponseEntity<ResponseEvent<List<UserRequest>>> getMecanic() {
		final ResponseEvent<List<UserRequest>> responseEvent = this.iUserService.getMecanic();
		return ResponseEntityUtility.buildHttpResponse(responseEvent);
	}
}
