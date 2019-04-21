package com.leyou.test;

import com.leyou.annotation.MyAnnotation;
import org.junit.Test;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * @author zhang
 * @date 2019年04月20日 22:53
 */

public class MyTest {

    public static void main(String[] args) throws IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
        Method test = MyTest.class.getDeclaredMethod("test", null);
        if(test.isAnnotationPresent(MyAnnotation.class)){
            test.invoke(new MyTest());
        }
        System.out.println(MyTest.class.getDeclaredMethods().length);
        System.out.println(MyTest.class.getMethods().length);
    }
    //@MyAnnotation(age = 1)
    public void test(){
        System.out.println(this);
        System.out.println(1111);
    }
    private void test1(){
        System.out.println(this);
        System.out.println(1111);
    }
}
