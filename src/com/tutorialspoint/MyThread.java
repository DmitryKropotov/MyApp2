package com.tutorialspoint;

public class MyThread extends Thread {
    private Counter counter;

    public MyThread(Counter counter) {
        this.counter = counter;
    }

    @Override
    public void run() {
        System.out.println("Thread " + Thread.currentThread().getName() + " classNotStaticVariable before reading=" + counter.notStaticVar);
        counter.notStaticVar.incrementAndGet();
        //System.out.println("Thread " + Thread.currentThread().getName() + " classNotStaticVariable after reading=" + counter.notStaticVar);
        //System.out.println("Thread " + Thread.currentThread().getName() + " local=" + local);
        //counter.notStaticVar = local + 1;
        System.out.println("Thread " + Thread.currentThread().getName() + " classNotStaticVariable after modificatioin =" + counter.notStaticVar);

        System.out.println("Thread " + Thread.currentThread().getName() + " classStaticVariable before reading=" + Counter.staticVar);
        int local2 = Counter.staticVar;
        System.out.println("Thread " + Thread.currentThread().getName() + " classStaticVariable after reading=" +  Counter.staticVar);
        System.out.println("Thread " + Thread.currentThread().getName() + " local2=" + local2);
        Counter.staticVar = local2 + 1;
        System.out.println("Thread " + Thread.currentThread().getName() + " classStaticVariable after modification =" +  Counter.staticVar);
    }
}
