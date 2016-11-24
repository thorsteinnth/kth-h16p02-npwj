package se.kth.h16p02.npwj.gretarttsi.hw2.shared.Domain;

import se.kth.h16p02.npwj.gretarttsi.hw2.shared.RemoteInterfaces.Trader;

public class WishListItem {
    private final Trader trader;
    private final Item item;
    //private float specifiedPrice;

    public WishListItem(Trader trader, Item item, float specifiedPrice) {
        this.trader = trader;
        this.item = item;
        //this.specifiedPrice = specifiedPrice;
    }

    public Trader getTrader() {
        return trader;
    }

    public Item getItem() {
        return item;
    }

    /*
    public float getSpecifiedPrice() {
        return specifiedPrice;
    }
    */

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        WishListItem that = (WishListItem) o;

        if (!trader.equals(that.trader)) return false;
        return item.equals(that.item);

    }

    @Override
    public int hashCode() {
        int result = trader.hashCode();
        result = 31 * result + item.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "WishListItem{" +
                "trader=" + trader +
                ", item=" + item +
                '}';
    }
}
