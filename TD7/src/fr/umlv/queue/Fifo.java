package fr.umlv.queue;

import java.util.*;

public class Fifo<E> implements Iterable<E> {
    private final E[] elements;
    private int size;
    private int head;
    private int tail;

    public Fifo(int capacity) {
        if (capacity <= 0)
            throw new IllegalArgumentException("need capacity > 0");
        @SuppressWarnings("unchecked")
        E[] elements = (E[]) new Object[capacity];
        this.elements = elements;
    }

    public void offer(E elem) {
        Objects.requireNonNull(elem);
        if (tail == head && size != 0)
            throw new IllegalStateException("no more capacity");
        elements[tail] = elem;
        tail = (tail + 1) % elements.length;
        size++;
    }

    public E poll() {
        if (tail == head && size == 0)
            throw new IllegalStateException("is empty");
        var last_head = elements[head];
        elements[head] = null;
        head = (head + 1) % elements.length;
        size--;
        return last_head;
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public String toString() {
        var ret = new StringJoiner(", ", "[", "]");
        var h =  head;
        for (var i = 0; i < size; i++) {
            ret.add(elements[h].toString());
            h = (h + 1) % elements.length;
        }
        return ret.toString();
    }

    public Iterator<E> iterator() {
        return new Iterator<>() {
            private int i = 0;
            private int h = head;

            @Override
            public boolean hasNext() {
                return i < size;
            }

            @Override
            public E next() {
                if (!hasNext())
                    throw new NoSuchElementException("no next");
                var elem = elements[h];
                i++;
                h = (h + 1) % elements.length;
                return elem;
            }
        };
    }
}
