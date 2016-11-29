package bankjdbc.server.model;

import bankjdbc.server.integration.BankDBException;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface AccountNotUsed extends Remote {

    public float getBalance() throws RemoteException;

    public void deposit(float value) throws RemoteException, BankDBException;

    public void withdraw(float value) throws RemoteException, BankDBException;
}
