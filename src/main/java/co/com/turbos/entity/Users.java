package co.com.turbos.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "USER")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(UserEntityListener.class)
public class Users implements Serializable {

	@Id
	@Column(name = "user_name", unique = true, length = 20)
	private String userName;

	@Column(length = 60)
	private String password;

	private Boolean enabled;
	
	private String name;
	
	@Column(name = "last_name") 
	private String lastName;
	
	@Column(unique = true)
	private String email;
	
	@Column(name = "document_number")
	private String documentNumber;
	
	private String gender;
	
	private String photo;
	
	private String birthdate;
	
	@Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at")
	private Date createdAt;

	@ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinTable(name="users_roles", joinColumns= @JoinColumn(name="user_id"),
	inverseJoinColumns=@JoinColumn(name="role_id"),
	uniqueConstraints= {@UniqueConstraint(columnNames= {"user_id", "role_id"})})
	private List<Role> role;
	
	private static final long serialVersionUID = 1L;
}
