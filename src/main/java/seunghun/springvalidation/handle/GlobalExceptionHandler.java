package seunghun.springvalidation.handle;

import java.util.List;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.method.ParameterErrors;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;

@RestControllerAdvice("seunghun.springvalidation.handle")
public class GlobalExceptionHandler {
	@ExceptionHandler(HandlerMethodValidationException.class)
	public ResponseEntity<ErrorResponse> handleHandlerMethodValidationException(
		HandlerMethodValidationException exception,
		HttpServletRequest request
	) {
		List<ErrorDetail> errorDetails = exception.getParameterValidationResults()
			.stream()
			.flatMap(result -> {
				if (result instanceof ParameterErrors errors) {
					return errors.getFieldErrors()
						.stream()
						.map(fieldError -> new ErrorDetail(getFieldName(result.getContainerIndex(), fieldError.getField()), fieldError.getRejectedValue(), fieldError.getDefaultMessage()));
				}

				return result.getResolvableErrors()
				.stream()
				.map(resolvable -> new ErrorDetail(result.getMethodParameter().getParameterName(), result.getArgument(), resolvable.getDefaultMessage()));
			})
			.toList();

		return ResponseEntity.badRequest().body(ErrorResponse.of("입력값 검증에 실패했습니다", request.getRequestURI(), errorDetails));
	}

	private String getFieldName(Integer containerIndex, String fieldName) {
		if (containerIndex != null) {
			return "[" + containerIndex + "]." + fieldName;
		}

		return fieldName;
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(
		MethodArgumentNotValidException exception,
		HttpServletRequest request
	) {
		List<ErrorDetail> errorDetails = exception.getBindingResult()
			.getFieldErrors()
			.stream()
			.map(fieldError -> new ErrorDetail(fieldError.getField(), fieldError.getRejectedValue(), fieldError.getDefaultMessage()))
			.toList();

		return ResponseEntity.badRequest().body(ErrorResponse.of("입력값 검증에 실패했습니다", request.getRequestURI(), errorDetails));
	}

	// @ExceptionHandler(HandlerMethodValidationException.class)
	// public ResponseEntity<ErrorResponse> handleHandlerMethodValidationException(
	// 	HandlerMethodValidationException exception,
	// 	HttpServletRequest request,
	// ) {
	// 	List<ErrorDetail> errorDetails = new ArrayList<>();
	//
	// 	exception.visitResults(new HandlerMethodValidationException.Visitor() {
	//
	// 		@Override
	// 		public void cookieValue(CookieValue cookieValue, ParameterValidationResult result) {
	//
	// 		}
	//
	// 		@Override
	// 		public void matrixVariable(MatrixVariable matrixVariable, ParameterValidationResult result) {
	//
	// 		}
	//
	// 		@Override
	// 		public void modelAttribute(ModelAttribute modelAttribute, ParameterErrors errors) {
	//
	// 		}
	//
	// 		@Override
	// 		public void pathVariable(PathVariable pathVariable, ParameterValidationResult result) {
	//
	// 		}
	//
	// 		@Override
	// 		public void requestBody(RequestBody requestBody, ParameterErrors errors) {
	//
	// 		}
	//
	// 		@Override
	// 		public void requestHeader(RequestHeader requestHeader, ParameterValidationResult result) {
	//
	// 		}
	//
	// 		@Override
	// 		public void requestParam(RequestParam requestParam, ParameterValidationResult result) {
	// 			List<ErrorDetail> errorDetailList = result.getResolvableErrors()
	// 				.stream()
	// 				.map(resolvable -> new ErrorDetail(result.getMethodParameter().getParameterName(), result.getArgument(), resolvable.getDefaultMessage()))
	// 				.toList();
	//
	// 			errorDetails.addAll(errorDetailList);
	// 		}
	//
	// 		@Override
	// 		public void requestPart(RequestPart requestPart, ParameterErrors errors) {
	//
	// 		}
	//
	// 		@Override
	// 		public void other(ParameterValidationResult result) {
	//
	// 		}
	// 	});
	//
	// 	return ResponseEntity.badRequest().body(ErrorResponse.of("입력값 검증에 실패했습니다", request.getRequestURI(), errorDetails));
	// }
}
