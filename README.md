# Spring Validation Examples

Spring Boot에서 파라미터 검증을 위한 다양한 애너테이션 비교 및 예외 처리 예제 프로젝트

## 📋 프로젝트 개요

이 프로젝트는 Spring Boot 3.5.6과 Jakarta Bean Validation을 활용하여 다음을 학습할 수 있습니다:

- `@Valid`, `@Validated`, Constraints만 사용한 검증의 차이점
- 각 방식에서 발생하는 예외 타입과 처리 방법
- Request Body, Request Param, Path Variable 검증 패턴
- Spring 예외 처리 메커니즘

## 🛠 기술 스택

- **Spring Boot**: 3.5.6
- **Java**: 21
- **Validation**: Jakarta Bean Validation (spring-boot-starter-validation)
- **Build Tool**: Gradle 8.14.3
- **Test Framework**: JUnit 5, Spring Boot Test

## 📁 프로젝트 구조

```
src/main/java/seunghun/springvalidation/
├── SpringValidationApplication.java          # 메인 애플리케이션
├── valid/                                     # @Valid 사용 예제
│   ├── ValidController.java                  # @Valid 적용 컨트롤러
│   ├── UserDto.java                          # 검증 규칙이 있는 DTO
│   └── ContentDto.java                       # 중첩 객체 검증 DTO
├── constraints/                               # 제약조건만 사용 예제
│   └── ConstraintsController.java            # @Valid 없이 제약조건만 사용
├── validated/                                 # @Validated 사용 예제
│   └── ValidatedController.java              # @Validated 적용 컨트롤러
└── other/                                     # 기타 예외 시나리오
    └── OtherController.java                  # HTTP 메시지 관련 예외

src/test/java/                                 # 모든 시나리오에 대한 테스트
```

## 🎯 검증 방식 비교

### 1. @Valid 사용 (`ValidController`)

```java
@RestController
@RequestMapping("/valid")
public class ValidController {

    // 개별 파라미터 검증
    @GetMapping("/request-param")
    public ResponseEntity<String> requestParam(
        @Valid @RequestParam @Min(10) int age,
        @Valid @RequestParam @NotBlank String name
    ) {
        return ResponseEntity.ok("Hello " + name + ", your age is " + age);
    }

    // 모델 객체 검증 (중첩 객체까지 검증됨)
    @GetMapping("/model/request-param")
    public ResponseEntity<String> requestParamModel(@Valid UserDto userDto) {
        return ResponseEntity.ok("Hello " + userDto.name() + ", your age is " + userDto.age());
    }

    // Request Body 검증
    @PostMapping("/request-body")
    public ResponseEntity<String> requestBodyContent(@RequestBody @Valid ContentDto contentDto) {
        return ResponseEntity.ok("Data received: " + contentDto.content());
    }
}
```

**발생 예외**:
- 개별 파라미터: `HandlerMethodValidationException`
- 모델 객체: `MethodArgumentNotValidException`

### 2. 제약조건만 사용 (`ConstraintsController`)

```java
@RestController
@RequestMapping("/constraints")
public class ConstraintsController {

    // @Valid 없이 제약조건만 사용
    @GetMapping("/request-param")
    public ResponseEntity<String> requestParam(
        @RequestParam @Min(10) int age,        // 검증됨
        @RequestParam @NotBlank String name    // 검증됨
    ) {
        return ResponseEntity.ok("Hello " + name + ", your age is " + age);
    }

    // 모델 객체는 검증되지 않음
    @GetMapping("/model/request-param")
    public ResponseEntity<String> requestParamModel(UserDto userDto) {  // 검증 안됨
        return ResponseEntity.ok("Hello " + userDto.name() + ", your age is " + userDto.age());
    }
}
```

**특징**: 개별 파라미터만 검증, 모델 객체는 검증되지 않음

### 3. @Validated 사용 (`ValidatedController`)

```java
@RestController
@RequestMapping("/validated")
@Validated  // 클래스 레벨에 적용
public class ValidatedController {

    @GetMapping("/request-param")
    public ResponseEntity<String> requestParam(
        @RequestParam @Min(10) int age,
        @RequestParam @NotBlank String name
    ) {
        return ResponseEntity.ok("Hello " + name + ", your age is " + age);
    }

    // 모델 객체는 검증되지 않음 (constraints와 동일)
    @GetMapping("/model/request-param")
    public ResponseEntity<String> requestParamModel(UserDto userDto) {
        return ResponseEntity.ok("Hello " + userDto.name() + ", your age is " + userDto.age());
    }
}
```

**발생 예외**: `ConstraintViolationException`

## 🚨 예외 타입 및 처리

### 주요 예외 타입

| 예외 타입 | 발생 상황 | 컨트롤러 |
|----------|----------|----------|
| `HandlerMethodValidationException` | @Valid 개별 파라미터 검증 실패 | Valid |
| `MethodArgumentNotValidException` | @Valid 모델 객체 검증 실패 | Valid |
| `ConstraintViolationException` | @Validated 검증 실패 | Validated |
| `MissingServletRequestParameterException` | 필수 파라미터 누락 | 모든 컨트롤러 |
| `MethodArgumentTypeMismatchException` | 타입 변환 실패 | 모든 컨트롤러 |
| `HttpMessageNotReadableException` | 잘못된 JSON, 빈 body | Other |
| `HttpMediaTypeNotSupportedException` | Content-Type 불일치 | Other |

### 예외 처리 예제

```java
@ExceptionHandler(MethodArgumentNotValidException.class)
public ResponseEntity<String> handleValidationException(MethodArgumentNotValidException ex) {
    return ResponseEntity.badRequest().body("Validation failed: " + ex.getMessage());
}

@ExceptionHandler(ConstraintViolationException.class)
public ResponseEntity<String> handleConstraintViolation(ConstraintViolationException ex) {
    return ResponseEntity.badRequest().body("Constraint violation: " + ex.getMessage());
}
```

## 🧪 테스트 실행

```bash
# 전체 테스트 실행
./gradlew test

# 특정 컨트롤러 테스트만 실행
./gradlew test --tests "*ValidControllerTest*"
./gradlew test --tests "*ConstraintsControllerTest*"
./gradlew test --tests "*ValidatedControllerTest*"
./gradlew test --tests "*OtherControllerTest*"
```

## 📖 DTO 구조

### UserDto
```java
public record UserDto(
    @NotBlank String name,
    @Min(value = 10, message = "The value must be greater than 10") int age
) {}
```

### ContentDto
```java
public record ContentDto(
    @Valid UserDto user,        // 중첩 객체 검증
    @NotBlank String content
) {}
```

## 🔍 Path Variable 모델 바인딩

Spring Framework 6.0+ (Spring Boot 3.0+)부터 지원되는 기능:

```java
@GetMapping("/model/path-variable/{name}/{age}")
public ResponseEntity<String> pathVariableModel(UserDto userDto) {
    // path variable 이름과 DTO 필드명이 일치하면 자동 바인딩
    return ResponseEntity.ok("Hello " + userDto.name() + ", your age is " + userDto.age());
}
```

**조건**: Path variable명 ≡ 객체 필드명이어야 자동 바인딩됨

## 🚀 실행 방법

1. **프로젝트 클론**
```bash
git clone <repository-url>
cd spring-validation
```

2. **애플리케이션 실행**
```bash
./gradlew bootRun
```

3. **API 테스트**
```bash
# @Valid 예제 - 성공
curl "http://localhost:8080/valid/request-param?age=25&name=John"

# @Valid 예제 - 검증 실패
curl "http://localhost:8080/valid/request-param?age=5&name=John"

# Constraints만 사용 - 모델 검증 안됨
curl "http://localhost:8080/constraints/model/request-param?age=-1&name=John"

# @Validated 예제
curl "http://localhost:8080/validated/request-param?age=25&name=John"
```

## 📚 학습 포인트

1. **@Valid vs @Validated**:
   - `@Valid`: JSR-303 표준, 모델 객체 검증 지원
   - `@Validated`: Spring 전용, 그룹 검증 지원하지만 모델 객체 검증 제한적

2. **예외 패턴**:
   - 각 검증 방식마다 다른 예외 타입 발생
   - 예외 처리 전략을 미리 수립해야 함

3. **모델 바인딩**:
   - `@Valid` 없이는 모델 객체 내부 검증 규칙이 적용되지 않음
   - Path Variable 모델 바인딩은 Spring 6.0+ 신기능

4. **실무 권장사항**:
   - Request Body: `@Valid` 사용
   - Request Param: 상황에 따라 선택
   - 일관된 예외 처리 전략 수립

---

**개발 환경**: Spring Boot 3.5.6, Java 21
**테스트 커버리지**: 모든 검증 시나리오 및 예외 상황 포함