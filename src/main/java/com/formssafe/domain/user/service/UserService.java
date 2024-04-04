package com.formssafe.domain.user.service;

import com.formssafe.domain.oauth.client.OauthMemberClientComposite;
import com.formssafe.domain.user.dto.UserRequest.NicknameUpdateDto;
import com.formssafe.domain.user.dto.UserResponse.UserProfileDto;
import com.formssafe.domain.user.entity.User;
import com.formssafe.domain.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;


@Service
@Transactional(readOnly = true)
@Slf4j
public class UserService {
    private final UserRepository userRepository;
    private final OauthMemberClientComposite oauthMemberClientComposite;

    public UserService(UserRepository userRepository, OauthMemberClientComposite oauthMemberClientComposite){
        this.userRepository = userRepository;
        this.oauthMemberClientComposite = oauthMemberClientComposite;
    }

    public UserProfileDto getProfile(HttpServletRequest request){
        Long userId = getUserIdFromSession(request);
        User user = userRepository.findById(userId).orElseThrow(()->
             new ResponseStatusException(HttpStatus.BAD_REQUEST, "존재하지 않는 userId입니다."));

        return UserProfileDto.convertEntityToDto(user);
    }

    private Long getUserIdFromSession(HttpServletRequest request){
        HttpSession session = request.getSession(false);
        if (session != null) {
            return (Long) session.getAttribute("userId");
        }
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "세션이 없습니다.");
    }

    @Transactional
    public void updateNickname(HttpServletRequest request, NicknameUpdateDto nicknamePatchDto){
        Long userId = getUserIdFromSession(request);
        String nickname = nicknamePatchDto.nickname();
        User user = userRepository.findById(userId)
            .orElseThrow(()-> {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "존재하지 않는 userId입니다.");
            });
        if (user.getNickname().equals(nickname) || userRepository.existsByNickname(nickname)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "중복된 닉네임이 존재합니다.");
        }
        user.updateNickname(nickname);
        userRepository.save(user);
    }

    @Transactional
    public void deleteAccount(HttpServletRequest request, long userId) {
        Long sessionUserId = getUserIdFromSession(request);
        if(sessionUserId!= userId){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "userId가 올바르지 않습니다.");
        }
        User user = userRepository.findById(userId)
                .orElseThrow(()-> new EntityNotFoundException("올바른 ID가 존재하지 않습니다."));

        oauthMemberClientComposite.deleteAccount(user.getOauthId().oauthServer(), user.getRefreshToken());

        userRepository.delete(user);
    }
}
