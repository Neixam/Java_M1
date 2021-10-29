package fr.uge.ex2;

import java.util.ArrayDeque;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

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
