package fr.uge.ex2;

import java.util.ArrayDeque;

public class SynchronizedBlockingBuffer<E> {
    private final ArrayDeque<E> arrayDeque;
    private final int capacity;

    public SynchronizedBlockingBuffer(int capacity) {
        if (capacity <= 0) {
            throw new IllegalStateException("capacity must > 0");
        }
        this.arrayDeque = new ArrayDeque<>(capacity);
        this.capacity = capacity;
    }

    public void put(E element) throws InterruptedException {
        synchronized (arrayDeque) {
            while (arrayDeque.size() == capacity) {
                arrayDeque.wait();
            }
            arrayDeque.addLast(element);
            arrayDeque.notifyAll();
        }
    }

    public E take() throws InterruptedException {
        synchronized (arrayDeque) {
            while (arrayDeque.size() == 0) {
                arrayDeque.wait();
            }
            arrayDeque.notifyAll();
            return arrayDeque.removeFirst();
        }
    }
}
