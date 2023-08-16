package com.example.water.domain.user.dto;

import lombok.*;

import javax.validation.constraints.NotEmpty;

@Getter
@Builder
@AllArgsConstructor
@Data
public class UserResponse {

    private Long userId;
    private String email;
    private String nickname;
    private String image;
    private String token;

    @Data
    static class CreateUserResponse{
        @NotEmpty
        private Long userId;
        public CreateUserResponse(Long userId){
            this.userId=userId;
        }
    }

}
