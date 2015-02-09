package org.power.classloader;

import java.lang.reflect.Method;

/**
 * ��ο�webdoc�ļ����´�ŵ���������.
 * 
 * 1. һ�������ǣ�ÿ�������ص�Class����Ҫ������(link)������ͨ��ִ��ClassLoader.resolve()��ʵ�ֵģ���������� final�ģ�
 * ����޷���д��Resove()����������һ��ClassLoaderʵ��linkһ��Class���Σ���ˣ�������Ҫ���¼���һ�� Class��ʱ��
 * ����Ҫ����Newһ�����Լ���ClassLoaderʵ��. �ղ�˵��һ��Class���ܱ�һ��ClassLoaderʵ����������(����final���͵ķ���findLoadedClass()������Ե��.)��
 * ���ǿ��Ա���ͬ��ClassLoaderʵ������. 
 * 
 * 2.��һ��JavaӦ���У�Class�Ǹ�������ȫ��������+�������ͼ������� ClassLoader��Ψһ��ʶ�ġ�
 * ����,"org.power.dao.StudentDao"����࣬ʹ�ò�ͬ���������"ʵ��"���ɵ�Class��������ͬ�Ķ���,����֮�������������ת����
 * �ǻᱨ����ת���쳣��.
 * 
 * 
 * 3. �����ڶ�̬reloadһ�����ʱ�򣬽��2���׳�������ת���쳣�����⣬���������:
 * ���磺 Ҫreload����"org.power.dao.StudentDao"
 *     <1> StudentDaoʵ��һ���ӿڣ����߸���.
 *     <2> StudentDaoInterface object = (StudentDaoInterface)myClassReloadingFactory.newInstance("org.power.dao.StudentDao");  
 *         StudentDaoSuperclass object = (StudentDao)myClassReloadingFactory.newInstance("org.power.dao.StudentDao");  
 *       ת���ɽӿڻ��߸���.
 *     <3> �Խӿڻ��߸���ִ�и��ֲ���.
 *      
 * �����������������ζ��:
 *      �����Զ���������������loadClass(String name, boolean resolve)�����У�����Ҫ֧��reload���ص���ĸ���͸��ӿ�,
 *   ʹ�ø�����������أ��Դ˱�֤������ֻ������һ�Σ���Ӧһ��Class�� ������Ҫ֧��reload���ص���(��������"org.power.dao.StudentDao")
 *   ���ڼ��ص�ʱ��ÿ����һ���µ����Ƕ��Ƶ�classLoaderʵ�������أ�������OK.
 *  
 * @author li.zhang
 * 2014-2-18
 *
 */
public class Main
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
        Class<?> clazz1 = Class.forName("org.power.dao.StudentDao", true, classLoader);
        
        //����sayHello����
        Method method = clazz1.getMethod("sayHello");
        method.invoke(clazz1);
        
        //����query����
        method = clazz1.getMethod("query");
        method.invoke(clazz1.newInstance());
        
        System.out.println(clazz1.hashCode());
        
        System.out.println("========================================================");
        
        Class<?> clazz2 = Class.forName("org.power.dao.StudentDao", true, classLoader);
        
        //����sayHello����
        method = clazz2.getMethod("sayHello");
        method.invoke(clazz2);
        
        //����query����
        method = clazz2.getMethod("query");
        method.invoke(clazz2.newInstance());
        
        
        System.out.println(clazz2.hashCode()); //��������˵��������һ�����ͬһ��class�ļ������յ��ñ��ط��������ڴ��е�Class����ʱ������������ȵ�.
        System.out.println(clazz1 == clazz2); //��������˵��������һ�����ͬһ��class�ļ������յ��ñ��ط��������ڴ��е�Class����ʱ������������ȵ�.
        
        System.out.println("=========================================================");
        
        Class<?> clazz3 = Class.forName("java.lang.String", true, classLoader);
        System.out.println(clazz3);
        
    }

}
