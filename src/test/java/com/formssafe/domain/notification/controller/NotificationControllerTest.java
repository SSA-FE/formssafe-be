package com.formssafe.domain.notification.controller;

import static com.formssafe.util.Fixture.createNotification;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@AutoConfigureMockMvc
class NotificationControllerTest {
    private final MockMvc mockMvc;
    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;

    private User testUser1;
    private User testUser2;

    @Autowired
    public NotificationControllerTest(MockMvc mockMvc,
                                      NotificationRepository notificationRepository,
                                      UserRepository userRepository) {
        this.mockMvc = mockMvc;
        this.notificationRepository = notificationRepository;
        this.userRepository = userRepository;
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
            Notification n2 = createNotification(testUser1, "내용2", NotificationType.FINISH_FORM, false);
            Notification unReadN1 = createNotification(testUser2, "내용3", NotificationType.FINISH_FORM, true);
            Notification n3 = createNotification(testUser1, "내용4", NotificationType.FINISH_FORM, false);
            Notification unReadN2 = createNotification(testUser1, "내용5", NotificationType.FINISH_FORM, true);
            Notification n4 = createNotification(testUser2, "내용6", NotificationType.FINISH_FORM, false);
            notificationRepository.saveAll(List.of(n1, n2, unReadN1, n3, unReadN2, n4));

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
}