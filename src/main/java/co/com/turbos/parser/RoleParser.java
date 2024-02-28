package co.com.turbos.parser;

import java.util.List;
import java.util.stream.Collectors;

import co.com.turbos.entity.Role;
import co.com.turbos.request.RoleRequest;

public class RoleParser { 

	public static List<Role> buildRole(List<RoleRequest> entity, String key) {
		return entity.stream()
				.map(enty -> Role.builder().id(enty.getId()).name(enty.getName()).build())
				.collect(Collectors.toList());
	}

	public static List<RoleRequest> buildRoleAndRequest(List<Role> entity) {
		return entity.stream().map(entitys -> RoleRequest.builder().id(entitys.getId()).name(entitys.getName()).build())
				.collect(Collectors.toList());
	}

}
