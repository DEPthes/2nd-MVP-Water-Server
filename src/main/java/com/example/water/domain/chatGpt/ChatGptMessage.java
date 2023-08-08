package com.example.water.domain.chatGpt;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ChatGptMessage {
    private String role;
    private String content;
}