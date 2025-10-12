package seunghun.springvalidation.handle.dto;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

public record GroupDto(
	@NotBlank(message = "이름은 필수입니다") String name,
	@Valid List<UserDto> users
) {
}
