package fr.umlv.movie;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Main {
    public static void main(String[] args) throws IOException {
        var path = Path.of("movies.txt");
        try (var lines = Files.lines(path)) {
            var count = lines.count();
        }
    }
}
