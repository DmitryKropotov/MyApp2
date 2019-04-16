package com.tutorialspoint;

public class Counter {
    int notStaticVar = 0;

    static int staticVar = 0;

    private int notStaticPrivateVar = 0;

    private static int staticPrivateVar = 0;

    public int getNotStaticVar() {
        return notStaticVar;
    }

    public static int getStaticVar() {
        return staticVar;
    }

    public int getNotStaticPrivateVar() {
        return notStaticPrivateVar;
    }

    public void setNotStaticPrivateVar(int notStaticPrivateVar) {
        this.notStaticPrivateVar = notStaticPrivateVar;
    }

    public static int getStaticPrivateVar() {
        return staticPrivateVar;
    }

    public static void setStaticPrivateVar(int staticPrivateVar) {
        Counter.staticPrivateVar = staticPrivateVar;
    }
}
