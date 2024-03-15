package co.com.turbos.parser;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Service;

import co.com.turbos.config.ConfigPassword;
import co.com.turbos.entity.Role;
import co.com.turbos.entity.Users;
import co.com.turbos.request.UserRequest;

@Service
public class UserParser {
	
	private ConfigPassword passwordEncoder; 
	
	public UserParser(ConfigPassword passwordEncoder) {
		this.passwordEncoder = passwordEncoder;
	}

	public UserRequest buildUser(Users entity) {
		return UserRequest.builder().userName(entity.getUserName()).enabled(entity.getEnabled()).name(entity.getName())
				.lastName(entity.getLastName()).email(entity.getEmail()).documentNumber(entity.getDocumentNumber())
				.gender(entity.getGender()).photo(entity.getPhoto()).creationAt(Objects.isNull(entity.getCreatedAt()) ? null : entity.getCreatedAt().toString())
				.role(RoleParser.buildRoleAndRequest(entity.getRole())).BirthDate(entity.getBirthdate()).build();
	}

	public Users buildAddUser(UserRequest requestEvent) {		
		
		Users userNew = Users.builder().userName(requestEvent.getUserName()).password(passwordEncoder.generateassword(requestEvent))
				.enabled(true).name(requestEvent.getName()).lastName(requestEvent.getLastName())
				.email(requestEvent.getEmail()).documentNumber(requestEvent.getDocumentNumber())
				.gender(requestEvent.getGender()).photo(requestEvent.getPhoto()).birthdate(requestEvent.getBirthDate())
				.build(); 

		List<Role> roles = new ArrayList<>();
		requestEvent.getRole().forEach(rol -> {
			Role role = Role.builder().id(rol.getId())
					.name(rol.getName()).build();
			roles.add(role);
		});

		userNew.setRole(roles);
		return userNew;
	}
	
	public Users buildEditUser(UserRequest requestEvent, Date createdAt, String password, String photo) {		
		
		Users userNew = Users.builder().userName(requestEvent.getUserName())
				.enabled(requestEvent.getEnabled()).name(requestEvent.getName()).lastName(requestEvent.getLastName())
				.email(requestEvent.getEmail()).documentNumber(requestEvent.getDocumentNumber())
				.gender(requestEvent.getGender()).photo(photo).birthdate(requestEvent.getBirthDate())
				.createdAt(createdAt)
				.password(password)
				.build(); 

		List<Role> roles = new ArrayList<>();
		requestEvent.getRole().forEach(rol -> {
			Role role = Role.builder().id(rol.getId())
					.name(rol.getName()).build();
			roles.add(role);
		});

		userNew.setRole(roles);
		return userNew;
	}
	

}
