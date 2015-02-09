package org.power.server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import org.power.interf.CountService;

public class CountServiceImpl01 extends UnicastRemoteObject implements CountService
{

    /** serialVersionUID **/
    private static final long serialVersionUID = -2529707203436745059L;
    
    /** 计数器 **/
    public static int count = 0;

    /**
     * 构造方法
     * @throws RemoteException
     */
    public CountServiceImpl01() throws RemoteException
    {
        super();
    }

    public int increase() throws RemoteException
    {
        synchronized (this)
        {
            System.out.println("server--> The current count is :" + (++count));
        }
        
        return count;
    }

    public int decrease() throws RemoteException
    {
        synchronized (this)
        {
            System.out.println("server--> The current count is :" + (--count));
        }
        return count;
    }
    
}
