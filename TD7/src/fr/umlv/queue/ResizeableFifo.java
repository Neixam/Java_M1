package fr.umlv.queue;

import java.util.*;

public class ResizeableFifo<E> implements Iterable<E>, Queue<E> {
    private E[] elements;
    private int size;
    private int head;
    private int tail;

    public ResizeableFifo(int capacity) {
        if (capacity <= 0)
            throw new IllegalArgumentException("need capacity > 0");
        @SuppressWarnings("unchecked")
        E[] elements = (E[]) new Object[capacity];
        this.elements = elements;
    }

    @Override
    public boolean add(E e) {
        return false;
    }

    @Override
    public boolean remove(Object o) {
        return false;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return false;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        return false;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return false;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return false;
    }

    @Override
    public void clear() {

    }

    public boolean offer(E elem) {
        Objects.requireNonNull(elem);
        if (size == elements.length)
            resizeElements();
        elements[tail] = elem;
        tail = (tail + 1) % elements.length;
        size++;
        return true;
    }

    @Override
    public E remove() {
        return null;
    }

    private void resizeElements() {
        @SuppressWarnings("unchecked")
        E[] copy = (E[]) new Object[size * 2];
        System.arraycopy(elements, head, copy, 0, size - head);
        System.arraycopy(elements, 0, copy, head, tail);
        elements = copy;
        head = 0;
        tail = size;
    }

    public E poll() {
        if (size == 0)
            return null;
        var last_head = elements[head];
        elements[head] = null;
        head = (head + 1) % elements.length;
        size--;
        return last_head;
    }

    @Override
    public E element() {
        return null;
    }

    @Override
    public E peek() {
        return null;
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean contains(Object o) {
        return false;
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

    @Override
    public Object[] toArray() {
        return new Object[0];
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return null;
    }
}
