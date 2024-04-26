package co.com.turbos.entity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data; 
import lombok.NoArgsConstructor;

@Entity
@Table(name = "WORk_DESCRIPTION")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WorkDescription {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idWork;
	
	private String typeWork;
	
	private Double coste;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "mechanic_id")
	private Users mechanic;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "work_order_id")
	@JsonIgnore
	private WorkOrder workOrder;

}
