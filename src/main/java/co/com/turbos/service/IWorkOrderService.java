package co.com.turbos.service;

import java.util.List;

import co.com.turbos.entity.WorkDescription;
import co.com.turbos.entity.WorkOrder;
import co.com.turbos.response.CommandEvent;
import co.com.turbos.response.ResponseEvent;

public interface IWorkOrderService {

	ResponseEvent<List<WorkOrder>> getWorkOrder();

	ResponseEvent<WorkOrder> addWorkOrder(CommandEvent<WorkOrder> requestEvent);

	ResponseEvent<WorkOrder> updateWorkOrder(CommandEvent<WorkOrder> requestEvent);

	ResponseEvent<Boolean> deleteWorkOrder(CommandEvent<WorkOrder> requestEvent);
	
	ResponseEvent<List<WorkDescription>> getWorkOrderDescription(WorkOrder workOrder);
}
