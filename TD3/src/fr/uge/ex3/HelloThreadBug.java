package fr.uge.ex3;

import java.util.ArrayList;

public class HelloThreadBug {

    public static void main(String[] args) throws InterruptedException {
        var nbThread = 4;
        var list = new ThreadSafeList();
        var threads = new ArrayList<Thread>();
        for (var i = 0; i < nbThread; i++) {
            var thread = new Thread(() -> {
                for (int j = 0; j < 5000; j++)
                    list.add(j);
            });
            thread.setName(i + "");
            threads.add(thread);
        }
        threads.forEach(Thread::start);
        for (var thread : threads) {
            thread.join();
        }
        System.out.println(list.size());
    }
}
