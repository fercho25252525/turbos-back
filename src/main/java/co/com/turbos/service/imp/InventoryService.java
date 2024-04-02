package co.com.turbos.service.imp;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import co.com.turbos.entity.Inventory;
import co.com.turbos.entity.Provider;
import co.com.turbos.repository.IInventoryRepository;
import co.com.turbos.repository.IProviderRepository;
import co.com.turbos.response.CommandEvent;
import co.com.turbos.response.ResponseEvent;
import co.com.turbos.service.IInventoryService;

@Service
public class InventoryService implements IInventoryService {

	private final IInventoryRepository inventoryRepository;

	private final IProviderRepository iProviderRepository;

	public InventoryService(IInventoryRepository inventoryRepository, IProviderRepository iProviderRepository) {
		this.inventoryRepository = inventoryRepository;
		this.iProviderRepository = iProviderRepository;
	}

	@Override
	public ResponseEvent<List<Inventory>> getInventory() {
		return new ResponseEvent<List<Inventory>>().ok("Success", inventoryRepository.findAll());
	}

	@Override
	public ResponseEvent<Inventory> addInventory(CommandEvent<Inventory> requestEvent) {

		Optional<Provider> provider = iProviderRepository.findById(requestEvent.getRequest().getProvider().getId());
		Inventory inventory = Inventory.builder().name(requestEvent.getRequest().getName())
				.description(requestEvent.getRequest().getDescription())
				.cuantityStock(requestEvent.getRequest().getCuantityStock())
				.unitPrice(requestEvent.getRequest().getUnitPrice())
				.purchaseDate(requestEvent.getRequest().getPurchaseDate()).status(requestEvent.getRequest().getStatus())
				.category(requestEvent.getRequest().getCategory()).provider(provider.orElse(null)).build();

		Inventory inventoryResponse = inventoryRepository.save(inventory);
		return new ResponseEvent<Inventory>().created(
				"Se ha creado correctamente el producto " + requestEvent.getRequest().getName(), inventoryResponse);
	}

	@Override
	public ResponseEvent<Inventory> updateInventory(CommandEvent<Inventory> requestEvent) {

		Optional<Boolean> existInventory = existProduct(requestEvent.getRequest().getId());

		if (!existInventory.get()) {
			return new ResponseEvent<Inventory>().badRequest("El producto no existe.");
		}

		Optional<Provider> provider = iProviderRepository.findById(requestEvent.getRequest().getProvider().getId());
		Inventory inventory = Inventory.builder().id(requestEvent.getRequest().getId())
				.name(requestEvent.getRequest().getName()).description(requestEvent.getRequest().getDescription())
				.cuantityStock(requestEvent.getRequest().getCuantityStock())
				.unitPrice(requestEvent.getRequest().getUnitPrice())
				.purchaseDate(requestEvent.getRequest().getPurchaseDate()).status(requestEvent.getRequest().getStatus())
				.category(requestEvent.getRequest().getCategory()).provider(provider.orElse(null)).build();

		Inventory inventoryResponse = inventoryRepository.save(inventory);
		return new ResponseEvent<Inventory>().created(
				"Se ha actualizado correctamente el producto " + requestEvent.getRequest().getName(),
				inventoryResponse);
	}

	@Override
	public ResponseEvent<Boolean> deleteInventory(CommandEvent<Inventory> requestEvent) {
		Optional<Boolean> existProduct = existProduct(requestEvent.getRequest().getId());

		if (!existProduct.get()) {
			return new ResponseEvent<Boolean>().badRequest("El proveedor no existe.");
		}

		this.inventoryRepository.deleteById(requestEvent.getRequest().getId());
		return new ResponseEvent<Boolean>().noContent("Producto eliminado.", null);
	}

	public Optional<Boolean> existProduct(Long id) {
		Optional<Inventory> inventory = inventoryRepository.findById(id);
		if (inventory.isPresent()) {
			return Optional.of(true);
		}
		return Optional.of(false);
	}

	@Override
	public void updateInventoryByProvider(Provider provider) {
		List<Inventory> inventories = inventoryRepository.findByProvider(provider);
		inventories.forEach(inventory -> inventory.setProvider(null));
		inventoryRepository.saveAll(inventories);
	}

}
