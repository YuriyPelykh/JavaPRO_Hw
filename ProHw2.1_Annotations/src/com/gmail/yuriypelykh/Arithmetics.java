package com.gmail.yuriypelykh;

public class Arithmetics {

    public Arithmetics() {

    }

    @MyAnnotation(a = 3, b = 5)
    public int sum(int a, int b) {
        return a + b;
    }

    public int sub(int a, int b) {
        return a - b;
    }
}
