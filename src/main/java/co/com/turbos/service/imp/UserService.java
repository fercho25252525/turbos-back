package co.com.turbos.service.imp;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.com.turbos.entity.Users;
import co.com.turbos.parser.UserParser;
import co.com.turbos.repository.IUserRepository;
import co.com.turbos.request.UserRequest;
import co.com.turbos.response.CommandEvent;
import co.com.turbos.response.ResponseEvent;
import co.com.turbos.service.IUserService;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UserService implements IUserService, UserDetailsService {

	private final IUserRepository usuarioRepository;
	
	private final UserParser userParser;

	@Autowired
	public UserService(IUserRepository usuarioRepository, UserParser userParser) {
		this.usuarioRepository = usuarioRepository;
		this.userParser = userParser;
	}

	@Override
	@Transactional(readOnly = true)
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		Users usuario = this.usuarioRepository.findByUserName(username);

		if (usuario == null) {
			log.error("Error en el login: no existe el usuario '" + username + "' en el sistema!");
			throw new UsernameNotFoundException(
					"Error en el login: no existe el usuario '" + username + "' en el sistema!");
		}

		List<GrantedAuthority> authorities = usuario.getRole().stream()
				.map(role -> new SimpleGrantedAuthority(role.getName()))
				.peek(authority -> log.info("Role: " + authority.getAuthority())).collect(Collectors.toList());

		return new User(usuario.getUserName(), usuario.getPassword(), usuario.getEnabled(), true, true, true,
				authorities);
	}

	@Override
	@Transactional(readOnly = true)
	public Users findByUsername(String username) {
		return this.usuarioRepository.findByUserName(username);
	}

	@Override
	@Transactional(readOnly = true)
	public ResponseEvent<List<UserRequest>> getUsers() {
		try {
			List<Users> usersList = this.usuarioRepository.findAll();
			if (usersList.isEmpty() || Objects.isNull(usersList)) {
				return new ResponseEvent<List<UserRequest>>().notFound("No existen usuarios.");
			}

			List<UserRequest> requestList = new ArrayList<>();
			usersList.forEach(user -> requestList.add(this.userParser.buildUser(user)));

			return new ResponseEvent<List<UserRequest>>().ok("Success", requestList);
		} catch (Exception e) {
			log.error("Ocurrio un error al obtener los usuarios: " + e);
			return new ResponseEvent<List<UserRequest>>().applicationError("Ocurrio un error al obtener los usuarios.");
		}
	}

	@Override
	@Transactional(readOnly = false)
	public ResponseEvent<UserRequest> addUsers(CommandEvent<UserRequest> requestEvent) {

		if (Objects.isNull(requestEvent)) {
			return new ResponseEvent<UserRequest>().badRequest("Evento null al crear usuario.");
		}
		if (Objects.isNull(requestEvent.getRequest().getEmail())
				|| Objects.isNull(requestEvent.getRequest().getUserName())) {
			return new ResponseEvent<UserRequest>().badRequest("Correo o usuario vacio.");
		}

		if (Boolean.TRUE.equals(validateUserExist(requestEvent.getRequest()))) {
			return new ResponseEvent<UserRequest>().badRequest("El usuario ya existe.");
		}
		
		Users userNew = this.userParser.buildAddUser(requestEvent.getRequest());

	    Users user = this.usuarioRepository.save(userNew);
		return new ResponseEvent<UserRequest>().created("Success", this.userParser.buildUser(user));
	}

	@Override
	public ResponseEvent<UserRequest> updateUsers(CommandEvent<UserRequest> requestEvent) {
		if (Objects.isNull(requestEvent)) {
			return new ResponseEvent<UserRequest>().badRequest("Evento null al crear usuario.");
		}
		if (Objects.isNull(requestEvent.getRequest().getEmail())
				|| Objects.isNull(requestEvent.getRequest().getUserName())) {
			return new ResponseEvent<UserRequest>().badRequest("Correo o usuario vacio.");
		}

		if (Boolean.TRUE.equals(validateUserExist(requestEvent.getRequest()))) {
			Users userUpdate = this.userParser.buildAddUser(requestEvent.getRequest());
		    Users user = this.usuarioRepository.save(userUpdate);
		    
		    return new ResponseEvent<UserRequest>().created("Success", this.userParser.buildUser(user));			
		}
		
		return new ResponseEvent<UserRequest>().badRequest("El usuario no existe.");
		
	}

	@Override
	public ResponseEvent<Boolean> deleteUser(CommandEvent<UserRequest> requestEvent) {
		if (Objects.isNull(requestEvent)) {
			return new ResponseEvent<Boolean>().badRequest("Evento null al crear usuario.");
		}
		if (Objects.isNull(requestEvent.getRequest().getEmail())
				|| Objects.isNull(requestEvent.getRequest().getUserName())) {
			return new ResponseEvent<Boolean>().badRequest("Correo o usuario vacio.");
		}

		if (Boolean.FALSE.equals(validateUserExist(requestEvent.getRequest()))) {		    
		    return new ResponseEvent<Boolean>().badRequest("El usuario no existe.");			
		}
		
		this.usuarioRepository.deleteById(requestEvent.getRequest().getUserName());
		return new ResponseEvent<Boolean>().noContent("Usuario eliminado.", null);
	}
	

	private Boolean validateUserExist(UserRequest userRequest) {
		List<Users> existUser = this.usuarioRepository.findByUserNameOrEmail(userRequest.getUserName(),
				userRequest.getEmail());
		if (!existUser.isEmpty()) {
			return Boolean.TRUE;
		}
		
		return Boolean.FALSE;
	}


}
