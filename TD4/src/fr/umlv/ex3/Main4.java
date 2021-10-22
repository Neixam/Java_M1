package fr.umlv.ex3;

public class Main4 {
    private static int slow() {
        var result = 1;
        for (var i = 0; i < 1_000_000; i++) {
            if (Thread.interrupted()) {
                System.out.println("end");
                throw new AssertionError("Interrupted");
            }
            result += (result * 7) % 513;
        }
        return result;
    }

    public static void main(String[] args) throws InterruptedException {
        var thread = new Thread(() -> {
            var forNothing = 0;
            while (true) {
                forNothing += slow();
                System.out.println(forNothing);
            }
        });
        thread.start();
        Thread.sleep(1_000);
        thread.interrupt();
    }
}
