package apg.model;

public class Item
{
    private String SKU;
    private String description;
    private int price;

    public Item(String SKU, String description, int price)
    {
        this.SKU = SKU;
        this.description = description;
        this.price = price;
    }

    public String getSKU()
    {
        return SKU;
    }

    public String getDescription()
    {
        return description;
    }

    public int getPrice()
    {
        return price;
    }

    @Override
    public String toString()
    {
        return "Item{" +
                "SKU='" + SKU + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                '}';
    }
}
