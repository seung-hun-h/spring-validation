package seunghun.springvalidation.valid;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

public record ContentDto(
	@Valid
	UserDto user,
	@NotBlank
	String content
) {
}
