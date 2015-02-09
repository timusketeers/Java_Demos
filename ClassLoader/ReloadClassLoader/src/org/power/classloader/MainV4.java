package org.power.classloader;

import java.lang.reflect.Method;

/**
 * @author li.zhang
 * 2014-2-18
 *
 */
public class MainV4
{

    /**
     * @param args
     * @throws ClassNotFoundException 
     * @throws NoSuchMethodException 
     * @throws SecurityException 
     */
    public static void main(String[] args) throws Exception
    {
        ReloadClassLoaderV2 classLoader1 = new ReloadClassLoaderV2("lib/test.jar");
        Class<?> clazz1 = Class.forName("org.power.dao.StudentDao", true, classLoader1);
        
        //����sayHello����
        Method method = clazz1.getMethod("sayHello");
        method.invoke(clazz1);
        
        //����query����
        method = clazz1.getMethod("query");
        method.invoke(clazz1.newInstance());
        
        System.out.println(clazz1.hashCode());
        
        System.out.println("========================================================");
        
        ReloadClassLoaderV2 classLoader2 = new ReloadClassLoaderV2("lib/test.jar");
        Class<?> clazz2 = Class.forName("org.power.dao.StudentDao", true, classLoader2);;
        
        //����sayHello����
        method = clazz2.getMethod("sayHello");
        method.invoke(clazz2);
        
        //����query����
        method = clazz2.getMethod("query");
        method.invoke(clazz2.newInstance());
        
        
        System.out.println(clazz2.hashCode()); //��������˵��������һ�����ͬһ��class�ļ������յ��ñ��ط��������ڴ��е�Class����ʱ������������ȵ�.
        System.out.println(clazz1 == clazz2); //��������˵��������һ�����ͬһ��class�ļ������յ��ñ��ط��������ڴ��е�Class����ʱ������������ȵ�.

        System.out.println("=========================================================");
        
    }

}
