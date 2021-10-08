package fr.umlv.movie;

import java.util.List;
import java.util.Objects;

public record Movie(String title, List<String> actors) {
    public Movie(String title, List<String> actors) {
        this.title = Objects.requireNonNull(title);
        this.actors = List.copyOf(Objects.requireNonNull(actors));
    }
}
