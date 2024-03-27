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

import co.com.turbos.request.CustomerRequest;
import co.com.turbos.request.UserRequest;
import co.com.turbos.response.CommandEvent;
import co.com.turbos.response.ResponseEntityUtility;
import co.com.turbos.response.ResponseEvent;
import co.com.turbos.service.ICustomerService;
import co.com.turbos.service.IUserService;

@RestController
@RequestMapping("/api/customerController/v1/")
public class CustomerController {
	
	private IUserService iUserService;
	
	private ICustomerService iCustomerService;
	
	@Autowired
	public CustomerController(IUserService iUserService, ICustomerService iCustomerService) {
		this.iUserService = iUserService;
		this.iCustomerService = iCustomerService;
	}	
	 
	@GetMapping(value = "getCustomer")
	public ResponseEntity<ResponseEvent<List<CustomerRequest>>> getCustomer() {
		final ResponseEvent<List<CustomerRequest>> responseEvent = this.iCustomerService.getCustomer();
		return ResponseEntityUtility.buildHttpResponse(responseEvent);
	}
	
	@PostMapping(value = "addCustomer")
	public ResponseEntity<ResponseEvent<CustomerRequest>> addCustomer(@RequestBody CustomerRequest customerRequest) {
		final CommandEvent<CustomerRequest> requestEvent = new CommandEvent<>();
		requestEvent.setRequest(customerRequest);
		final ResponseEvent<CustomerRequest> responseEvent = this.iCustomerService.addCustomer(requestEvent);
		return ResponseEntityUtility.buildHttpResponse(responseEvent);
	}
	
	@PutMapping(value = "UpdateCustomer")
	public ResponseEntity<ResponseEvent<CustomerRequest>> updateCustomer(@RequestBody CustomerRequest customerRequest) {
		final CommandEvent<CustomerRequest> requestEvent = new CommandEvent<>();
		requestEvent.setRequest(customerRequest);
		final ResponseEvent<CustomerRequest> responseEvent = this.iCustomerService.updateCustomer(requestEvent);
		return ResponseEntityUtility.buildHttpResponse(responseEvent);
	}
	
	@DeleteMapping(value = "deleteCustomer")
	public ResponseEntity<ResponseEvent<Boolean>> deleteUser(@RequestBody CustomerRequest userRequest) {
		final CommandEvent<CustomerRequest> requestEvent = new CommandEvent<>();
		requestEvent.setRequest(userRequest);
		final ResponseEvent<Boolean> responseEvent = this.iCustomerService.deleteCustomer(requestEvent);
		return ResponseEntityUtility.buildHttpResponse(responseEvent);
	}

}
