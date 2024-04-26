package co.com.turbos.service.imp;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.com.turbos.entity.Users;
import co.com.turbos.entity.Vehicle;
import co.com.turbos.parser.UserParser;
import co.com.turbos.repository.IVehicleRepository;
import co.com.turbos.response.CommandEvent;
import co.com.turbos.response.ResponseEvent;
import co.com.turbos.service.IVehicleService;

@Service
public class VehicleService implements IVehicleService {

	private final IVehicleRepository iVehicleRepository;
	private final UserParser userParser;

	@Autowired
	public VehicleService(IVehicleRepository iVehicleRepository, UserParser userParser) {
		this.iVehicleRepository = iVehicleRepository;
		this.userParser = userParser;
	}

	@Override
	public ResponseEvent<List<Vehicle>> getVehicle() {
//		iVehicleRepository.findAll().forEach(vehicle -> vehicle.getCustomer().setPassword(null));
		return new ResponseEvent<List<Vehicle>>().ok("Success", iVehicleRepository.findAll());
	}

	@Override
	public ResponseEvent<Vehicle> addVehicle(CommandEvent<Vehicle> requestEvent) {

		Optional<Vehicle> vehicleOpt = iVehicleRepository.findById(requestEvent.getRequest().getPlate());

		if (vehicleOpt.isPresent()) {
			return new ResponseEvent<Vehicle>().badRequest("El vehiculo ya existe.");
		}

		Users user = requestEvent.getRequest().getCustomer();

		Optional<Users> userOpt = this.userParser.userByUserNameOrEmail(user.getUserName(), user.getEmail());

		if (userOpt.isEmpty()) {
			return new ResponseEvent<Vehicle>().badRequest("El cliente no existe.");
		}

		Vehicle vehicle = Vehicle.builder().plate(requestEvent.getRequest().getPlate()).city(requestEvent.getRequest().getCity())
				.brand(requestEvent.getRequest().getBrand()).line(requestEvent.getRequest().getLine())
				.model(requestEvent.getRequest().getModel()).typeVehicle(requestEvent.getRequest().getTypeVehicle())
				.typeFuels(requestEvent.getRequest().getTypeFuels()).status(requestEvent.getRequest().getStatus())
				.color(requestEvent.getRequest().getColor())
				.nextMaintenanceDate(requestEvent.getRequest().getNextMaintenanceDate()).registerDate(new Date())
				.customer(userOpt.get()).build();

		Vehicle vehicleResponse = iVehicleRepository.save(vehicle);
//		vehicleResponse.getCustomer().setPassword(null);
		return new ResponseEvent<Vehicle>().created(
				"Se ha creado correctamente el vehuculo " + requestEvent.getRequest().getPlate(), vehicleResponse);
	}

	@Override
	public ResponseEvent<Vehicle> updateVehicle(CommandEvent<Vehicle> requestEvent) {

		Optional<Vehicle> vehicleOpt = iVehicleRepository.findById(requestEvent.getRequest().getPlate());

		if (vehicleOpt.isEmpty()) {
			return new ResponseEvent<Vehicle>().badRequest("El vehiculo no existe.");
		}

		Users user = requestEvent.getRequest().getCustomer();

		Optional<Users> userOpt = this.userParser.userByUserNameOrEmail(user.getUserName(), user.getEmail());

		if (userOpt.isEmpty()) {
			return new ResponseEvent<Vehicle>().badRequest("El cliente no existe.");
		}

		Vehicle vehicle = Vehicle.builder().plate(requestEvent.getRequest().getPlate())
				.brand(requestEvent.getRequest().getBrand()).line(requestEvent.getRequest().getLine())
				.model(requestEvent.getRequest().getModel()).typeVehicle(requestEvent.getRequest().getTypeVehicle())
				.typeFuels(requestEvent.getRequest().getTypeFuels()).status(requestEvent.getRequest().getStatus())
				.color(requestEvent.getRequest().getColor()).registerDate(vehicleOpt.get().getRegisterDate())
				.nextMaintenanceDate(requestEvent.getRequest().getNextMaintenanceDate()).customer(userOpt.get())
				.build();

		Vehicle vehicleResponse = iVehicleRepository.save(vehicle);
		return new ResponseEvent<Vehicle>().created(
				"Se ha actualizado correctamente el vehuculo " + requestEvent.getRequest().getPlate(), vehicleResponse);
	}

	@Override
	public ResponseEvent<Boolean> deleteVehicle(CommandEvent<Vehicle> requestEvent) {
		Optional<Vehicle> vehicleOpt = iVehicleRepository.findById(requestEvent.getRequest().getPlate());

		if (vehicleOpt.isEmpty()) {
			return new ResponseEvent<Boolean>().badRequest("El vehiculo no existe.");
		}

		try {
			this.iVehicleRepository.deleteById(requestEvent.getRequest().getPlate());
			return new ResponseEvent<Boolean>().noContent("Producto eliminado.", null);
		} catch (Exception e) {
			return new ResponseEvent<Boolean>().badRequest("Ocurrio un error al eliminar vehiculo.");
		}
	}

}
