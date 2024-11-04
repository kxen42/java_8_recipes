package streams.reduce;

import model.User;
import org.junit.Test;

import java.util.List;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.OptionalLong;
import java.util.function.BiFunction;
import java.util.function.BinaryOperator;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;
import static org.assertj.core.api.Assertions.withinPercentage;

/**
 * The flavors of <em>reduce</em>:
 * <pre>
 * Optional&lt;T&gt; reduce(BinaryOperator&lt;T&gt;  accumulator)
 *
 * T reduce(T identity, BinaryOperator&lt;T&gt;  accumulator)
 *
 * &lt;U&gt; U reduce(U identity, BiFunction&lt;U, ? super T, U&gt; accumulator, BinaryOperator&lt;U&gt;  combiner)
 * </pre>
 *
 * <em>Important Note:</em> The identity value must have certain properties,
 * It must be the neutral element of the operation. For example, 0 is the
 * neutral element for addition, and 1 is the neutral element for multiplication.
 * Applying the operation with the identity value should not change the result.
 * For instance, adding 0 to any number should return the number itself.
 * <p>
 * From the Stream API documentation about using the combiner:<br>
 * The identity value must be an identity for the <em>combiner function</em>. This means that for all u,<br>
 * {@code combiner(identity, u)} is equal to u. <br>
 * Additionally, the combiner function must be compatible with the accumulator function;<br>
 * for all u and t, the following must hold:<br>
 * {@code combiner.apply(u, accumulator.apply(identity, t)) == accumulator.apply(u, t)}
 *
 * @see <a href="https://docs.oracle.com/javase/8/docs/api/java/util/stream/Stream.html" target="_top">https://docs.oracle.com/javase/8/docs/api/java/util/stream/Stream.html</a>
 */
public class BasicsTest {


    @Test
    // T reduce(T identity, BinaryOperator<T> accumulator)
    public void emptyResult() {
        // empty stream returns identity
        List<Integer> items = List.of();
        var a = items.stream()
                     .reduce((x, y) -> x * y);

        assertThat(a).isEqualTo(Optional.empty());

        var b = IntStream.of()
                         .reduce((x, y) -> x * y);

        assertThat(b).isEqualTo(OptionalInt.empty());

        var c = LongStream.of()
                          .reduce((x, y) -> x * y);

        assertThat(c).isEqualTo(OptionalLong.empty());

        var d = DoubleStream.of()
                            .reduce((x, y) -> x * y);

        assertThat(d).isEqualTo(OptionalDouble.empty());
    }

    @Test
    // T reduce(T identity, BinaryOperator<T> accumulator)
    public void sumList() {
        // the accumulator sums the numbers
        List<Integer> nums = List.of(1, 2, 3, 4, 5, 6);

        // use two parameters for accumulator
        int a = nums.stream()
                    .reduce(0, (partialResult, nextElement) -> partialResult + nextElement);

        assertThat(a).isEqualTo(21);

        // use method reference for accumulator
        int b = nums.stream()
                    .reduce(0, Integer::sum);

        assertThat(b).isEqualTo(21);

        List<String> strings = List.of("a", "b", "c");
        String c = strings.stream()
                          .reduce("", (x, y) -> x + y);

        assertThat(c).isEqualTo("abc");

        String d = strings.stream()
                          .reduce("", String::concat);

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

    @Test
    // T reduce(T identity, BinaryOperation<T> accumulator)
    public void canDo() {
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
        var b = IntStream.of(2, 4, 8)
                         .reduce(0, (x, y) -> x - y);

        assertThat(b).isEqualTo(-14);

        // neutral element of multiplication is 1
        var c = IntStream.of(2, 2, 2)
                         .reduce(1, (x, y) -> x * y);

        assertThat(c).isEqualTo(8);

        // integer division doesn't have a neutral element
        // (((1/33) / 3)
        var d = IntStream.of(33, 3)
                         .reduce(1, (x, y) -> x / y);

        assertThat(d).isEqualTo(0);

        // integer mod doesn't have neutral element
        // ((1 % 33) % 3)
        var e = IntStream.of(33, 3)
                         .reduce(1, (x, y) -> x % y);

        assertThat(e).isEqualTo(1);

        // (((1 / 3.0) / 4.0) / 0.2)
        var f = DoubleStream.of(3.0, 4.0, 0.2)
                            .reduce(1, (x, y) -> x / y);
        // f is within this range 0.246 <= f <= 0.586
        assertThat(f).isCloseTo(0.416, withinPercentage(0.17));

        // OptionalDoubleAssert
        // (((1.0 / 3.0) / 4.0) / 0.2)
        var g = DoubleStream.of(1.0, 3.0, 4.0, 0.2)
                            .reduce((x, y) -> x / y);

        assertThat(g).hasValueCloseTo(0.416, within(0.17));

        // neutral element for String is empty string
        var h = Stream.of("Fred", "Wilma", "Barney")
                      .reduce("", (x, y) -> String.join(",", x, y));

        assertThat(h).isEqualTo(",Fred,Wilma,Barney");
    }

    @Test
    public void findDog() {
        List<String> strings = List.of("dog", "over", "good");

        var a = strings.stream()
                       .reduce(new String(), (x, y) -> {
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

        var b = strings.stream()
                       .reduce("", (x, y) -> x.length() < 3 ? x : y);

        assertThat(b).isEmpty();

        // returns Optional[good]
        // using AssertJ hasValue to test the value of an Optional, avoids potential exception from get()
        var c = strings.stream()
                       .reduce((x, y) -> x.length() < 3 ? x : y);

        assertThat(c).hasValue("good");

        // returns Optional[dog]
        var d = strings.stream()
                       .reduce((x, y) -> x.length() <= 3 ? x : y);

        assertThat(d).hasValue("dog");
    }

    @Test
    public void joinStrings() {
        // avoid a leading comma
        var a = Stream.of("Fred", "Wilma", "Betty")
                      .reduce("", (x, y) -> x.isEmpty() ? y : String.join(", ", x, y));

        assertThat(a).isEqualTo("Fred, Wilma, Betty");
    }

    /*
    This is an example of using the Combiner when the Accumulator arguments aren't the same type.
    Accumulator arguments have different types (i.e. the 2nd argument type doesn't match Identity type).

    Here the Identity type is int, so the Combiner must produce an int.
    The 2nd argument of the Accumulator is a String.
    Accumulator "int (int, String) -> int + int"
    Accumulator evaluation (((0 + 3) + 4) + 5)  this looks like it would be an int, but the result is actual a String.
    Combiner "int (int, int) -> int + int".

    The other use for a Combiner is for combining the partial results from parallel streams.
     */
    @Test
    public void withCombiner() {
        List<String> strings = List.of("boo", "ouch", "yadda");
        var a = strings.stream()
                       .reduce(0,
                               (i, str) -> i + str.length(),
                               (x, y) -> x + y);

        assertThat(a).isEqualTo(12);
    }

    @Test
    public void combinerWithCharacter() {
        List<String> strings = List.of("dog", "over", "good");

        var a = strings.stream()
                       .reduce(Character.MIN_VALUE,
                               new BiFunction<Character, String, Character>() {
                                   @Override
                                   public Character apply(Character c, String s) {
                                       System.out.println("partialResult: [" + c + "], currentString: [" + s + "]");
                                       return (char) (c + s.charAt(0));
                                   }
                               },
                               new BinaryOperator<Character>() {
                                   @Override
                                   public Character apply(Character c1, Character c2) {
                                       // this doesn't run, it's just to get the right type
                                       return Character.MAX_VALUE;
                                   }
                               }
                       );

        System.out.println("combinerWithCharacter: " + a);
        assertThat(a).isEqualTo('ĺ');

    }

    @Test
    public void buildStringFromCharacters() {
        List<String> strings = List.of("dog", "over", "good");

        var a = strings.stream()
                       .map(v -> v.charAt(0))
                       .reduce("",
                               new BiFunction<String, Character, String>() {
                                   @Override
                                   public String apply(String s, Character c) {
                                       System.out.println("partialResult: [" + s + "], currentString: [" + c + "]");
                                       return s.concat(String.valueOf(c));
                                   }
                               },
                               new BinaryOperator<String>() {
                                   @Override
                                   public String apply(String s1, String s2) {
                                       System.out.println("s1: [" + s1 + "], s2: [" + s2 + "]");
                                       // this doesn't run
                                       return "does not matter";
                                   }
                               }
                       );

        System.out.println("buildStringFromCharacters: " + a);
        assertThat(a).isEqualTo("dog");

    }

    @Test
    public void payAttentionToIdentity() {
        Integer a = Stream.of(1, 1, 1)
                          .filter(n -> n > 5) // empty stream
                          .reduce(2, (x, y) -> x); // means we get identity
        assertThat(a).isEqualTo(2);

        Integer b = Stream.of(1, 1, 1)
                          .reduce(2, (x, y) -> x); // means we get identity
        assertThat(b).isEqualTo(2);
    }

    /**
     * What else can you do with this? I haven't found a useful example.
     */
    @Test
    public void threeArgReduce() {
        Stream<String> strings = Stream.of("Gumball", "Bart", "Barney");
        Integer reduce = strings.reduce(0,
                                        (n, str) -> n + str.length(),
                                        Integer::sum);

        assertThat(reduce).isEqualTo(17);

    }
}
