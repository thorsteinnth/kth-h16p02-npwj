package se.kth.h16p02.npwj.gretarttsi.hw2.shared.RemoteInterfaces;

import se.kth.h16p02.npwj.gretarttsi.hw2.shared.Exceptions.RejectedException;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Bank extends Remote
{
    Account newAccount(String name) throws RemoteException, RejectedException;

    Account getAccount(String name) throws RemoteException;

    boolean deleteAccount(String name) throws RemoteException;

    String[] listAccounts() throws RemoteException;
}
