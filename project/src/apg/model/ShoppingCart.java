package apg.model;

import java.util.ArrayList;
import java.util.List;

public class ShoppingCart
{
    private User user;
    private List<Item> items;

    public ShoppingCart(User user)
    {
        this.user = user;
        this.items = new ArrayList<>();
    }

    public User getUser()
    {
        return user;
    }

    public List<Item> getItems()
    {
        return items;
    }

    @Override
    public String toString()
    {
        return "ShoppingCart{" +
                "user=" + user +
                ", items=" + items +
                '}';
    }
}
