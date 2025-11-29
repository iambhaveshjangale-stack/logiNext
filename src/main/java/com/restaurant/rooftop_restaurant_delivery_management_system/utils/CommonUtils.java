package com.restaurant.rooftop_restaurant_delivery_management_system.utils;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class CommonUtils {

    public static String getNewYorkDate() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss a z");
        return Instant.now().atZone(ZoneId.of("America/New_York")).format(formatter);
    }
}
