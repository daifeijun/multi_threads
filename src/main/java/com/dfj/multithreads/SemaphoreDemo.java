package com.dfj.multithreads;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

/**信号量（Semaphore）主要用于两个目的，一个是用于多个共享资源的互斥使用，另一个用于并发线程数的控制
 *
 */
public class SemaphoreDemo {
    public static void main(String[] args) {
        Semaphore semaphore = new Semaphore(3);
        for (int i = 1; i <= 6; i++) {//模拟6部汽车
            new Thread(() -> {
                try {
                    semaphore.acquire();
                    System.out.println(Thread.currentThread().getName()+"\t 抢到车位！！！");
                    TimeUnit.SECONDS.sleep(3);
                    System.out.println(Thread.currentThread().getName()+"\t 停3秒后离开车位！！！");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    semaphore.release();
                }
            }, String.valueOf(i)).start();
        }
    }
}
