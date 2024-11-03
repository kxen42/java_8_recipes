package model;

import java.util.List;

public class UserGenerator {

    /**
     * Create an unmodifiable list of Flintstones and Jetsons.
     * @return unmodifiable List of User objects
     */
    public static List<User> createUsers() {
        // it would be nice to use something to generate dummy data
        return List.of(
            User.builder()
                .age(36)
                .city("Bedrock")
                .firstName("Fred")
                .lastName("Flintstone")
                .build(),
            User.builder()
                .age(34)
                .city("Rapid City")
                .firstName("Wilma")
                .lastName("Flintstone")
                .build(),
            User.builder()
                .age(34)
                .city("Sturgis")
                .firstName("Betty")
                .lastName("Rubble")
                .build(),
            User.builder()
                .age(35)
                .city("Sturgis")
                .firstName("Barney")
                .lastName("Rubble")
                .build(),
            User.builder()
                .age(45)
                .city("Zap")
                .firstName("George")
                .lastName("Jetson")
                .build(),
            User.builder()
                .age(8)
                .city("Zap")
                .firstName("Elroy")
                .lastName("Jetson")
                .build(),
            User.builder()
                .age(39)
                .city("Zap")
                .firstName("Jane")
                .lastName("Jetson")
                .build(),
            User.builder()
                .age(15)
                .city("Zap")
                .firstName("Judy")
                .lastName("Jetson")
                .build(),
            User.builder()
            .age(5)
            .city("Bikini Bottom")
            .firstName("Patrick")
            .lastName("Star")
            .build(),
            User.builder()
                .age(5)
                .city("Bikini Bottom")
                .firstName("SpongeBob")
                .lastName("Squarepants")
                .build()
        );
    }
}
