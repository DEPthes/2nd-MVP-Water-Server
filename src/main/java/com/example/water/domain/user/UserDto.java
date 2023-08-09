package com.example.water.domain.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@NoArgsConstructor
@AllArgsConstructor
@Builder
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
