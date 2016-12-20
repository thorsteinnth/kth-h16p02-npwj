package apg.controller;

import apg.model.Item;
import apg.model.ShoppingCartItem;
import apg.model.User;
import apg.utils.SessionUtils;

import javax.annotation.PostConstruct;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;

@Stateful
public class HomeController
{
    User user;

    @PersistenceContext(unitName = "apgPU")
    private EntityManager em;

    public List<Item> getItems()
    {
        Query query = em.createNamedQuery("getAllItems", Item.class);
        return query.getResultList();
    }

    public void addShoppingCartItem(Item item)
    {
        Query query = em.createNamedQuery("findScItemsBySkyAndEmail",ShoppingCartItem.class);
        query.setParameter("SKU",item.getSKU());
        query.setParameter("email",user.getEmail());
        List<ShoppingCartItem> shoppingCartItems = query.getResultList();

        if(shoppingCartItems.size() == 0)
        {
            ShoppingCartItem shoppingCartItem = new ShoppingCartItem(user,item);
            em.persist(shoppingCartItem);
        }
        else
        {
            shoppingCartItems.get(0).increaseQuantity();
        }
    }

    public int getNumberOfShoppingListItems()
    {
        Query query = em.createNamedQuery("findScItemsByEmail",ShoppingCartItem.class);
        query.setParameter("email",user.getEmail());
        List<ShoppingCartItem> shoppingCartItems = query.getResultList();
        int i = 0;

        for(ShoppingCartItem scItems: shoppingCartItems)
        {
            i = i + scItems.getQuantity();
        }

        return i;
    }

    public User getUser(String email)
    {
        Query query = em.createNamedQuery("findUserWithoutPassword", User.class);
        query.setParameter("email",email);

        User user = (User)query.getSingleResult();

        return user;
    }

    @PostConstruct
    public void init()
    {
        if(user == null)
        {
            user = getUser(SessionUtils.getUsername());
        }
    }

    //Get shoppingListForUser
}
