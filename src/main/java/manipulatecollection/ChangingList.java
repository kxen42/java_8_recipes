package manipulatecollection;

import model.User;
import model.UserGenerator;

import java.util.List;

public class ChangingList {

    public static void main(String[] args) {
        removeAllMatchingElements();
    }

    public static void removeAllMatchingElements() {
        System.out.println(" ----- removeAllMatchingElements");
        // most efficient to remove all matching elements
        List<User> users = new java.util.ArrayList<>(UserGenerator.createUsers());
        System.out.println("before: " + users);

        boolean listChanged = users.removeIf(user -> user.getLastName()
                                                    .equals("Jetson"));

        System.out.println("list changed: " + listChanged);
        System.out.println("after: " + users);

    }
}
