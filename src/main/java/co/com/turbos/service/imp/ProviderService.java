package co.com.turbos.service.imp;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.com.turbos.entity.Provider;
import co.com.turbos.repository.IProviderRepository;
import co.com.turbos.response.CommandEvent;
import co.com.turbos.response.ResponseEvent;
import co.com.turbos.service.IInventoryService;
import co.com.turbos.service.IProviderService;

@Service
public class ProviderService implements IProviderService {

	private final IProviderRepository providerRepository;
	
	private final IInventoryService inventoryService;

	@Autowired
	public ProviderService(IProviderRepository providerRepositry, IInventoryService inventoryService) {
		this.providerRepository = providerRepositry;
		this.inventoryService = inventoryService;
	}

	@Override
	public ResponseEvent<List<Provider>> getProvider() {
		return new ResponseEvent<List<Provider>>().ok("Success", providerRepository.findAll());
	}

	@Override
	public ResponseEvent<Provider> addProvider(CommandEvent<Provider> requestEvent) {

		Provider provider = Provider.builder().name(requestEvent.getRequest().getName())
				.address(requestEvent.getRequest().getAddress()).phone(requestEvent.getRequest().getPhone())
				.email(requestEvent.getRequest().getEmail())
				.additionalInfo(requestEvent.getRequest().getAdditionalInfo()).dateCreation(new Date()).build();

		Provider providerResponse = providerRepository.save(provider);
		return new ResponseEvent<Provider>().created(
				"Se ha creado correctamente el proveedor " + requestEvent.getRequest().getName(), providerResponse);
	}

	@Override
	public ResponseEvent<Provider> updateProvider(CommandEvent<Provider> requestEvent) {

		Optional<Boolean> existProvider = existProvider(requestEvent.getRequest().getId());

		if (existProvider.isEmpty()) {
			return new ResponseEvent<Provider>().badRequest("El proveedor no existe.");
		}

		Provider provider = Provider.builder().id(requestEvent.getRequest().getId()).name(requestEvent.getRequest().getName())
				.address(requestEvent.getRequest().getAddress()).phone(requestEvent.getRequest().getPhone())
				.email(requestEvent.getRequest().getEmail())
				.additionalInfo(requestEvent.getRequest().getAdditionalInfo()).dateCreation(new Date()).build();

		Provider providerResponse = providerRepository.save(provider);
		return new ResponseEvent<Provider>().created(
				"Se ha actualizado correctamente el proveedor " + requestEvent.getRequest().getName(), providerResponse);
	}

	@Override
	public ResponseEvent<Boolean> deleteProvider(CommandEvent<Provider> requestEvent) {
		
		Optional<Boolean> existProvider = existProvider(requestEvent.getRequest().getId());

		if (Boolean.FALSE.equals(existProvider.get())) {
			return new ResponseEvent<Boolean>().badRequest("El proveedor no existe.");
		}
		
		inventoryService.updateInventoryByProvider(requestEvent.getRequest());
		
		this.providerRepository.deleteById(requestEvent.getRequest().getId());
		return new ResponseEvent<Boolean>().noContent("Proveedor eliminado.", null);
	}

	public Optional<Boolean> existProvider(Long id) {
		Optional<Provider> provider = providerRepository.findById(id);
		if (provider.isPresent()) {
			return Optional.of(true);
		}
		return Optional.of(false);
	}

}
