package threadpool;

import junit.framework.TestCase;
import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.atomic.AtomicInteger;

public class ScalableThreadPoolTest extends TestCase {
    @Test
    public void testSimpleThreadPoolShouldDoAllTasks() throws Exception {
        int runnableCount = 40;
        final AtomicInteger count = new AtomicInteger(0);
        ScalableThreadPool threadpool = new ScalableThreadPool(10, 20);

        threadpool.start();
        Runnable r = new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2_000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                count.getAndIncrement();
            }
        };
        for (int i = 0; i < runnableCount; i++) {
            threadpool.execute(r);
        }
        threadpool.stop();
        Assert.assertEquals("Should work", runnableCount, count.get());
    }
    @Test
    public void testSimpleThreadPoolShouldDoTasksFast() throws Exception {
        int runnableCount = 40;
        final AtomicInteger count = new AtomicInteger(0);
        ScalableThreadPool threadpool = new ScalableThreadPool(10, 20);

        threadpool.start();
        Runnable r = new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2_000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                count.getAndIncrement();
            }
        };

        long startTime = System.nanoTime();
        for (int i = 0; i < runnableCount; i++) {
            threadpool.execute(r);
        }
        long endTime = System.nanoTime();

        threadpool.stop();
        Assert.assertTrue("Execution time should be under 4500 ms", (endTime - startTime) / 1_000_000 <= 4500);
    }
}