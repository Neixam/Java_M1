package fr.uge.ex1;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class Main5 {
    public static void createProducer(String name, long millis, BlockingQueue<String> buffer) {
        var t = new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(millis);
                    buffer.put("hello " + Thread.currentThread().getName());
                } catch (InterruptedException e) {
                    return;
                }
            }
        });
        t.setName(name);
        t.start();
    }

    public static void createConsumer(String name, long millis, BlockingQueue<String> buffer) {
        var t = new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(millis);
                    System.out.println(Thread.currentThread().getName() + " => " + buffer.take());
                } catch (InterruptedException e) {
                    return;
                }
            }
        });
        t.setName(name);
        t.start();
    }

    public static void main(String[] args) {
        var buffer = new ArrayBlockingQueue<String>(500);
        for (var i = 0; i < 500; i++) {
            createProducer(i + "", (i + 1) * 2, buffer);
        }
        createConsumer("3", 10, buffer);
    }
}
