package com.formssafe.domain.user.dto;

public class UserRequest {

    public record LoginUserDto(Long id) {
    }

    public record NicknameUpdateDto(String nickname) {
    }

    public record JoinDto(String nickname) {
    }
}
