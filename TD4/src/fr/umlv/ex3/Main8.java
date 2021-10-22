package fr.umlv.ex3;

import java.util.ArrayList;
import java.util.Scanner;

public class Main8 {
    public static void main(String[] args) {
        var list = new ArrayList<Thread>();
        for (var i = 0; i < 4; i++) {
            list.add(new Thread(() -> {
                Count count = new Count();
                for (;;) {
                    System.out.println(count);
                    count.add();
                    try {
                        Thread.sleep(1_000);
                    } catch (InterruptedException e) {
                        return ;
                    }
                }
            }));
        }
        list.forEach(t -> t.setDaemon(true));
        list.forEach(Thread::start);
        System.out.println("enter a thread id:");
        try(var scanner = new Scanner(System.in)) {
            while(scanner.hasNextInt()) {
                var threadId = scanner.nextInt();
                if (threadId < 0 || threadId >= 4)
                    continue;
                list.get(threadId).interrupt();
            }
        }
    }
}
