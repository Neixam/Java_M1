package fr.umlv.conc;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodHandles.Lookup;
import java.lang.invoke.VarHandle;
import java.util.HashSet;

public class RandomNumberGeneratorVarHandler {
    private volatile long x;
    private final static VarHandle X_REF;
    static {
        Lookup lookup = MethodHandles.lookup();
        try {
            X_REF = lookup.findVarHandle(RandomNumberGeneratorVarHandler.class,
                    "x", long.class);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new AssertionError(e);
        }
    }

    public RandomNumberGeneratorVarHandler(long seed) {
        if (seed == 0) {
            throw new IllegalArgumentException("seed == 0");
        }
        X_REF.set(this, seed);
    }

    public long next() {  // Marsaglia's XorShift
        for (;;) {
            var current = this.x;
            var newValue = current;
            newValue ^= newValue >>> 12;
            newValue ^= newValue << 25;
            newValue ^= newValue >>> 27;
            if (X_REF.compareAndSet(this, current, newValue))
                return newValue;
        }
    }

    public static void main(String[] args) throws InterruptedException {
        var set0 = new HashSet<Long>();
        var set1 = new HashSet<Long>();
        var set2 = new HashSet<Long>();
        var rng0 = new RandomNumberGeneratorVarHandler(1);
        var rng = new RandomNumberGeneratorVarHandler(1);

        for (var i = 0; i < 10_000; i++) {
            set0.add(rng0.next());
        }

        var t = new Thread(() -> {
            for (var i = 0; i < 5_000; i++) {
                set1.add(rng.next());
            }
        });
        t.start();
        for (var i = 0; i < 5_000; i++) {
            set2.add(rng.next());
        }
        t.join();

        System.out.println("set1: " + set1.size() + ", set2: " + set2.size());

        set1.addAll(set2);
        System.out.println("union (should be 10 000): " + set1.size());

        System.out.println("intersection (should be true): " + set0.containsAll(set1));

        set0.removeAll(set1);
        System.out.println("intersection (should be 0): " + set0.size());
    }
}