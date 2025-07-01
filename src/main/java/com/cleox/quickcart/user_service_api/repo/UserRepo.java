package com.cleox.quickcart.user_service_api.repo;

import com.cleox.quickcart.user_service_api.entity.BillingAddress;
import com.cleox.quickcart.user_service_api.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepo extends JpaRepository<User, String> {
    public Optional<User> findByUsername(String email);
}
