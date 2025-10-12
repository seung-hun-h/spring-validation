package seunghun.springvalidation.valid;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import seunghun.springvalidation.validation.valid.ValidController;

/**
 * PathVariable 모델 바인딩 동작 확인 테스트
 */
@WebMvcTest(ValidController.class)
class PathVariableModelTest {
	@Autowired
	MockMvc mockMvc;

	@Test
	void pathVariableModelBindingWorks() throws Exception {
		// UserDto의 필드명(name, age)과 path variable명이 일치하면 자동 바인딩됨
		mockMvc.perform(get("/valid/model/path-variable/{name}/{age}", "seunghun", "25"))
			.andExpect(status().isOk())
			.andExpect(content().string("Hello seunghun, your age is 25"));
	}

	@Test
	void pathVariableModelBindingWithInvalidAge() throws Exception {
		// @Valid가 있으면 UserDto 내부 검증 규칙(@Min(10))이 적용됨
		mockMvc.perform(get("/valid/model/path-variable/{name}/{age}", "seunghun", "5"))
			.andExpect(status().isBadRequest())
			.andExpect(content().string(org.hamcrest.Matchers.containsString("MethodArgumentNotValidException")));
	}
}