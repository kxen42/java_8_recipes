package streams.collect;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toMap;

public class ToMap {

    public static void main(String[] args) {
        usingFunctionIdentity();
        mitigatingDuplicateKeys();

    }

    public static void usingFunctionIdentity() {
        System.out.println("usingFunctionIdentity");
        List<String> strings = List.of("Fred", "Wilma", "Barney", "Betty");
        Map<String, Integer> toMap = strings.stream()
                                              .collect(toMap(Function.identity(), String::length));

        System.out.println("toMap type: " + toMap.getClass().getCanonicalName());
        toMap.forEach((x,y) -> System.out.println(x + ":" + y));
    }

    /**
     * Using the toMap that uses a function to determine how to handle duplicate keys.
     * <p>
     * If you get to the mergeFunction you have to args that are the same.
     * <pre>
     * toMap(Function<? super T,? extends K> keyMapper,
     * Function<? super T,? extends U> valueMapper,
     * BinaryOperator<U> mergeFunction)
     * </pre>
     */
    public static void mitigatingDuplicateKeys() {
        System.out.println("mitigatingDuplicateKeys");
        List<String> strings = List.of("Fred", "Fred", "Fred", "Wilma", "Wilma", "Betty");

        Map<String, Integer> collect = strings.stream()
                                              .collect(toMap(Function.identity(), String::length,
                                                             (item, duplicate) -> item));

        System.out.println(collect);
    }
}
