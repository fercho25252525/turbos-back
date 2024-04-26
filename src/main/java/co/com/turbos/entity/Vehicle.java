package co.com.turbos.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "VEHICLE")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Vehicle {
	
	@Id
	private String plate;
	private String city;
	private String brand;
	private String line;
	private String model;
	private String typeVehicle;
	private String typeFuels;
	private String status;
	private String color;
	private Date nextMaintenanceDate;
	private Date registerDate;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "customer_id")
	private Users customer;
	

}
