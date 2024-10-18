package streams.reduce;

import java.util.List;

/**
 * ParallelStream Reduce
 * parallelStream().reduce(Identity, Accumulator, Combiner)
 *
 */
public class Parallel {
    public static void main(String[] args) {
        /*
        * Remember that
        *  T BinaryOperator<T,T> is the same as
        *  BiFunction<T,T,T>
         */
        sumSum();
    }

    // <U> U reduce(U Identity, BiFunction<U, ? super T, U> Accumulator, BinaryOperator<U> Combiner)
    public static void sumSum() {
        List<Integer> ages = List.of(1, 2, 3, 4, 5, 6);

        // accumulator does sum and combiner does sum
        int computedAges = ages.parallelStream()
                               .reduce(0, (a, b) -> a + b, Integer::sum);
        // expect 21

        computedAges = ages.parallelStream().reduce(0, Integer::sum, Integer::sum);
    }
}
