package com.gmail.yuriypelykh;

import java.lang.reflect.Method;

public class Main {
    public static void main(String[] args){
        Container con = new Container();
        Class<?> cls = con.getClass();

        SaveTo st = cls.getAnnotation(SaveTo.class);
        String p = st.path();

        Method[] methods = cls.getDeclaredMethods();
        for (Method method: methods) {
            if(method.isAnnotationPresent(Saver.class)){
                con.save(p);
            }
        }
    }

}
