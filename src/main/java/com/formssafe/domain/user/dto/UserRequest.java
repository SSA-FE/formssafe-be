package com.formssafe.domain.user.dto;

public class UserRequest {
    public record LoginUserDto(Long id){};
    public record NicknamePatchDto(String nickname){};
}
