package seunghun.springvalidation.validation.other;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;

import seunghun.springvalidation.validation.valid.UserDto;

@RestController
@RequestMapping("/other")
public class OtherController {

	@PostMapping("/json")
	public ResponseEntity<String> handleJson(@RequestBody UserDto userDto) {
		return ResponseEntity.ok("Received: " + userDto.name());
	}


	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<String> handleHttpMessageNotReadableException(HttpMessageNotReadableException exception) {
		return ResponseEntity.badRequest().body("HttpMessageNotReadableException: " + exception.getMessage());
	}

	@ExceptionHandler(HttpMediaTypeNotSupportedException.class)
	public ResponseEntity<String> handleHttpMediaTypeNotSupportedException(HttpMediaTypeNotSupportedException exception) {
		return ResponseEntity.badRequest().body("HttpMediaTypeNotSupportedException: " + exception.getMessage());
	}
}