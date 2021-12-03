package fr.umlv.seq;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class Seq<E> implements Iterable<E> {
    private final List<Object> cnt;
    private final Function<? super Object, ? extends E> mapper;

    public Seq(List<? extends E> list) {
        Objects.requireNonNull(list);
        cnt = List.copyOf(list);
        mapper = e -> (E) e;
    }

    public Seq(List<Object> list, Function<? super Object, ? extends E> mapper) {
        Objects.requireNonNull(list);
        Objects.requireNonNull(mapper);
        cnt = List.copyOf(list);
        this.mapper = mapper;
    }

    @SafeVarargs
    public static <T> Seq<T> of(T... elements) {
        Objects.requireNonNull(elements);
        return new Seq<>(List.of(elements));
    }

    public Optional<? extends E> findFirst() {
        return cnt.stream().map(mapper::apply).findFirst();
    }

    public static <T> Seq<T> from(List<? extends T> list) {
        Objects.requireNonNull(list);
        return new Seq<T>(list);
    }

    public int size() {
        return cnt.size();
    }

    public E get(int index) {
        return mapper.apply(cnt.get(index));
    }

    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            private int i;

            @Override
            public boolean hasNext() {
                return cnt.size() != i;
            }

            @Override
            public E next() {
                if (!hasNext())
                    throw new NoSuchElementException();
                return get(i++);
            }

            @Override
            public void forEachRemaining(Consumer<? super E> action) {
                while (hasNext()) {
                    action.accept(get(i++));
                }
            }
        };
    }

    public void forEach(Consumer<? super E> consumer) {
        Objects.requireNonNull(consumer);
        for (var elem : cnt) {
            consumer.accept(mapper.apply(elem));
        }
    }

    public <R> Seq<R> map(Function<? super E, ? extends R> mapper) {
        return new Seq<R>(cnt, mapper.compose(this.mapper));
    }

    @Override
    public Spliterator<E> spliterator() {
        return cnt.stream().map(e -> (E) mapper.apply(e)).spliterator();
    }

    public Stream<E> stream() {
        return StreamSupport.stream(spliterator(), true);
    }

    @Override
    public String toString() {
        return cnt.stream()
                .map(e -> mapper.apply(e).toString())
                .collect(Collectors.joining(", ", "<",">"));
    }
}
