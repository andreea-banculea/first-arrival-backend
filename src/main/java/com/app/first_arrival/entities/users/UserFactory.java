//package com.app.first_arrival.entities.users;
//
//import com.app.first_arrival.entities.users.*;
//
//public class UserFactory {
//
//    public static User createUser(String userType) {
//        return switch (userType.toLowerCase()) {
//            case "admin" -> new Admin();
//            case "basicuser" -> new BasicUser();
//            case "organization" -> new Organization();
//            case "volunteer" -> new Volunteer();
//            default -> throw new IllegalArgumentException("Unknown user type: " + userType);
//        };
//    }
//}
