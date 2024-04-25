package com.formssafe.domain.notification.controller;

import static com.formssafe.util.Fixture.createNotification;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.formssafe.domain.notification.dto.NotificationResponse.NotificationResponseDto;
import com.formssafe.domain.notification.entity.Notification;
import com.formssafe.domain.notification.entity.NotificationType;
import com.formssafe.domain.notification.repository.NotificationRepository;
import com.formssafe.domain.user.entity.User;
import com.formssafe.domain.user.repository.UserRepository;
import com.formssafe.util.security.WithMockSessionAuthentication;
import java.nio.charset.StandardCharsets;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class NotificationControllerTest {
    private final MockMvc mockMvc;
    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;
    private final ObjectMapper mapper;

    private User testUser1;
    private User testUser2;

    @Autowired
    public NotificationControllerTest(MockMvc mockMvc,
                                      NotificationRepository notificationRepository,
                                      UserRepository userRepository,
                                      ObjectMapper mapper) {
        this.mockMvc = mockMvc;
        this.notificationRepository = notificationRepository;
        this.userRepository = userRepository;
        this.mapper = mapper;
    }

    @BeforeEach
    void setUp() {
        testUser1 = userRepository.findById(1L).orElseThrow(IllegalStateException::new);
        testUser2 = userRepository.findById(2L).orElseThrow(IllegalStateException::new);
        testUser2 = userRepository.findById(2L).orElseThrow(IllegalStateException::new);
    }

    @Nested
    @DisplayName("[읽지 않은 알림 개수 조회]")
    class getUnreadNotificationCount {

        @DisplayName("사용자가 읽지 않은 알림의 개수를 조회한다.")
        @Test
        @WithMockSessionAuthentication
        void success() throws Exception {
            //given
            Notification n1 = createNotification(testUser1, "내용1", NotificationType.FINISH_FORM, false);
            Notification n2 = createNotification(testUser1, "내용2", NotificationType.GET_REWARD, false);
            Notification readN1 = createNotification(testUser2, "내용3", NotificationType.RESPOND_FORM, true);
            Notification n3 = createNotification(testUser1, "내용4", NotificationType.SUBSCRIBED_FORM, false);
            Notification readN2 = createNotification(testUser1, "내용5", NotificationType.FINISH_FORM, true);
            Notification n4 = createNotification(testUser2, "내용6", NotificationType.FINISH_FORM, false);
            notificationRepository.saveAll(List.of(n1, n2, readN1, n3, readN2, n4));

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
    @DisplayName("[읽지 않은 5개 알림 목록 조회]")
    class getUnreadNotifications {

        @DisplayName("사용자가 읽지 않은 최근 5개 알림을 목록 조회한다.")
        @Test
        @WithMockSessionAuthentication
        void success() throws Exception {
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
            notificationRepository.saveAll(List.of(n1, n2, readN1, n3, readN2, n4, readN3, n5, n6));

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
            List<NotificationResponseDto> notificationResponseDtos = mapper.readValue(content, new TypeReference<>() {
            });
            assertThat(notificationResponseDtos).hasSize(5);
            assertThat(notificationResponseDtos).extracting("content")
                    .containsExactly("내용9", "내용8", "내용4", "내용2", "내용1");
        }
    }
}