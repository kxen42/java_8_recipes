package streams.collect.oddballs;

import model.User;
import model.UserGenerator;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.mapping;
import static java.util.stream.Collectors.maxBy;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

/**
 * <em>Collector.mapping()</em> is
 * <p>
 * public static <T,U,A,R> Collector<T,?,R> mapping(Function<? super T,? extends U> mapper,
 * Collector<? super U,A,R> downstream)
 * <p>
 * Adapts a Collector accepting elements of type U to one accepting elements of type T by applying a mapping function to each input element before accumulation.
 * <p>
 * API Note:
 * The mapping() collectors are most useful when used in a multi-level reduction, such as downstream of a groupingBy or partitioningBy. For example, given a stream of Person, to accumulate the set of last names in each city:
 * <p>
 * Map<City, Set<String>> lastNamesByCity
 * = people.stream().collect(
 * groupingBy(Person::getCity,
 * mapping(Person::getLastName,
 * toSet())));
 * <p>
 * Type Parameters:
 * T - the type of the input elements
 * U - type of elements accepted by downstream collector
 * A - intermediate accumulation type of the downstream collector
 * R - result type of collector
 * <p>
 * Parameters:
 * mapper - a function to be applied to the input elements
 * Parameters:
 * mapper - a function to be applied to the input elements
 * downstream - a collector which will accept mapped values
 * <p>
 * Returns:
 * a collector which applies the mapping function to the input elements and provides the mapped results
 * to the downstream collector
 */
public class MappingCollector {

    public static void main(String[] args) {
        collectorMapping();
        groupingByCity();
        findUserWithLongestFirstName();
    }


    public static void collectorMapping() {
        System.out.println(" ----- " + "collectorMapping");
        List<User> users = UserGenerator.createUsers();

        Set<String> collect = users.stream()
                                   .collect(mapping(User::getLastName, toSet()));

        System.out.println(collect);

        // same as
        Set<String> collect1 = UserGenerator.createUsers()
                                            .stream()
                                            .map(User::getLastName)
                                            .collect(toSet());
        System.out.println(collect1);

    }

    /**
     * * Map<City, Set<String>> lastNamesByCity
     * * = people.stream().collect(
     * * groupingBy(Person::getCity,
     * * mapping(Person::getLastName,
     * * toSet())));
     */
    public static void groupingByCity() {
        System.out.println(" ----- groupingByCity");

        Map<String, Set<String>> collect = UserGenerator.createUsers()
                                                        .stream()
                                                        .collect(
                                                            groupingBy(
                                                                User::getCity,
                                                                mapping(User::getLastName, toSet())
                                                            )
                                                        );

        System.out.println(collect);
    }

    /**
     * @see <a href="https://stackabuse.com/guide-to-java-8-collectors-collectingandthen/">Stack Abuse - Guide to Java 8 Collectors: collectingAndThen()</a>
     */
    public static void findUserWithLongestFirstName() {
        String longestName = UserGenerator.createUsers()
                                          .stream()
                                          .collect(collectingAndThen(              //<--- Collectors.collectingAndThen()
                                                                                   // Encounter all the User objects
                                                                                   // Map them to their first names
                                                                                   // Collect those names in a list
                                                                                   mapping(                              //<--- Collectors.mapping()
                                                                                                                         User::getFirstName,
                                                                                                                         toList()
                                                                                   ),
                                                                                   // Stream those names again
                                                                                   // Find the longest name
                                                                                   // If not available, return "not found"
                                                                                   listFirstNames -> {
                                                                                       return listFirstNames
                                                                                           .stream()
                                                                                           .collect(maxBy(                 //<--- Collectors.maxBy()
                                                                                                                           comparing(String::length)
                                                                                           ))
                                                                                           .orElse("not found");      //<--- Optional.orElse()
                                                                                   }
                                                   )
                                          );
        System.out.println("longest firstName: " + longestName);

    }
}