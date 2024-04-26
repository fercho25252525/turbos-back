package co.com.turbos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import co.com.turbos.entity.Vehicle;

@Repository
public interface IVehicleRepository extends JpaRepository<Vehicle, String>{

}
