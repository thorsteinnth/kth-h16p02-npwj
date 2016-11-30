package se.kth.h16p02.npwj.gretarttsi.hw3.bank.server.model;

import java.io.Serializable;

/**
 * Specifies a read-only view of n account.
 */
public interface AccountDTO extends Serializable {
    /**
     * @return The balance.
     */
    public int getBalance();

    /**
     * @return The holder's name.
     */
    public String getHolderName();
}
