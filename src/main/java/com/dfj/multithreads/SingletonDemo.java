package com.dfj.multithreads;

/**
 *不加volatile存在指令重排的问题
 * 实例化对象的步骤：
 * 1.分配对象内存地址
 * 2.初始化对象
 * 3.此时判断对象不为null
 * 因为2和3步骤不存在依赖关系，当指令重排时可能导致，还没有初始化对象，就去判断对象为null
 */
public class SingletonDemo {
    private static volatile SingletonDemo instance = null;
    private SingletonDemo(){
        System.out.println(Thread.currentThread().getName()+"\t 我是构造方法Singleton");
    }
    //DCL （double check lock 双端检索机制）
    public static SingletonDemo getInstance(){

        if(instance == null){
            synchronized (SingletonDemo.class){
                if (instance == null){
                    instance = new SingletonDemo();
                }
            }
        }
        return instance;
    }
    /*public static SingletonDemo getInstance(){
        if(instance == null){
            instance = new SingletonDemo();
        }
        return instance;
    }*/
    public static void main(String[] args) {
        //单线程(main线程)的单例模式,只打印一次
        /*System.out.println(SingletonDemo.getInstance() == SingletonDemo.getInstance());
        System.out.println(SingletonDemo.getInstance() == SingletonDemo.getInstance());
        System.out.println(SingletonDemo.getInstance() == SingletonDemo.getInstance());*/
        //并发情况下，情况发生了很大的变化
        for (int i = 1; i <= 10; i++) {
            new Thread(()->{
                SingletonDemo.getInstance();
            },String.valueOf(i)).start();
        }
    }
}
