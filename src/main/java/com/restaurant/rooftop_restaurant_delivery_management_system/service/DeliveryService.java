package com.restaurant.rooftop_restaurant_delivery_management_system.service;

import com.restaurant.rooftop_restaurant_delivery_management_system.models.CustomerOrder;
import com.restaurant.rooftop_restaurant_delivery_management_system.models.DeliveryAssignment;
import com.restaurant.rooftop_restaurant_delivery_management_system.repo.DeliveryAssignmentRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Slf4j
public class DeliveryService {

    @Autowired
    private DeliveryAssignmentRepository repository;


    public List<String> assignDrivers(List<CustomerOrder> orders, int totalDrivers) {
        List<String> result = new ArrayList<>();

        PriorityQueue<int[]> driverQueue = new PriorityQueue<>(
                Comparator.comparingInt((int[] a) -> a[1]).thenComparingInt(a -> a[0])
        );

        for (int i = 1; i <= totalDrivers; i++) {
            driverQueue.offer(new int[]{i, 0});
        }

        for (int i = 0; i < orders.size(); i++) {
            CustomerOrder order = orders.get(i);
            int[] driver = driverQueue.peek();

            String assignment;
            if (driver == null || driver[1] > order.getOrderTime()) {
                assignment = "No Food :-(";
            } else {
                driverQueue.poll();
                assignment = "D" + driver[0];
                driver[1] = order.getOrderTime() + order.getTravelTime();
                driverQueue.offer(driver);
            }

            String customerId = "C" + (i + 1);
            result.add(customerId + " - " + assignment);

            repository.save(new DeliveryAssignment(null, customerId, assignment));
        }

        return result;
    }

    public void printDBState() {
        log.debug("\n📌Fetching History for Orders,Assigned Drivers");
        repository.findAll().forEach(assignDrivers -> {
            System.out.println(assignDrivers.toString());
        });
    }
}

