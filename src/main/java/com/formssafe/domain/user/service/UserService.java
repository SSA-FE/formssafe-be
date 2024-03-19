package com.formssafe.domain.user.service;

import com.formssafe.domain.user.dto.UserResponse;
import com.formssafe.domain.user.entity.User;
import com.formssafe.domain.user.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import com.formssafe.domain.user.dto.UserResponse.UserProfileDto;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;


@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public UserProfileDto getProfile(HttpServletRequest request){
        Long userId = getUserIdFromSession(request);
        User user = userRepository.findById(userId).orElseThrow(()->{
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "존재하지 않는 userId입니다.");
        });

        return new UserProfileDto(user.id(), user.nickname(), user.email(), user.imageUrl());
    }

    public Long getUserIdFromSession(HttpServletRequest request){
        HttpSession session = request.getSession(false);
        if (session != null) {
            return (Long) session.getAttribute("userId");
        }
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "세션이 없습니다.");
    }

    @Transactional
    public void patchNickname(HttpServletRequest request,String nickname){
        Long userId = getUserIdFromSession(request);
        User user = userRepository.findById(userId)
            .orElseThrow(()-> {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "존재하지 않는 userId입니다.");
            });
        if(!user.nickname().equals(nickname) && !userRepository.existsByNickname(nickname)){
            BeanUtils.copyProperties(nickname, user, "nickname");
            userRepository.save(user);
        }
        else{
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "중복된 닉네임이 존재합니다.");
        }
    }
}
