package se.kth.h16p02.npwj.gretarttsi.hw3.shared.domain;

import se.kth.h16p02.npwj.gretarttsi.hw3.shared.remoteInterfaces.Trader;

import java.io.Serializable;

public class SaleItem implements Serializable
{

    private Item item;
    private Trader trader;

    public SaleItem(Item item, Trader trader)
    {
        this.item = item;
        this.trader = trader;
    }

    public Item getItem()
    {
        return item;
    }

    public Trader getTrader()
    {
        return trader;
    }

    @Override
    public String toString()
    {
        return "SaleItem{" +
                "item=" + item +
                ", trader=" + trader +
                '}';
    }
}
