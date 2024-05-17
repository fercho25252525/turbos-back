package co.com.turbos.entity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "PQRS")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Pqrs {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idPqrs;
	private String typePqrs;
	private String datePqrs;
	private String status;
	private String priority;
	private String description;
	private String response;
	private long view;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "user_id")
	private Users user;
}
