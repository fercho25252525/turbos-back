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

import co.com.turbos.entity.Vehicle;
import co.com.turbos.response.CommandEvent;
import co.com.turbos.response.ResponseEntityUtility;
import co.com.turbos.response.ResponseEvent;
import co.com.turbos.service.IVehicleService;

@RestController
@RequestMapping("/api/vehicleController/v1/")
public class VehicleController {

	private final IVehicleService iVehicleService;

	public VehicleController(IVehicleService iVehicleService) {
		this.iVehicleService = iVehicleService;
	}

	@GetMapping(value = "getVehicle")
	public ResponseEntity<ResponseEvent<List<Vehicle>>> getCustomer() {
		final ResponseEvent<List<Vehicle>> responseEvent = this.iVehicleService.getVehicle();
		return ResponseEntityUtility.buildHttpResponse(responseEvent);
	}
	
	@PostMapping(value = "addVehicle")
	public ResponseEntity<ResponseEvent<Vehicle>> addCustomer(@RequestBody Vehicle customerRequest) {
		final CommandEvent<Vehicle> requestEvent = new CommandEvent<>();
		requestEvent.setRequest(customerRequest);
		final ResponseEvent<Vehicle> responseEvent = this.iVehicleService.addVehicle(requestEvent);
		return ResponseEntityUtility.buildHttpResponse(responseEvent);
	}
	
	@PutMapping(value = "UpdateVehicle")
	public ResponseEntity<ResponseEvent<Vehicle>> updateCustomer(@RequestBody Vehicle customerRequest) {
		final CommandEvent<Vehicle> requestEvent = new CommandEvent<>();
		requestEvent.setRequest(customerRequest);
		final ResponseEvent<Vehicle> responseEvent = this.iVehicleService.updateVehicle(requestEvent);
		return ResponseEntityUtility.buildHttpResponse(responseEvent);
	}
	
	@DeleteMapping(value = "deleteVehicle")
	public ResponseEntity<ResponseEvent<Boolean>> deleteUser(@RequestBody Vehicle userRequest) {
		final CommandEvent<Vehicle> requestEvent = new CommandEvent<>();
		requestEvent.setRequest(userRequest);
		final ResponseEvent<Boolean> responseEvent = this.iVehicleService.deleteVehicle(requestEvent);
		return ResponseEntityUtility.buildHttpResponse(responseEvent);
	}
}
