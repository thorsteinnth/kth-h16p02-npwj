package se.kth.h16p02.npwj.gretarttsi.hw2.shared.Domain;

public class WishListItem {
    private final int id;
    private final Item item;
    private float specifiedPrice;

    public WishListItem(int id, Item item, float specifiedPrice) {
        this.id = id;
        this.item = item;
        this.specifiedPrice = specifiedPrice;
    }

    public int getId(){
        return this.id;
    }

    public Item getName(){
        return this.item;
    }

    public float getSpecifiedPrice(){
        return this.specifiedPrice;
    }

    @Override
    public String toString()
    {
        return "WishListItem{" +
                "id=" + id +
                ", item=" + item +
                ", specifiedPrice=" + specifiedPrice +
                '}';
    }
}
