# <center>Compte Rendu Tp3</center>
<p align="right">BOURENNANE Amine</p>

## Exercice 1
1) Le problème résolu par le design pattern _Producer/Consumer_ est celui de la répartition des tâches pour chaque thread. On utilise une interface, buffer, entre les
threads qui donnent une tâche et ceux qui s'en occupe qui va stopper les threads qui donnent des tâches si celui est plein et stopper les threads qui les récupère
quand celui-ci est vide.
2) Les méthodes qui permettent respectivement à mettre une valeur dans le buffer et d'en retirer sont `put()` et `take()`.
3)
```java
public class Main3 {
    public static void main(String[] args) {
        var t = new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(1);
                    System.out.println("hello");
                } catch (InterruptedException e) {
                    return;
                }
            }
        });
        t.start();
    }
}
```
```java
public class Main3 {
    public static void createThread(String name, long millis) {
        var t = new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(millis);
                    System.out.println("hello " + Thread.currentThread().getName());
                } catch (InterruptedException e) {
                    return;
                }
            }
        });
        t.setName(name);
        t.start();
    }

    public static void main(String[] args) {
        createThread("0", 1);
        createThread("1", 4);
    }
}
```
4) Si l'on implante avec une _LinkedBlockingQueue_ on observe que si on ne lui donne pas une borne maximal, on le fait exploser en mémoire. Tandis qu'avec la _ArrayBlockingQueue_ on est obligé de lui donner une borne max. On peut conclure que la seconde est plus safe que la première car celle-ci n'est pas toujours cohérente.
```java
public class Main4 {
    public static void createThread(String name, long millis, BlockingQueue<String> buffer) {
        var t = new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(millis);
                    System.out.println("hello " + Thread.currentThread().getName());
                    buffer.put("hello " + Thread.currentThread().getName());
                } catch (InterruptedException e) {
                    return;
                }
            }
        });
        t.setName(name);
        t.start();
    }

    public static void main(String[] args) {
        var buffer = new ArrayBlockingQueue<String>(3);
        createThread("0", 1, buffer);
        createThread("1", 4, buffer);
    }
}
```
5)
```java
public class Main5 {
    public static void createProducer(String name, long millis, BlockingQueue<String> buffer) {
        var t = new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(millis);
                    buffer.put("hello " + Thread.currentThread().getName());
                } catch (InterruptedException e) {
                    return;
                }
            }
        });
        t.setName(name);
        t.start();
    }

    public static void createConsumer(String name, long millis, BlockingQueue<String> buffer) {
        var t = new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(millis);
                    System.out.println(Thread.currentThread().getName() + " => " + buffer.take());
                } catch (InterruptedException e) {
                    return;
                }
            }
        });
        t.setName(name);
        t.start();
    }

    public static void main(String[] args) {
        var buffer = new ArrayBlockingQueue<String>(500);
        for (int i = 0; i < 500; i++) {
            createProducer(i + "", (i + 1) * 2, buffer);
        }
        createConsumer("3", 10, buffer);
    }
}
```

## Exercice 2

1)
```java
public class SynchronizedBlockingBuffer<E> {
    private final ArrayDeque<E> arrayDeque = new ArrayDeque<>();
    private final int capacity;

    SynchronizedBlockingBuffer(int capacity) {
        if (capacity <= 0) {
            throw new IllegalStateException("capacity must > 0");
        }
        this.capacity = capacity;
    }

    public void put(E element) throws  {
        synchronized (arrayDeque) {
            if (arrayDeque.size() == capacity)
                throw new IllegalStateException();
            arrayDeque.addLast(element);
        }
    }

    public E take() throws InterruptedException {
        synchronized (arrayDeque) {
            if (arrayDeque.size() == 0)
                throw new IllegalStateException();
            return arrayDeque.removeFirst();
        }
    }
}
```
2)
```java
public void put(E element) throws InterruptedException {
    synchronized (arrayDeque) {
        while (arrayDeque.size() <= capacity) {
            arrayDeque.wait();
        }
        arrayDeque.addLast(element);
        arrayDeque.notifyAll();
    }
}
```
3)
```java
public E take() throws InterruptedException {
    synchronized (arrayDeque) {
        while (arrayDeque.size() <= 0) {
            arrayDeque.wait();
        }
        arrayDeque.notifyAll();
        return arrayDeque.removeFirst();
    }
}
```
4)
```java
public class LockedBlockingBuffer<E> {
    private final ArrayDeque<E> arrayDeque;
    private final int capacity;
    private final ReentrantLock lock = new ReentrantLock();
    private final Condition isEmpty = lock.newCondition();
    private final Condition isFull = lock.newCondition();

    public LockedBlockingBuffer(int capacity) {
        if (capacity <= 0) {
            throw new IllegalStateException("capacity must > 0");
        }
        this.arrayDeque = new ArrayDeque<>(capacity);
        this.capacity = capacity;
    }

    public void put(E element) throws InterruptedException {
        lock.lock();
        try {
            while (arrayDeque.size() == capacity) {
                isFull.await();
            }
            arrayDeque.addLast(element);
            isEmpty.signalAll();
        } finally {
            lock.unlock();
        }
    }

    public E take() throws InterruptedException {
        lock.lock();
        try {
            while (arrayDeque.size() == 0) {
                isEmpty.await();
            }
            isFull.signalAll();
            return arrayDeque.removeFirst();
        } finally {
            lock.unlock();
        }
    }
}
```