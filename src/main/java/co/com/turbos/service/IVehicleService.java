package co.com.turbos.service;

import java.util.List;

import co.com.turbos.entity.Vehicle;
import co.com.turbos.response.CommandEvent;
import co.com.turbos.response.ResponseEvent;

public interface IVehicleService {
	
	ResponseEvent<List<Vehicle>> getVehicle();

	ResponseEvent<Vehicle> addVehicle(CommandEvent<Vehicle> requestEvent);

	ResponseEvent<Vehicle> updateVehicle(CommandEvent<Vehicle> requestEvent);

	ResponseEvent<Boolean> deleteVehicle(CommandEvent<Vehicle> requestEvent);
}
