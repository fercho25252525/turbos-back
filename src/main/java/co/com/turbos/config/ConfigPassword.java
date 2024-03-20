package co.com.turbos.config;

import java.time.LocalDate;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class ConfigPassword {

	private final BCryptPasswordEncoder passwordEncoder;

	public ConfigPassword(BCryptPasswordEncoder passwordEncoder) {
		this.passwordEncoder = passwordEncoder;
	}


	public String generateassword(String documentNumber) {
		return passwordEncoder.encode(documentNumber + "-" + LocalDate.now().getYear());
	}	
}
