package co.com.turbos.repository;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import co.com.turbos.entity.WorkDescription;
import co.com.turbos.entity.WorkOrder;

@Repository
public interface IWorkDescriptionRepository extends JpaRepository<WorkDescription, Long>{
	
	Optional<List<WorkDescription>> findByWorkOrder(WorkOrder workOrder);
	
	@Modifying
	@Transactional 
	@Query("DELETE FROM WorkDescription u WHERE u.workOrder.idOrder = :idOrder")
	void deleteByWorkOrder(Long idOrder);

}
