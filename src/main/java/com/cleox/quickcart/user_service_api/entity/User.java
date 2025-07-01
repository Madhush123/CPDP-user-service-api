package com.cleox.quickcart.user_service_api.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "users")
public class User {
    @Id
    @Column(name = "user_id",nullable = false, unique = true,length = 80)
    private  String userId;

    @Column(name = "first_name",nullable = false, length = 50)
    private  String firstName;

    @Column(name = "last_name",nullable = false, length = 50)
    private  String lastName;

    @Column(name = "username",unique = true,nullable = false, length = 80)
    private  String username;

    @Column(name = "active_status",columnDefinition = "TINYINT")
    private  boolean activeStatus;


    @OneToOne(mappedBy = "user")
    private ShippingAddress shippingAddress;

    @OneToOne(mappedBy = "user")
    private BillingAddress billingAddress;

    @OneToOne(mappedBy = "user")
    private UserAvatar userAvatar;

    @Column(name = "is_account_non_expired",columnDefinition = "TINYINT",nullable = false)
    private Boolean isAccountNonExpired;

    @Column(name = "is_email_verified",columnDefinition = "TINYINT",nullable = false)
    private Boolean isEmailVerified;

    @Column(name = "is_account_non_locked",columnDefinition = "TINYINT",nullable = false)
    private Boolean isAccountNonLocked;

    @Column(name = "is_enabled",columnDefinition = "TINYINT",nullable = false)
    private Boolean isEnabled;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_date",columnDefinition = "DATETIME",nullable = false)
    private Date createdDate;

    @OneToOne(mappedBy = "systemUser",fetch = FetchType.EAGER,cascade = CascadeType.REMOVE)
    private Otp otp;


}
