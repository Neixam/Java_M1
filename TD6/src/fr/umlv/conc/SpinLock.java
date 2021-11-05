package fr.umlv.conc;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodHandles.Lookup;
import java.lang.invoke.VarHandle;

public class SpinLock {
  private volatile boolean lock = false;
  private final static VarHandle LOCK_REF;
  static {
    Lookup lookup = MethodHandles.lookup();
    try {
      LOCK_REF = lookup.findVarHandle(SpinLock.class,
              "lock", boolean.class);
    } catch (NoSuchFieldException | IllegalAccessException e) {
      throw new AssertionError(e);
    }
  }

  public void lock() {
    for (;;) {
      if (LOCK_REF.compareAndSet(this, false, true))
        return;
      Thread.onSpinWait();
    }
  }
  
  public void unlock() {
    lock = false;
  }
  
  public static void main(String[] args) throws InterruptedException {
    var runnable = new Runnable() {
      private int counter;
      private final SpinLock spinLock = new SpinLock();
      
      @Override
      public void run() {
        for(int i = 0; i < 1_000_000; i++) {
          spinLock.lock();
          try {
            counter++;
          } finally {
            spinLock.unlock();
          }
        }
      }
    };
    var t1 = new Thread(runnable);
    var t2 = new Thread(runnable);
    t1.start();
    t2.start();
    t1.join();
    t2.join();
    System.out.println("counter " + runnable.counter);
  }
}
