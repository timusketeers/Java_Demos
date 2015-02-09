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
public class ReloadClassLoader extends ClassLoader
{
    /** classpath **/
    private String classPath;
    
    /**
     * ���췽��
     * @param jarFile ������classpath·��
     */
    public ReloadClassLoader(String jarFile)
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
        
        ClassLoader parent = getParent();
        
        //����,�ø��������������ָ�����ࡣ �Դ˱������jvm�İ�ȫɳ��.
        try
        {
            if (parent != null)
            {
                c = parent.loadClass(name);
            }
        }
        catch (ClassNotFoundException e)
        {
            System.out.println("Parent classloader load class " + name + " fail, then try using current classloader.");
        }

        //���,������������Զ�����������ĺ����߼�.
        if (c == null)
        {
            c = findClass(name);
            System.out.println("our class loader............." + name);
        }
        else
        {
            System.out.println("system class loader..........." + name);
        }

        if (resolve)
        {
            resolveClass(c);
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
        
        try
        {
            InputStream is = jar.getInputStream(entry);
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

//    private Object invokeMethod(final Object obj, final String methodName, final Object... params)
//            throws Exception
//    {
//        return AccessController.doPrivileged(new PrivilegedAction<Object>()
//        {
//            @Override
//            public Object run()
//            {
//                Class<?>[] parameterTypes = null;
//                if (null != params)
//                {
//                    parameterTypes = new Class<?>[params.length];
//                    for (int i = 0; i < params.length; i++)
//                    {
//                        parameterTypes[i] = params[i].getClass();
//                    }
//                }
//                
//                Method method = null;
//                Class<?> clszz = obj.getClass();
//                try
//                {
//                    while (true)
//                    {
//                        method = clszz.getDeclaredMethod(methodName, parameterTypes);
//                        clszz = clszz.getSuperclass();
//                        if (null != method || Object.class == clszz)
//                        {
//                            break;
//                        }
//                    }
//                    
//                    method.setAccessible(true);
//                    return method.invoke(obj, params);
//                    
//                }
//                catch (SecurityException e)
//                {
//                    e.printStackTrace();
//                }
//                catch (NoSuchMethodException e)
//                {
//                    e.printStackTrace();
//                }
//                catch (IllegalArgumentException e)
//                {
//                    e.printStackTrace();
//                }
//                catch (IllegalAccessException e)
//                {
//                    e.printStackTrace();
//                }
//                catch (InvocationTargetException e)
//                {
//                    e.printStackTrace();
//                }
//              
//                return null;
//            }
//            
//        });
//    }
}
