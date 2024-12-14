package threadpool;

import static java.lang.Math.max;
import static java.lang.Math.min;

public class ScalableThreadPool extends FixedThreadPool implements ThreadPool {

    private final Integer minNumThread,
                          maxNumThread;

    public ScalableThreadPool(int minNumThread, int maxNumThread) {
        super(minNumThread);
        if(minNumThread > maxNumThread) {
            throw new IllegalArgumentException("minNumThread > maxNumThread");
        }
        this.minNumThread = minNumThread;
        this.maxNumThread = maxNumThread;
    }

    @Override
    public void execute(Runnable task) {
        if (task == null) {return;}
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
                    SimpleThread newThread = new SimpleThread(this);
                    threads.add(newThread);
                    newThread.start();
                }
            }
        }
    }

    private boolean hasAnyFreeThread() {
        synchronized(threads) {
            for (SimpleThread thread : threads) {
                if (thread.isFree()) {
                    return true;
                }
            }
            return false;
        }
    }
}
