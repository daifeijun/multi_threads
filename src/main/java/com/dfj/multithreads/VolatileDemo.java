package com.dfj.multithreads;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
/**
 * volatile是java虚拟机提供的轻量级的同步机制
 * 三大特性：
 * 1.保证可见性
 * 2.不保证原子性
 * 3.禁止指令重排
 */
/**
 * JMM模型：
 * 保证线程安全->
 * 1.可见性
 * 2.原子性
 * 3.有序性
 */
class Mydata{
    volatile int number = 0;

    //int number = 0; 不加volatile，main线程一直循环，没有得到number的最新值
    public void addTo60(){
        this.number = 60;
    }
    public void addPlus(){
        number++;
    }
    AtomicInteger atomicInteger = new AtomicInteger();
    public void addAtomic(){
        atomicInteger.getAndIncrement();
    }
}
/*验证volatile的可见性 */
public class VolatileDemo {
    public static void main(String[] args) {
        //seeOkByVolatile();
        //atomicTest();
        Mydata mydata = new Mydata();
        for (int i = 1; i <= 20; i++) {
            new Thread(()->{
                for (int j = 1; j <= 1000; j++) {
                    mydata.addPlus();
                    mydata.addAtomic();
                }
            },String.valueOf(i)).start();
        }
        while (Thread.activeCount() > 2){
            Thread.yield();
        }
        System.out.println("当前值："+mydata.number);
        System.out.println("当前atomicInteger值："+mydata.atomicInteger);

    }

    private static void atomicTest() {
        /*volatile不保证原子性的原因，可能某一时刻，多个线程同时获得变量值，当其中某线程+1后写回主内存，其他线程
        * 先被挂起，再进行写回主内存时，覆盖之前线程写回主内存的值，导致了数据丢失的情况*/
        Mydata mydata = new Mydata();
        for (int i = 1; i <= 20; i++) {
            new Thread(()->{
                for (int j = 1; j <= 1000; j++) {
                    mydata.addPlus();
                }
            },String.valueOf(i)).start();
        }
        while (Thread.activeCount() > 2){
            Thread.yield();
        }
        System.out.println("当前值："+mydata.number);
    }

    private static void seeOkByVolatile() {
        Mydata mydata = new Mydata();
        new Thread(()->{
            System.out.println(Thread.currentThread().getName()+"\t come in");
            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            mydata.addTo60();
            System.out.println(Thread.currentThread().getName()+"\t update number value:"+mydata.number);
        },"AAA").start();

        while (mydata.number == 0){

        }
        System.out.println(Thread.currentThread().getName()+"\t mission overs");
    }
    /**如何解决原子性
     * 1.加sync
     * 2.AtomicInteger
     * */

}
