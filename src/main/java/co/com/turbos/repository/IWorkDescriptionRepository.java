package co.com.turbos.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import co.com.turbos.entity.WorkDescription;
import co.com.turbos.entity.WorkOrder;

@Repository
public interface IWorkDescriptionRepository extends JpaRepository<WorkDescription, Long>{
	
	Optional<List<WorkDescription>> findByWorkOrder(WorkOrder workOrder);

}
