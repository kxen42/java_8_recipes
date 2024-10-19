package optionals;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;
import static org.assertj.core.api.Assertions.assertThat;


public class BasicsTest {

    @Test
    public void cantUseLiteralNull() {
        assertThatNullPointerException().isThrownBy(() -> {
            Optional.of(null);
        });
    }

    @Test
    public void ofNullableIsOk() {
        String str = null;
        Optional<String> optional = Optional.ofNullable(str);

        /*
        Optional<T> isPresent()
        Optional<T> isEmpty()
        testing using AssertJ.
         */
        assertThat(optional).isNotPresent();
        assertThat(optional).isEmpty();

        /*
        Optional<T> get() throws NoSuchElementException if value is not present
         */
        assertThatExceptionOfType(NoSuchElementException.class)
            .isThrownBy(optional::get);

    }

    @Test
    public void ifPresent_WithConsumer() {
        Optional.of("Fred").ifPresent(System.out::println);

    }

    @Test
    public void ifPresentOrElse_WhenValue_NotPresent() {
        // throws NPE if the emptyAction is null
        Optional<Integer> optionalString = Optional.ofNullable(null);
        List<Integer> l = new ArrayList<>();

        optionalString.ifPresentOrElse(
            v -> l.add(v * 2),
            () -> {
                l.add(42);
            }
        );

        // Make sure package is org.assertj.core.api.Assertions.assertThat
        assertThat(l).containsExactly(42);
    }

    @Test
    public void ifPresentOrElse_WhenValue_IsPresent() {
        // throws NPE if the emptyAction is null
        Optional<Integer> optionalString = Optional.of(47);
        List<Integer> l = new ArrayList<>();

        optionalString.ifPresentOrElse(
            v -> l.add(v * 2),
            () -> {
                l.add(42);
            }
        );

        // Make sure package is org.assertj.core.api.Assertions.assertThat
        assertThat(l).containsExactly(94);
    }

    @Test
    public void orElse_ProvidesDefault() {
        String str = null;
        String s = Optional.ofNullable(str)
                           .orElse("Barney");

        assertThat(s).isEqualTo("Barney");

        str = "Fred";
        String s2 = Optional.ofNullable(str)
                            .orElse("Barney");

        assertThat(s2).isEqualTo("Fred");
    }

    @Test
    public void orElseThrow_ThrowExceptionIfValueNotPresent() {
        String str = null;

        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() ->
                                                                             {
                                                                                 Optional.ofNullable(str)
                                                                                         .orElseThrow(IllegalArgumentException::new);
                                                                             }
        );
    }
}