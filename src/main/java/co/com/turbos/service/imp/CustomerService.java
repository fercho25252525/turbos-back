package co.com.turbos.service.imp;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.stereotype.Service;

import co.com.turbos.entity.Users;
import co.com.turbos.parser.UserParser;
import co.com.turbos.repository.IUserRepository;
import co.com.turbos.request.CustomerRequest;
import co.com.turbos.request.UserRequest;
import co.com.turbos.response.CommandEvent;
import co.com.turbos.response.ResponseEvent;
import co.com.turbos.service.ICustomerService;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CustomerService implements ICustomerService {

	private final IUserRepository usuarioRepository;

	private final UserParser userParser;

	public CustomerService(IUserRepository usuarioRepository, UserParser userParser) {
		this.usuarioRepository = usuarioRepository;
		this.userParser = userParser;
	}

	@Override
	public ResponseEvent<List<CustomerRequest>> getCustomer() {
		try {
			List<Users> usersList = this.usuarioRepository.findUsersCustomer();
			if (usersList.isEmpty() || Objects.isNull(usersList)) {
				return new ResponseEvent<List<CustomerRequest>>().ok("No existen usuarios.", null);
			}

			List<CustomerRequest> requestList = new ArrayList<>();
			usersList.forEach(user -> requestList.add(this.userParser.buildCustomer(user)));

			return new ResponseEvent<List<CustomerRequest>>().ok("Success", requestList);
		} catch (Exception e) {
			log.error("Ocurrio un error al obtener los usuarios: " + e);
			return new ResponseEvent<List<CustomerRequest>>()
					.applicationError("Ocurrio un error al obtener los usuarios.");
		}
	}

	@Override
	public ResponseEvent<CustomerRequest> addCustomer(CommandEvent<CustomerRequest> requestEvent) {
		if (Objects.isNull(requestEvent)) {
			return new ResponseEvent<CustomerRequest>().badRequest("Evento null al crear usuario.");
		}
		if (Objects.isNull(requestEvent.getRequest().getEmail())
				|| Objects.isNull(requestEvent.getRequest().getUserName())) {
			return new ResponseEvent<CustomerRequest>().badRequest("Correo o usuario vacio.");
		}

		if (Boolean.TRUE.equals(this.userParser.validateUserExist(requestEvent.getRequest().getUserName(),
				requestEvent.getRequest().getEmail()))) {
			return new ResponseEvent<CustomerRequest>().badRequest("El usuario ya existe.");
		}

		Users userNew = this.userParser.buildAddCustomer(requestEvent.getRequest());

		Users user = this.usuarioRepository.save(userNew);
		return new ResponseEvent<CustomerRequest>().created(
				"Se ha creado correctamente el usuario " + user.getUserName(), this.userParser.buildCustomer(user));
	}

	@Override
	public ResponseEvent<CustomerRequest> updateCustomer(CommandEvent<CustomerRequest> requestEvent) {
		if (Objects.isNull(requestEvent)) {
			return new ResponseEvent<CustomerRequest>().badRequest("Evento null al crear usuario.");
		}
		if (Objects.isNull(requestEvent.getRequest().getEmail())
				|| Objects.isNull(requestEvent.getRequest().getUserName())) {
			return new ResponseEvent<CustomerRequest>().badRequest("Correo o usuario vacio.");
		}
		
		log.info(requestEvent.getRequest()+"");
		
		log.info(requestEvent.getRequest().getUserName()+"");

		if (Boolean.TRUE.equals(this.userParser.validateUserExist(requestEvent.getRequest().getUserName(), requestEvent.getRequest().getEmail()))) {
			Optional<Users> userOpt = this.usuarioRepository.findById(requestEvent.getRequest().getUserName());
			Users userUpdate = this.userParser.buildEditCustomer(requestEvent.getRequest(), userOpt.get().getCreatedAt(),
					userOpt.get().getPassword(), userOpt.get().getPhoto());
			Users user = this.usuarioRepository.save(userUpdate);

			return new ResponseEvent<CustomerRequest>().created(
					"Se ha actualizado correctamente el usuario " + user.getUserName(),
					this.userParser.buildCustomer(user));
		}
		return new ResponseEvent<CustomerRequest>().badRequest("El usuario no existe.");
	}

	@Override
	public ResponseEvent<Boolean> deleteCustomer(CommandEvent<CustomerRequest> requestEvent) {
		if (Objects.isNull(requestEvent)) {
			return new ResponseEvent<Boolean>().badRequest("Evento null al eliminar usuario.");
		}
		if (Objects.isNull(requestEvent.getRequest().getEmail())
				|| Objects.isNull(requestEvent.getRequest().getUserName())) {
			return new ResponseEvent<Boolean>().badRequest("Correo o usuario vacio.");
		}

		if (Boolean.FALSE.equals(this.userParser.validateUserExist(requestEvent.getRequest().getUserName(), requestEvent.getRequest().getEmail()))) {
			return new ResponseEvent<Boolean>().badRequest("El usuario no existe.");
		}

		this.usuarioRepository.deleteById(requestEvent.getRequest().getUserName());
		return new ResponseEvent<Boolean>().noContent("Usuario eliminado.", null);
	}

}
