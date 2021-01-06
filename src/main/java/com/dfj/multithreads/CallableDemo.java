package com.dfj.multithreads;

import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

/**创建线程的方式
 * 1.继承Thread类
 * 2.实现Runnable接口
 * 3.实现Callable<返回值类型>接口（有返回值，会抛异常）
 * 4.线程池
 */
class MyThread implements Runnable{
    @Override
    public void run() {

    }
}
class MyThread2 implements Callable<Integer>{
    @Override
    public Integer call() throws Exception {
        System.out.println("come in********call()");
        return 1024;
    }
}
public class CallableDemo {
    public static void main(String[] args) throws Exception {
        FutureTask<Integer> futureTask = new FutureTask<>(new MyThread2());
        Thread thread = new Thread(futureTask,"AAA");
        Thread thread2 = new Thread(futureTask,"BBB");
        thread.start();
        thread2.start();
        int result01 = 100;
        while (!futureTask.isDone()){

        }
        int result02 = futureTask.get();//建议放在最后
        System.out.println("***********result:"+(result01+result02));
    }
}
