package com.cleox.quickcart.user_service_api.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name="users")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
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

    @OneToOne(mappedBy = "systemUser",fetch = FetchType.EAGER,cascade = CascadeType.REMOVE)
    private Otp otp;


}
