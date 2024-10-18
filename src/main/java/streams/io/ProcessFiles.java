package streams.io;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class ProcessFiles {

    public static void main(String[] args) {
        spewFile();
    }

    public static void spewFile() {
        try (Stream<String> lines = Files.lines(Paths.get("src/main/resources/simple_file.txt"))) {
            lines.forEach(System.out::println);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
