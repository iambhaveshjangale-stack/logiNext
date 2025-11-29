package com.restaurant.rooftop_restaurant_delivery_management_system;

import com.restaurant.rooftop_restaurant_delivery_management_system.models.CustomerOrder;
import com.restaurant.rooftop_restaurant_delivery_management_system.service.DeliveryService;
import com.restaurant.rooftop_restaurant_delivery_management_system.utils.CommonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

@SpringBootApplication
@Slf4j
public class RooftopRestaurantDeliveryManagementSystemApplication implements CommandLineRunner {


    @Autowired
    private DeliveryService deliveryService;
    private final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        SpringApplication.run(RooftopRestaurantDeliveryManagementSystemApplication.class, args);
    }

    @Override
    public void run(String... args) {
        printWelcomeMessage();

        while (true) {
            OrderDriverInput input = getOrderDriverInput();
            if (input == null) continue;

            printLogger(input.totalOrders, input.totalDrivers);

            List<CustomerOrder> orders = getCustomerOrders(input.totalOrders);
            if (orders == null) continue;

            printAssignedDeliveries(orders, input.totalDrivers);

            if (!shouldContinue()) {
                printGoodbyeMessage();
                break;
            }
        }

        log.info("\n===== System Closed =====");
    }

    private void printWelcomeMessage() {
        System.out.println("===== Welcome to Roof Top Restaurant, New York =====");
        System.out.println("===== " + CommonUtils.getNewYorkDate().toUpperCase() + " =====");
    }

    private static class OrderDriverInput {
        int totalOrders;
        int totalDrivers;

        OrderDriverInput(int orders, int drivers) {
            this.totalOrders = orders;
            this.totalDrivers = drivers;
        }
    }

    private OrderDriverInput getOrderDriverInput() {
        try {
            System.out.println("Enter number of Customer orders (C), drivers (M): (3,1) 3-> Orders , 1-> Drivers\n");
            String inputOrderDriver = scanner.nextLine();
            String[] parts = inputOrderDriver.split(",");

            if (parts.length != 2) {
                System.err.println("❌ Invalid format! Enter again like (3,1) 3-> Orders , 1-> Drivers\n");
                return null;
            }

            return new OrderDriverInput(
                    Integer.parseInt(parts[0]),
                    Integer.parseInt(parts[1])
            );
        } catch (NumberFormatException e) {
            System.err.println("Input Error ! please try again");
            return null;
        }
    }

    private List<CustomerOrder> getCustomerOrders(int totalOrders) {
        List<CustomerOrder> orders = new ArrayList<>();
        while (totalOrders != 0) {
            try {
                String input = scanner.nextLine().trim();
                String[] parts = input.split(",");
                if (parts.length != 2) {
                    throw new ArithmeticException("\"❌ Invalid format! Enter again like 4,20\"");
                }
                orders.add(new CustomerOrder(
                        Integer.parseInt(parts[0]),
                        Integer.parseInt(parts[1])
                ));
                totalOrders--;
            } catch (ArithmeticException e) {
                System.err.println(e.getMessage());
                continue;
            } catch (NumberFormatException e) {
                System.err.println("Input Error ! please try again");
                continue;
            }
        }
        return orders;
    }

    private void printAssignedDeliveries(List<CustomerOrder> orders, int totalDrivers) {
        System.out.println("\n📌 Assigned Deliveries:\n");
        deliveryService.assignDrivers(orders, totalDrivers)
                .forEach(System.out::println);
    }

    private boolean shouldContinue() {
        System.out.println("\nDo you want to continue? (Y/N): ");
        return scanner.nextLine().equalsIgnoreCase("Y");
    }

    private void printGoodbyeMessage() {
        System.out.println("\n Have a Great Day, Thank You ! ");
        deliveryService.printDBState();
    }

    private void printLogger(int totalOrders, int totalDrivers) {
        System.out.println("\n=====================================================\n");
        System.out.println("processing " + totalOrders + " Orders for Drivers " + totalDrivers);
        System.out.println("\n=====================================================\n");

        System.out.println("\nEnter orders one by one as: O,T  (Example: 4,20)");
        System.out.println("Type 'Y' when done and want to assign drivers.\n");
    }
}
