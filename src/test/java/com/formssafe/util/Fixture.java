package com.formssafe.util;

import com.formssafe.domain.form.entity.Form;
import com.formssafe.domain.form.entity.FormStatus;
import com.formssafe.domain.oauth.OauthServerType;
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
                .createTime(LocalDateTime.now())
                .build();
    }

    public static Form createForm(User author, String title, String detail) {
        return Form.builder()
                .user(author)
                .title(title)
                .imageUrl(new ArrayList<>())
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
}
