package streams.collect;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toMap;

public class ToMap {

    public static void main(String[] args) {
        usingFunctionIdentity();

    }

    public static void usingFunctionIdentity() {
        List<String> strings = List.of("Fred", "Wilma", "Barney", "Betty");
        Map<String, Integer> toMap = strings.stream()
                                              .collect(toMap(Function.identity(), String::length));

        System.out.println("toMap type: " + toMap.getClass().getCanonicalName());
        toMap.forEach((x,y) -> System.out.println(x + ":" + y));
    }
}
