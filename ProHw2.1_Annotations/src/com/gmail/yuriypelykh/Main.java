package com.gmail.yuriypelykh;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) throws InvocationTargetException, IllegalAccessException {
        Arithmetics ar = new Arithmetics();
        Class<?> cls = ar.getClass();

        Method[] methods = cls.getDeclaredMethods();
        for (Method method: methods) {
            if(method.isAnnotationPresent(MyAnnotation.class)){
                MyAnnotation an = method.getAnnotation(MyAnnotation.class);
                int result = (Integer) method.invoke(ar, an.a(), an.b());
                System.out.println("Result of " + an.a() + " and " + an.b() + " " + method.getName() + " is " + result);
            }
        }
    }
}
