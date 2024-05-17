package co.com.turbos.service.imp;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.com.turbos.entity.Pqrs;
import co.com.turbos.repository.IPqrsRepository;
import co.com.turbos.response.CommandEvent;
import co.com.turbos.response.ResponseEvent;
import co.com.turbos.service.IPqrsService;

@Service
public class PqrsService implements IPqrsService{
	
	private final IPqrsRepository iPqrsRepository;

	@Autowired
	public PqrsService(IPqrsRepository iPqrsRepository) {
		this.iPqrsRepository = iPqrsRepository;
	}

	@Override
	public ResponseEvent<List<Pqrs>> getPqrs() {
		return new ResponseEvent<List<Pqrs>>().ok("Success", iPqrsRepository.findAllByOrderByViewDesc());

	}

	@Override
	public ResponseEvent<Pqrs> addPqrs(CommandEvent<Pqrs> requestEvent) {
		Pqrs pqrs = Pqrs.builder()
				.typePqrs(requestEvent.getRequest().getTypePqrs())
				.status("Creado")
				.priority(requestEvent.getRequest().getPriority())
				.description(requestEvent.getRequest().getDescription())
				.response(requestEvent.getRequest().getResponse())
				.view(1)
				.user(requestEvent.getRequest().getUser())
				.datePqrs(new Date().toString())
				.build();
 
		Pqrs pqrsrResponse = iPqrsRepository.save(pqrs);
		return new ResponseEvent<Pqrs>().created(
				"Se ha creado correctamente el PQRS " + pqrsrResponse.getIdPqrs(), pqrsrResponse);
	
	}

	@Override
	public ResponseEvent<Pqrs> updatePqrs(CommandEvent<Pqrs> requestEvent) {
		Optional<Boolean> existPqrs = existPqrs(requestEvent.getRequest().getIdPqrs());

		if (existPqrs.isEmpty()) {
			return new ResponseEvent<Pqrs>().badRequest("El PQRS no existe.");
		}
		
		Pqrs pqrs = Pqrs.builder()
				.idPqrs(requestEvent.getRequest().getIdPqrs())
				.typePqrs(requestEvent.getRequest().getTypePqrs())
				.status(requestEvent.getRequest().getStatus())
				.priority(requestEvent.getRequest().getPriority())
				.description(requestEvent.getRequest().getDescription())
				.response(requestEvent.getRequest().getResponse())
				.user(requestEvent.getRequest().getUser())
				.view(requestEvent.getRequest().getView())
				.datePqrs(new Date().toString())
				.build();

		Pqrs pqrsrResponse = iPqrsRepository.save(pqrs);
		return new ResponseEvent<Pqrs>().created(
				"Se ha actualizado correctamente el PQRS " + requestEvent.getRequest().getIdPqrs(), pqrsrResponse);
	}

	@Override
	public ResponseEvent<Boolean> deletePqrs(CommandEvent<Pqrs> requestEvent) {

		Optional<Boolean> existPqrs = existPqrs(requestEvent.getRequest().getIdPqrs());

		if (Boolean.FALSE.equals(existPqrs.get())) {
			return new ResponseEvent<Boolean>().badRequest("El PQRS no existe.");
		}
		
		
		this.iPqrsRepository.deleteById(requestEvent.getRequest().getIdPqrs());
		return new ResponseEvent<Boolean>().noContent("Proveedor eliminado.", null);
	}
	
	
	public Optional<Boolean> existPqrs(Long id) {
		Optional<Pqrs> pqrs = iPqrsRepository.findById(id);
		if (pqrs.isPresent()) {
			return Optional.of(true);
		}
		return Optional.of(false);
	}

}
