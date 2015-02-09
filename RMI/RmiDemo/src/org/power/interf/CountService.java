package org.power.interf;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface CountService extends Remote
{
    public int increase() throws RemoteException;
    
    public int decrease() throws RemoteException;
}
