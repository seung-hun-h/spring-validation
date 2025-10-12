package seunghun.springvalidation.handle.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record UserDto(
	@NotBlank(message = "이름은 필수입니다")
	String name,

	@NotNull(message = "나이는 필수입니다")
	@Positive
	@Min(value = 10, message = "나이는 10 이상이어야 합니다")
	Integer age
) {
}
