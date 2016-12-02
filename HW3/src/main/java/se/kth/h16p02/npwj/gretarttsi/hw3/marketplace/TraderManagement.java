package se.kth.h16p02.npwj.gretarttsi.hw3.marketplace;

import se.kth.h16p02.npwj.gretarttsi.hw3.shared.remoteInterfaces.Trader;

import java.rmi.ConnectException;
import java.rmi.RemoteException;
import java.util.ArrayList;

/**
 * A wrapper for the traders (remote objects).
 * We have a remote trader object for the traders that are logged in.
 * The marketplacerepository can only access those objects through here.
 * We log users out if we get a ConnectException while trying to access them.
 * */
public class TraderManagement
{
    private static ArrayList<Trader> loggedInTraders;

    static
    {
        loggedInTraders = new ArrayList<>();
    }

    public static void loginTrader(Trader trader)
    {
        loggedInTraders.add(trader);
    }

    public static void logoutTrader(Trader trader)
    {
        loggedInTraders.remove(trader);
    }

    /**
     * Get trader by username.
     * Log users out automatically if we get a ConnectException while trying to access them.
     * (we assume that they have gone offline and should therefore be logged out).
     * */
    public static Trader getLoggedInTraderByUsername(String username)
    {
        Trader foundTrader = null;
        ArrayList<Trader> tradersToRemove = new ArrayList<>();

        for (Trader trader : loggedInTraders)
        {
            try
            {
                if (trader.getUsername().equals(username))
                {
                    foundTrader = trader;
                    break;
                }
            }
            catch (RemoteException ex)
            {
                System.err.println(ex);

                if (ex instanceof ConnectException)
                {
                    // We assume we have lost connection to the trader
                    // Let's log him out
                    tradersToRemove.add(trader);
                }
            }
        }

        System.out.println("Logging out " + tradersToRemove.size() + " traders");
        for (Trader traderToRemove : tradersToRemove)
        {
            System.out.println("Logging out trader");
            logoutTrader(traderToRemove);
        }

        return foundTrader;
    }
}
