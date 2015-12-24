package com.mine.base.commom.designpatten.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * project Ants
 *
 * @author hewei
 * @version 15/12/23
 */
public class DynamicProxy implements InvocationHandler{

    private Object object;

    public DynamicProxy(Object object) {
        this.object = object;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Object  result = method.invoke(object, args);
        return result;
    }

    public static void main(String [] args){
        ILawsuit demo = new DemoUser();

        // 被代理实例
        DynamicProxy dynamicProxy = new DynamicProxy(demo);

        ClassLoader classLoader = demo.getClass().getClassLoader();

        // 动态构造代理类
        ILawsuit iLawsuit = (ILawsuit) Proxy.newProxyInstance(classLoader,new Class[]{ILawsuit.class} /*dynamicProxy.getClass().getInterfaces()*/, dynamicProxy);

        iLawsuit.submit();
        iLawsuit.burden();
        iLawsuit.finish();
    }
}
