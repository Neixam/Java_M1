package fr.umlv.movie;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Main {
    public static void main(String[] args) {
        var path = Path.of("ressources/movies.txt");
        try {
            var lines = Files.lines(path);
            var count = lines.count();
            lines.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
