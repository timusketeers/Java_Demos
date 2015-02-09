package org.power.client;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import org.power.interf.CountService;

public class Client
{

    /**
     * @param args
     * @throws NotBoundException 
     * @throws RemoteException 
     * @throws MalformedURLException 
     */
    public static void main(String[] args) throws MalformedURLException, RemoteException, NotBoundException
    {
        CountService service01 = (CountService)Naming.lookup("rmi://localhost:19090/CountService01");
        service01.increase();
        service01.increase();
        service01.increase();
        service01.decrease();
        
        
        CountService service02 = (CountService)Naming.lookup("rmi://localhost:19090/CountService02");
        service02.increase();
        service02.increase();
        service02.increase();
        service02.decrease();
        
    }

}
