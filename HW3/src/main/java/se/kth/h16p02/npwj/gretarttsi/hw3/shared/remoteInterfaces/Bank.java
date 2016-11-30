package se.kth.h16p02.npwj.gretarttsi.hw3.shared.remoteInterfaces;

import se.kth.h16p02.npwj.gretarttsi.hw3.bank.exceptions.RejectedException;
import se.kth.h16p02.npwj.gretarttsi.hw3.bank.entities.Account;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Bank extends Remote
{
    public Account newAccount(String ownerName) throws RemoteException, RejectedException;

    public Account findAccount(String ownerName) throws RemoteException;

    public void deleteAccount(String ownerName) throws RemoteException;

    public void deposit(String ownerName, float value) throws RemoteException, RejectedException;

    public void withdraw(String ownerName, float value) throws RemoteException, RejectedException;
}