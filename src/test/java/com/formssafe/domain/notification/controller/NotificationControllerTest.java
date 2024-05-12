package com.formssafe.domain.notification.controller;

import static com.formssafe.util.Fixture.createNotification;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.formssafe.config.ControllerTestConfig;
import com.formssafe.domain.notification.dto.NotificationResponse.NotificationListResponseDto;
import com.formssafe.domain.notification.entity.Notification;
import com.formssafe.domain.notification.entity.NotificationType;
import com.formssafe.domain.user.entity.User;
import com.formssafe.global.error.ErrorCode;
import com.formssafe.util.EntityManagerUtil;
import com.formssafe.util.security.WithMockSessionAuthentication;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SuppressWarnings("NonAsciiCharacters")
@DisplayName("[알림 컨트롤러 테스트]")
class NotificationControllerTest extends ControllerTestConfig {
    private final MockMvc mockMvc;
    private final EntityManager em;
    private final ObjectMapper mapper;

    private User testUser1;
    private User testUser2;

    @Autowired
    public NotificationControllerTest(MockMvc mockMvc,
                                      EntityManager em,
                                      ObjectMapper mapper) {
        this.mockMvc = mockMvc;
        this.em = em;
        this.mapper = mapper;
    }

    @BeforeEach
    void setUp() {
        testUser1 = em.find(User.class, 1L);
        testUser2 = em.find(User.class, 2L);
    }

    @Nested
    class 읽지_않은_알림_개수_조회 {

        @Test
        @WithMockSessionAuthentication
        void 사용자는_읽지_않은_알림의_개수를_조회할_수_있다() throws Exception {
            //given
            Notification n1 = createNotification(testUser1, "내용1", NotificationType.FINISH_FORM, false);
            Notification n2 = createNotification(testUser1, "내용2", NotificationType.GET_REWARD, false);
            Notification readN1 = createNotification(testUser2, "내용3", NotificationType.RESPOND_FORM, true);
            Notification n3 = createNotification(testUser1, "내용4", NotificationType.SUBSCRIBED_FORM, false);
            Notification readN2 = createNotification(testUser1, "내용5", NotificationType.FINISH_FORM, true);
            Notification n4 = createNotification(testUser2, "내용6", NotificationType.FINISH_FORM, false);
            EntityManagerUtil.persist(em, n1, n2, readN1, n3, readN2, n4);
            EntityManagerUtil.flushAndClear(em);

            RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/v1/notifications/unread/count")
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .characterEncoding(StandardCharsets.UTF_8.displayName());
            //when then
            mockMvc.perform(requestBuilder)
                    .andDo(print())
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(jsonPath("$.count")
                            .value(3))
                    .andReturn()
                    .getResponse();
        }
    }

    @Nested
    class 읽지_않은_알림_목록_조회 {

        @Test
        @WithMockSessionAuthentication
        void 사용자는_읽지_않은_최근_10개_알림_목록을_조회할_수_있다() throws Exception {
            //given
            Notification n1 = createNotification(testUser1, "내용1", NotificationType.FINISH_FORM, false);
            Notification n2 = createNotification(testUser1, "내용2", NotificationType.GET_REWARD, false);
            Notification readN1 = createNotification(testUser2, "내용3", NotificationType.RESPOND_FORM, true);
            Notification n3 = createNotification(testUser1, "내용4", NotificationType.SUBSCRIBED_FORM, false);
            Notification readN2 = createNotification(testUser1, "내용5", NotificationType.FINISH_FORM, true);
            Notification n4 = createNotification(testUser2, "내용6", NotificationType.FINISH_FORM, false);
            Notification readN3 = createNotification(testUser1, "내용7", NotificationType.FINISH_FORM, true);
            Notification n5 = createNotification(testUser1, "내용8", NotificationType.FINISH_FORM, false);
            Notification n6 = createNotification(testUser1, "내용9", NotificationType.FINISH_FORM, false);
            Notification n7 = createNotification(testUser1, "내용10", NotificationType.FINISH_FORM, false);
            Notification n8 = createNotification(testUser1, "내용11", NotificationType.FINISH_FORM, false);
            EntityManagerUtil.persist(em, n1, n2, readN1, n3, readN2, n4, readN3, n5, n6, n7, n8);
            EntityManagerUtil.flushAndClear(em);

            RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/v1/notifications/unread")
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .characterEncoding(StandardCharsets.UTF_8.displayName());
            //when then
            MockHttpServletResponse response = mockMvc.perform(requestBuilder)
                    .andDo(print())
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andReturn()
                    .getResponse();

            String content = new String(response.getContentAsByteArray(), StandardCharsets.UTF_8);
            NotificationListResponseDto notificationResponses = mapper.readValue(content, new TypeReference<>() {
            });
            assertThat(notificationResponses.notifications()).hasSize(7);
            assertThat(notificationResponses.notifications()).extracting("content")
                    .containsExactly("내용11", "내용10", "내용9", "내용8", "내용4", "내용2", "내용1");
        }
    }

    @Nested
    class 알림_목록_조회 {

        @Test
        @WithMockSessionAuthentication
        void 사용자는_최근_10개_알림_목록을_조회할_수_있다() throws Exception {
            //given
            Notification last = null;
            for (int i = 0; i < 30; i++) {
                Notification notification = createNotification(testUser1, "내용" + i, NotificationType.FINISH_FORM,
                        false);
                em.persist(notification);
                last = notification;
            }
            EntityManagerUtil.flushAndClear(em);

            RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/v1/notifications")
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .characterEncoding(StandardCharsets.UTF_8.displayName())
                    .queryParam("top", String.valueOf(last.getId() - 10));
            //when then
            MockHttpServletResponse response = mockMvc.perform(requestBuilder)
                    .andDo(print())
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andReturn()
                    .getResponse();

            String content = new String(response.getContentAsByteArray(), StandardCharsets.UTF_8);
            NotificationListResponseDto notificationResponses = mapper.readValue(content, new TypeReference<>() {
            });

            assertThat(notificationResponses.notifications()).hasSize(10);
            assertThat(notificationResponses.notifications()).extracting("id")
                    .containsExactly(last.getId() - 11, last.getId() - 12, last.getId() - 13,
                            last.getId() - 14, last.getId() - 15, last.getId() - 16,
                            last.getId() - 17, last.getId() - 18, last.getId() - 19, last.getId() - 20);
        }

        @Test
        @WithMockSessionAuthentication
        void 검색_파라미터가_없다면_사용자의_최근_10개_알림_목록을_조회한다() throws Exception {
            //given
            Notification last = null;
            for (int i = 0; i < 10; i++) {
                Notification notification = createNotification(testUser1, "내용" + i, NotificationType.FINISH_FORM,
                        false);
                em.persist(notification);
                last = notification;
            }
            EntityManagerUtil.flushAndClear(em);

            RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/v1/notifications")
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .characterEncoding(StandardCharsets.UTF_8.displayName());
            //when then
            MockHttpServletResponse response = mockMvc.perform(requestBuilder)
                    .andDo(print())
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andReturn()
                    .getResponse();

            String content = new String(response.getContentAsByteArray(), StandardCharsets.UTF_8);
            NotificationListResponseDto notificationResponses = mapper.readValue(content, new TypeReference<>() {
            });
            assertThat(notificationResponses.notifications()).hasSize(10);
            assertThat(notificationResponses.notifications()).extracting("id")
                    .containsExactly(last.getId(), last.getId() - 1, last.getId() - 2,
                            last.getId() - 3, last.getId() - 4, last.getId() - 5,
                            last.getId() - 6, last.getId() - 7, last.getId() - 8, last.getId() - 9);
        }
    }

    @Nested
    class 알림_읽기 {

        @Test
        @WithMockSessionAuthentication
        void 사용자는_알림을_읽을_수_있다() throws Exception {
            //given
            Notification notification = createNotification(testUser1, "알림1", NotificationType.FINISH_FORM,
                    false);
            em.persist(notification);
            EntityManagerUtil.flushAndClear(em);

            RequestBuilder requestBuilder = MockMvcRequestBuilders.patch("/v1/notifications/{id}/read",
                            notification.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .characterEncoding(StandardCharsets.UTF_8.displayName());
            //when then
            mockMvc.perform(requestBuilder)
                    .andDo(print())
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andReturn();

            Notification result = em.find(Notification.class, notification.getId());
            assertThat(result.isRead()).isTrue();
        }

        @Test
        @WithMockSessionAuthentication(id = 2L)
        void 사용자가_다른_사용자의_알림을_읽으려_하면_예외가_발생한다() throws Exception {
            //given
            Notification notification = createNotification(testUser1, "알림1", NotificationType.FINISH_FORM,
                    false);
            em.persist(notification);
            EntityManagerUtil.flushAndClear(em);

            RequestBuilder requestBuilder = MockMvcRequestBuilders.patch("/v1/notifications/{id}/read",
                            notification.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .characterEncoding(StandardCharsets.UTF_8.displayName());
            //when then
            mockMvc.perform(requestBuilder)
                    .andDo(print())
                    .andExpect(MockMvcResultMatchers.status().isForbidden())
                    .andExpect(jsonPath("$.code")
                            .value(ErrorCode.INVALID_RECEIVER.getCode()))
                    .andReturn();
        }
    }

    @Nested
    class 모든_알림_읽기 {

        @Test
        @WithMockSessionAuthentication
        void 사용자는_모든_알림을_읽을_수_있다() throws Exception {
            //given
            for (int i = 0; i < 5; i++) {
                Notification notification = createNotification(testUser1, "내용" + i, NotificationType.FINISH_FORM,
                        false);
                em.persist(notification);
            }
            for (int i = 0; i < 5; i++) {
                Notification notification = createNotification(testUser2, "내용" + i, NotificationType.FINISH_FORM,
                        false);
                em.persist(notification);
            }
            EntityManagerUtil.flushAndClear(em);

            RequestBuilder requestBuilder = MockMvcRequestBuilders.patch("/v1/notifications/read")
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .characterEncoding(StandardCharsets.UTF_8.displayName());
            //when then
            mockMvc.perform(requestBuilder)
                    .andDo(print())
                    .andExpect(MockMvcResultMatchers.status().isOk());

            TypedQuery<Notification> query = em.createQuery("select n from Notification n where n.receiver = :user",
                    Notification.class);
            query.setParameter("user", testUser1);
            query.getResultList().forEach(n -> assertThat(n.isRead()).isTrue());
        }
    }
}