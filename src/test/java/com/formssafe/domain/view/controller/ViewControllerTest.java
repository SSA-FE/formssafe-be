package com.formssafe.domain.view.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import com.formssafe.domain.form.entity.Form;
import com.formssafe.domain.form.repository.FormRepository;
import com.formssafe.domain.user.entity.User;
import com.formssafe.domain.user.repository.UserRepository;
import com.formssafe.util.Fixture;
import com.formssafe.util.security.WithMockSessionAuthentication;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@AutoConfigureMockMvc
class ViewControllerTest {
    private final MockMvc mockMvc;
    private final FormRepository formRepository;
    private final UserRepository userRepository;

    private User testUser;

    @Autowired
    public ViewControllerTest(MockMvc mockMvc, FormRepository formRepository, UserRepository userRepository) {
        this.mockMvc = mockMvc;
        this.formRepository = formRepository;
        this.userRepository = userRepository;
    }

    @BeforeEach
    void setUp() {
        testUser = userRepository.findById(1L).orElseThrow(IllegalStateException::new);
    }

    @Nested
    @DisplayName("[설문 상세 조회]")
    class getForm {

        @Test
        @WithMockSessionAuthentication
        @DisplayName("설문을 상세 조회한다")
        void success() throws Exception {
            // given
            Form form = formRepository.save(Fixture.createForm(testUser, "제목1", "설명1"));

            RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/v1/view/forms/{id}", form.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .characterEncoding(StandardCharsets.UTF_8.displayName());
            //when then
            mockMvc.perform(requestBuilder)
                    .andDo(print())
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(jsonPath("$.id")
                            .value(form.getId()))
                    .andExpect(jsonPath("$.title")
                            .value("제목1"))
                    .andExpect(jsonPath("$.description")
                            .value("설명1"))
                    .andExpect(jsonPath("$.recipients").doesNotExist())
                    .andReturn()
                    .getResponse();
        }
    }
}