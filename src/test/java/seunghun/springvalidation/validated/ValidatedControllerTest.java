package seunghun.springvalidation.validated;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import seunghun.springvalidation.valid.ContentDto;
import seunghun.springvalidation.valid.UserDto;

@WebMvcTest(ValidatedController.class)
class ValidatedControllerTest {
	@Autowired
	MockMvc mockMvc;

	@Autowired
	ObjectMapper objectMapper;

	@Test
	void requestParam() throws Exception {
		mockMvc.perform(get("/validated/request-param")
				.queryParam("age", "10")
				.queryParam("name", "seunghun"))
			.andExpect(status().isOk())
			.andExpect(content().string("Hello seunghun, your age is 10"));
	}

	@Test
	void requestParamWithoutAge() throws Exception {
		mockMvc.perform(get("/validated/request-param")
				.queryParam("name", "seunghun"))
			.andExpect(status().isBadRequest())
			.andExpect(content().string(containsString("MissingServletRequestParameterException")));
	}

	@Test
	void requestParamWrongType() throws Exception {
		mockMvc.perform(get("/validated/request-param")
				.queryParam("age", "abc")
				.queryParam("name", "seunghun"))
			.andExpect(status().isBadRequest())
			.andExpect(content().string(containsString("MethodArgumentTypeMismatchException")));
	}

	@Test
	void requestParamInvalid() throws Exception {
		mockMvc.perform(get("/validated/request-param")
				.queryParam("age", "-1")
				.queryParam("name", "seunghun"))
			.andExpect(status().isBadRequest())
			.andExpect(content().string(containsString("ConstraintViolationException")));
	}


	@Test
	void requestParamModel() throws Exception {
		mockMvc.perform(get("/validated/model/request-param")
				.queryParam("age", "10")
				.queryParam("name", "seunghun"))
			.andExpect(status().isOk())
			.andExpect(content().string("Hello seunghun, your age is 10"));
	}

	@Test
	void requestParamModelWithoutAge() throws Exception {
		mockMvc.perform(get("/validated/model/request-param")
				.queryParam("name", "seunghun"))
			.andExpect(status().isBadRequest())
			.andExpect(content().string(containsString("MethodArgumentNotValidException")));
	}

	@Test
	void requestParamModelWrongType() throws Exception {
		mockMvc.perform(get("/validated/model/request-param")
				.queryParam("age", "abc")
				.queryParam("name", "seunghun"))
			.andExpect(status().isBadRequest())
			.andExpect(content().string(containsString("MethodArgumentNotValidException")));
	}

	@Test
	void requestParamModelInvalid() throws Exception { // 유효성 검증이 안됨
		mockMvc.perform(get("/validated/model/request-param")
				.queryParam("age", "-1")
				.queryParam("name", "seunghun"))
			.andExpect(status().isOk())
			.andExpect(content().string("Hello seunghun, your age is -1"));
	}

	@Test
	void requestBodyContentWithoutAge() throws Exception { // 유효성 검증 안됨
		ContentDto contentDto = new ContentDto(
			new UserDto("", -1),
			"content"
		);

		mockMvc.perform(post("/validated/request-body")
				.content(objectMapper.writeValueAsString(contentDto))
				.contentType("application/json"))
			.andExpect(status().isOk());
	}

	@Test
	void pathVariable() throws Exception {
		mockMvc.perform(get("/validated/path-variable/{name}/{age}", "seunghun", "10"))
			.andExpect(status().isOk())
			.andExpect(content().string("Hello seunghun, your age is 10"));
	}

	// 컨트롤러까지 도달하지 못함
	@Test
	void pathVariableWithoutAge() throws Exception {
		mockMvc.perform(get("/validated/path-variable/{name}", "seunghun"))
			.andExpect(status().isNotFound());
	}

	@Test
	void pathVariableWrongType() throws Exception {
		mockMvc.perform(get("/validated/path-variable/{name}/{age}", "seunghun", "abc"))
			.andExpect(status().isBadRequest())
			.andExpect(content().string(containsString("MethodArgumentTypeMismatchException")));
	}

	@Test
	void pathVariableInvalid() throws Exception {
		mockMvc.perform(get("/validated/path-variable/{name}/{age}", "seunghun", "-1"))
			.andExpect(status().isBadRequest())
			.andExpect(content().string(containsString("ConstraintViolationException")));
	}

	@Test
	void pathVariableModel() throws Exception {
		mockMvc.perform(get("/validated/model/path-variable/{name}/{age}", "seunghun", "10"))
			.andExpect(status().isOk())
			.andExpect(content().string("Hello seunghun, your age is 10"));
	}

	// 컨트롤러까지 도달하지 못함
	@Test
	void pathVariableModelWithoutAge() throws Exception {
		mockMvc.perform(get("/validated/model/path-variable/{name}", "seunghun"))
			.andExpect(status().isNotFound());
	}

	@Test
	void pathVariableModelWrongType() throws Exception {
		mockMvc.perform(get("/validated/model/path-variable/{name}/{age}", "seunghun", "abc"))
			.andExpect(status().isBadRequest())
			.andExpect(content().string(containsString("MethodArgumentNotValidException")));
	}

	@Test
	void pathVariableModelInvalid() throws Exception { // 유효성 검증 안됨
		mockMvc.perform(get("/validated/model/path-variable/{name}/{age}", "seunghun", "-1"))
			.andExpect(status().isOk())
			.andExpect(content().string("Hello seunghun, your age is -1"));
	}
}