package se.kth.h16p02.npwj.gretarttsi.hw2.marketplace;

import se.kth.h16p02.npwj.gretarttsi.hw2.shared.Exceptions.RejectedException;
import se.kth.h16p02.npwj.gretarttsi.hw2.shared.RemoteInterfaces.Bank;
import se.kth.h16p02.npwj.gretarttsi.hw2.shared.RemoteInterfaces.MarketPlace;
import se.kth.h16p02.npwj.gretarttsi.hw2.shared.RemoteInterfaces.Trader;
import se.kth.h16p02.npwj.gretarttsi.hw2.traders.TraderImpl;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

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
            try
            {
                LocateRegistry.getRegistry(1099).list();
            }
            catch (RemoteException e)
            {
                LocateRegistry.createRegistry(1099);
            }

            Bank bank = (Bank) Naming.lookup("Nordea");
            MarketPlace marketPlace = (MarketPlace)Naming.lookup("MarketPlace");

            // Register traders
            Trader trader1 = new TraderImpl("trader1");
            Trader trader2 = new TraderImpl("trader2");
            Trader trader3 = new TraderImpl("trader3");

            try
            {
                // Create bank accounts
                bank.newAccount(trader1.getUsername());
                bank.newAccount(trader2.getUsername());
                bank.newAccount(trader3.getUsername());

                // Register in marketplace
                marketPlace.register(trader1);
                marketPlace.register(trader2);
                marketPlace.register(trader2);
                System.out.println("MarketPlaceTest.testRegisterTraders() - able to register trader2 twice");
                marketPlace.register(trader3);
                System.out.println("MarketPlaceTest.testRegisterTraders() - able to register trader3 without bank account");
                return false;
            }
            catch (RejectedException ex)
            {
                System.out.println("testRegisterTraders: " + ex);
                return false;
            }
            catch (TraderAlreadyExistsException ex)
            {
                // We should get an exception for trader2
                System.out.println("Expected exception for trader2: " + ex);
            }
            catch (BankAccountNotFoundException ex)
            {
                System.out.println("Expected exception for trader3: " + ex);
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
            if (marketPlace.checkIfRegistered(trader3))
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
        catch (NotBoundException|MalformedURLException ex)
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
