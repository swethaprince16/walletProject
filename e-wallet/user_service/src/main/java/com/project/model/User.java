package com.project.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "wallet_user")
public class User implements Serializable{

//    	- userId
//	- username (not null)
//	- user mobile number (not null, unique, validated)
//	- userEmailId (not null, unique, validated)
//	- userAddress
//	- userPan
//	- createdDate
//	- updatedDate

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @NotBlank(message = "username ise blank for the current user")
    private String userName;

    @NotBlank(message = "mobile number is blank for the current user")
    @Column(unique = true, nullable = false)
    private String mobileNumber;

    @NotBlank(message = "emailID is blank for the current user")
    @Column(unique = true, nullable = false)
    private String emailId;
    private String address;

    @Column(unique = true)
    private String panNumber;
    @CreationTimestamp
    private LocalDateTime createdDate;
    @CreationTimestamp
    private LocalDateTime updatedDate;

}
