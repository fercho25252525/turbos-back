package co.com.turbos.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import co.com.turbos.entity.WorkDescription;
import co.com.turbos.entity.WorkOrder;
import co.com.turbos.response.CommandEvent;
import co.com.turbos.response.ResponseEntityUtility;
import co.com.turbos.response.ResponseEvent;
import co.com.turbos.service.IWorkOrderService;

@RestController
@RequestMapping("/api/workOrderController/v1/")
public class WorkOrderController {
	
	private final IWorkOrderService iWorkOrderService;
	
	@Autowired
	public WorkOrderController(IWorkOrderService iWorkOrderService) {
		this.iWorkOrderService = iWorkOrderService;
	}

	@GetMapping(value = "getWorkOrder")
	public ResponseEntity<ResponseEvent<List<WorkOrder>>> getWorkOrder() {
		final ResponseEvent<List<WorkOrder>> responseEvent = this.iWorkOrderService.getWorkOrder();
		return ResponseEntityUtility.buildHttpResponse(responseEvent);
	}
	
	@PostMapping(value = "addWorkOrder")
	public ResponseEntity<ResponseEvent<WorkOrder>> addCustomer(@RequestBody WorkOrder customerRequest) {
		final CommandEvent<WorkOrder> requestEvent = new CommandEvent<>();
		requestEvent.setRequest(customerRequest);
		final ResponseEvent<WorkOrder> responseEvent = this.iWorkOrderService.addWorkOrder(requestEvent);
		return ResponseEntityUtility.buildHttpResponse(responseEvent);
	}
	
//	@PutMapping(value = "UpdateProvider")
//	public ResponseEntity<ResponseEvent<Provider>> updateCustomer(@RequestBody Provider customerRequest) {
//		final CommandEvent<Provider> requestEvent = new CommandEvent<>();
//		requestEvent.setRequest(customerRequest);
//		final ResponseEvent<Provider> responseEvent = this.providerService.updateProvider(requestEvent);
//		return ResponseEntityUtility.buildHttpResponse(responseEvent);
//	}
//	
//	@DeleteMapping(value = "deleteProvider")
//	public ResponseEntity<ResponseEvent<Boolean>> deleteUser(@RequestBody Provider userRequest) {
//		final CommandEvent<Provider> requestEvent = new CommandEvent<>();
//		requestEvent.setRequest(userRequest);
//		final ResponseEvent<Boolean> responseEvent = this.providerService.deleteProvider(requestEvent);
//		return ResponseEntityUtility.buildHttpResponse(responseEvent);
//	}
	
	@GetMapping(value = "getWorkOrderDescription")
	public ResponseEntity<ResponseEvent<List<WorkDescription>>> getWorkOrderDescription(@RequestBody WorkOrder workOrder) {
		final ResponseEvent<List<WorkDescription>> responseEvent = this.iWorkOrderService.getWorkOrderDescription(workOrder);
		return ResponseEntityUtility.buildHttpResponse(responseEvent);
	}

}
