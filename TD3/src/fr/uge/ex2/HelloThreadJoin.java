package fr.uge.ex2;


import java.util.ArrayList;

public class HelloThreadJoin {
    public static void main(String[] args) throws InterruptedException {
        var threads = new ArrayList<Thread>();
        for (var i = 0; i < 4; i++) {
            var thread = new Thread(() -> {
                for (var j = 0; j < 5000; j++)
                    System.out.println("hello " + Thread.currentThread() + " " + j);
            });
            thread.setName(i + "");
            threads.add(thread);
        }
        threads.forEach(Thread::start);
        for (var thread : threads) {
            thread.join();
        }
        System.out.println("Le programme est finis !");
    }
}
