package co.com.turbos.service;

import java.util.List;

import co.com.turbos.entity.Pqrs;
import co.com.turbos.response.CommandEvent;
import co.com.turbos.response.ResponseEvent;

public interface IPqrsService {

	ResponseEvent<List<Pqrs>> getPqrs();

	ResponseEvent<Pqrs> addPqrs(CommandEvent<Pqrs> requestEvent);

	ResponseEvent<Pqrs> updatePqrs(CommandEvent<Pqrs> requestEvent);

	ResponseEvent<Boolean> deletePqrs(CommandEvent<Pqrs> requestEvent);
}
