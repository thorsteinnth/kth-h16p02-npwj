package se.kth.h16p02.npwj.gretarttsi.hw2.shared.Domain;

public class Wish {
    private final int id;
    private final Item item;
    private float specifiedPrice;

    public Wish(int id, Item item, float specifiedPrice) {
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
        return "Wish{" +
                "id=" + id +
                ", item=" + item +
                ", specifiedPrice=" + specifiedPrice +
                '}';
    }
}
