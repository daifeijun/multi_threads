package com.dfj.multithreads;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 1.CAS是什么？
 * 比较和交换
 * 是一条CPU并发原语
 * 原语的执行必须是连续的，在执行过程中不允许被中断，也就是说CAS是一条CPU的原子指令，不会造成所谓的数据不一致问题
 *
 * 2.unsafe类=》
 * jre/lib/rt.jar/sun/misc/unsafe.class
 * 是CAS核心类，由于java方法无法直接访问底层系统，需要通过本地(native)方法来访问，Unsafe相当于一个后门，基于该类可以直接
 * 操作特定内存的数据。其内部方法操作可以像C的指针一样操作内存，因此java中CAS操作的执行依赖于Unsafe类的方法
 * 注意：在Unsafe类中所有的方法都是用native修饰的，也就是说Unsafe类中的方法都直接调用操作系统底层资源执行相应任务
 *
 * 3.CAS的缺点
 * 1>循环时间长，开销大:如果CAS长时间一直不成功，则会给CPU带来很大的开销
 * 2>只能保证一个共享变量的原子操作
 * 3>ABA问题
 */
public class CASDemo {
    public static void main(String[] args) {
        AtomicInteger atomicInteger = new AtomicInteger(5);
        atomicInteger.compareAndSet(10,11);
        atomicInteger.getAndIncrement();
        System.out.println(atomicInteger.get());

    }
    /**底层代码：自旋锁思想
     * public final int getAndAddInt(Object var1, long var2, int var4) {
     *         int var5;
     *         do {
     *             var5 = this.getIntVolatile(var1, var2);
     *         } while(!this.compareAndSwapInt(var1, var2, var5, var5 + var4));
     *
     *         return var5;
     *     }
     */
}
