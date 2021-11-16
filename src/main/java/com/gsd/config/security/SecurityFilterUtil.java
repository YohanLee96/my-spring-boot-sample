package com.gsd.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.experimental.UtilityClass;
import org.springframework.http.MediaType;
import org.springframework.security.core.GrantedAuthority;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Collection;

@UtilityClass
public class SecurityFilterUtil {
	
	public void jsonResponse(HttpServletResponse response, Object dto) {
		ObjectMapper objectMapper = new ObjectMapper();
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		
		try (OutputStream os = response.getOutputStream()) {
			objectMapper.writeValue(os, dto);
			os.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public LoginDTO toLoginDTO(HttpServletRequest request) throws IOException {
		ObjectMapper objectMapper = new ObjectMapper();
		return objectMapper.readValue(request.getInputStream(), LoginDTO.class);
	}
	
	public UserType getAuthority(Collection<? extends GrantedAuthority> grantedAuthorities) {
		return (UserType)grantedAuthorities
				.stream()
				.findFirst()
				.orElseThrow(NullPointerException::new);
	}
}