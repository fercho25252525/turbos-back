package co.com.turbos.request;

import org.springframework.web.multipart.MultipartFile;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UploadFile {
	private MultipartFile file;
	private String userName;
}
