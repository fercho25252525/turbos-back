package co.com.turbos.service;

import java.util.List;

import co.com.turbos.entity.Provider;
import co.com.turbos.response.CommandEvent;
import co.com.turbos.response.ResponseEvent;

public interface IProviderService {

	ResponseEvent<List<Provider>> getProvider();

	ResponseEvent<Provider> addProvider(CommandEvent<Provider> requestEvent);

	ResponseEvent<Provider> updateProvider(CommandEvent<Provider> requestEvent);

	ResponseEvent<Boolean> deleteProvider(CommandEvent<Provider> requestEvent);

}
