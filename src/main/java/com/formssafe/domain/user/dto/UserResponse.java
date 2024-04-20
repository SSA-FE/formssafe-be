package com.formssafe.domain.user.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.formssafe.domain.user.entity.User;
import io.swagger.v3.oas.annotations.media.Schema;

public class UserResponse {

    private UserResponse() {
    }

    public record UserProfileDto(
            @Schema(description = "사용자 ID") Long userId,
            @Schema(description = "사용자 별명") String nickname,
            @Schema(description = "이미지 URL") String imageUrl,
            @Schema(description = "이메일 주소") String email,
            @Schema(description = "사이트 회원가입 여부") boolean isActive) {

        public static UserProfileDto from(User user) {
            return new UserProfileDto(user.getId(), user.getNickname(), user.getImageUrl(), user.getEmail(),
                    user.isActive());
        }
    }

    public record UserListDto(
            @Schema(description = "사용자 ID") Long userId,
            @Schema(description = "사용자 별명") String nickname,
            @JsonInclude(JsonInclude.Include.NON_NULL)
            @Schema(description = "사용자 이메일") String email) {

        public static UserListDto from(User user) {
            return new UserListDto(user.getId(), user.getNickname(), null);
        }

        public static UserListDto from(User user, boolean isEmailVisible) {
            return new UserListDto(user.getId(), user.getNickname(),
                    isEmailVisible ? user.getEmail() : null);
        }
    }

    public record UserAuthorDto(
            @Schema(description = "사용자 ID") Long userId,
            @Schema(description = "사용자 별명") String nickname,
            @JsonInclude(JsonInclude.Include.NON_NULL)
            @Schema(description = "사용자 이메일") String email) {

        public static UserAuthorDto from(User user) {
            return new UserAuthorDto(user.getId(), user.getNickname(), null);
        }

        public static UserAuthorDto from(User user, boolean isEmailVisible) {
            return new UserAuthorDto(user.getId(), user.getNickname(),
                    isEmailVisible ? user.getEmail() : null);
        }
    }
}
