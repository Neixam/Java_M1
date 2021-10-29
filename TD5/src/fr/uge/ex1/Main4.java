package fr.uge.ex1;


import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Main4 {
    public static void createThread(String name, long millis, BlockingQueue<String> buffer) {
        var t = new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(millis);
                    System.out.println("hello " + Thread.currentThread().getName());
                    buffer.put("hello " + Thread.currentThread().getName());
                } catch (InterruptedException e) {
                    return;
                }
            }
        });
        t.setName(name);
        t.start();
    }

    public static void main(String[] args) {
        var buffer = new ArrayBlockingQueue<String>(55);
        createThread("0", 1, buffer);
        createThread("1", 4, buffer);
    }
}
