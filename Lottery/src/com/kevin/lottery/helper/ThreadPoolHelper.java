package com.kevin.lottery.helper;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by xianda on 2016/10/18.
 * 线程池工具类,创建一个自定义线程池
 * 单利设计模式
 */
public class ThreadPoolHelper {

    private ThreadPoolExecutor executor;
    //核心线程数，闲置线程最大数
    private int coreThreadSize = 5;
    //线程池最大线程数
    private int maxThreadSize = 10;
    //闲置线程存活时间
    private long keepAliveTime = 0l;
    private TimeUnit aliveTimeUnit = TimeUnit.MILLISECONDS;
    /**
     * 单利必须
     */
    private ThreadPoolHelper(){
        executor = new ThreadPoolExecutor(coreThreadSize, maxThreadSize, keepAliveTime, aliveTimeUnit
        , new LinkedBlockingQueue<Runnable>(), new ThreadPoolExecutor.AbortPolicy());
    }

    private ThreadPoolHelper INSTANCE;

    public static ThreadPoolHelper newInstace() {
        return ThreadPoolHolder.INSTANCE;
    }

    private static class ThreadPoolHolder {
        public static final ThreadPoolHelper INSTANCE = new ThreadPoolHelper();
    }

    /**
     *  执行线程
     * @param task
     * @return
     */
    public boolean execute(Runnable task) {
        executor.execute(task);
        return true;
    }

    /**
     * 取消线程、任务
     *
     * @return
     */
    public void cancel() {

    }

    public String getTaskInfo(){
        int activeCount = executor.getActiveCount();
        long completedTaskCount = executor.getCompletedTaskCount();
        BlockingQueue<Runnable> queue = executor.getQueue();
        long taskCount = executor.getTaskCount();
        System.out.println(activeCount+"---"+completedTaskCount+"---"+queue+"---"+taskCount);
        return "";
    }

    public BlockingQueue<Runnable> getQueue() {
        return executor.getQueue();
    }
}
