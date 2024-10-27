package threadpool;

import java.util.*;

public class FixedThreadPool implements ThreadPool {
    protected volatile Queue<Runnable> tasks;
    protected volatile Boolean isRunning = true;
    protected volatile List<SimpleThread> threads;

    protected class SimpleThread extends Thread {
        private boolean isFree = true;

        @Override
        public void run() {
            Runnable task;
            while (isRunning) {
                synchronized (tasks) {
                    while (tasks.isEmpty() && isRunning) {
                        try {
                            tasks.wait();
                        } catch (InterruptedException e) {
                            System.out.println("Меня убили");
                            Thread.currentThread().interrupt();
                            return;
                        }
                    }
                    task = tasks.poll();
                    isFree = false;
                }
                if (task != null) {
                    task.run();
                }
            }
        }

        public boolean isFree() {
            return isFree;
        }
    }

    protected class ThreadPoolException extends RuntimeException {
        public ThreadPoolException(Throwable cause) {
            super(cause);
        }
    }

    public FixedThreadPool(int numThreads) {
        if (numThreads <= 0) {
            throw new IllegalArgumentException("numThreads must be greater than 0");
        }
        this.tasks = new LinkedList<>() {
        };
        this.threads = new ArrayList<>();

        for (int i = 0; i < numThreads; i++) {
            threads.add(new SimpleThread());
        }
    }

    public FixedThreadPool() {
        this(Runtime.getRuntime().availableProcessors());
    }

    @Override
    public void start() {
        for (Thread thread : threads) {
            thread.start();
        }
    }

    @Override
    public void execute(Runnable task) {
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