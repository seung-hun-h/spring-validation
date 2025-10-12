package seunghun.springvalidation.handle;

public record ErrorDetail(
	String field,
	Object rejectedValue,
	String message
) {
}
