package se.kth.h16p02.npwj.gretarttsi.hw3.shared.domain;

import java.io.Serializable;

public class SaleItem implements Serializable
{
    private Item item;
    private String sellerName;
    private String buyerName;
    private boolean sold;

    public SaleItem(Item item, String sellerName)
    {
        this.item = item;
        this.sellerName = sellerName;
        this.buyerName = null;
        this.sold = false;
    }

    public SaleItem(Item item, String sellerName, String buyerName, boolean sold)
    {
        this.item = item;
        this.sellerName = sellerName;
        this.buyerName = buyerName;
        this.sold = sold;
    }

    public Item getItem()
    {
        return item;
    }

    public String getSellerName()
    {
        return sellerName;
    }

    public String getBuyerName()
    {
        return buyerName;
    }

    public boolean isSold()
    {
        return sold;
    }

    @Override
    public String toString()
    {
        return "SaleItem{" +
                "item=" + item +
                ", sellerName='" + sellerName + '\'' +
                ", buyerName='" + buyerName + '\'' +
                ", sold=" + sold +
                '}';
    }
}
