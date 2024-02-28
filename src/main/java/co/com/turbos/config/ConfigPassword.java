package co.com.turbos.config;

import java.time.LocalDate;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import co.com.turbos.request.UserRequest;

@Component
public class ConfigPassword {

	private final BCryptPasswordEncoder passwordEncoder;

	public ConfigPassword(BCryptPasswordEncoder passwordEncoder) {
		this.passwordEncoder = passwordEncoder;
	}


	public String generateassword(UserRequest requestEvent) {
		return passwordEncoder.encode(requestEvent.getDocumentNumber() + "-" + LocalDate.now().getYear());
	}	
}
