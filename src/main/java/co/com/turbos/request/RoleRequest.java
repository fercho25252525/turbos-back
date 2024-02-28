package co.com.turbos.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RoleRequest {

	private Long id;

	private String name;
}
