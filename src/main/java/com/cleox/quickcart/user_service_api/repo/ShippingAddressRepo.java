package com.cleox.quickcart.user_service_api.repo;

import com.cleox.quickcart.user_service_api.entity.BillingAddress;
import com.cleox.quickcart.user_service_api.entity.ShippingAddress;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShippingAddressRepo extends JpaRepository<ShippingAddress, String> {
}
