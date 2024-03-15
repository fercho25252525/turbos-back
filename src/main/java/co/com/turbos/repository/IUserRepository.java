package co.com.turbos.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import co.com.turbos.entity.Users;

@Repository
public interface IUserRepository extends JpaRepository<Users, String> {

	Users findByUserName(String userName);
	
	@Query("SELECT u.photo FROM Users u WHERE u.userName =:userName")
	String findByUserNamePhoto(String userName);

	List<Users> findByUserNameOrEmail(String userName, String email);

	@Transactional
	@Modifying
	@Query("UPDATE Users u SET u.photo = :photo WHERE u.userName = :userName")
	void updatePhoto(String photo, String userName);

}
