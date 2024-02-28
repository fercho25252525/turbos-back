package co.com.turbos.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import co.com.turbos.entity.Role;

public interface IRoleRepository extends JpaRepository<Role, Long>{

}
