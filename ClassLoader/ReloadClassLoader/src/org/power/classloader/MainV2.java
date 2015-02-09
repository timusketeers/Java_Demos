package org.power.classloader;

import java.lang.reflect.Method;

/**
 * @author li.zhang
 * 2014-2-18
 *
 */
public class MainV2
{

    /**
     * @param args
     * @throws ClassNotFoundException 
     * @throws NoSuchMethodException 
     * @throws SecurityException 
     */
    public static void main(String[] args) throws Exception
    {
        ReloadClassLoaderV2 classLoader = new ReloadClassLoaderV2("lib/test.jar");
        Class<?> clazz1 = classLoader.loadClass("org.power.dao.StudentDao", false);
        
        //����sayHello����
        Method method = clazz1.getMethod("sayHello");
        method.invoke(clazz1);
        
        //����query����
        method = clazz1.getMethod("query");
        method.invoke(clazz1.newInstance());
        
        System.out.println(clazz1.hashCode());
        
        System.out.println("========================================================");
        
        Class<?> clazz2 = classLoader.loadClass("org.power.dao.StudentDao", false);
        
        //����sayHello����
        method = clazz2.getMethod("sayHello");
        method.invoke(clazz2);
        
        //����query����
        method = clazz2.getMethod("query");
        method.invoke(clazz2.newInstance());
        
        
        System.out.println(clazz2.hashCode()); //��������˵��������һ�����ͬһ��class�ļ������յ��ñ��ط��������ڴ��е�Class����ʱ������������ȵ�.
        System.out.println(clazz1 == clazz2); //��������˵��������һ�����ͬһ��class�ļ������յ��ñ��ط��������ڴ��е�Class����ʱ������������ȵ�.

        System.out.println("=========================================================");
        
        Class<?> clazz5 = Class.forName("java.lang.String", true, classLoader);
        System.out.println(clazz5);
        
    }

}
