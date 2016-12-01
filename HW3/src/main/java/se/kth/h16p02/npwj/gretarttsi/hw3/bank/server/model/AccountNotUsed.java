package se.kth.h16p02.npwj.gretarttsi.hw3.bank.server.model;

import se.kth.h16p02.npwj.gretarttsi.hw3.bank.server.database.BankDBException;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface AccountNotUsed extends Remote {

    public float getBalance() throws RemoteException;

    public void deposit(float value) throws RemoteException, BankDBException;

    public void withdraw(float value) throws RemoteException, BankDBException;
}
