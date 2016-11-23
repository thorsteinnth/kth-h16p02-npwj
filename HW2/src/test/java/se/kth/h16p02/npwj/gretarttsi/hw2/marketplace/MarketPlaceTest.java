package se.kth.h16p02.npwj.gretarttsi.hw2.marketplace;

import se.kth.h16p02.npwj.gretarttsi.hw2.marketPlace.MarketPlaceImpl;
import se.kth.h16p02.npwj.gretarttsi.hw2.marketPlace.TraderAlreadyExistsException;
import se.kth.h16p02.npwj.gretarttsi.hw2.shared.RemoteInterfaces.MarketPlace;
import se.kth.h16p02.npwj.gretarttsi.hw2.shared.RemoteInterfaces.Trader;
import se.kth.h16p02.npwj.gretarttsi.hw2.traders.TraderImpl;

import java.rmi.RemoteException;

public class MarketPlaceTest
{
    public MarketPlaceTest()
    {}

    public static boolean runTests()
    {
        boolean success;
        success = testRegisterTraders();
        return success;
    }

    public static boolean testRegisterTraders()
    {
        try
        {
            MarketPlace marketPlace = new MarketPlaceImpl();

            // Register traders
            Trader trader1 = new TraderImpl("trader1");
            Trader trader2 = new TraderImpl("trader2");

            try
            {
                marketPlace.register(trader1);
                marketPlace.register(trader2);
                marketPlace.register(trader2);
                System.out.println("MarketPlaceTest.testRegisterTraders() - able to register trader2 twice");
                return false;
            }
            catch (TraderAlreadyExistsException ex)
            {
                // We should get an exception for trader2
                System.out.println("Expected exception for trader2: " + ex);
            }

            boolean registrationSuccess = true;
            if (!marketPlace.checkIfRegistered(trader1))
            {
                System.out.println("MarketPlaceTest.testRegisterTraders() - failed to register trader1");
                registrationSuccess = false;
            }
            if (!marketPlace.checkIfRegistered(trader2))
            {
                System.out.println("MarketPlaceTest.testRegisterTraders() - failed to register trader2");
                registrationSuccess = false;
            }
            if (marketPlace.checkIfRegistered(new TraderImpl("trader3")))
            {
                System.out.println("MarketPlaceTest.testRegisterTraders() - trader3 registered when he shouldn't be");
                registrationSuccess = false;
            }

            return registrationSuccess;
        }
        catch (RemoteException ex)
        {
            System.err.println(ex);
            return false;
        }
    }

    public static void main(String[] args)
    {
        boolean testSuccess = runTests();
        System.out.println("MarketPlaceTest - Success: " + testSuccess);
    }
}
