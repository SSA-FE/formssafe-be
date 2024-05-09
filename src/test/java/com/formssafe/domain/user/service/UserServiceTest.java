package com.formssafe.domain.user.service;

import com.formssafe.config.IntegrationTestConfig;
import com.formssafe.domain.form.service.FormService;
import com.formssafe.domain.oauth.OauthServerType;
import com.formssafe.domain.oauth.client.OauthMemberClientComposite;
import com.formssafe.domain.user.dto.UserRequest.JoinDto;
import com.formssafe.domain.user.dto.UserRequest.LoginUserDto;
import com.formssafe.domain.user.dto.UserRequest.NicknameUpdateDto;
import com.formssafe.domain.user.dto.UserResponse.UserProfileDto;
import com.formssafe.domain.user.entity.User;
import com.formssafe.global.error.ErrorCode;
import com.formssafe.util.EntityManagerUtil;
import jakarta.persistence.EntityManager;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import static com.formssafe.util.Fixture.*;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@DisplayName("[사용자 회원가입/사용자 인증/프로필 조회/닉네임 변경/탈퇴]")
class UserServiceTest extends IntegrationTestConfig {
    private final UserService userService;
    private final EntityManager em;

    @MockBean
    private OauthMemberClientComposite oauthMemberClientComposite;

    @MockBean
    private FormService formService;

    @Autowired
    public UserServiceTest(final UserService userService, final EntityManager em) {
        this.userService = userService;
        this.em = em;
    }

    @Nested
    class 사용자_회원가입 {
        @Test
        void 사이트에_처음_방문한_사용자는_입력한_닉네임으로_회원가입_할_수_있다() {
            //given
            User testUser = createNotActiveUser("testUser", "testUser@example.com");
            em.persist(testUser);
            EntityManagerUtil.flushAndClearContext(em);

            LoginUserDto loginUserDto = new LoginUserDto(testUser.getId());
            JoinDto joinDto = new JoinDto("joinTestUser");

            //when
            userService.join(joinDto, loginUserDto);
            User user = em.find(User.class, testUser.getId());

            //then
            SoftAssertions.assertSoftly(softAssertions -> {
                softAssertions.assertThat(user).isNotNull();
                softAssertions.assertThat(user.isActive()).isTrue();
            });
        }

        @Test
        void 사이트에_처음_방문하지_않은_사용자는_회원가입_시_예외가_발생한다() {
            //given
            User testUser = createUser("testUser", "userTest@example.com");
            em.persist(testUser);
            EntityManagerUtil.flushAndClearContext(em);

            LoginUserDto loginUserDto = new LoginUserDto(testUser.getId());
            JoinDto joinDto = new JoinDto("joinTestUser");

            //when then
            assertThatThrownBy(() -> userService.join(joinDto, loginUserDto))
                    .extracting("errorCode")
                    .isEqualTo(ErrorCode.USER_ALREADY_JOIN);
        }

        @Test
        void 사용자가_입력한_닉네임이_유효하지_않다면_예외가_발생한다() {
            //given
            User testUser = createNotActiveUser("testUser", "userTest@example.com");
            em.persist(testUser);
            EntityManagerUtil.flushAndClearContext(em);

            LoginUserDto loginUserDto = new LoginUserDto(testUser.getId());
            JoinDto joinDto = new JoinDto("testtesttesttesttestt");

            //when then
            assertThatThrownBy(() -> userService.join(joinDto, loginUserDto))
                    .extracting("errorCode")
                    .isEqualTo(ErrorCode.INVALID_USER_NICKNAME);
        }

        @Test
        void 사용자가_입력한_닉네임과_중복된_닉네임이_존재한다면_예외가_발생한다() {
            //given
            User testUser = createNotActiveUser("testUser", "userTest@example.com");
            em.persist(testUser);
            EntityManagerUtil.flushAndClearContext(em);

            LoginUserDto loginUserDto = new LoginUserDto(testUser.getId());
            JoinDto joinDto = new JoinDto("test");

            //when then
            assertThatThrownBy(() -> userService.join(joinDto, loginUserDto))
                    .extracting("errorCode")
                    .isEqualTo(ErrorCode.USER_NICKNAME_DUPLICATE);
        }
    }

    @Nested
    class 사용자_인증 {
        @Test
        void 등록된_사용자는_자신의_정보를_인증할_수_있다() {
            //given
            User testUser = createUser("getTest", "userTest@example.com");
            em.persist(testUser);
            EntityManagerUtil.flushAndClearContext(em);

            LoginUserDto loginUserDto = new LoginUserDto(testUser.getId());

            //when
            User user = userService.getUserById(loginUserDto.id());

            //then
            SoftAssertions.assertSoftly(softAssertions -> {
                softAssertions.assertThat(user).isNotNull();
                softAssertions.assertThat(user.getId()).isEqualTo(testUser.getId());
                softAssertions.assertThat(user.getEmail()).isEqualTo(testUser.getEmail());
                softAssertions.assertThat(user.getNickname()).isEqualTo(testUser.getNickname());
            });
        }

        @Test
        void 등록되지_않은_사용자는_자신의_정보_인증시_예외가_발생한다() {
            //given
            LoginUserDto loginUserDto = new LoginUserDto(3L);

            //when then
            assertThatThrownBy(() -> userService.getUserById(loginUserDto.id()))
                    .extracting("errorCode")
                    .isEqualTo(ErrorCode.USER_NOT_FOUND);
        }

        @Test
        void 탈퇴한_사용자는_자신의_정보_인증시_예외가_발생한다() {
            //given
            User testUser = createDeletedUser("deleteUser1", "delete-oauthId1", "delete_email1@email.com");
            em.persist(testUser);
            EntityManagerUtil.flushAndClearContext(em);

            //given
            LoginUserDto loginUserDto = new LoginUserDto(3L);

            //when then
            assertThatThrownBy(() -> userService.getUserById(loginUserDto.id()))
                    .extracting("errorCode")
                    .isEqualTo(ErrorCode.USER_NOT_FOUND);
        }
    }

    @Nested
    class 사용자_프로필_조회 {
        @Test
        void 등록된_사용자는_자신의_프로필을_조회할_수_있다() {
            //given
            User profileTestUser = createUser("profileTestUser", "profileTest@example.com");
            em.persist(profileTestUser);
            EntityManagerUtil.flushAndClearContext(em);

            LoginUserDto loginUserDto = new LoginUserDto(profileTestUser.getId());

            //when
            UserProfileDto assertUser = userService.getProfile(loginUserDto);

            //then
            SoftAssertions.assertSoftly(softAssertions -> {
                        softAssertions.assertThat(assertUser).isNotNull();
                        softAssertions.assertThat(assertUser.email()).isEqualTo("profileTest@example.com");
                        softAssertions.assertThat(assertUser.nickname()).isEqualTo("profileTestUser");
                        softAssertions.assertThat(assertUser.isActive()).isEqualTo(true);
                    }
            );
        }
    }

    @Nested
    class 사용자_닉네임_변경 {
        @Test
        void 등록된_사용자는_자신의_닉네임을_변경할_수_있다() {
            //given
            User testUser = createUser("testUser", "testUser@example.com");
            em.persist(testUser);
            EntityManagerUtil.flushAndClearContext(em);

            LoginUserDto loginUserDto = new LoginUserDto(testUser.getId());
            NicknameUpdateDto nicknameUpdateDto = new NicknameUpdateDto("changeNickname");

            //when
            userService.updateNickname(nicknameUpdateDto, loginUserDto);
            User user = em.find(User.class, testUser.getId());

            //then
            SoftAssertions.assertSoftly(softAssertions -> {
                softAssertions.assertThat(user).isNotNull();
                softAssertions.assertThat(user.getNickname()).isEqualTo("changeNickname");
            });
        }

        @Test
        void 사용자가_입력한_닉네임이_유효하지_않다면_예외가_발생한다() {
            //given
            User testUser = createUser("testUser", "testUser@example.com");
            em.persist(testUser);
            EntityManagerUtil.flushAndClearContext(em);

            LoginUserDto loginUserDto = new LoginUserDto(testUser.getId());
            NicknameUpdateDto nicknameUpdateDto = new NicknameUpdateDto("");

            //when then
            assertThatThrownBy(() -> userService.updateNickname(nicknameUpdateDto, loginUserDto))
                    .extracting("errorCode")
                    .isEqualTo(ErrorCode.INVALID_USER_NICKNAME);
        }

        @Test
        void 사용자가_입력한_닉네임과_중복된_닉네임이_존재한다면_예외가_발생한다() {
            //given
            User testUser = createUser("testUser", "testUser@example.com");
            em.persist(testUser);
            EntityManagerUtil.flushAndClearContext(em);

            LoginUserDto loginUserDto = new LoginUserDto(testUser.getId());
            NicknameUpdateDto nicknameUpdateDto = new NicknameUpdateDto("test");

            //when then
            assertThatThrownBy(() -> userService.updateNickname(nicknameUpdateDto, loginUserDto))
                    .extracting("errorCode")
                    .isEqualTo(ErrorCode.USER_NICKNAME_DUPLICATE);
        }
    }

    @Nested
    class 사용자_회원_탈퇴 {
        @Test
        void 등록된_사용자는_회원_탈퇴를_진행할_수_있다() {
            //given
            User testUser = createUser("testUser", "testUser@example.com");
            em.persist(testUser);
            EntityManagerUtil.flushAndClearContext(em);

            LoginUserDto loginUserDto = new LoginUserDto(testUser.getId());
            doNothing().when(oauthMemberClientComposite).deleteAccount(any(OauthServerType.class), anyString());
            doNothing().when(formService).deleteFormByUser(any(User.class));

            //when
            userService.deleteAccount(testUser.getId(), loginUserDto);
            User user = em.find(User.class, testUser.getId());

            //then
            SoftAssertions.assertSoftly(softAssertions -> {
                softAssertions.assertThat(user).isNotNull();
                softAssertions.assertThat(user.isDeleted()).isTrue();
            });
        }

        @Test
        void 입력받은_사용자_ID와_세션내_ID가_다를시_예외가_발생한다() {
            //given
            User testUser = createUser("testUser", "testUser@example.com");
            em.persist(testUser);
            EntityManagerUtil.flushAndClearContext(em);

            LoginUserDto loginUserDto = new LoginUserDto(testUser.getId() + 1);
            doNothing().when(oauthMemberClientComposite).deleteAccount(any(OauthServerType.class), anyString());
            doNothing().when(formService).deleteFormByUser(any(User.class));

            //when then
            assertThatThrownBy(() -> userService.deleteAccount(testUser.getId(), loginUserDto))
                    .extracting("errorCode")
                    .isEqualTo(ErrorCode.INVALID_USER);
        }
    }
}