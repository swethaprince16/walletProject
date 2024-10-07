package com.project.dto;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDetailsDto {

    private String userName;
    private String mobileNumber;
    private String emailId;
    private String address;
}
