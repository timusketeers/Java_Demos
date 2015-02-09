package org.power.classloader;

import java.io.IOException;
import java.io.InputStream;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * ������Ƶ�classLoader�������Ƕ���֮ǰ���ع����࣬�����¼��ظ��࣬������ȡ�Ѿ����صĻ������.
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
     * ����Class.forName("org.power.dao.StudentDao", true, classLoader)�Ĺ�����:
     *   1. JVM������ᴥ��classLoader.loadClass("org.power.dao.StudentDao", boolean resolve)����
     *   2. JVM������ᴥ��classLoader.loadClass("java.lang.Ojbect", boolean resolve)��������ΪObject��StudentDao�ĸ���.
     */
    protected synchronized Class<?> loadClass(String name, boolean resolve)
        throws ClassNotFoundException
    {
        Class<?> c = null;
        try
        {
            //�����Զ�����������ĺ����߼�.
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
     * ����������������������Լ���������߼�.
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
         * ��һ���ǹؼ�...�����е�defineClass()�������ܱ����า�ǣ�����ȴ������������ʹ�ø÷���.
         */
        clasz = super.defineClass(name, bytes, 0, bytes.length);
        
        return clasz;
    }
}
