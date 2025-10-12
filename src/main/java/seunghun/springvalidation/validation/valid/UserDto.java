package seunghun.springvalidation.validation.valid;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record UserDto(
	@NotBlank
	String name,

	@Min(value = 10, message = "The value must be greater than 10")
	int age
) {
}
