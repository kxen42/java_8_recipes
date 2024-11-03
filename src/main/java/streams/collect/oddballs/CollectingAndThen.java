package streams.collect.oddballs;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

public class CollectingAndThen {

    public static void main(String[] args) {
        toCollectingAndThenToUnmodifiableList();
        toCollectingAndThenToUnmodifiableMap();
        changingTypeDrastically();
    }

    /**
     * public static &lt;T,A,R,RR&gt; Collector&lt;T,A,RR&gt; collectingAndThen(Collector&lt;T,A,R&gt; downstream,
     * Function&lt;R,RR&gt; finisher)
     * <p>
     * Adapts a Collector to perform an additional finishing transformation. For example, one could adapt the toList()
     * collector to always produce an immutable list with:
     * <p>
     * List<String> list = people.stream().collect(
     * collectingAndThen(toList(),
     * Collections::unmodifiableList));
     */
    public static void toCollectingAndThenToUnmodifiableList() {
        System.out.println("toCollectingAndThenToUnmodifiableList");
        List<String> collect = Stream.of("a", "b", "c")
                                     .collect(
                                         collectingAndThen(toList(),
                                                           Collections::unmodifiableList));

        System.out.println(collect);

        try {
            collect.add("z");
        } catch (UnsupportedOperationException e) {
            System.out.println("got expected UnsupportedOperationException");
        }
    }

    public static void toCollectingAndThenToUnmodifiableMap() {
        System.out.println("toCollectingAndThenToUnmodifiableMap");
        Map<String, String> collect = Stream.of(new String[][]{
                                                {"a", "1"},
                                                {"b", "2"},
                                                {"c", "42"},
                                            })
                                            .collect(
                                                collectingAndThen(toMap(twoDarray -> twoDarray[0], twoDarray -> twoDarray[1]),
                                                                  Collections::unmodifiableMap));

        System.out.println(collect);

        try {
            collect.put("z", "47");
        } catch (UnsupportedOperationException e) {
            System.out.println("got expected UnsupportedOperationException");
        }
    }

    public static void changingTypeDrastically() {
        System.out.println("changingTypeDrastically");

        Boolean result = Stream.of()
                                .collect(collectingAndThen(toList(), List::isEmpty));

        System.out.println("result is boolean, is it empty? " + result);

        result = Stream.of(1,2,3)
                       .collect(collectingAndThen(toList(), List::isEmpty));

        System.out.println("result is boolean, is it empty? " + result);
    }
}
