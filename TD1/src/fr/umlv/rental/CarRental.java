package fr.umlv.rental;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CarRental {
    private final List<Vehicle> stock = new ArrayList<>();

    public void add(Vehicle v) {
        Objects.requireNonNull(v);
        stock.add(v);
    }

    public List<Vehicle> findAllByYear(int year) {
        return stock.stream().filter(c -> c.year() == year).collect(Collectors.toList());
    }

    public void remove(Vehicle v) {
        if (!stock.remove(v))
            throw new IllegalStateException(v + "is not in stock");
    }

    public int insuranceCostAt(int year) {
        return stock.stream().mapToInt(v -> v.insurance(year)).sum();
    }

    public List<Vehicle> stock() {
        return List.copyOf(stock);
    }

    public Optional<Car> findACarByModel(String model) {
        Objects.requireNonNull(model);
        return stock.stream().flatMap(v -> switch (v) {
            case Car c && c.model().equals(model) -> Stream.of(c);
            default -> Stream.empty();
        }).findFirst();
    }

    @Override
    public String toString() {
        return stock.stream().map(Vehicle::toString).collect(Collectors.joining("\n", "",""));
    }
}
