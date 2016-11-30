package se.kth.h16p02.npwj.gretarttsi.hw3.bank.server.model;

import se.kth.h16p02.npwj.gretarttsi.hw3.bank.server.integration.BankDAO;
import se.kth.h16p02.npwj.gretarttsi.hw3.bank.server.integration.BankDBException;
import se.kth.h16p02.npwj.gretarttsi.hw3.shared.exceptions.InsufficientFundsException;
import se.kth.h16p02.npwj.gretarttsi.hw3.shared.exceptions.RejectedException;

/**
 * An account in the bank.
 */
public class Account implements AccountDTO {
    private static final long serialVersionUID = -8644371780655515273L;

    private int balance;
    private String holderName;
    private transient BankDAO bankDB;

    /**
     * Creates an account for the specified holder with the specified balance.
     *
     * @param holderName The account holder's holderName.
     * @param balance    The initial balance.
     * @param bankDB     The DAO used to store updates to the database.
     */
    public Account(String holderName, int balance, BankDAO bankDB) {
        this.holderName = holderName;
        this.balance = balance;
        this.bankDB = bankDB;
    }

    /**
     * Creates an account for the specified holder with the balance zero.
     *
     * @param holderName The account holder's holderName.
     * @param balance    The initial balance.
     * @param bankDB     The DAO used to store updates to the database.
     */
    public Account(String name, BankDAO bankDB) {
        this(name, 0, bankDB);
    }

    /**
     * Deposits the specified amount.
     *
     * @param amount The amount to deposit.
     * @throws RejectedException If the specified amount is negative, or if unable to perform the
     *                           update.
     */
    public void deposit(int amount) throws RejectedException {
        if (amount < 0) {
            throw new RejectedException(
                    "Tried to deposit negative value, account name: " + holderName
                            + ", illegal value: " + amount);
        }
        changeBalance(balance + amount, "Could not deposit to account " + this);
    }

    /**
     * Withdraws the specified amount.
     *
     * @param amount The amount to withdraw.
     * @throws RejectedException If the specified amount is negative, if the amount is larger than
     *                           the balance, or if unable to perform the update.
     */
    public void withdraw(int amount) throws RejectedException, InsufficientFundsException {
        if (amount < 0) {
            throw new RejectedException("Tried to withdraw negative value, account: " + this
                    + ", illegal value: " + amount);
        }
        if (balance - amount < 0) {
            throw new InsufficientFundsException("Tried to overdraft, account: " + this
                    + ", illegal value: " + amount);
        }
        changeBalance(balance - amount, "Could not withdraw from account " + this);
    }

    private void changeBalance(int newBalance, String failureMsg) throws RejectedException {
        int initialBalance = balance;
        try {
            balance = newBalance;
            bankDB.updateAccount(this);
        } catch (BankDBException bdbe) {
            balance = initialBalance;
            throw new RejectedException(failureMsg, bdbe);
        }
    }

    /**
     * @return The balance.
     */
    public int getBalance() {
        return balance;
    }

    /**
     * @return The holder's name.
     */
    public String getHolderName() {
        return holderName;
    }

    /**
     * @return A string representation of all fields in this object.
     */
    public String toString() {
        StringBuilder stringRepresentation = new StringBuilder();
        stringRepresentation.append("Account: [");
        stringRepresentation.append("holder: ");
        stringRepresentation.append(holderName);
        stringRepresentation.append(", balance: ");
        stringRepresentation.append(balance);
        stringRepresentation.append("]");
        return stringRepresentation.toString();
    }
}
