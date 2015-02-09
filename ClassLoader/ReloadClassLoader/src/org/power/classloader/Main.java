package org.power.classloader;

import java.lang.reflect.Method;

/**
 * 请参考webdoc文件夹下存放的网上资料.
 * 
 * 1. 一个问题是，每个被加载的Class都需要被链接(link)，这是通过执行ClassLoader.resolve()来实现的，这个方法是 final的，
 * 因此无法重写。Resove()方法不允许一个ClassLoader实例link一个Class两次，因此，当你需要重新加载一个 Class的时候，
 * 你需要重新New一个你自己的ClassLoader实例. 刚才说到一个Class不能被一个ClassLoader实例加载两次(这是final类型的方法findLoadedClass()方法的缘故.)，
 * 但是可以被不同的ClassLoader实例加载. 
 * 
 * 2.在一个Java应用中，Class是根据它的全名（包名+类名）和加载它的 ClassLoader来唯一标识的。
 * 例如,"org.power.dao.StudentDao"这个类，使用不同的类加载器"实例"生成的Class是两个不同的对象,他们之间如果进行类型转换，
 * 是会报类型转换异常的.
 * 
 * 
 * 3. 所以在动态reload一个类的时候，解决2中抛出的类型转换异常的问题，解决方案是:
 * 例如： 要reload加载"org.power.dao.StudentDao"
 *     <1> StudentDao实现一个接口，或者父类.
 *     <2> StudentDaoInterface object = (StudentDaoInterface)myClassReloadingFactory.newInstance("org.power.dao.StudentDao");  
 *         StudentDaoSuperclass object = (StudentDao)myClassReloadingFactory.newInstance("org.power.dao.StudentDao");  
 *       转换成接口或者父类.
 *     <3> 对接口或者父类执行各种操作.
 *      
 * 上面的这个解决方案意味着:
 *      我们自定义的类加载器在其loadClass(String name, boolean resolve)方法中，对于要支持reload加载的类的父类和父接口,
 *   使用父类加载器加载，以此保证父类型只被加载一次，对应一个Class。 而对于要支持reload加载的类(本例中是"org.power.dao.StudentDao")
 *   ，在加载的时候，每次用一个新的我们定制的classLoader实例来加载，这样就OK.
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
        
        //调用sayHello方法
        Method method = clazz1.getMethod("sayHello");
        method.invoke(clazz1);
        
        //调用query方法
        method = clazz1.getMethod("query");
        method.invoke(clazz1.newInstance());
        
        System.out.println(clazz1.hashCode());
        
        System.out.println("========================================================");
        
        Class<?> clazz2 = Class.forName("org.power.dao.StudentDao", true, classLoader);
        
        //调用sayHello方法
        method = clazz2.getMethod("sayHello");
        method.invoke(clazz2);
        
        //调用query方法
        method = clazz2.getMethod("query");
        method.invoke(clazz2.newInstance());
        
        
        System.out.println(clazz2.hashCode()); //这里例子说明，对于一个类的同一个class文件，最终调用本地方法生成内存中的Class对象时，他们总是相等的.
        System.out.println(clazz1 == clazz2); //这里例子说明，对于一个类的同一个class文件，最终调用本地方法生成内存中的Class对象时，他们总是相等的.
        
        System.out.println("=========================================================");
        
        Class<?> clazz3 = Class.forName("java.lang.String", true, classLoader);
        System.out.println(clazz3);
        
    }

}
