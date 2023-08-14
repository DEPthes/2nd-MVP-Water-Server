package com.example.water.domain.user.DTO;

import lombok.*;

import javax.validation.constraints.NotEmpty;

@Getter
@Builder
@AllArgsConstructor
@Data
public class UserDto {

    private Long userId;
    private String email;
    private String nickname;
    private String image;

    @Data
    static class CreateUserResponse{
        @NotEmpty
        private Long userId;
        public CreateUserResponse(Long userId){
            this.userId=userId;
        }
    }
}
