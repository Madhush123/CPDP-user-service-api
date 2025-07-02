package com.cleox.quickcart.user_service_api.repo;

import com.cleox.quickcart.user_service_api.entity.UserAvatar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.Optional;

@EnableJpaRepositories
public interface SystemUserAvatarRepo extends JpaRepository<UserAvatar,String> {

    @Query(value = "SELECT * FROM user_avatars WHERE user_id=?1",nativeQuery = true)
    public Optional<UserAvatar> findUserId(String id);
}
