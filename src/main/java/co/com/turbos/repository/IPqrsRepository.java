package co.com.turbos.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import co.com.turbos.entity.Pqrs;

@Repository
public interface IPqrsRepository extends JpaRepository<Pqrs, Long>{

    List<Pqrs> findAllByOrderByViewDesc();

}
