package se.kth.h16p02.npwj.gretarttsi.hw2.shared.RemoteInterfaces;

import se.kth.h16p02.npwj.gretarttsi.hw2.bank.InsufficientFundsException;
import se.kth.h16p02.npwj.gretarttsi.hw2.shared.Exceptions.RejectedException;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Account extends Remote
{
    float getBalance() throws RemoteException;

    void deposit(float value) throws RemoteException, RejectedException;

    void withdraw(float value) throws RemoteException, RejectedException, InsufficientFundsException;
}
