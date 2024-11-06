import threadpool.FixedThreadPool;
import threadpool.ScalableThreadPool;

import java.util.concurrent.atomic.AtomicInteger;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        int numTasks = 20;
        final AtomicInteger count = new AtomicInteger(0);

        FixedThreadPool threadpool = new ScalableThreadPool(2, 4);

        threadpool.start();
        for (int i = 0; i < numTasks; i++) {
            threadpool.execute(() -> {
                try {
                    Thread.sleep(4_000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                System.out.println(count.getAndIncrement());
            });
        }
        threadpool.stop();

        System.out.println("result counter is " + count.get());
    }
}