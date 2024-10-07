package com.project.dto;

import lombok.*;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateUserDto {
    private Long userId;
    private String mobileNumber;
    private String emailId;
    private String address;
}
