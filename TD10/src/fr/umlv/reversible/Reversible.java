package fr.umlv.reversible;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public interface Reversible<E> extends Iterable<E> {
    int size();

    E get(int index);

    default Reversible<E> reversed() {
        var rev = this;
        return new Reversible<>() {
            @Override
            public Reversible<E> reversed() {
                return rev;
            }

            @Override
            public int size() {
                return rev.size();
            }

            @Override
            public E get(int index) {
                return rev.get((size() - 1) - index);
            }
        };
    }

    @SafeVarargs
    static <T> Reversible<T> fromArray(T... elements) {
        return fromList(Arrays.asList(elements));
    }

    static <T> Reversible<T> fromList(List<? extends T> elements) {
        Objects.requireNonNull(elements);
        if (elements.stream().anyMatch(Objects::isNull)) {
            throw new NullPointerException();
        }
        var size = elements.size();
        return new Reversible<>() {
            public int size() {
                return size;
            }

            public T get(int index) {
                Objects.checkIndex(index, size);
                if (size > elements.size())
                    throw new IllegalStateException();
                return Objects.requireNonNull(elements.get(index));
            }
        };
    }

    private <T> Spliterator<T> fromReversible(int start, int end, Reversible<T> reversible) {
        return new Spliterator<T>() {
            private int i = start;
            @Override
            public boolean tryAdvance(Consumer<? super T> action) {
                if (i >= end) {
                    return false;
                }
                try {
                    action.accept(reversible.get(i++));
                    return true;
                } catch (IllegalStateException e) {
                    throw new ConcurrentModificationException();
                }
            }

            @Override
            public Spliterator<T> trySplit() {
                var middle = (i + end) >>> 1;
                if (middle == i) {
                    return null;
                }
                var spliterator = fromReversible(i, middle, reversible);
                i = middle;
                return spliterator;
            }

            @Override
            public long estimateSize() {
                return end - i;
            }

            @Override
            public int characteristics() {
                return NONNULL | ORDERED | SIZED | SUBSIZED;
            }
        };
    }

    default Stream<E> stream() {
        return StreamSupport.stream(fromReversible(0, size(), this), false);
    }

    @Override
    default Iterator<E> iterator() {
        return new Iterator<>() {
            private int i;
            @Override
            public boolean hasNext() {
                return size() > i;
            }

            @Override
            public E next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                try {
                    return get(i++);
                } catch (IllegalStateException e) {
                    throw new ConcurrentModificationException();
                }
            }
        };
    }
}
