package co.com.turbos.service;

import java.util.List;

import co.com.turbos.entity.Inventory;
import co.com.turbos.entity.Provider;
import co.com.turbos.response.CommandEvent;
import co.com.turbos.response.ResponseEvent;

public interface IInventoryService {

	ResponseEvent<List<Inventory>> getInventory();

	ResponseEvent<Inventory> addInventory(CommandEvent<Inventory> requestEvent);

	ResponseEvent<Inventory> updateInventory(CommandEvent<Inventory> requestEvent);

	ResponseEvent<Boolean> deleteInventory(CommandEvent<Inventory> requestEvent);
	
	void updateInventoryByProvider(Provider provider);
}
