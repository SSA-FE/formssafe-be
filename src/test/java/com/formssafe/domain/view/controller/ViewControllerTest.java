package com.formssafe.domain.view.controller;

import static com.formssafe.util.Fixture.createForm;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import com.formssafe.config.ControllerTestConfig;
import com.formssafe.domain.form.entity.Form;
import com.formssafe.domain.user.entity.User;
import com.formssafe.util.EntityManagerUtil;
import com.formssafe.util.security.WithMockSessionAuthentication;
import jakarta.persistence.EntityManager;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SuppressWarnings("NonAsciiCharacters")
@DisplayName("[설문 탐색 컨트롤러 테스트]")
class ViewControllerTest extends ControllerTestConfig {
    private final MockMvc mockMvc;
    private final EntityManager em;

    private User testUser;

    @Autowired
    public ViewControllerTest(MockMvc mockMvc,
                              EntityManager em) {
        this.mockMvc = mockMvc;
        this.em = em;
    }

    @BeforeEach
    void setUp() {
        testUser = em.find(User.class, 1L);
    }

    @Nested
    class 설문_상세_조회 {

        @Test
        @WithMockSessionAuthentication
        void 설문을_상세_조회한다() throws Exception {
            // given
            Form form = createForm(testUser, "제목1", "설명1");
            em.persist(form);
            EntityManagerUtil.flushAndClear(em);

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

        @Test
        @WithMockSessionAuthentication
        void 존재하지_않는_설문_조회_시_예외가_발생한다() throws Exception {
            // given
            Form form = createForm(testUser, "제목1", "설명1");
            em.persist(form);
            EntityManagerUtil.flushAndClear(em);

            RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/v1/view/forms/{id}", form.getId() + 1)
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .characterEncoding(StandardCharsets.UTF_8.displayName());
            //when then
            mockMvc.perform(requestBuilder)
                    .andDo(print())
                    .andExpect(MockMvcResultMatchers.status().isBadRequest())
                    .andReturn()
                    .getResponse();
        }
    }
}