package se.kth.h16p02.npwj.gretarttsi.hw2.shared.Domain;

public class Item {

    private final int id;
    private final String name;
    private float price;

    public Item (int id, String name,float price){
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public int getId(){
        return this.id;
    };

    public String getName(){
        return this.name;
    }

    public float getPrice(){
        return this.price;
    }
}
