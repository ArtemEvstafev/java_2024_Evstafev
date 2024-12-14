package threadpool;

public class SimpleThread extends Thread {
    private boolean isFree = true;
    public FixedThreadPool threadPool = null;

    SimpleThread(FixedThreadPool threadPool) {
        this.threadPool = threadPool;
    }

    @Override
    public void run() {
        Runnable task;
        while (threadPool.isRunning()) {
            synchronized (threadPool.getTasks()) {
                while (threadPool.getTasks().isEmpty() && threadPool.isRunning()) {
                    try {
                        threadPool.getTasks().wait();
                    } catch (InterruptedException e) {
                        System.out.println("Меня убили");
                        Thread.currentThread().interrupt();
                        return;
                    } finally {
                        isFree = true;
                    }
                }
                task = threadPool.getTasks().poll();
                isFree = false;
            }
            if (task != null) {
                task.run();
            }
            isFree = true;
        }
    }

    public boolean isFree() {
        return isFree;
    }
}

