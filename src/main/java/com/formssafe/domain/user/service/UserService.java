package com.formssafe.domain.user.service;

import com.formssafe.domain.oauth.client.OauthMemberClientComposite;
import com.formssafe.domain.user.dto.UserRequest.JoinDto;
import com.formssafe.domain.user.dto.UserRequest.LoginUserDto;
import com.formssafe.domain.user.dto.UserRequest.NicknameUpdateDto;
import com.formssafe.domain.user.dto.UserResponse.UserProfileDto;
import com.formssafe.domain.user.entity.User;
import com.formssafe.domain.user.repository.UserRepository;
import com.formssafe.global.exception.type.BadRequestException;
import com.formssafe.global.exception.type.DataNotFoundException;
import com.formssafe.global.exception.type.ForbiddenException;
import com.formssafe.global.util.CommonUtil;
import jakarta.persistence.EntityNotFoundException;
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

    @Transactional
    public void join(JoinDto request, LoginUserDto loginUser) {
        Long userId = loginUser.id();
        User user = userRepository.findById(userId).orElseThrow(() ->
                new DataNotFoundException("존재하지 않는 userId입니다.: " + userId));
        if (user.isActive()) {
            throw new BadRequestException("이미 회원가입하셨습니다.");
        }

        user.updateNickname(request.nickname());
        user.activate();
    }

    public UserProfileDto getProfile(LoginUserDto loginUser) {
        Long userId = loginUser.id();
        User user = userRepository.findById(userId).orElseThrow(() ->
                new DataNotFoundException("존재하지 않는 userId입니다.: " + userId));

        return UserProfileDto.from(user);
    }

    @Transactional
    public void updateNickname(NicknameUpdateDto request, LoginUserDto loginUser) {
        String nickname = request.nickname();
        User user = userRepository.findById(loginUser.id())
                .orElseThrow(() -> new BadRequestException("존재하지 않는 userId입니다."));
        if (user.getNickname().equals(nickname) || userRepository.existsByNickname(nickname)) {
            throw new BadRequestException("중복된 닉네임이 존재합니다.");
        }

        user.updateNickname(request.nickname());
    }

    @Transactional
    public void deleteAccount(long userId, LoginUserDto loginUser) {
        if (userId != loginUser.id()) {
            throw new ForbiddenException(
                    "현재 로그인한 유저와 탈퇴하려는 유저가 다릅니다.: 탈퇴하려는 유저 id:" + userId + " 로그인한 유저 id:" + loginUser.id());
        }

        User user = userRepository.findById(loginUser.id())
                .orElseThrow(() -> new EntityNotFoundException("올바른 ID가 존재하지 않습니다."));

        oauthMemberClientComposite.deleteAccount(user.getOauthId().oauthServer(), user.getRefreshToken());

        user.deleteUser(CommonUtil.generateRandomDeleteNickname(), CommonUtil.generateRandomDeleteEmail());
        userRepository.save(user);
    }
}
