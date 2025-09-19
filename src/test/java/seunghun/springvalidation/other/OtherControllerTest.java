package seunghun.springvalidation.other;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Spring 예외 발생 시나리오 테스트
 */
@WebMvcTest(OtherController.class)
class OtherControllerTest {
	@Autowired
	MockMvc mockMvc;

	// HttpMessageNotReadableException 테스트
	@Test
	void httpMessageNotReadableException_InvalidJson() throws Exception {
		mockMvc.perform(post("/other/json")
				.content("{ invalid json }")
				.contentType("application/json"))
			.andExpect(status().isBadRequest())
			.andExpect(content().string(containsString("HttpMessageNotReadableException")));
	}

	@Test
	void httpMediaTypeNotSupportedException_WrongContentType() throws Exception {
		mockMvc.perform(post("/other/json")
				.content(
					"{\"name\":\"test\"}")
				.contentType("text/plain"))  // JSON이지만 Content-Type이 다름
			.andExpect(status().isBadRequest())
			.andExpect(content().string(containsString("HttpMediaTypeNotSupportedException")));
	}

	@Test
	void httpMessageNotReadableException_EmptyBody() throws Exception {
		mockMvc.perform(post("/other/json")
				.content("")
				.contentType("application/json"))
			.andExpect(status().isBadRequest())
			.andExpect(content().string(containsString("HttpMessageNotReadableException")));
	}

	@Test
	void httpMessageNotReadableException_TypeMismatch() throws Exception {
		mockMvc.perform(post("/other/json")
				.content("{\"name\":\"test\",\"age\":\"not_number\"}")
				.contentType("application/json"))
			.andExpect(status().isBadRequest())
			.andExpect(content().string(containsString("HttpMessageNotReadableException")));
	}

}