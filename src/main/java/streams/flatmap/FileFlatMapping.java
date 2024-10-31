package streams.flatmap;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

/**
 * flatMap is essential {@code Stream<R> flatMap(<T, Stream<R> mapper)}
 * <p>
 * The mapper has to produce a <em>Stream</em>!
 */
public class FileFlatMapping {
    public static void main(String[] args) throws IOException {
        justGetTheStream();
        spewTheStream();
    }

    public static void justGetTheStream() throws IOException {
        Stream<String> lines = Files.lines(Paths.get("src/test/resources/cars.csv"), StandardCharsets.UTF_8);

        // using flatMap(<String, Stream<String> mapper)
        // using Stream.of(array)
        Stream<String> stringStream = lines.flatMap(line -> Stream.of(line.split(",")));
        System.out.println("stringStream: " + stringStream);
    }

    public static void spewTheStream() throws IOException {
        Stream<String> lines = Files.lines(Paths.get("src/test/resources/cars.csv"), StandardCharsets.UTF_8);

        // using flatMap(<String, Stream<String> mapper)
        // using Stream.of(array)
        Stream<String> stringStream = lines.flatMap(line -> Stream.of(line.split(",")));
        stringStream.forEach(System.out::println);
    }
}
