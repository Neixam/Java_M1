package fr.uge.ex2;

public class Main2 {
    public static void createProducer(int id, long millis, LockedBlockingBuffer<String> buffer) {
        var t = new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(millis);
                    buffer.put("hello " + id);
                } catch (InterruptedException e) {
                    return;
                }
            }
        });
        t.start();
    }

    public static void createConsumer(int id, long millis, LockedBlockingBuffer<String> buffer) {
        var t = new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(millis);
                    System.out.println(id + " => " + buffer.take());
                } catch (InterruptedException e) {
                    return;
                }
            }
        });
        t.start();
    }

    public static void main(String[] args) {
        var buffer = new LockedBlockingBuffer<String>(500);
        for (var i = 0; i < 500; i++) {
            createProducer(i, (i + 1) * 2, buffer);
        }
        createConsumer(-1, 10, buffer);
    }
}
