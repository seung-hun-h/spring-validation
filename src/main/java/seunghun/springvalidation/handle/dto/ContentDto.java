package seunghun.springvalidation.handle.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

public record ContentDto(
	@Valid UserDto user,
	@NotBlank(message = "내용은 필수입니다") String content
) {
}
