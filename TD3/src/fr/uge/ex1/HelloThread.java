package fr.uge.ex1;

public class HelloThread {
    public static void main(String[] args) {
        for (int i = 0; i < 4; i++) {
            var thread = new Thread(() -> {
                for (int j = 0; j <= 5000; j++)
                    System.out.println("hello " + Thread.currentThread().getName() + " " + j);
            });
            thread.setName(i + "");
            thread.start();
        }
    }
}
