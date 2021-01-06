package com.dfj.multithreads;

import java.util.concurrent.TimeUnit;

/**死锁是指两个或两个以上的进程在执行过程中，因争夺资源而造成的一种互相等待的现象，若无外力干涉，那它们将无法推进下去，
 * 如果系统资源不足，进程的资源请求都能够得到满足，死锁出现的可能性就很低，否则就会因争夺有限的资源而陷入死锁
 *
 */
class Hold_LockThread implements Runnable{
    private String lockA;
    private String lockB;

    public void setLockA(String lockA) {
        this.lockA = lockA;
    }

    public void setLockB(String lockB) {
        this.lockB = lockB;
    }

    public String getLockA() {
        return lockA;
    }

    public String getLockB() {
        return lockB;
    }

    public Hold_LockThread(String lockA, String lockB) {
        this.lockA = lockA;
        this.lockB = lockB;
    }

    @Override
    public void run() {
        synchronized (lockA){
            System.out.println(Thread.currentThread().getName()+"\t 自己持有:"+lockA+"，尝试获得："+lockB);
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            synchronized (lockB){
                System.out.println(Thread.currentThread().getName()+"\t 自己持有:"+lockB+"，尝试获得："+lockA);
            }
        }
    }
}
public class DeadLockDemo {
    public static void main(String[] args) {
        String lockA="lockA";
        String lockB="lockB";
        new Thread(new Hold_LockThread(lockA,lockB),"AAA").start();
        new Thread(new Hold_LockThread(lockB,lockA),"BBB").start();
    }
}
