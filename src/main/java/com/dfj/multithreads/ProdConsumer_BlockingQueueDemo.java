package com.dfj.multithreads;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

class MyResource{
    private volatile boolean FLAG = true;//默认开启进行生产消费
    private AtomicInteger atomicInteger = new AtomicInteger();
    BlockingQueue<String> blockingQueue = null;

    public MyResource(BlockingQueue<String> blockingQueue) {
        this.blockingQueue = blockingQueue;
        System.out.println(blockingQueue.getClass().getName());
    }
    public void myProd() throws Exception{
        String data = null;
        boolean retValue;
        while (FLAG){
            data = atomicInteger.incrementAndGet()+"";
            retValue = blockingQueue.offer(data,2, TimeUnit.SECONDS);
            if(retValue){
                System.out.println(Thread.currentThread().getName()+"\t 插入队列"+data+"成功");
            }else{
                System.out.println(Thread.currentThread().getName()+"\t 插入队列"+data+"失败");
            }
            TimeUnit.SECONDS.sleep(1);
        }
        System.out.println(Thread.currentThread().getName()+"\t 大老板叫停了，表示FLAG=false，生产动作结束");

    }
    public void myConsumer()throws Exception{
        String result = null;
        while (FLAG){
            result = blockingQueue.poll(2,TimeUnit.SECONDS);
            if(null == result || result.equalsIgnoreCase("")){
                FLAG = false;
                System.out.println(Thread.currentThread().getName()+"\t 超过2秒钟没有取到，消费退出");
                System.out.println();
                System.out.println();
                System.out.println();
                return;
            }
            System.out.println(Thread.currentThread().getName()+"\t 消费队列"+result+"成功");
        }
    }
    public void stop() throws  Exception{
        this.FLAG = false;
    }
}
public class ProdConsumer_BlockingQueueDemo {
    public static void main(String[] args) throws Exception {
        MyResource myResource = new MyResource(new ArrayBlockingQueue<>(3));
        new Thread(()->{
            System.out.println(Thread.currentThread().getName()+"\t 生产线程启动");
            try {
                myResource.myProd();
            } catch (Exception e) {
                e.printStackTrace();
            }
        },"prod").start();
        new Thread(()->{
            System.out.println(Thread.currentThread().getName()+"\t 消费线程启动");
            try {
                myResource.myConsumer();
                System.out.println();
                System.out.println();
            } catch (Exception e) {
                e.printStackTrace();
            }
        },"consumer").start();
        TimeUnit.SECONDS.sleep(20);
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println("5秒钟时间到，大老板main线程叫停，活动结束");
        myResource.stop();
    }
}
