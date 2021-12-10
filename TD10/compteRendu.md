# <center>Compte Rendu TP10</center>
<p align="right">BOURENNANE Amine</p>

## Exercice 1
1)
```java
public interface Reversible<E> {
    int size();

    E get(int index);

    static <T> Reversible<T> fromArray(T... elements) {
         if (Arrays.stream(elements).anyMatch(Objects::isNull)) {
             throw new NullPointerException();
         }
         return new Reversible<T>() {
             public int size() {
                 if (Arrays.stream(elements).anyMatch(Objects::isNull)) {
                     throw new NullPointerException();
                 }
                 return elements.length;
             }

             public T get(int index) {
                 if (Arrays.stream(elements).anyMatch(Objects::isNull)) {
                     throw new NullPointerException();
                 }
                 return elements[index];
             }
         };
    }
}
```

2)
```java
public interface Reversible<E> extends Iterable<E> {
...
public Iterator<T> iterator() {
    return new Iterator<T>() {
        private int i;
        @Override
        public boolean hasNext() {
            return size() != i;
        }
        @Override
        public T next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            return elements[i++];
        }
    };
}
```
3)
```java
default Reversible<E> reversed() {
    var rev = this;
    return new Reversible<E>() {
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
```
4)
```java
public Reversible<E> reversed() {
    return rev;
}
```
5)
```java
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
```
6)
```java
default Stream<E> stream() {
    return StreamSupport.stream(new Spliterator<>() {
        private int i;
        @Override
        public boolean tryAdvance(Consumer<? super E> action) {
            if (i < size()) {
                try {
                    action.accept(get(i++));
                    return true;
                } catch (IllegalStateException e) {
                    throw new ConcurrentModificationException();
                }
            }
            return false;
        }

        @Override
        public Spliterator<E> trySplit() {
            return null;
        }

        @Override
        public long estimateSize() {
            return size();
        }

        @Override
        public int characteristics() {
            return NONNULL | ORDERED | SIZED | SUBSIZED;
        }
    }, false);
}
```
7)
```java
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
```