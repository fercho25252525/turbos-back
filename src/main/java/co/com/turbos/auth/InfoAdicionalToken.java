package co.com.turbos.auth;

import java.util.HashMap;
import java.util.Map;

import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.stereotype.Component;

import co.com.turbos.entity.Users;
import co.com.turbos.service.IUserService;

@Component
public class InfoAdicionalToken implements TokenEnhancer{
	
	private final IUserService usuarioService;
	
	public InfoAdicionalToken(IUserService usuarioService) {
		super();
		this.usuarioService = usuarioService;
	}


	@Override
	public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
		
		Users usuario = usuarioService.findByUsername(authentication.getName());
		Map<String, Object> info = new HashMap<>();
		info.put("infoAdicional", "Hola que tal!: ".concat(authentication.getName()));
		
		info.put("name", usuario.getName());
		info.put("lasName", usuario.getLastName());
		info.put("email", usuario.getEmail());
		
		((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(info);
		
		return accessToken;
	}

}
