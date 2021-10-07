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