package co.com.turbos.request;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SendImageRequest {

	private HttpHeaders headers;
	private ByteArrayResource resource;
}
