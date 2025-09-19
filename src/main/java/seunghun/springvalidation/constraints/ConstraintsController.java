package seunghun.springvalidation.constraints;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import seunghun.springvalidation.valid.ContentDto;
import seunghun.springvalidation.valid.UserDto;

@RestController
@RequestMapping("/constraints")
public class ConstraintsController {

	@GetMapping("/request-param")
	public ResponseEntity<String> requestParam(
		@RequestParam
		@Min(value = 10, message = "The value must be greater than 10")
		int age,

		@RequestParam
		@NotBlank(message = "The name must not be blank")
		String name
	) {
		return ResponseEntity.ok("Hello " + name + ", your age is " + age);
	}


	@GetMapping("/model/request-param")
	public ResponseEntity<String> requestParamModel(
		UserDto userDto
	) {
		return ResponseEntity.ok("Hello " + userDto.name() + ", your age is " + userDto.age());
	}

	@PostMapping("/request-body")
	public ResponseEntity<String> requestBodyContent(
		@RequestBody ContentDto contentDto
	) {
		UserDto userDto = contentDto.user();

		return ResponseEntity.ok("Hello " + userDto.name() + ", your age is " + userDto.age() + ". content: " + contentDto.content());
	}


	@GetMapping("/path-variable/{name}/{age}")
	public ResponseEntity<String> pathVariable(
		@PathVariable
		@NotBlank
		String name,

		@PathVariable
		@Min(value = 10, message = "The value must be greater than 10")
		int age
	) {
		return ResponseEntity.ok("Hello " + name + ", your age is " + age);
	}

	@GetMapping("/model/path-variable/{name}/{age}")
	public ResponseEntity<String> pathVariableModel(
		UserDto userDto
	) {
		return ResponseEntity.ok("Hello " + userDto.name() + ", your age is " + userDto.age());
	}

	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	public ResponseEntity<String> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException exception) {
		return ResponseEntity.badRequest().body("MethodArgumentTypeMismatchException: " + exception.getMessage());
	}

	@ExceptionHandler(HandlerMethodValidationException.class)
	public ResponseEntity<String> handleHandlerMethodValidationException(HandlerMethodValidationException exception) {
		return ResponseEntity.badRequest().body("HandlerMethodValidationException: " + exception.getMessage());
	}

	@ExceptionHandler(MissingServletRequestParameterException.class)
	public ResponseEntity<String> handleMissingServletRequestParameterException(MissingServletRequestParameterException exception) {
		return ResponseEntity.badRequest().body("MissingServletRequestParameterException: " + exception.getMessage());
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<String> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
		return ResponseEntity.badRequest().body("MethodArgumentNotValidException: " + exception.getMessage());
	}

}
