package fr.umlv.rental;

import java.util.Objects;


public record Car(String model, int year) implements Vehicle {
	public Car {
		Objects.requireNonNull(model);
		if (year < 0)
			throw new IllegalStateException("year >= 0");
	}

	public int insurance(int year) {
		if (year < this.year)
			throw new IllegalArgumentException(year + " >= " + this.year);
		if (year - this.year >= 10)
			return 500;
		return 200;
	}

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof Car))
			return false;
		Car c = (Car) o;
		if (!c.model().equals(model))
			return false;
		return c.year() == year;
	}

	@Override
	public int hashCode() {
		return model.hashCode() * year;
	}

	@Override
	public String toString() {
		return model() + " " + year();
	}
}
