package fr.umlv.movie;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Movies {
    static public List<Movie> movies(Path path) throws IOException {
        try (var lines = Files.lines(path)) {
            return lines.map(s -> {
                var tokens = s.split(";");
                return new Movie(tokens[0], Arrays.stream(tokens).skip(1).toList());
            }).toList();
        }
    }

    static public Map<String, Movie> movieMap(List<Movie> movies) {
        return movies.stream().
                collect(Collectors.toUnmodifiableMap(Movie::title, Function.identity()));
    }

    static public long numberOfUniqueActors(List<Movie> movies) {
        return movies.stream().flatMap(m -> m.actors().stream())
                .distinct().count();
    }

    static public Map<String, Long> numberOfMoviesByActor(List<Movie> movies) {
        return movies.stream().flatMap(m -> m.actors().stream())
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
    }

    static public Optional<ActorMovieCount> actorInMostMovies(Map<String, Long> movie_number) {
        return movie_number.entrySet().stream().map(v -> new ActorMovieCount(v.getKey(), v.getValue()))
                .max(Comparator.comparingLong(ActorMovieCount::movieCount));
    }
}
