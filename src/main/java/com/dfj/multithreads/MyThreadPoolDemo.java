package com.dfj.multithreads;

import java.util.concurrent.*;

/**线程池做的工作主要是控制运行的线程的数量，处理过程中将任务放入队列，然后在线程创建后启动这些任务，如果线程数量超过了
 * 最大数量，超出数量的线程排队等候，等其他线程执行完毕，再从队列中取出任务来执行
 *
 * 主要特点：线程复用，控制最大并发数，管理线程
 *
 * 1.降低资源消耗，通过重复利用已创建的线程降低线程创建和销毁造成的消耗
 * 2.提高响应速度。当任务到达时，任务可以不需要等到线程创建就能立即执行
 * 3.提高线程的可管理型。线程是稀缺资源，如果无限制的创建，不仅会消耗系统资源，还会降低系统的稳定性，
 * 使用线程池可以进行统一的分配，调优和监控
 *
 * java中获得使用多线程的方法：
 * 1.Executors.newFixedThreadPool(int)
 * 2.Executors.newSingleThreadExecutor()
 * 3.Executors.newCachedThreadPool()
 *
 *  线程池的底层是ThreadPoolExecutor:
 *  七大参数：
 * int corePoolSize,  线程池中的常驻核心线程数
 * int maximumPoolSize,  线程池能够容纳同时执行的最大线程数，此值必须大于等于1
 * long keepAliveTime,  多余的空闲线程的存活时间。
 *                      当前线程池数量超过corePoolSize时，当空闲时间达到keepAliveTime值时，
 *                      多余空闲线程会被销毁直到剩下corePoolSize个线程为止
 * TimeUnit unit,  keepAliveTime的单位
 * BlockingQueue<Runnable> workQueue,  任务队列，被提交但尚未被执行的任务
 * ThreadFactory threadFactory,  表示生成线程池中工作线程的线程工厂，用于创建线程一般用默认的即可
 * RejectedExecutionHandler handler  拒绝策略，表示当队列满了并且工作线程大于等于线程池的
 *                                   最大线程数maximumPoolSize
 *
 * 拒绝策略：
 * AbortPolicy(默认):直接抛出RejectedExecutionException异常阻止系统正常运行
 * CallerRunsPolicy:"调用者运行"一种调节机制，该策略既不会抛弃任务，也不会抛出异常，而是将某些任务回退到调用者
 * DiscardOldestPolicy:抛弃队列中等待最久的任务，然后把当前任务加入队列中尝试再次提交当前任务
 * DiscardPolicy:直接丢弃任务，不予任何处理也不抛出异常，如果允许任务丢失，这是最好的一种方案
 *
 *
 * 阿里巴巴java开发手册：实际工作中不允许用Executors去创建线程池，因为底层使用的是阻塞队列（LinkedBlockingQueue）,
 * 允许的请求队列长度为Integer.MAX_VALUE，可能会堆积大量请求，从而导致OOM
 * 而是通过ThreadPoolExecutor手写一个线程池
 *
 * 合理的配置线程池你是如何考虑的？
 * 先查看服务器的核数 System.out.println(Runtime.getRuntime().availableProcessors());//查看电脑系统核数
 * 1.CPU密集型
 * 该任务需要大量的运算，而没有阻塞，CPU一直全速运行
 * CPU密集任务只有在多核CPU上才可以得到加速（通过多线程）
 * CPU密集型任务配置尽可能少的线程数量
 * 一般公式：CPU核数+1个线程的线程池
 * 2.IO密集型
 * 1>由于IO密集型任务线程并不是一直在执行任务，则应配置尽可能多的线程，如CPU核数*2
 * 2>大部分线程被阻塞，故需要多配置线程数
 * 参考公式：CPU核数/1-阻塞系数   阻塞系数在0.8~0.9直间
 * 比如8核CPU：8/1-0.9=80个线程数
 */

public class MyThreadPoolDemo {
    public static void main(String[] args) {
        ExecutorService threadPool = new ThreadPoolExecutor(
                                            2,
                                            5,
                                            1,
                                            TimeUnit.SECONDS,
                                            new LinkedBlockingQueue<Runnable>(3),
                                            Executors.defaultThreadFactory(),
                                            //new ThreadPoolExecutor.AbortPolicy()
                                            //new ThreadPoolExecutor.CallerRunsPolicy()
                                            new ThreadPoolExecutor.DiscardOldestPolicy()
                                            //new ThreadPoolExecutor.DiscardPolicy()
                                            );
        try {
            for (int i = 1; i <= 20; i++) {
                threadPool.execute(() -> {
                    System.out.println(Thread.currentThread().getName() + "\t 处理业务");
                });
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            threadPool.shutdown();
        }

    }

    private static void baseThreadPool() {
        //System.out.println(Runtime.getRuntime().availableProcessors());//查看电脑系统核数
        //ExecutorService threadPool = Executors.newFixedThreadPool(5);//一池5个处理线程
        ExecutorService threadPool = Executors.newSingleThreadExecutor();//一池1个处理线程
        //ExecutorService threadPool = Executors.newCachedThreadPool();//一池n个处理线程
        //模拟10个用户来办理业务，每个用户就是一个来自外部的请求线程
        try {
            for (int i = 1; i <= 10; i++) {
                threadPool.execute(() -> {
                    System.out.println(Thread.currentThread().getName() + "\t 处理业务");
                });
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            threadPool.shutdown();
        }
    }
}
