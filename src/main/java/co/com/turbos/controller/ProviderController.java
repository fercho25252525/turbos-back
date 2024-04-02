package co.com.turbos.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import co.com.turbos.entity.Provider;
import co.com.turbos.response.CommandEvent;
import co.com.turbos.response.ResponseEntityUtility;
import co.com.turbos.response.ResponseEvent;
import co.com.turbos.service.IProviderService;

@RestController
@RequestMapping("/api/providerController/v1/")
public class ProviderController {
	
	private final IProviderService providerService;

	public ProviderController(IProviderService providerService) {
		this.providerService = providerService;
	}
	
	@GetMapping(value = "getProvider")
	public ResponseEntity<ResponseEvent<List<Provider>>> getCustomer() {
		final ResponseEvent<List<Provider>> responseEvent = this.providerService.getProvider();
		return ResponseEntityUtility.buildHttpResponse(responseEvent);
	}
	
	@PostMapping(value = "addProvider")
	public ResponseEntity<ResponseEvent<Provider>> addCustomer(@RequestBody Provider customerRequest) {
		final CommandEvent<Provider> requestEvent = new CommandEvent<>();
		requestEvent.setRequest(customerRequest);
		final ResponseEvent<Provider> responseEvent = this.providerService.addProvider(requestEvent);
		return ResponseEntityUtility.buildHttpResponse(responseEvent);
	}
	
	@PutMapping(value = "UpdateProvider")
	public ResponseEntity<ResponseEvent<Provider>> updateCustomer(@RequestBody Provider customerRequest) {
		final CommandEvent<Provider> requestEvent = new CommandEvent<>();
		requestEvent.setRequest(customerRequest);
		final ResponseEvent<Provider> responseEvent = this.providerService.updateProvider(requestEvent);
		return ResponseEntityUtility.buildHttpResponse(responseEvent);
	}
	
	@DeleteMapping(value = "deleteProvider")
	public ResponseEntity<ResponseEvent<Boolean>> deleteUser(@RequestBody Provider userRequest) {
		final CommandEvent<Provider> requestEvent = new CommandEvent<>();
		requestEvent.setRequest(userRequest);
		final ResponseEvent<Boolean> responseEvent = this.providerService.deleteProvider(requestEvent);
		return ResponseEntityUtility.buildHttpResponse(responseEvent);
	}

}
