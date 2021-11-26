package fr.umlv.series;

import java.util.*;
import java.util.function.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class TimeSeries<E> {
    private final List<Data<E>> datas = new ArrayList<>();
    public record Data<E>(long timestamp, E element) {
        public Data {
            Objects.requireNonNull(element);
        }
    }

    public final class Index implements Iterable<Data<E>> {
        private final int[] index;
        private final TimeSeries<E> dis = TimeSeries.this;

        private Index(Predicate<? super E> filter, int capacity) {
            Objects.requireNonNull(filter);
            if (capacity < 0)
                throw new IllegalStateException("capacity must positive");
            index = IntStream.range(0, capacity).filter(i -> filter.test(datas.get(i).element)).toArray();
        }

        private Index(int[] arrays) {
            index = Arrays.copyOf(arrays, arrays.length);
        }

        public int size() {
            return index.length;
        }

        public void forEach(Consumer<? super Data<E>> consumer) {
            Arrays.stream(index).forEach(i -> consumer.accept(datas.get(i)));
        }

        @Override
        public String toString() {
            return Arrays.stream(index).mapToObj(TimeSeries.this::get)
                    .map(d -> d.timestamp() + " | " + d.element())
                    .collect(Collectors.joining("\n"));
        }

        @Override
        public Iterator<Data<E>> iterator() {
            return new Iterator<>() {
                private int i;

                @Override
                public boolean hasNext() {
                    return size() != i;
                }

                @Override
                public Data<E> next() {
                    if (!hasNext()) {
                        throw new NoSuchElementException("no next");
                    }
                    return datas.get(index[i++]);
                }
            };
        }

        public Index or(Index indexOth) {
            if (!dis.equals(indexOth.dis)) {
                throw new IllegalArgumentException("It's not the same TimeSeries");
            }
            return new Index(IntStream.concat(Arrays.stream(index), Arrays.stream(indexOth.index))
                    .sorted()
                    .distinct()
                    .toArray());
        }

        public Index and(Index indexOth) {
            if (!dis.equals(indexOth.dis)) {
                throw new IllegalArgumentException("It's not the same TimeSeries");
            }
            HashSet<Integer> indexSet = Arrays.stream(indexOth.index).boxed().collect(Collectors.toCollection(HashSet<Integer>::new));
            return new Index(Arrays.stream(index).filter(indexSet::contains).toArray());
        }
    }

    public void add(long timestamp, E element) {
        Objects.requireNonNull(element);
        if (!(datas.isEmpty() || timestamp >= datas.get(size() - 1).timestamp)) {
            throw new IllegalStateException(timestamp + " need higher of precedently added");
        }
        datas.add(new Data<>(timestamp, element));
    }

    public int size() {
        return datas.size();
    }

    public Index index() {
        return index(e -> true);
    }

    public Index index(Predicate<? super E> filter) {
        return new Index(filter, size());
    }

    public Data<E> get(int index) {
        var select = datas.get(index);
        return new Data<>(select.timestamp, select.element);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o instanceof TimeSeries<?>) {
            return false;
        }
        TimeSeries<?> that = (TimeSeries<?>) o;
        return Objects.equals(datas, that.datas);
    }

    @Override
    public int hashCode() {
        return Objects.hash(datas);
    }
}
