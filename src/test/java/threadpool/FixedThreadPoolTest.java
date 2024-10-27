package threadpool;

import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicInteger;

public class FixedThreadPoolTest {
    @Test
    public void testSimpleThreadPool() throws Exception {
        int runnableCount = 10;
        final AtomicInteger count = new AtomicInteger(0);

        FixedThreadPool threadpool = new FixedThreadPool(10);

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
    public void testStop() throws Exception {
        int runnableCount = 6;
        final AtomicInteger count = new AtomicInteger(0);

        FixedThreadPool threadpool = new FixedThreadPool(runnableCount / 2);

        threadpool.start();
        Runnable r = new Runnable() {
            @Override
            public void run() {
                count.getAndIncrement();
                try {
                    Thread.sleep(1_000);
                } catch (Exception e) {}
            }
        };
        for (int i = 0; i < runnableCount; i++) {
            threadpool.execute(r);
        }
        threadpool.stop();
        Assert.assertEquals("All runnables must be executed", runnableCount, count.get());
    }

    @Test
    public void testTerminate() throws Exception {
        int runnableCount = 10;
        final AtomicInteger count = new AtomicInteger(0);

        FixedThreadPool threadpool = new FixedThreadPool(runnableCount / 2);

        threadpool.start();
        Runnable r = new Runnable() {
            @Override
            public void run() {
                count.getAndIncrement();
                try {
                    Thread.sleep(200);
                } catch (Exception e) {
                    //Do nothing
                }
            }
        };
        for (int i = 0; i < runnableCount; i++) {
            threadpool.execute(r);
        }
        threadpool.terminate();
        Assert.assertFalse("Threadpool should terminate without waiting", runnableCount == count.get());
    }
}