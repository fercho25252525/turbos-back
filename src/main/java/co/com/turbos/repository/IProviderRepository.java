package co.com.turbos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import co.com.turbos.entity.Provider;

@Repository
public interface IProviderRepository extends JpaRepository<Provider, Long>{

}
