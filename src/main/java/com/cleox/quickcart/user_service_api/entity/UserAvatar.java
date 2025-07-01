package com.cleox.quickcart.user_service_api.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name="user_avatars")
public class UserAvatar {
    @Id
    @Column(name = "avatar_id",nullable = false, unique = true,length = 80)
    private  String avatarId;

    @Embedded
    private FileResource fileResource;

    @OneToOne
    @JoinColumn(name = "user_id",nullable = false,unique = true)
    private User user;

}
