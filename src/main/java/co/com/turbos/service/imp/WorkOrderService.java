package co.com.turbos.service.imp;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.com.turbos.entity.Users;
import co.com.turbos.entity.Vehicle;
import co.com.turbos.entity.WorkDescription;
import co.com.turbos.entity.WorkOrder;
import co.com.turbos.parser.UserParser;
import co.com.turbos.repository.IVehicleRepository;
import co.com.turbos.repository.IWorkDescriptionRepository;
import co.com.turbos.repository.IWorkOrderRepository;
import co.com.turbos.response.CommandEvent;
import co.com.turbos.response.ResponseEvent;
import co.com.turbos.service.ICustomerService;
import co.com.turbos.service.IVehicleService;
import co.com.turbos.service.IWorkOrderService;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class WorkOrderService implements IWorkOrderService {

	private final IWorkOrderRepository iWorkOrderRepository;

	private final IWorkDescriptionRepository iWorkDescriptionRepository;

	private final IVehicleService iVehicleService;

	private final ICustomerService iCustomerService;

	private final IVehicleRepository iVehicleRepository;

	private final UserParser userParser;

	@Autowired
	public WorkOrderService(IWorkOrderRepository iWorkOrderRepository, IVehicleService iVehicleService,
			IVehicleRepository iVehicleRepository, IWorkDescriptionRepository iWorkDescriptionRepository,
			UserParser userParser, ICustomerService iCustomerService) {
		this.iWorkOrderRepository = iWorkOrderRepository;
		this.iVehicleService = iVehicleService;
		this.iVehicleRepository = iVehicleRepository;
		this.iWorkDescriptionRepository = iWorkDescriptionRepository;
		this.userParser = userParser;
		this.iCustomerService = iCustomerService;
	}

	@Override
	public ResponseEvent<List<WorkOrder>> getWorkOrder() {

		List<WorkOrder> orderList = iWorkOrderRepository.findAll();

		List<WorkOrder> orderListResponse = new ArrayList<>();
		orderList.forEach(order -> {
			WorkOrder workOrder = WorkOrder.builder().idOrder(order.getIdOrder()).statusOrder(order.getStatusOrder())
					.estimatedCost(order.getEstimatedCost()).realCost(order.getRealCost())
					.startDate(order.getStartDate()).endDate(order.getEndDate()).creationDate(order.getCreationDate())
					.comments(order.getComments()).vehicle(order.getVehicle())
					.workDescription(order.getWorkDescription()).build();
			orderListResponse.add(workOrder);
		});
		return new ResponseEvent<List<WorkOrder>>().ok("Success", orderListResponse);
	}
	
	@Override
	public ResponseEvent<List<WorkOrder>> getWorkOrderByQuery(String query) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public ResponseEvent<WorkOrder> addWorkOrder(CommandEvent<WorkOrder> requestEvent) {

		if (Objects.isNull(requestEvent.getRequest().getVehicle())) {
			return new ResponseEvent<WorkOrder>().badRequest("No se a ingreado vehiculo.");
		}

		Optional<Users> clienteDB = userParser.userByUserNameOrEmail(
				requestEvent.getRequest().getVehicle().getCustomer().getUserName(),
				requestEvent.getRequest().getVehicle().getCustomer().getEmail());

		if (Objects.isNull(clienteDB.get().getUserName())) {
			return new ResponseEvent<WorkOrder>().badRequest("No se a ingreado Cliente.");
		}

		Optional<Vehicle> vehicleDB = iVehicleRepository.findById(requestEvent.getRequest().getVehicle().getPlate());

		if (vehicleDB.isEmpty() || Objects.isNull(vehicleDB.get().getPlate())) {
			final CommandEvent<Vehicle> vehicleEvent = new CommandEvent<>();
			vehicleEvent.setRequest(requestEvent.getRequest().getVehicle());
			ResponseEvent<Vehicle> v = this.iVehicleService.addVehicle(vehicleEvent);
			vehicleDB = Optional.of(v.getData());
		}

		WorkOrder workOrder = WorkOrder.builder().statusOrder("Creado")
				.estimatedCost(requestEvent.getRequest().getEstimatedCost())
				.realCost(requestEvent.getRequest().getRealCost()).startDate(requestEvent.getRequest().getStartDate())
				.endDate(requestEvent.getRequest().getEndDate()).creationDate(new Date())
				.comments(requestEvent.getRequest().getComments())
				.workDescription(requestEvent.getRequest().getWorkDescription()).vehicle(vehicleDB.get()).build();
		WorkOrder workOrderResponse = iWorkOrderRepository.save(workOrder);

		requestEvent.getRequest().getWorkDescription().stream().filter(work -> work.getMechanic() != null)
				.forEach(work -> {
					Optional<Users> userOpt = this.userParser.userByUserNameOrEmail(work.getMechanic().getUserName(),
							work.getMechanic().getEmail());

					WorkDescription workDescription = WorkDescription.builder().typeWork(work.getTypeWork())
							.workOrder(workOrderResponse).mechanic(userOpt.get()).coste(work.getCoste()).build();

					iWorkDescriptionRepository.save(workDescription);
				});

		return new ResponseEvent<WorkOrder>().created(
				"Se ha creado correctamente la orden de trabajo " + workOrderResponse.getIdOrder(), workOrderResponse);
	}

	@Override
	public ResponseEvent<WorkOrder> updateWorkOrder(CommandEvent<WorkOrder> requestEvent) {
		if (Objects.isNull(requestEvent.getRequest().getVehicle())) {
			return new ResponseEvent<WorkOrder>().badRequest("No se a ingreado vehiculo.");
		}

		Optional<Users> clienteDB = userParser.userByUserNameOrEmail(
				requestEvent.getRequest().getVehicle().getCustomer().getUserName(),
				requestEvent.getRequest().getVehicle().getCustomer().getEmail());

		if (Objects.isNull(clienteDB.get().getUserName())) {
			return new ResponseEvent<WorkOrder>().badRequest("No se a ingreado Cliente.");
		}

		Optional<Vehicle> vehicleDB = iVehicleRepository.findById(requestEvent.getRequest().getVehicle().getPlate());

		if (vehicleDB.isPresent() && Objects.nonNull(vehicleDB.get().getPlate())) {
			final CommandEvent<Vehicle> vehicleEvent = new CommandEvent<>();
			vehicleEvent.setRequest(requestEvent.getRequest().getVehicle());
			ResponseEvent<Vehicle> v = this.iVehicleService.updateVehicle(vehicleEvent);

			vehicleDB = Optional.of(v.getData());
		}
//		iWorkDescriptionRepository.deleteByWorkOrder(56L);
		WorkOrder workOrder = WorkOrder.builder().idOrder(requestEvent.getRequest().getIdOrder())
				.statusOrder(requestEvent.getRequest().getStatusOrder())
				.estimatedCost(requestEvent.getRequest().getEstimatedCost())
				.realCost(requestEvent.getRequest().getRealCost()).startDate(requestEvent.getRequest().getStartDate())
				.endDate(requestEvent.getRequest().getEndDate()).creationDate(new Date())
				.comments(requestEvent.getRequest().getComments())
//				.workDescription(requestEvent.getRequest().getWorkDescription())
				.vehicle(vehicleDB.get()).build();
		WorkOrder workOrderResponse = iWorkOrderRepository.save(workOrder);

		iWorkDescriptionRepository.deleteByWorkOrder(workOrder.getIdOrder());
		requestEvent.getRequest().getWorkDescription().stream().filter(work -> work.getMechanic() != null)
				.forEach(work -> {
					Optional<Users> userOpt = this.userParser.userByUserNameOrEmail(work.getMechanic().getUserName(),
							work.getMechanic().getEmail());

					WorkDescription workDescription = WorkDescription.builder().typeWork(work.getTypeWork())
							.workOrder(workOrderResponse).mechanic(userOpt.get()).coste(work.getCoste()).build();

					iWorkDescriptionRepository.save(workDescription);
				});

		return new ResponseEvent<WorkOrder>().created(
				"Se ha actualizado correctamente la orden de trabajo " + workOrderResponse.getIdOrder(),
				workOrderResponse);
	}

	@Override
	public ResponseEvent<Boolean> deleteWorkOrder(CommandEvent<WorkOrder> requestEvent) {

		try {
			iWorkDescriptionRepository.deleteByWorkOrder(requestEvent.getRequest().getIdOrder());
			iWorkOrderRepository.deleteById(requestEvent.getRequest().getIdOrder());
			return new ResponseEvent<Boolean>().noContent("Orden eliminada.", null);
		} catch (Exception e) {
			return new ResponseEvent<Boolean>().badRequest("Ocurrio un error al eliminar la orden.");
		}
	}

	@Override
	public ResponseEvent<List<WorkDescription>> getWorkOrderDescription(WorkOrder workOrder) {
		Optional<List<WorkDescription>> orderDescriptionList = null;
		try {
			orderDescriptionList = iWorkDescriptionRepository.findByWorkOrder(workOrder);
		} catch (Exception e) {
			log.error("ocurrio un errror al obtener orderDescriptionList");
		}
		return new ResponseEvent<List<WorkDescription>>().ok("Success", orderDescriptionList.orElse(new ArrayList<>()));
	}

	
}
