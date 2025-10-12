package seunghun.springvalidation.valid;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import seunghun.springvalidation.validation.valid.ContentDto;
import seunghun.springvalidation.validation.valid.UserDto;
import seunghun.springvalidation.validation.valid.ValidController;

@WebMvcTest(ValidController.class)
class ValidControllerTest {
	@Autowired
	MockMvc mockMvc;

	@Autowired
	ObjectMapper objectMapper;

	@Test
	void requestParam() throws Exception {
		mockMvc.perform(get("/valid/request-param")
				.queryParam("age", "10")
				.queryParam("name", "seunghun"))
			.andExpect(status().isOk())
			.andExpect(content().string("Hello seunghun, your age is 10"));
	}

	@Test
	void requestParamWithoutAge() throws Exception {
		mockMvc.perform(get("/valid/request-param")
				.queryParam("name", "seunghun"))
			.andExpect(status().isBadRequest())
			.andExpect(content().string(containsString("MissingServletRequestParameterException")));
	}

	@Test
	void requestParamWrongType() throws Exception {
		mockMvc.perform(get("/valid/request-param")
				.queryParam("age", "abc")
				.queryParam("name", "seunghun"))
			.andExpect(status().isBadRequest())
			.andExpect(content().string(containsString("MethodArgumentTypeMismatchException")));
	}

	@Test
	void requestParamInvalid() throws Exception {
		mockMvc.perform(get("/valid/request-param")
				.queryParam("age", "-1")
				.queryParam("name", "seunghun"))
			.andExpect(status().isBadRequest())
			.andExpect(content().string(containsString("HandlerMethodValidationException")));
	}


	@Test
	void requestParamModel() throws Exception {
		mockMvc.perform(get("/valid/model/request-param")
				.queryParam("age", "10")
				.queryParam("name", "seunghun"))
			.andExpect(status().isOk())
			.andExpect(content().string("Hello seunghun, your age is 10"));
	}

	@Test
	void requestParamModelWithoutAge() throws Exception {
		mockMvc.perform(get("/valid/model/request-param")
				.queryParam("name", "seunghun"))
			.andExpect(status().isBadRequest())
			.andExpect(content().string(containsString("MethodArgumentNotValidException")));
	}

	@Test
	void requestParamModelWrongType() throws Exception {
		mockMvc.perform(get("/valid/model/request-param")
				.queryParam("age", "abc")
				.queryParam("name", "seunghun"))
			.andExpect(status().isBadRequest())
			.andExpect(content().string(containsString("MethodArgumentNotValidException")));
	}

	@Test
	void requestParamModelInvalid() throws Exception {
		mockMvc.perform(get("/valid/model/request-param")
				.queryParam("age", "-1")
				.queryParam("name", "seunghun"))
			.andExpect(status().isBadRequest())
			.andExpect(content().string(containsString("MethodArgumentNotValidException")));
	}

	@Test
	void requestBodyContentWithoutAge() throws Exception {
		ContentDto contentDto = new ContentDto(
			new UserDto("", -1),
			"content"
		);

		mockMvc.perform(post("/valid/request-body")
				.content(objectMapper.writeValueAsString(contentDto))
				.contentType("application/json"))
			.andExpect(status().isBadRequest())
			.andExpect(content().string(containsString("MethodArgumentNotValidException")));
	}

	@Test
	void pathVariable() throws Exception {
		mockMvc.perform(get("/valid/path-variable/{name}/{age}", "seunghun", "10"))
			.andExpect(status().isOk())
			.andExpect(content().string("Hello seunghun, your age is 10"));
	}

	// 컨트롤러까지 도달하지 못함
	@Test
	void pathVariableWithoutAge() throws Exception {
		mockMvc.perform(get("/valid/path-variable/{name}", "seunghun"))
			.andExpect(status().isNotFound());
	}

	@Test
	void pathVariableWrongType() throws Exception {
		mockMvc.perform(get("/valid/path-variable/{name}/{age}", "seunghun", "abc"))
			.andExpect(status().isBadRequest())
			.andExpect(content().string(containsString("MethodArgumentTypeMismatchException")));
	}

	@Test
	void pathVariableInvalid() throws Exception {
		mockMvc.perform(get("/valid/path-variable/{name}/{age}", "seunghun", "-1"))
			.andExpect(status().isBadRequest())
			.andExpect(content().string(containsString("HandlerMethodValidationException")));
	}

	@Test
	void pathVariableModel() throws Exception {
		mockMvc.perform(get("/valid/model/path-variable/{name}/{age}", "seunghun", "10"))
			.andExpect(status().isOk())
			.andExpect(content().string("Hello seunghun, your age is 10"));
	}

	// 컨트롤러까지 도달하지 못함
	@Test
	void pathVariableModelWithoutAge() throws Exception {
		mockMvc.perform(get("/valid/model/path-variable/{name}", "seunghun"))
			.andExpect(status().isNotFound());
	}

	@Test
	void pathVariableModelWrongType() throws Exception {
		mockMvc.perform(get("/valid/model/path-variable/{name}/{age}", "seunghun", "abc"))
			.andExpect(status().isBadRequest())
			.andExpect(content().string(containsString("MethodArgumentNotValidException")));
	}

	@Test
	void pathVariableModelInvalid() throws Exception {
		mockMvc.perform(get("/valid/model/path-variable/{name}/{age}", "seunghun", "-1"))
			.andExpect(status().isBadRequest())
			.andExpect(content().string(containsString("MethodArgumentNotValidException")));
	}
}