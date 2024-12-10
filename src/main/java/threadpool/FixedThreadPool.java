package threadpool;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

public class FixedThreadPool implements ThreadPool {
    protected final Queue<Runnable> tasks;
    protected volatile boolean isRunning = false;
    protected final List<SimpleThread> threads;

    public FixedThreadPool(int numThreads) {
        if (numThreads <= 0) {
            throw new IllegalArgumentException("numThreads must be greater than 0");
        }
        this.tasks = new ArrayDeque<>();
        this.threads = new ArrayList<>();

        for (int i = 0; i < numThreads; i++) {
            threads.add(new SimpleThread(this));
        }
    }

    public FixedThreadPool() {
        this(Runtime.getRuntime().availableProcessors());
    }

    public boolean isRunning() {
        return isRunning;
    }

    public Queue<Runnable> getTasks() {
        return tasks;
    }

    @Override
    public synchronized void start() {
        if (!isRunning) {
            isRunning = true;
            for (Thread thread : threads) {
                thread.start();
            }
        }
    }

    @Override
    public void execute(Runnable task) {
        if (task == null) {
            return;
        }
        if (!isRunning) {
            throw new IllegalStateException("ThreadPool has been stopped.");
        }
        synchronized (tasks) {
            tasks.add(task);
            tasks.notify();
        }
    }

    public void terminate() {
        synchronized (tasks) {
            tasks.clear();
        }
        stop();
    }

    public void stop() {
        while (hasAnyTasks()) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                throw new ThreadPoolException(e);
            }
        }
        isRunning = false;
        synchronized (tasks) {
            tasks.notifyAll();
        }
        awaitTermination();
    }

    protected boolean hasAnyTasks() {
        synchronized (tasks) {
            return !tasks.isEmpty();
        }
    }

    public void awaitTermination() {
        if (isRunning) {
            throw new IllegalStateException("Threadpool was not terminated before awaiting termination");
        }
        while (true) {
            boolean flag = true;
            for (Thread thread : threads) {
                if (thread.isAlive()) {
                    flag = false;
                    break;
                }
            }
            if (flag) {
                for (SimpleThread thread : threads) {
                    thread.interrupt();
                }
                return;
            }
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                throw new ThreadPoolException(e);
            }
        }
    }
}