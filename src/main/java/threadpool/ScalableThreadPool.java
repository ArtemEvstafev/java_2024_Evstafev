package threadpool;

import static java.lang.Math.max;
import static java.lang.Math.min;

public class ScalableThreadPool extends FixedThreadPool implements ThreadPool {

    private final Integer minNumThread,
                          maxNumThread;

    public ScalableThreadPool(int minNumThread, int maxNumThread) {
        super(min(minNumThread, maxNumThread));
        this.minNumThread = min(minNumThread, maxNumThread);
        this.maxNumThread = max(maxNumThread, minNumThread);
    }

    @Override
    public void execute(Runnable task) {
        super.execute(task);
        synchronized (threads) {
            while (!hasAnyTasks() && threads.size() > minNumThread) {
                for (SimpleThread thread : threads) {
                    if (thread.isFree()) {
                        threads.remove(thread);
                        thread.interrupt();
                    }
                    break;
                }
            }

            while (!hasAnyFreeThread() && threads.size() < maxNumThread) {
                for (int i = 0; i < maxNumThread - minNumThread; i++) {
                    SimpleThread newThread = new SimpleThread();
                    threads.add(newThread);
                    newThread.start();
                }
            }
        }
    }

    private synchronized boolean hasAnyFreeThread() {
        for (SimpleThread thread : threads) {
            if (thread.isFree()) {
                return true;
            }
        }
        return false;
    }
}
