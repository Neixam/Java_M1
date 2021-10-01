package fr.umlv.rental;

public record Camel(int year) implements Vehicle {
    public Camel {
        if (year < 0)
            throw new IllegalStateException("year >= 0");
    }

    public int insurance(int year) {
        if (year < this.year)
            throw new IllegalArgumentException(year + " >= " + this.year);
        return (year - this.year) * 100;
    }

    @Override
    public String toString() {
        return "camel " + year;
    }
}
