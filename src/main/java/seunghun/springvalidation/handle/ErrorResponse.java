package seunghun.springvalidation.handle;

import java.time.LocalDateTime;
import java.util.List;

public record ErrorResponse(
	String message,
	LocalDateTime timestamp,
	String path,
	List<ErrorDetail> errors
) {
	public static ErrorResponse of(String message, String path, List<ErrorDetail> errors) {
		return new ErrorResponse(message, LocalDateTime.now(), path, errors);
	}

	public static ErrorResponse of(String message, String path, ErrorDetail... errors) {
		return new ErrorResponse(message, LocalDateTime.now(), path, List.of(errors));
	}
}
