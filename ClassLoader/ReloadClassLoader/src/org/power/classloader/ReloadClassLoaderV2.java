package org.power.classloader;

import java.io.IOException;
import java.io.InputStream;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * 这个定制的classLoader的作用是对于之前加载过的类，会重新加载该类，而不是取已经加载的缓存的类.
 * 
 * @author li.zhang 2014-2-17
 * 
 */
public class ReloadClassLoaderV2 extends ClassLoader
{
    /** classpath **/
    private String classPath;
    
    public ReloadClassLoaderV2(String jarFile)
    {
        super();
        this.classPath = jarFile;
    }
    
    /**
     * 调用Class.forName("org.power.dao.StudentDao", true, classLoader)的过程是:
     *   1. JVM虚拟机会触发classLoader.loadClass("org.power.dao.StudentDao", boolean resolve)方法
     *   2. JVM虚拟机会触发classLoader.loadClass("java.lang.Ojbect", boolean resolve)方法，因为Object是StudentDao的父类.
     */
    protected synchronized Class<?> loadClass(String name, boolean resolve)
        throws ClassNotFoundException
    {
        Class<?> c = null;
        try
        {
            //我们自定义类加载器的核心逻辑.
            c = findClass(name);
        }
        catch (ClassNotFoundException e)
        {
            
        }
        
        if (c == null)
        {
            c = super.findSystemClass(name);
            System.out.println("system class loader..........." + name);
        }
        else
        {
            System.out.println("our class loader............." + name);
        }
        
        return c;
    }

    /**
     * 这个方法是这个类加载器，自己加载类的逻辑.
     */
    protected Class<?> findClass(String name) throws ClassNotFoundException
    {
        Class<?> clasz = null;
        JarFile jar = null;
        try
        {
            jar = new JarFile(this.classPath);
        }
        catch (IOException e)
        {
            throw new ClassNotFoundException(name);
        }

        byte[] bytes = null;
        String className = name.replace('.', '/');
        className = className + ".class";
        JarEntry entry = jar.getJarEntry(className);
        if (null == entry)
        {
            throw new ClassNotFoundException(name);
        }
        
        InputStream is = null;
        try
        {
            is = jar.getInputStream(entry);
            int len = 0;
            while ((len = is.available()) != 0)
            {
                if (null == bytes)
                {
                    bytes = new byte[len];
                    is.read(bytes, 0, len);
                }
                else
                {
                    byte[] temp = new byte[bytes.length + len];
                    System.arraycopy(bytes, 0, temp, 0, bytes.length);
                    is.read(bytes, bytes.length, len);

                    bytes = temp;
                }
            }
        }
        catch (IOException ex)
        {
            throw new ClassNotFoundException(name);
        }
        finally
        {
            if (null != is)
            {
                try
                {
                    is.close();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        }

        if (null == bytes || 0 == bytes.length)
        {
            throw new ClassNotFoundException(name);
        }

        /**
         * 这一句是关键...父类中的defineClass()方法不能被子类覆盖，但是却可以在子类中使用该方法.
         */
        clasz = super.defineClass(name, bytes, 0, bytes.length);
        
        return clasz;
    }
}
