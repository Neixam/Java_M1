package fr.umlv.ex3;

import java.util.concurrent.locks.ReentrantLock;

public class Count {
    private int count;
    private final ReentrantLock lock = new ReentrantLock();

    public void add() {
        lock.lock();
        try {
            count++;
        } finally {
            lock.unlock();
        }
    }

    @Override
    public String toString() {
        lock.lock();
        try {
            return count + "";
        } finally {
            lock.unlock();
        }
    }
}
