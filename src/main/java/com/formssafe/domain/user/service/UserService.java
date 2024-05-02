package com.formssafe.domain.user.service;

import com.formssafe.domain.form.service.FormService;
import com.formssafe.domain.oauth.client.OauthMemberClientComposite;
import com.formssafe.domain.user.dto.UserRequest.JoinDto;
import com.formssafe.domain.user.dto.UserRequest.LoginUserDto;
import com.formssafe.domain.user.dto.UserRequest.NicknameUpdateDto;
import com.formssafe.domain.user.dto.UserResponse.UserProfileDto;
import com.formssafe.domain.user.entity.OauthId;
import com.formssafe.domain.user.entity.User;
import com.formssafe.domain.user.repository.UserRepository;
import com.formssafe.global.error.ErrorCode;
import com.formssafe.global.error.type.BadRequestException;
import com.formssafe.global.error.type.ForbiddenException;
import com.formssafe.global.error.type.UserNotFoundException;
import com.formssafe.global.util.CommonUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class UserService {
    private final UserRepository userRepository;
    private final OauthMemberClientComposite oauthMemberClientComposite;
    private final FormService formService;

    public User getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(ErrorCode.USER_NOT_FOUND, "존재하지 않는 userId 입니다 : " + id));

        return user;
    }

    @Transactional
    public void join(JoinDto request, LoginUserDto loginUser) {
        User user = getUserById(loginUser.id());
        if (user.isActive()) {
            throw new BadRequestException(ErrorCode.USER_ALREADY_JOIN, "이미 회원가입하셨습니다.");
        }

        user.updateNickname(request.nickname());
        user.activate();
    }

    public UserProfileDto getProfile(LoginUserDto loginUser) {
        User user = getUserById(loginUser.id());

        return UserProfileDto.from(user);
    }

    @Transactional
    public void updateNickname(NicknameUpdateDto request, LoginUserDto loginUser) {
        String nickname = request.nickname();
        User user = getUserById(loginUser.id());

        if (user.getNickname().equals(nickname) || userRepository.existsByNickname(nickname)) {
            throw new BadRequestException(ErrorCode.USER_NICKNAME_DUPLICATE, "중복된 닉네임이 존재합니다.");
        }

        user.updateNickname(request.nickname());
    }

    @Transactional
    public void deleteAccount(long userId, LoginUserDto loginUser) {
        if (userId != loginUser.id()) {
            throw new ForbiddenException(
                    ErrorCode.INVALID_USER,
                    "Invalid user id " + loginUser.id() + " for delete user id " + userId);
        }

        User user = getUserById(loginUser.id());

        oauthMemberClientComposite.deleteAccount(user.getOauthId().getOauthServerType(), user.getRefreshToken());

        user.deleteUser(CommonUtil.generateRandomDeleteNickname(), CommonUtil.generateRandomDeleteEmail(),
                new OauthId(CommonUtil.generateRandomDeleteOauthId(), user.getOauthId().getOauthServerType()));

        formService.deleteFormByUser(user);
    }
}
