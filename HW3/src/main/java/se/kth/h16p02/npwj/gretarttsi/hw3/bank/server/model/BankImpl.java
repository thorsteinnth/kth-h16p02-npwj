package se.kth.h16p02.npwj.gretarttsi.hw3.bank.server.model;

import se.kth.h16p02.npwj.gretarttsi.hw3.bank.server.database.BankDBException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Map;
import se.kth.h16p02.npwj.gretarttsi.hw3.bank.server.database.BankDAO;
import se.kth.h16p02.npwj.gretarttsi.hw3.marketplace.exceptions.BankAccountNotFoundException;
import se.kth.h16p02.npwj.gretarttsi.hw3.shared.exceptions.InsufficientFundsException;
import se.kth.h16p02.npwj.gretarttsi.hw3.shared.exceptions.RejectedException;
import se.kth.h16p02.npwj.gretarttsi.hw3.shared.remoteInterfaces.Bank;

/**
 * Implementations of the bank's remote methods.
 */
public class BankImpl extends UnicastRemoteObject implements Bank {
    private Map<String, Account> accounts = new HashMap<>();
    private BankDAO bankDb;

    public BankImpl(String datasource, String dbms)
            throws RemoteException, BankDBException {
        super();
        bankDb = new BankDAO(dbms, datasource);
    }

    @Override
    public synchronized String[] listAccounts() {
        return accounts.keySet().toArray(new String[1]);
    }

    @Override
    public synchronized void newAccount(String holderName) throws RejectedException {
        String acctExistsMsg = "Account for: " + holderName + " already exists";
        String failureMsg = "Could not create account for: " + holderName;
        try {
            AccountDTO account = cachedAcct(holderName);
            if (account != null) {
                throw new RejectedException(acctExistsMsg);
            }
            if ((account = bankDb.findAccountByName(holderName)) != null) {
                accounts.put(holderName, (Account) account);
                throw new RejectedException(acctExistsMsg);
            }
            bankDb.createAccount(new Account(holderName, bankDb));
        } catch (BankDBException bdbe) {
            throw new RejectedException(failureMsg, bdbe);
        }
    }

    @Override
    public synchronized AccountDTO getAccount(String holderName) throws BankAccountNotFoundException {
        if (holderName == null) {
            return null;
        }

        Account acct = cachedAcct(holderName);
        if (acct == null) {
            try {
                acct = (Account) bankDb.findAccountByName(holderName);
            } catch (BankDBException exception) {
                throw new BankAccountNotFoundException("Could not find bank account for user: " + holderName);
            }
        }

        if (acct != null) {
            accounts.put(holderName, acct);
        }
        return acct;
    }

    @Override
    public synchronized boolean deleteAccount(AccountDTO account) throws RejectedException {
        boolean deletedFromDB = false;
        try {
            deletedFromDB = bankDb.deleteAccount(account);
            if (deletedFromDB) {
                accounts.remove(account.getHolderName());
            }
        } catch (BankDBException exception) {
            throw new RejectedException("Could not delete account: " + account, exception);
        }
        return deletedFromDB;
    }

    @Override
    public void deposit(AccountDTO acctDTO, int amt) throws RejectedException, BankAccountNotFoundException {
        Account acct = (Account) getAccount(acctDTO.getHolderName());
        acct.deposit(amt);
    }

    @Override
    public void withdraw(AccountDTO acctDTO, int amt) throws RejectedException, BankAccountNotFoundException, InsufficientFundsException {
        Account acct = (Account) getAccount(acctDTO.getHolderName());
        acct.withdraw(amt);
    }

    private Account cachedAcct(String name) {
        return accounts.get(name);
    }

}
