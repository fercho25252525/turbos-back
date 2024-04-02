package co.com.turbos.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import co.com.turbos.entity.Inventory;
import co.com.turbos.entity.Provider;

@Repository
public interface IInventoryRepository extends JpaRepository<Inventory, Long>{

	List<Inventory> findByProvider(Provider provider);
}
