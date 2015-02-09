package org.power.server;

import java.rmi.RemoteException;

import org.power.interf.CountService;

public class CountServiceImpl02 implements CountService
{

    /** serialVersionUID **/
    private static final long serialVersionUID = -2529707203436745059L;
    
    /** 计数器 **/
    public static int count = 0;

    /**
     * 构造方法
     * @throws RemoteException
     */
    public CountServiceImpl02() throws RemoteException
    {
        super();
    }

    public synchronized int increase() throws RemoteException
    {
        System.out.println("The current count is :" + (++count));
        return count;
    }

    public synchronized int decrease() throws RemoteException
    {
        System.out.println("The current count is :" + (--count));
        return count;
    }
    
}
