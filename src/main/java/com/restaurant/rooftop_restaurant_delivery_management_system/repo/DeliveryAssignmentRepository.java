package com.restaurant.rooftop_restaurant_delivery_management_system.repo;

import com.restaurant.rooftop_restaurant_delivery_management_system.models.DeliveryAssignment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeliveryAssignmentRepository extends JpaRepository<DeliveryAssignment, Long> {
}