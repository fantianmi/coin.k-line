package com.bkl.Kcoin.task;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;

public enum SystemTaskPool {
	INSTANCE;
	
	private final static ExecutorService executor = Executors.newFixedThreadPool(3);
	
	private static BlockingQueue<Runnable> task_pool = new LinkedBlockingDeque<Runnable>();

	static {
		Thread thread = new Thread(new SystemTaskRunner());
		thread.start();
	}

	public static void add(Runnable task) {
		task_pool.add(task);
	}

	public static Runnable take() {
		try {
			return task_pool.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return null;
	}

	static class SystemTaskRunner implements Runnable {

		@Override
		public void run() {
			while (true) {
				final Runnable task = SystemTaskPool.take();
				executor.execute(task);
			}
		}
	}
}
