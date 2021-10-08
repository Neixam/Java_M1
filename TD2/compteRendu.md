#<center>Compte Rendu Tp1</center>
<p align="right">BOURENNANE Amine</p>

###Exercice 1
1) On a changé de _File_ à _Path_ dans la version 7 de Java car cela permet d'être compatible qu'importe l'OS sur lequel
tourne le code.
2) 
```java
var path = Path.of("ressources/movies.txt");
```
3)
```java
try {
    System.out.println(Files.lines(path).count());
} catch (IOException e) {
    e.printStackTrace();
}
```
4) 
```java
try {
    var lines = Files.lines(path);
    System.out.println(lines.count());
    lines.close();
} catch (IOException e) {
    e.printStackTrace();
}
```
5) 
```java
try {
    var count = lines.count();
} finally {
    lines.close();
}
```
7) La principale différence entre un __try-catch__ et un __throw__ est que le deuxième va renvoyer l'erreur plus haut
dans le code et son objectif c'est d'être transféré jusqu'au _main_ qui va s'occuper lui de check l'erreur.
```java
public static void main(String[] args) throws IOException {
    var path = Path.of("ressources/movies.txt");
    var lines = Files.lines(path);
    if (lines == null)
        throw new IOException("invalid path");
    try {
        var count = lines.count();
    } finally {
        lines.close();
    }
}
```
8) 
```java
public class Main {
    public static void main(String[] args) throws IOException {
        var path = Path.of("ressources/movies.txt");
        try (var lines = Files.lines(path)) {
            var count = lines.count();
        }
    }
}
```
9) Le __try(...)__ est mieux que le __try/finally__ car il permet de n'avoir une donnée temporaire et sera donc libéré à
la fin du __try__
###Exercice 2
1) 
```java
public record Movie(String title, List<String> actors) {
    public Movie(String title, List<String> actors) {
        this.title = Objects.requireNonNull(title);
        this.actors = List.copyOf(Objects.requireNonNull(actors));
    }
}
```
2)
```java
static public List<Movie> movies(Path path) throws IOException {
    try (var lines = Files.lines(path)) {
        return lines.map(s -> {
            var tokens = s.split(";");
            return new Movie(tokens[0],
                Arrays.stream(tokens).skip(1).toList());
        }).toList();
    }
}
```
3) On doit utiliser une _UnmodiableMap_.
```java
static public Map<String, Movie> movieMap(List<Movie> movies) {
    return movies.stream().
        collect(Collectors.toUnmodifiableMap(Movie::title, m -> m));
}
```
4) Elle va nous permettre de se débarrasser de la lambda `m -> m`.
```java
static public Map<String, Movie> movieMap(List<Movie> movies) {
    return movies.stream().
        collect(Collectors.toUnmodifiableMap(Movie::title, Function.identity()));
}
```
5) La méthode _flatMap_ sert à créer un stream qui remplace chaque élément par 0 ou plusieurs, d'un autre ou non,
éléments. Dans notre cas on peut l'utiliser pour transformer chaque __Movies__ en sa liste d'acteur puis de split
chaque acteur.
```java
movies.stream().flatMap(m -> m.actors().stream()).limit(20)
        .forEach(System.out::println);
```
6)
```java
System.out.println(movies.stream().flatMap(m -> m.actors().stream()).count());
```
7) Les _Set_ permettent de ne pas avoir de doublon.
```java
static public long numberOfUniqueActors(List<Movie> movies) {
    return movies.stream().flatMap(m -> m.actors().stream())
        .collect(Collectors.toSet()).size();
}
```
8) Il suffit de l'appeler dès qu'on a notre stream d'acteur.
```java
static public long numberOfUniqueActors(List<Movie> movies) {
    return movies.stream().flatMap(m -> m.actors().stream())
        .distinct().count();
}
```
9) Son type de retour est `Map<String, Long>` car pour chaque acteur on aura son nombre de film.
La méthode `collect()` permet de récupérer un objet _Collector_ du stream, un _Collector_ est 
un regroupement d'objet. `goupingBy(Function.identity(), Collectors.counting())`
```java
static public Map<String, Long> numberOfMoviesByActor(List<Movie> movies) {
    return movies.stream().flatMap(m -> m.actors().stream())
        .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
}
```
10) On doit retourner un _Optional_ car il permet de renvoyer une valeur équivalente à null
```java
static public Optional<ActorMovieCount> actorInMostMovies(Map<String, Long> movie_number) {
    return movie_number.entrySet().stream()
        .map(v -> new ActorMovieCount(v.getKey(), v.getValue()))
        .max(Comparator.comparingLong(ActorMovieCount::movieCount));
}
```
##Conclusion
Durant ce tp j'ai pu d'avantage travailler sur mon apprentissage des _Stream_ et de la gestion des 
exceptions. Je sais maintenant comment fonctionne les exceptions en java et comment faire pour bien les déployer.
J'ai eu du mal sur certaine de ces parties et ce tp m'a montré comment fonctionne chaque façon de gestion d'exception.
Pour conclure, j'ai aussi vu comment faire pour utiliser les fichiers et les lire via un chemin et un _Path_.