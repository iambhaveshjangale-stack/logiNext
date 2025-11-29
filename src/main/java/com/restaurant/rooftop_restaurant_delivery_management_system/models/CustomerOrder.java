package com.restaurant.rooftop_restaurant_delivery_management_system.models;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerOrder {
    private int orderTime;
    private int travelTime;

}

