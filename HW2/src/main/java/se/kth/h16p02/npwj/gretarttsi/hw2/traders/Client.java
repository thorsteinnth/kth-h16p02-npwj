package se.kth.h16p02.npwj.gretarttsi.hw2.traders;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.util.StringTokenizer;
import se.kth.h16p02.npwj.gretarttsi.hw2.shared.RemoteInterfaces.Account;
import se.kth.h16p02.npwj.gretarttsi.hw2.shared.RemoteInterfaces.Bank;
import se.kth.h16p02.npwj.gretarttsi.hw2.shared.Exceptions.RejectedException;


public class Client {
    private static final String USAGE = "java bankrmi.Client <bank_url>";
    private static final String DEFAULT_BANK_NAME = "Nordea";

    public Client() {
    }


    public static void main(String[] args) {
        if ((args.length > 1) || (args.length > 0 && args[0].equals("-h"))) {
            System.out.println(USAGE);
            System.exit(1);
        }

        String bankName;
        if (args.length > 0) {
            bankName = args[0];
            try{
                new TraderImpl(bankName).run();
            }
            catch (Exception e){
                e.printStackTrace();
            }
        } else {
            try{
                new TraderImpl().run();
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}