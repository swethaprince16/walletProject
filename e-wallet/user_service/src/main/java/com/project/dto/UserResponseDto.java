package com.project.dto;

import com.project.model.User;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Builder
public class UserResponseDto {

    private String message;
    private User user;
}
