package fr.uge.ex1;

public class Main3 {
    public static void createThread(String name, long millis) {
        var t = new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(millis);
                    System.out.println("hello " + Thread.currentThread().getName());
                } catch (InterruptedException e) {
                    return;
                }
            }
        });
        t.setName(name);
        t.start();
    }

    public static void main(String[] args) {
        createThread("0", 1);
        createThread("1", 4);
    }
}
