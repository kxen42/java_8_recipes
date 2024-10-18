package streams.reduce.baeldung;

import static org.assertj.core.api.Assertions.*;
import model.User;

import java.util.List;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.OptionalLong;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;

/**
 * reduce(Identity, Accumulator)
 *
 * <em>Important Note:</em> The identity value must have certain properties,
 * It must be the neutral element of the operation. For example, 0 is the
 * neutral element for addition, and 1 is the neutral element for multiplication.
 * Applying the operation with the identity value should not change the result.
 * For instance, adding 0 to any number should return the number itself.
 */
public class Basics {
    public static void main(String[] args) {
        emptyResult();
        sumList();
        canDo();
        findDog();
        withCombiner();
    }

    // T reduce(T identity, BinaryOperator<T> accumulator)
    public static void emptyResult() {
        // empty stream returns identity
        List<Integer> items = List.of();
        var a = items.stream()
                              .reduce((x, y) -> x * y);

        assertThat(a).isEqualTo(Optional.empty());

        var b = IntStream.of().reduce((x, y) -> x * y);

        assertThat(b).isEqualTo(OptionalInt.empty());

        var c = LongStream.of().reduce((x, y) -> x * y);

        assertThat(c).isEqualTo(OptionalLong.empty());

        var d = DoubleStream.of().reduce((x, y) -> x * y);

        assertThat(d).isEqualTo(OptionalDouble.empty());
    }

    // T reduce(T identity, BinaryOperator<T> accumulator)
    public static void sumList() {
        // the accumulator sums the numbers
        List<Integer> nums = List.of(1,2,3,4,5,6);

        // use two parameters for accumulator
        int a = nums.stream()
            .reduce(0, (partialResult, nextElement) -> partialResult + nextElement);

        assertThat(a).isEqualTo(21);

        // use method reference for accumulator
        int b = nums.stream()
            .reduce(0, Integer::sum);

        assertThat(b).isEqualTo(21);

        List<String> strings = List.of("a", "b", "c");
        String c = strings.stream().reduce("", (x, y) -> x + y);

        assertThat(c).isEqualTo("abc");

        String d = strings.stream().reduce("", String::concat);

        assertThat(d).isEqualTo("abc");
    }

    // won't compile
//    public static void cantInferType() {
// Stream of User objects trying to accumulate int
//       List<User> users = List.of(new User("John", 30), new
//            User("Julie", 35));
//  one partialAgeResult is int, user is User
//        int computedAges =
//            users.stream().reduce(0, (partialAgeResult, user) ->
//                partialAgeResult + user.getAge());
//    }

    // T reduce(T identity, BinaryOperation<T> accumulator)
    public static void canDo() {
        List<User> users = List.of(new User("John", 30), new
            User("Julie", 35));

        // neutral element for addition is 0
        int a =
            users.stream()
                 .map(User::getAge)
                 .reduce(0, (partialAgeResult, nextAge) ->
                     partialAgeResult + nextAge);

        assertThat(a).isEqualTo(65);

        // neutral element of subtraction is 0
        var b = IntStream.of(2,4,8).reduce(0, (x,y) -> x - y);

        assertThat(b).isEqualTo(-14);

        // neutral element of multiplication is 1
        var c = IntStream.of(2,2,2).reduce(1, (x,y) -> x * y);

        assertThat(c).isEqualTo(8);

        // integer division doesn't have a neutral element
        // (((1/33) / 3)
        var d = IntStream.of(33,3).reduce(1, (x,y) -> x / y);

        assertThat(d).isEqualTo(0);

        // integer mod doesn't have neutral element
        // ((1 % 33) % 3)
        var e = IntStream.of(33,3).reduce(1, (x,y) -> x % y);

        assertThat(e).isEqualTo(1);

        // (((1 / 3.0) / 4.0) / 0.2)
        var f = DoubleStream.of(3.0, 4.0, 0.2).reduce(1,(x,y) -> x/y);
        // f is within this range 0.246 <= f <= 0.586
        assertThat(f).isCloseTo(0.416, withinPercentage(0.17));

        // OptionalDoubleAssert
        // (((1.0 / 3.0) / 4.0) / 0.2)
        var g = DoubleStream.of(1.0, 3.0, 4.0, 0.2).reduce((x,y) -> x/y);

        assertThat(g).hasValueCloseTo(0.416, within(0.17));

        // neutral element for String is empty string
        var h = Stream.of("Fred", "Wilma", "Barney").reduce("", (x,y) -> String.join(",", x, y));

        assertThat(h).isEqualTo(",Fred,Wilma,Barney");
    }

    public static void findDog() {
        List<String> strings = List.of("dog", "over", "good");

        var a = strings.stream().reduce(new String(), (x,y) -> {
            if (x.equals("dog")) return x;
            return y;
        });

        assertThat(a).isEqualTo("dog");

        // doesn't compile
        // var boo = strings.stream().reduce(new Character(), (Character x, Character y) -> x);
        // doesn't compile
//         var boo = strings.stream().reduce((Character x, y) -> x + y.charAt(0), (c1,c2) -> c1 += c2);
        // doesn't compile
//        var boo = strings.stream().reduce((Character x, Character y) -> x + y, (c1,c2) -> c1 += c2);
        // returns Optional[dogog]
//        var boo = strings.stream().reduce((x, y) -> x + y.charAt(0) );

        var b = strings.stream().reduce("", (x, y) -> x.length() < 3 ? x : y );

        assertThat(b).isEmpty();

        // returns Optional[good]
        // using AssertJ hasValue to test the value of an Optional, avoids potential exception from get()
        var c = strings.stream().reduce((x, y) -> x.length() < 3 ? x : y );

        assertThat(c).hasValue("good");

        // returns Optional[dog]
        var d = strings.stream().reduce((x, y) -> x.length() <= 3 ? x : y );

        assertThat(d).hasValue("dog");
    }

    public static void withCombiner() {
        List<String> strings = List.of("boo", "ouch", "yadda");
        var a = strings.stream().reduce(0,
                                        (i, s) -> i + s.length(),
                                        (x, y) -> x + y);

        assertThat(a).isEqualTo(12);
    }
}
