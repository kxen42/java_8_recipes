package streams.reduce;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

/**
 * ParallelStream Reduce
 * parallelStream().reduce(Identity, Accumulator, Combiner)
 * <p>
 * The flavors of <em>reduce</em>:
 * <pre>
 * Optional<T> reduce(BinaryOperator<T> accumulator);
 *
 * T reduce(T identity, BinaryOperator<T> accumulator);
 *
 * <U> U reduce(U identity, BiFunction<U, ? super T, U> accumulator, BinaryOperator<U> combiner);
 * </pre>
 *
 * <em>Important Note:</em> The identity value must have certain properties,
 * It must be the neutral element of the operation. For example, 0 is the
 * neutral element for addition, and 1 is the neutral element for multiplication.
 * Applying the operation with the identity value should not change the result.
 * For instance, adding 0 to any number should return the number itself.
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


    /*
    This is an example of using the Combiner for combining the partial results of the parallel streams.

    The other use for a Combiner is when the Accumulator 2nd argument type is not the same as the Identity.
    */
    public static void sumSum() {
        List<Integer> ages = List.of(1, 2, 3, 4, 5, 6);

        // accumulator does sum and combiner does sum
        // the accumulator streams do partial sums
        // the combiner does the final sum of those partial sums
        var a = ages.parallelStream()
                    .reduce(0, (x, y) -> x + y, Integer::sum);

        assertThat(a).isEqualTo(21);


        var b = ages.parallelStream()
                    .reduce(0, Integer::sum, Integer::sum);

        assertThat(b).isEqualTo(21);
    }
}
