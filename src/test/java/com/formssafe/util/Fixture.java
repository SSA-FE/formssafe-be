package com.formssafe.util;

import com.formssafe.domain.content.dto.ContentRequest.ContentCreateDto;
import com.formssafe.domain.content.question.entity.DescriptiveQuestion;
import com.formssafe.domain.content.question.entity.DescriptiveQuestionType;
import com.formssafe.domain.content.question.entity.ObjectiveQuestion;
import com.formssafe.domain.content.question.entity.ObjectiveQuestionOption;
import com.formssafe.domain.content.question.entity.ObjectiveQuestionType;
import com.formssafe.domain.form.entity.Form;
import com.formssafe.domain.form.entity.FormStatus;
import com.formssafe.domain.oauth.OauthServerType;
import com.formssafe.domain.reward.entity.Reward;
import com.formssafe.domain.reward.entity.RewardCategory;
import com.formssafe.domain.submission.entity.Submission;
import com.formssafe.domain.tag.entity.FormTag;
import com.formssafe.domain.tag.entity.Tag;
import com.formssafe.domain.user.entity.Authority;
import com.formssafe.domain.user.entity.OauthId;
import com.formssafe.domain.user.entity.User;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public final class Fixture {

    private Fixture() {
    }

    public static User createUser(String nickname) {
        return User.builder()
                .oauthId(new OauthId("123", OauthServerType.GOOGLE))
                .nickname(nickname)
                .email("test@example.com")
                .imageUrl(
                        "https://www.wfla.com/wp-content/uploads/sites/71/2023/05/GettyImages-1389862392.jpg?w=1280&h=720&crop=1")
                .authority(Authority.ROLE_USER)
                .refreshToken("refreshToken1")
                .isActive(true)
                .isDeleted(false)
                .build();
    }

    public static User createUser(String nickname, String oauthId, String email) {
        return User.builder()
                .oauthId(new OauthId(oauthId, OauthServerType.GOOGLE))
                .nickname(nickname)
                .email(email)
                .imageUrl(
                        "https://www.wfla.com/wp-content/uploads/sites/71/2023/05/GettyImages-1389862392.jpg?w=1280&h=720&crop=1")
                .authority(Authority.ROLE_USER)
                .refreshToken("refreshToken1")
                .isActive(true)
                .isDeleted(false)
                .build();
    }

    public static User createDeletedUser(String nickname, String oauthId, String email) {
        return User.builder()
                .oauthId(new OauthId(oauthId, OauthServerType.GOOGLE))
                .nickname(nickname)
                .email(email)
                .imageUrl(
                        "https://www.wfla.com/wp-content/uploads/sites/71/2023/05/GettyImages-1389862392.jpg?w=1280&h=720&crop=1")
                .authority(Authority.ROLE_USER)
                .refreshToken("refreshToken1")
                .isActive(true)
                .isDeleted(true)
                .build();
    }

    public static List<User> createUsers(int size) {
        List<User> users = new ArrayList<>();

        for (int i = 0; i < size; i++) {
            users.add(User.builder()
                    .oauthId(new OauthId("oauthId" + i, OauthServerType.GOOGLE))
                    .nickname("name" + i)
                    .email("email" + i + "@email.com")
                    .imageUrl(
                            "https://www.wfla.com/wp-content/uploads/sites/71/2023/05/GettyImages-1389862392.jpg?w=1280&h=720&crop=1")
                    .authority(Authority.ROLE_USER)
                    .refreshToken("refreshToken" + i)
                    .isActive(true)
                    .isDeleted(false)
                    .build());
        }

        return users;
    }

    /**
     * 진행 중인 설문 엔티티를 생성한다.
     *
     * @param author
     * @param title
     * @param detail
     * @return
     */
    public static Form createForm(User author, String title, String detail) {
        return Form.builder()
                .user(author)
                .title(title)
                .imageUrl(null)
                .detail(detail)
                .startDate(LocalDateTime.now())
                .endDate(LocalDateTime.now().plusDays(2))
                .expectTime(10)
                .isEmailVisible(false)
                .privacyDisposalDate(null)
                .status(FormStatus.PROGRESS)
                .isTemp(false)
                .isDeleted(false)
                .build();
    }

    /**
     * 삭제된 설문 엔티티를 생성한다.
     *
     * @param author
     * @param title
     * @param detail
     * @return
     */
    public static Form createDeletedForm(User author, String title, String detail) {
        return Form.builder()
                .user(author)
                .title(title)
                .imageUrl(null)
                .detail(detail)
                .startDate(LocalDateTime.now())
                .endDate(LocalDateTime.now().plusDays(2))
                .expectTime(10)
                .isEmailVisible(false)
                .privacyDisposalDate(null)
                .status(FormStatus.PROGRESS)
                .isTemp(false)
                .isDeleted(true)
                .build();
    }

    public static Form createFormWithStatus(User author, String title, String detail, FormStatus status) {
        return Form.builder()
                .user(author)
                .title(title)
                .imageUrl(null)
                .detail(detail)
                .startDate(LocalDateTime.now())
                .endDate(LocalDateTime.now().plusDays(2))
                .expectTime(10)
                .isEmailVisible(false)
                .privacyDisposalDate(null)
                .status(status)
                .isTemp(false)
                .isDeleted(false)
                .build();
    }

    /**
     * 임시 설문 엔티티를 생성한다.
     *
     * @param author
     * @param title
     * @param detail
     * @return
     */
    public static Form createTemporaryForm(User author, String title, String detail) {
        return Form.builder()
                .user(author)
                .title(title)
                .imageUrl(null)
                .detail(detail)
                .startDate(LocalDateTime.now())
                .endDate(LocalDateTime.now().plusDays(2))
                .expectTime(10)
                .isEmailVisible(false)
                .privacyDisposalDate(null)
                .status(FormStatus.NOT_STARTED)
                .isTemp(true)
                .isDeleted(false)
                .build();
    }

    public static Form createFormWithEndDate(User author, String title, String detail, LocalDateTime endDate,
                                             FormStatus status) {
        return Form.builder()
                .user(author)
                .title(title)
                .imageUrl(null)
                .detail(detail)
                .startDate(endDate.minusDays(1L))
                .endDate(endDate)
                .expectTime(10)
                .isEmailVisible(false)
                .privacyDisposalDate(null)
                .status(status)
                .isTemp(false)
                .isDeleted(false)
                .build();
    }

    public static Form createFormWithImages(User author, String title, String detail, List<String> images) {
        return Form.builder()
                .user(author)
                .title(title)
                .imageUrl(images)
                .detail(detail)
                .startDate(LocalDateTime.now())
                .endDate(LocalDateTime.now().plusDays(2))
                .expectTime(10)
                .isEmailVisible(false)
                .privacyDisposalDate(null)
                .status(FormStatus.NOT_STARTED)
                .isTemp(false)
                .isDeleted(false)
                .build();
    }

    public static Tag createTag(String tagName) {
        return Tag.builder()
                .tagName(tagName)
                .count(0)
                .build();
    }

    public static FormTag createFormTag(Form form, Tag tag) {
        return FormTag.builder()
                .form(form)
                .tag(tag)
                .build();
    }

    public static Reward createReward(String name, Form form, RewardCategory category, int count) {
        return Reward.builder()
                .rewardName(name)
                .form(form)
                .rewardCategory(category)
                .count(count)
                .build();
    }

    public static RewardCategory createRewardCategory(String name) {
        return RewardCategory.builder()
                .rewardCategoryName(name)
                .build();
    }

    public static DescriptiveQuestion createDescriptiveQuestion(Form form,
                                                                DescriptiveQuestionType type,
                                                                String title,
                                                                int position) {
        return DescriptiveQuestion.builder()
                .form(form)
                .questionType(type)
                .title(title)
                .detail("주관식 질문 설명")
                .position(position)
                .isRequired(false)
                .isPrivacy(false)
                .build();
    }

    public static ObjectiveQuestion createObjectiveQuestion(Form form,
                                                            ObjectiveQuestionType type,
                                                            String title,
                                                            int position,
                                                            List<ObjectiveQuestionOption> options) {
        return ObjectiveQuestion.builder()
                .form(form)
                .questionType(type)
                .title(title)
                .detail("객관식 질문 설명")
                .position(position)
                .questionOption(options)
                .isRequired(false)
                .isPrivacy(false)
                .build();
    }

    public static ContentCreateDto createContentCreate(String type, String title, String description,
                                                       List<String> options, boolean isPrivacy) {
        return new ContentCreateDto(type, title, description, options, false, isPrivacy);
    }

    public static List<Submission> createSubmissions(List<User> users, Form form) {
        List<Submission> submissions = new ArrayList<>();

        for (User user : users) {
            submissions.add(Submission.builder()
                    .user(user)
                    .form(form)
                    .isTemp(false)
                    .submitTime(LocalDateTime.now())
                    .build());
        }

        return submissions;
    }
}
