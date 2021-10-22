package fr.umlv.ex3;

public class Main2 {
    public static void main(String[] args) throws InterruptedException {
        var thread = new Thread(() -> {
            while(true){
                try {
                    System.out.println("je fais des choses");
                    Thread.sleep(1_000);
                } catch (InterruptedException e) {
                    System.out.println("end");
                    return ;
                }
            }
        });
        thread.start();
        Thread.sleep(1_000);
        thread.interrupt();
    }
}
