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

import co.com.turbos.entity.Pqrs;
import co.com.turbos.response.CommandEvent;
import co.com.turbos.response.ResponseEntityUtility;
import co.com.turbos.response.ResponseEvent;
import co.com.turbos.service.IPqrsService;

@RestController
@RequestMapping("/api/pqrsController/v1/")
public class PqrsController {

	private final IPqrsService iPqrsService;

	public PqrsController(IPqrsService iPqrsService) {
		this.iPqrsService = iPqrsService;
	}
	
	@GetMapping(value = "getPqrs")
	public ResponseEntity<ResponseEvent<List<Pqrs>>> getPqrs() {
		final ResponseEvent<List<Pqrs>> responseEvent = this.iPqrsService.getPqrs();
		return ResponseEntityUtility.buildHttpResponse(responseEvent);
	}
	
	@PostMapping(value = "addPqrs")
	public ResponseEntity<ResponseEvent<Pqrs>> addPqrs(@RequestBody Pqrs customerRequest) {
		final CommandEvent<Pqrs> requestEvent = new CommandEvent<>();
		requestEvent.setRequest(customerRequest);
		final ResponseEvent<Pqrs> responseEvent = this.iPqrsService.addPqrs(requestEvent);
		return ResponseEntityUtility.buildHttpResponse(responseEvent);
	}
	
	@PutMapping(value = "UpdatePqrs")
	public ResponseEntity<ResponseEvent<Pqrs>> updatePqrs(@RequestBody Pqrs customerRequest) {
		final CommandEvent<Pqrs> requestEvent = new CommandEvent<>();
		requestEvent.setRequest(customerRequest);
		final ResponseEvent<Pqrs> responseEvent = this.iPqrsService.updatePqrs(requestEvent);
		return ResponseEntityUtility.buildHttpResponse(responseEvent);
	}
	
	@DeleteMapping(value = "deletePqrs")
	public ResponseEntity<ResponseEvent<Boolean>> deletePqrs(@RequestBody Pqrs userRequest) {
		final CommandEvent<Pqrs> requestEvent = new CommandEvent<>();
		requestEvent.setRequest(userRequest);
		final ResponseEvent<Boolean> responseEvent = this.iPqrsService.deletePqrs(requestEvent);
		return ResponseEntityUtility.buildHttpResponse(responseEvent);
	}
}
