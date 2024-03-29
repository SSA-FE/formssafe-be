package com.formssafe.domain.user.dto;

import com.formssafe.domain.user.entity.User;
import io.swagger.v3.oas.annotations.media.Schema;

public class UserResponse {

    private UserResponse() {
    }

    public record UserProfileDto(
            @Schema(description = "사용자 ID") Long userId,
            @Schema(description = "사용자 별명") String nickname,
            @Schema(description = "이미지 URL") String imageUrl,
            @Schema(description = "이메일 주소") String email) {

        public static UserProfileDto convertEntityToDto(User user){
            return new UserProfileDto(user.id(), user.nickname(), user.imageUrl(),user.email());
        }
    }

    public record UserListDto(
            @Schema(description = "사용자 ID") Long userId,
            @Schema(description = "사용자 별명") String nickname) {

        public static UserListDto from(User user) {
            return new UserListDto(user.id(), user.nickname());
        }
    }

    public record UserAuthorDto(
            @Schema(description = "사용자 ID") Long userId,
            @Schema(description = "사용자 별명") String nickname) {

        public static UserAuthorDto from(User user) {
            return new UserAuthorDto(user.id(), user.nickname());
        }
    }
}
