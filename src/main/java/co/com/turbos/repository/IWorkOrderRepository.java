package co.com.turbos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import co.com.turbos.entity.WorkOrder;

@Repository
public interface IWorkOrderRepository extends JpaRepository<WorkOrder, Long>{

}
