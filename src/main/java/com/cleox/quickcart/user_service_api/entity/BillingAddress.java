package com.cleox.quickcart.user_service_api.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name="billing_addresses")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BillingAddress {
    @Id
    @Column(name = "address_id",nullable = false, unique = true,length = 80)
    private  String addressId;

    @Column(length = 80)
    private  String country;

    @Column(length = 80)
    private  String city;

    @Column(length = 80)
    private  String street;

    @OneToOne
    @JoinColumn(name = "user_id",nullable = false,unique = true)
    private User user;


}
