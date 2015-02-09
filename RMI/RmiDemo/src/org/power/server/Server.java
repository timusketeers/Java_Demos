package org.power.server;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;

public class Server
{
    /**
     * @param args
     * @throws RemoteException 
     * @throws MalformedURLException 
     */
    public static void main(String[] args) throws RemoteException, MalformedURLException
    {
        //registry服务器
        LocateRegistry.createRegistry(19090);
        
        //方式1，继承UnicastRemoteObject类来导出远程对象到远程服务器.
        CountServiceImpl01 svc01 = new CountServiceImpl01();
        Naming.rebind("rmi://localhost:19090/CountService01", svc01);
        
        //方式2，使用UnicastRemoteObject.exportObject()方法来导出远程对象到远程服务器.
        CountServiceImpl02 svc02 = new CountServiceImpl02();
        UnicastRemoteObject.exportObject(svc02);
        Naming.rebind("rmi://localhost:19090/CountService02", svc02);
        while (true);
    }

}
