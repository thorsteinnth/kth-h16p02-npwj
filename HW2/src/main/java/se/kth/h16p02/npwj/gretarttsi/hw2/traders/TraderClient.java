package se.kth.h16p02.npwj.gretarttsi.hw2.traders;

public class TraderClient
{
    private static final String USAGE = "java bankrmi.TraderClient <bank_url>";
    private static final String DEFAULT_BANK_NAME = "Nordea";

    public TraderClient() {
    }

    public static void main(String[] args)
    {
        if ((args.length > 1) || (args.length > 0 && args[0].equals("-h"))) {
            System.out.println(USAGE);
            System.exit(1);
        }

        String bankName;
        if (args.length > 0)
        {
            bankName = args[0];
            try
            {
                new TraderImpl(bankName).run();
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
        else
        {
            try
            {
                new TraderImpl().run();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }
}