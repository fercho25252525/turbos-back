package co.com.turbos.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "WORK_ORDER")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WorkOrder {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idOrder;
	private String statusOrder;
	private Double estimatedCost;
	private Double realCost;
	private Date startDate;
	private Date endDate;
	private Date creationDate;	
	private String comments;
	
	@OneToMany(mappedBy = "workOrder", fetch = FetchType.EAGER)
	private List<WorkDescription> workDescription;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "vehicle_id")
	private Vehicle vehicle;
	
	

}
