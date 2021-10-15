# <center>Compte Rendu Tp2</center>
<p align="right">BOURENNANE Amine</p>

## Exercice 1
1) un _Runnable_ sert à instancier une fonction avec 0 paramètre et à renvoyer `void`
2)
```java
public class HelloThread {
    public static void main(String[] args) {
        for (int i = 0; i < 4; i++) {
            var thread = new Thread(() -> {
                for (int j = 0; j <= 5000; j++)
                    System.out.println("hello " + Thread.currentThread().getName() + " " + j);
            });
            thread.setName(i + "");
            thread.start();
        }
    }
}
```
3) Que les sorties de chaques threads sont "emmêlées" de façon différentes. Oui car 
chaque thread sont éxécutés en "même" temps et sont décalé par l'horloge.
4)
```java 
System.out.println("hello " + Thread.currentThread().getName() + " " + i);
```

## Exercice 2

1) 
```java
public class HelloThreadJoin {
    public static void main(String[] args) throws InterruptedException {
        var threads = new ArrayList<Thread>();
        for (var i = 0; i < 4; i++) {
            var thread = new Thread(() -> {
                for (var j = 0; j < 5000; j++)
                    System.out.println("hello " + Thread.currentThread() + " " + j);
            });
            thread.setName(i + "");
            threads.add(thread);
        }
        threads.forEach(Thread::start);
        for (var thread : threads) {
            thread.join();
        }
        System.out.println("Le programme est finis !");
    }
}
```

## Exercice 3

1)
```java
public class HelloThreadBug {
    static private final int nbThread = 4;

    public static void main(String[] args) throws InterruptedException {
        var list = new ArrayList<Integer>(nbThread * 5000);
        var threads = new ArrayList<Thread>();
        for (var i = 0; i < nbThread; i++) {
            var thread = new Thread(() -> {
                for (int j = 0; j <= 5000; j++)
                    list.add(j);
            });
            thread.setName(i + "");
            threads.add(thread);
        }
        threads.forEach(Thread::start);
        for (var thread : threads) {
            thread.join();
        }
        System.out.println(list.size());
    }
}
```

2) La liste n'a pas la taille attendu car lors de l'appelle à `add()` on utilise une variable encapsuler dans la classe _ArrayList_ qui
se souvient de la taille actuelle de la liste, mais vu que l'opération `++` n'est pas une opération atomique (opération en 1 ligne 
assembleur) alors la valeur n'est surement plus la même entre la lecture et l'écriture et ce qui crée ce problème.

3) Le fait de faire ça lance une __ArrayIndexOutOfBoundsException__ car l'indice lu est surement plus petit que le nouvelle indice.

4)
```java
public class HelloThreadBug {
    static private final int nbThread = 4;
    private final ArrayList<Integer> encapList = new ArrayList<Integer>();

    public void add(Integer i) {
        synchronized (encapList) {
            encapList.add(i);
        }
    }

    public int size() {
        synchronized (encapList) {
            return encapList.size();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        var list = new HelloThreadBug();
        var threads = new ArrayList<Thread>();
        for (var i = 0; i < nbThread; i++) {
            var thread = new Thread(() -> {
                for (int j = 0; j < 5000; j++)
                    list.add(j);
            });
            thread.setName(i + "");
            threads.add(thread);
        }
        threads.forEach(Thread::start);
        for (var thread : threads) {
            thread.join();
        }
        System.out.println(list.size());
    }
}
```

5)
```java
public class ThreadSafeList {
    private final ArrayList<Integer> encapList = new ArrayList<>();

    public void add(Integer i) {
        synchronized (encapList) {
            encapList.add(i);
        }
    }

    public int size() {
        synchronized (encapList) {
            return encapList.size();
        }
    }
}
```

6) Une classe __thread-safe__ est une classe qui peut être utilisé sans créer de problème dans un code qui utilise des threads.

## Conclusion
J'ai consolidé les connaissances du cours sur les threads et j'ai appris à faire une classe thread-safe.
J'ai eu des problèmes sur la transmission de l'exception de la méthode `Thread.join()` car j'ai commencé
à le faire via un `forEach()` qui prend donc une _lambda_ sauf que c'est impossible de renvoyer une
exception avec une lambda.