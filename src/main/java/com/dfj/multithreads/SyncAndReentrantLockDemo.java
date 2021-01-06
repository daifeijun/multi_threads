package com.dfj.multithreads;

/**
 synchronized和lock有什么区别？用新的lock有什么好处？
 * 1.原始构成
 * synchronized是关键字，属于JVM层面
 *         monitorenter（底层是通过monitor对象来完成，其实wait/notify等方法也依赖于monitor对象只有在同步块或方法中才能调用）
 *         lock是具体类(java.util.concurrent),是api层面的锁
 * 2.使用方法
 * synchronized不需要用户去手动释放锁，当synchronized代码执行完后系统会自动让线程释放对锁的占用
 * ReentrantLock则需要用户去手动释放锁，没有主动释放锁，就有可能导致出现死锁现象，需要lock()和unlock()方法配合try/finally语句块来完成
 * 3.等待是否可中断
 * synchronized不可中断，除非抛出异常或者正常运行完成
 * ReentrantLock可中断，1.设置超时方法tryLock（long timeout,TimeUnit unit）
 *                      2.lockInterruptibly()放代码块中，调用interrupt()方法可中断
 * 4.加锁是否公平
 * synchronized 非公平锁
 * ReentrantLock两者都可以，默认是非公平锁，构造方法可以传入boolean值，true为公平锁
 * 5.锁绑定多个条件condition
 * synchronized没有
 * ReentrantLoc用来实现分组唤醒需要唤醒的线程，可以精准唤醒，而不是像synchronized要么随机唤醒一个要么唤醒全部线程
 */

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 题目：多线程之间按顺序调用，实现A-》B-》C三个线程的启动，要求如下：
 * AA打印5次，BB打印10次，CC打印15次
 * 紧接着
 * AA打印5次，BB打印10次，CC打印15次
 * 。。。
 * 来10轮
 */
class ShareResource{
    private int number=1;//A:1,B:2,C:3
    private Lock lock = new ReentrantLock();
    private Condition c1 = lock.newCondition();
    private Condition c2 = lock.newCondition();
    private Condition c3 = lock.newCondition();
    public void print5(){
        lock.lock();
        try{
            //1.判断
            while(number!=1){
                c1.await();
            }
            //2.干活
            for (int i = 1; i <=5 ; i++) {
                System.out.println(Thread.currentThread().getName()+"\t"+i);
            }
            //3.通知
            number = 2;
            c2.signal();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            lock.unlock();
        }
    }
    public void print10(){
        lock.lock();
        try{
            //1.判断
            while(number!=2){
                c2.await();
            }
            //2.干活
            for (int i = 1; i <= 10; i++) {
                System.out.println(Thread.currentThread().getName()+"\t"+i);
            }
            //3.通知
            number = 3;
            c3.signal();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            lock.unlock();
        }
    }
    public void print15(){
        lock.lock();
        try{
            //1.判断
            while(number!=3){
                c3.await();
            }
            //2.干活
            for (int i = 1; i <= 15; i++) {
                System.out.println(Thread.currentThread().getName()+"\t"+i);
            }
            //3.通知
            number = 1;
            c1.signal();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            lock.unlock();
        }
    }
}
public class SyncAndReentrantLockDemo {
    public static void main(String[] args) {
        ShareResource shareResource = new ShareResource();
        new Thread(()->{
            for (int i = 1; i <= 10; i++) {
                shareResource.print5();
            }
        },"A").start();
        new Thread(()->{
            for (int i = 1; i <= 10; i++) {
                shareResource.print10();
            }
        },"B").start();
        new Thread(()->{
            for (int i = 1; i <= 10; i++) {
                shareResource.print15();
            }
        },"C").start();
    }
}
