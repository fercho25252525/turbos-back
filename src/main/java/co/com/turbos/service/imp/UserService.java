package co.com.turbos.service.imp;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import co.com.turbos.entity.Role;
import co.com.turbos.entity.Users;
import co.com.turbos.parser.RoleParser;
import co.com.turbos.parser.UserParser;
import co.com.turbos.repository.IRoleRepository;
import co.com.turbos.repository.IUserRepository;
import co.com.turbos.request.RoleRequest;
import co.com.turbos.request.SendImageRequest;
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

	private final IRoleRepository roleRepository;

	private static final String DIR_IMAGES = "src/main/resources/static/images/user/";

	public UserService(IUserRepository usuarioRepository, UserParser userParser, IRoleRepository roleRepository) {
		super();
		this.usuarioRepository = usuarioRepository;
		this.userParser = userParser;
		this.roleRepository = roleRepository;
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
				return new ResponseEvent<List<UserRequest>>().ok("No existen usuarios.", null);
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
				|| Objects.isNull(requestEvent.getRequest().getUserName())
				|| Objects.isNull(requestEvent.getRequest().getRole())) {
			return new ResponseEvent<UserRequest>().badRequest("Correo, usuario o Rol vacio.");
		}

		if (Boolean.TRUE.equals(validateUserExist(requestEvent.getRequest()))) {
			return new ResponseEvent<UserRequest>().badRequest("El usuario ya existe.");
		}

		Users userNew = this.userParser.buildAddUser(requestEvent.getRequest());

		Users user = this.usuarioRepository.save(userNew);
		return new ResponseEvent<UserRequest>().created("Se ha creado correctamente el usuario " + user.getUserName(),
				this.userParser.buildUser(user));
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
			Optional<Users> userOpt = this.usuarioRepository.findById(requestEvent.getRequest().getUserName());
			Users userUpdate = this.userParser.buildEditUser(requestEvent.getRequest(), userOpt.get().getCreatedAt(),
					userOpt.get().getPassword(), userOpt.get().getPhoto());
			Users user = this.usuarioRepository.save(userUpdate);

			return new ResponseEvent<UserRequest>().created(
					"Se ha actualizado correctamente el usuario " + user.getUserName(),
					this.userParser.buildUser(user));
		}
		return new ResponseEvent<UserRequest>().badRequest("El usuario no existe.");

	}

	@Override
	public ResponseEvent<Boolean> deleteUser(CommandEvent<UserRequest> requestEvent) {
		if (Objects.isNull(requestEvent)) {
			return new ResponseEvent<Boolean>().badRequest("Evento null al eliminar usuario.");
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

	@Override
	public ResponseEvent<List<RoleRequest>> getRole() {
		try {
			List<Role> roleList = this.roleRepository.findAll();
			if (roleList.isEmpty() || Objects.isNull(roleList)) {
				return new ResponseEvent<List<RoleRequest>>().notFound("No existen roles.");
			}

			return new ResponseEvent<List<RoleRequest>>().ok("Success", RoleParser.buildRoleAndRequest(roleList));
		} catch (Exception e) {
			return new ResponseEvent<List<RoleRequest>>().applicationError("Ocurrio un error al obtener los usuarios.");
		}
	}

	@Override
	public ResponseEvent<String> uploadImage(MultipartFile file, String userName) throws IOException {
		try {
			log.info("ingresa uploadImage");

			Users user = this.usuarioRepository.findByUserName(userName);
			
			
			if (Objects.isNull(user)) {
				return new ResponseEvent<String>().applicationError("Ocurrio un error, El usuario no existe");
			}

			if (file.isEmpty() || userName.isEmpty()) {
				return new ResponseEvent<String>()
						.applicationError("Ocurrio un error, no se esta enviando el archivo o el username.");
			}
			
			validateExisFile(user.getPhoto());

			String contentType = file.getContentType();
			if (contentType == null || (!contentType.equals("image/jpeg") && !contentType.equals("image/png"))) {
				return new ResponseEvent<String>()
						.applicationError("Ocurrio un error, solo se aceptan imagenes png y jpg.");
			} 

			long fileSizeInBytes = file.getSize();
			long maxSizeInBytes = 2 * 1024 * 1024;
			if (fileSizeInBytes > maxSizeInBytes) {
				return new ResponseEvent<String>()
						.applicationError("Ocurrio un error, el tamaño del archivo excede el límite de 2 MB.");
			}

			String imageName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
			InputStream inputStream = file.getInputStream();
			Files.copy(inputStream, Paths.get(DIR_IMAGES).resolve(imageName), StandardCopyOption.REPLACE_EXISTING);
			inputStream.close();


			this.usuarioRepository.updatePhoto(imageName, userName);
			return new ResponseEvent<String>().ok("Success", "Imagen actualizada");
		} catch (Exception e) {
			return new ResponseEvent<String>().applicationError("Ocurrio un error al ocargar la imagen.");
		}
	}

	private void validateExisFile(String photo) throws IOException {
		try {
			Path imagePath = Paths.get(DIR_IMAGES).resolve(photo);
			
			if (Files.exists(imagePath)) {
			    Files.delete(imagePath);
			} 
		} catch (Exception e) {
			log.error("Ocurrio un error al valida si existe el archivo: " + e);
		}
	}

	@Override
	public SendImageRequest sendImage(String userName) throws IOException {

		Users user = findByUsername(userName);
		if (Objects.isNull(user)) {
			return new SendImageRequest();
		}

		try {
			String imagePath = DIR_IMAGES + user.getPhoto();
			Path path = Paths.get(imagePath);

			if (!Files.exists(path)) {
				return new SendImageRequest();
			}

			byte[] data = Files.readAllBytes(path);
			ByteArrayResource resource = new ByteArrayResource(data);

			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.IMAGE_JPEG);
			headers.setContentLength(data.length);

			SendImageRequest sendImageRequest = SendImageRequest.builder().headers(headers).resource(resource).build();
			return sendImageRequest;
		} catch (Exception e) {
			return new SendImageRequest();
		}
	}

}
