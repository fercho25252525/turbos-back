package co.com.turbos.request;

import java.io.Serializable;
import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CustomerRequest implements Serializable {

	private String userName;

	private Boolean enabled;

	private String name;

	private String lastName;

	private String email;

	private String documentNumber;

	private String gender;

	private String photo;

	private String BirthDate;

	private String creationAt;

	private String phone;

	private String address;

	private List<RoleRequest> role;

	private static final long serialVersionUID = 1L;

}
