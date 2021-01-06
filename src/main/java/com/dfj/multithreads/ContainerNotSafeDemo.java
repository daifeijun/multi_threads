package com.dfj.multithreads;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * 线程类不安全的问题
 * ArrayList
 * 1.故障现象
 * java.util.ConcurrentModificationException 并发修改异常
 * 2.导致原因
 * 并发争抢修改导致
 * 3.解决方案
 *   1>new Vector<>()
 *   2>Collections.synchronizedList(new ArrayList<>())
 *   3>new CopyOnWriteArrayList<>(); 写时复制 （map线程不安全时，用ConcurrentHashMap）
 *
 * 4.优化建议
 */
public class ContainerNotSafeDemo {
    public static void main(String[] args) {
        setNotSafe();
    }

    private static void setNotSafe() {
        Set<String> list = new CopyOnWriteArraySet<>();
        for (int i = 1; i <= 30; i++) {
            new Thread(()->{
                list.add(UUID.randomUUID().toString().substring(0,8));
                System.out.println(list);
            },String.valueOf(i)).start();
        }
        new HashSet<>();//底层调用的是hashmap，为什么add时只传一个？hashmap的value是一个PRESENT对象常量
    }

    private static void listNotSafe(){
        List<String> list = new CopyOnWriteArrayList<>();
        for (int i = 1; i <= 30; i++) {
            new Thread(()->{
                list.add(UUID.randomUUID().toString().substring(0,8));
                System.out.println(list);
            },String.valueOf(i)).start();
        }
    }
    /**源码
     *      public boolean add(E e) {
     *         final ReentrantLock lock = this.lock;
     *         lock.lock();
     *         try {
     *             Object[] elements = getArray();
     *             int len = elements.length;
     *             Object[] newElements = Arrays.copyOf(elements, len + 1);
     *             newElements[len] = e;
     *             setArray(newElements);
     *             return true;
     *         } finally {
     *             lock.unlock();
     *         }
     *     }
     */
}
