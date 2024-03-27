package co.com.turbos.service;

import java.util.List;

import co.com.turbos.request.CustomerRequest;
import co.com.turbos.response.CommandEvent;
import co.com.turbos.response.ResponseEvent;

public interface ICustomerService {
	
	ResponseEvent<List<CustomerRequest>> getCustomer();
	
	ResponseEvent<CustomerRequest> addCustomer(CommandEvent<CustomerRequest> requestEvent);
	
	ResponseEvent<CustomerRequest> updateCustomer(CommandEvent<CustomerRequest> requestEvent);
	
	ResponseEvent<Boolean> deleteCustomer(CommandEvent<CustomerRequest> requestEvent);

}
