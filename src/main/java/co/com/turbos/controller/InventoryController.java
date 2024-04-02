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

import co.com.turbos.entity.Inventory;
import co.com.turbos.response.CommandEvent;
import co.com.turbos.response.ResponseEntityUtility;
import co.com.turbos.response.ResponseEvent;
import co.com.turbos.service.IInventoryService;

@RestController
@RequestMapping("/api/inventoryController/v1/")
public class InventoryController {

	private final IInventoryService iInventoryService;

	public InventoryController(IInventoryService iInventoryService) {
		this.iInventoryService = iInventoryService;
	}
	

	@GetMapping(value = "getInventory")
	public ResponseEntity<ResponseEvent<List<Inventory>>> getCustomer() {
		final ResponseEvent<List<Inventory>> responseEvent = this.iInventoryService.getInventory();
		return ResponseEntityUtility.buildHttpResponse(responseEvent);
	}
	
	@PostMapping(value = "addInventory")
	public ResponseEntity<ResponseEvent<Inventory>> addCustomer(@RequestBody Inventory customerRequest) {
		final CommandEvent<Inventory> requestEvent = new CommandEvent<>();
		requestEvent.setRequest(customerRequest);
		final ResponseEvent<Inventory> responseEvent = this.iInventoryService.addInventory(requestEvent);
		return ResponseEntityUtility.buildHttpResponse(responseEvent);
	}
	
	@PutMapping(value = "UpdateInventory")
	public ResponseEntity<ResponseEvent<Inventory>> updateCustomer(@RequestBody Inventory customerRequest) {
		final CommandEvent<Inventory> requestEvent = new CommandEvent<>();
		requestEvent.setRequest(customerRequest);
		final ResponseEvent<Inventory> responseEvent = this.iInventoryService.updateInventory(requestEvent);
		return ResponseEntityUtility.buildHttpResponse(responseEvent);
	}
	
	@DeleteMapping(value = "deleteInventory")
	public ResponseEntity<ResponseEvent<Boolean>> deleteUser(@RequestBody Inventory userRequest) {
		final CommandEvent<Inventory> requestEvent = new CommandEvent<>();
		requestEvent.setRequest(userRequest);
		final ResponseEvent<Boolean> responseEvent = this.iInventoryService.deleteInventory(requestEvent);
		return ResponseEntityUtility.buildHttpResponse(responseEvent);
	}
}
