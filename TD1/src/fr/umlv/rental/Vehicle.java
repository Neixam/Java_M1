package fr.umlv.rental;

public sealed interface Vehicle permits Car, Camel {
    int year();

    int insurance(int year);
}
