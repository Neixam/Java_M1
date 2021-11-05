package fr.umlv.conc;

import java.util.HashSet;
import java.util.concurrent.atomic.AtomicLong;

public class RandomNumberGeneratorAtomicLong {
  private AtomicLong x = new AtomicLong();
  
  public RandomNumberGeneratorAtomicLong(long seed) {
    if (seed == 0) {
      throw new IllegalArgumentException("seed == 0");
    }
    x.set(seed);
  }
  
  public long next() {  // Marsaglia's XorShift
    return x.getAndUpdate(x -> {
      x ^= x >>> 12;
      x ^= x << 25;
      x ^= x >>> 27;
      return x * 2685821657736338717L;
    });
  }
  
  public static void main(String[] args) throws InterruptedException {
    var set0 = new HashSet<Long>();
    var set1 = new HashSet<Long>();
    var set2 = new HashSet<Long>();
    var rng0 = new RandomNumberGeneratorAtomicLong(1);
    var rng = new RandomNumberGeneratorAtomicLong(1);

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
