package com.dfj.multithreads;
/**
 * 解决ABA问题
 * 尽管线程one的CAS的操作成功，但不代表操作过程没有问题
 *
 * 原子引用解决ABA问题
 */

import java.util.concurrent.atomic.AtomicReference;
class User{
    String username;
    int age;

    public String getUsername() {
        return username;
    }

    public int getAge() {
        return age;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", age=" + age +
                '}';
    }

    public User(String username, int age) {
        this.username = username;
        this.age = age;
    }
}
public class AtomicReferenceDemo {
    public static void main(String[] args) {
        User z3 = new User("z3",22);
        User l4 = new User("l4",28);
        AtomicReference<User> atomicReference = new AtomicReference<>();
        atomicReference.set(z3);
        System.out.println(atomicReference.compareAndSet(z3,l4)+"\t"+atomicReference.get().toString());
        System.out.println(atomicReference.compareAndSet(z3,l4)+"\t"+atomicReference.get().toString());
    }
}
