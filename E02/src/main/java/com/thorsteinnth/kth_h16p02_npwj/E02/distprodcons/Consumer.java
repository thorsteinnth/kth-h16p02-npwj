package com.thorsteinnth.kth_h16p02_npwj.E02.distprodcons;

public class Consumer implements Runnable
{
    RemoteBuffer buffer;
    long sum;

    Consumer(RemoteBuffer buffer)
    {
        this.buffer = buffer;
        sum = 0;
    }

    @Override
    public void run()
    {
        while (true)
        {
            try
            {
                Integer i = buffer.get();
                if (i == null)
                {
                    // we are done
                    break;
                }
                sum = sum + i.intValue();
                System.out.println("Got " + i);

            } catch (java.rmi.RemoteException re)
            {
                re.printStackTrace();
            }
        }
        System.out.println("Total: " + sum);
    }
}

