package co.com.turbos.parser;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.stereotype.Service;

import co.com.turbos.config.ConfigPassword;
import co.com.turbos.entity.Role;
import co.com.turbos.entity.Users;
import co.com.turbos.repository.IRoleRepository;
import co.com.turbos.repository.IUserRepository;
import co.com.turbos.request.CustomerRequest;
import co.com.turbos.request.UserRequest;

@Service
public class UserParser {

	private ConfigPassword passwordEncoder;

	private final IUserRepository usuarioRepository;
	
	private final IRoleRepository iRoleRepository;

	public UserParser(ConfigPassword passwordEncoder, IUserRepository usuarioRepository, IRoleRepository iRoleRepository) {
		this.passwordEncoder = passwordEncoder;
		this.usuarioRepository = usuarioRepository;
		this.iRoleRepository = iRoleRepository;
	}

	public UserRequest buildUser(Users entity) {
		return UserRequest.builder().userName(entity.getUserName()).enabled(entity.getEnabled()).name(entity.getName())
				.lastName(entity.getLastName()).email(entity.getEmail()).documentNumber(entity.getDocumentNumber())
				.gender(entity.getGender()).photo(entity.getPhoto())
				.creationAt(Objects.isNull(entity.getCreatedAt()) ? null : entity.getCreatedAt().toString())
				.role(RoleParser.buildRoleAndRequest(entity.getRole())).BirthDate(entity.getBirthdate()).build();
	}

	public Users buildAddUser(UserRequest requestEvent) {

		Users userNew = Users.builder().userName(requestEvent.getUserName())
				.password(passwordEncoder.generateassword(requestEvent.getDocumentNumber())).enabled(true).name(requestEvent.getName())
				.lastName(requestEvent.getLastName()).email(requestEvent.getEmail())
				.documentNumber(requestEvent.getDocumentNumber()).gender(requestEvent.getGender())
				.photo(requestEvent.getPhoto()).birthdate(requestEvent.getBirthDate()).build();

		List<Role> roles = new ArrayList<>();
		requestEvent.getRole().forEach(rol -> {
			Role role = Role.builder().id(rol.getId()).name(rol.getName()).build();
			roles.add(role);
		});

		userNew.setRole(roles);
		return userNew;
	}


	public Users buildEditUser(UserRequest requestEvent, Date createdAt, String password, String photo) {

		Users userNew = Users.builder().userName(requestEvent.getUserName()).enabled(requestEvent.getEnabled())
				.name(requestEvent.getName()).lastName(requestEvent.getLastName()).email(requestEvent.getEmail())
				.documentNumber(requestEvent.getDocumentNumber()).gender(requestEvent.getGender()).photo(photo)
				.birthdate(requestEvent.getBirthDate()).createdAt(createdAt).password(password).build();

		List<Role> roles = new ArrayList<>();
		requestEvent.getRole().forEach(rol -> {
			Role role = Role.builder().id(rol.getId()).name(rol.getName()).build();
			roles.add(role);
		});

		userNew.setRole(roles);
		return userNew;
	}

	public Boolean validateUserExist(String userName, String email) {
		List<Users> existUser = this.usuarioRepository.findByUserNameOrEmail(userName,
				email);
		if (!existUser.isEmpty()) {
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}
	
	public Optional<Users> userByUserNameOrEmail(String userName, String email) {
		List<Users> user = this.usuarioRepository.findByUserNameOrEmail(userName,
				email);
		if (!user.isEmpty()) {
			return Optional.of(user.get(0));
		}
		
		return Optional.empty();
	}
	

	public Users buildAddCustomer(CustomerRequest requestEvent) {

		Users userNew = Users.builder().userName(requestEvent.getUserName())
				.password(passwordEncoder.generateassword(requestEvent.getDocumentNumber())).enabled(true).name(requestEvent.getName())
				.lastName(requestEvent.getLastName()).email(requestEvent.getEmail())
				.documentNumber(requestEvent.getDocumentNumber()).gender(requestEvent.getGender())
				.photo(requestEvent.getPhoto()).birthdate(requestEvent.getBirthDate()).phone(requestEvent.getPhone()).address(requestEvent.getAddress()).build();
		
		userNew.setRole(getRole());
		return userNew;
	}
	
	private List<Role> getRole() {
		Role role = this.iRoleRepository.findByName("ROLE_CUSTOMER");
		List<Role> roles = new ArrayList<>(); 
	  
		roles.add(role);
		return roles;
	}
	
	
	public CustomerRequest buildCustomer(Users entity) {
		return CustomerRequest.builder().userName(entity.getUserName()).enabled(entity.getEnabled()).name(entity.getName())
				.lastName(entity.getLastName()).email(entity.getEmail()).documentNumber(entity.getDocumentNumber())
				.gender(entity.getGender()).photo(entity.getPhoto())
				.creationAt(Objects.isNull(entity.getCreatedAt()) ? null : entity.getCreatedAt().toString())
				.role(RoleParser.buildRoleAndRequest(entity.getRole())).BirthDate(entity.getBirthdate()).phone(entity.getPhone()).address(entity.getAddress()).build();
	}
	
	public Users buildEditCustomer(CustomerRequest requestEvent, Date createdAt, String password, String photo) {

		Users userNew = Users.builder().userName(requestEvent.getUserName()).enabled(requestEvent.getEnabled())
				.name(requestEvent.getName()).lastName(requestEvent.getLastName()).email(requestEvent.getEmail())
				.documentNumber(requestEvent.getDocumentNumber()).gender(requestEvent.getGender()).photo(photo)
				.birthdate(requestEvent.getBirthDate()).createdAt(createdAt).password(password).phone(requestEvent.getPhone()).address(requestEvent.getAddress()).build();

		userNew.setRole(getRole());
		return userNew;
	}

}
