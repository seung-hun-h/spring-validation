package seunghun.springvalidation.handle;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import seunghun.springvalidation.handle.dto.ContentDto;
import seunghun.springvalidation.handle.dto.GroupDto;
import seunghun.springvalidation.handle.dto.UserDto;

@RestController
@RequestMapping("/handle/api/test")
public class ValidationTestController {
	@RequestMapping("/hello")
	public String hello() {
		return "Hello World";
	}

	@RequestMapping("/single-param")
	public String singleParam(
		@RequestParam @Min(10) int age
	) {
		return "age is " + age;
	}

	@RequestMapping("/multiple-param")
	public String multipleParam(
		@RequestParam @Positive @Min(1) Integer page,
		@RequestParam @Min(1) @Max(100) int size,
		@RequestParam @NotBlank String keyword
	) {
		return "page: " + page + ", size: " + size + ", keyword: " + keyword;
	}

	@PostMapping("/request-body")
	public String requestBody(@RequestBody @Valid UserDto user) {
		return "user: " + user.name();
	}

	@PostMapping("/nested")
	public String nested(@RequestBody @Valid ContentDto content) {
		return "content: " + content.content() + ", user: " + content.user().name();
	}

	@PostMapping("/nested-list")
	public String nestedList(@RequestBody @Valid GroupDto group) {
		return "group name: " + group.name() + ", users: " + group.users();
	}


	@PostMapping("/users-list")
	public String usersList(@RequestBody @Valid List<UserDto> users) {
		return "users count: " + users.size();
	}
}
