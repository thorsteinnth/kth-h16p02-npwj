package apg.controller;


import apg.exceptions.NotEnoughStockException;
import apg.model.Item;
import apg.model.ShoppingCartItem;
import apg.model.User;
import apg.utils.SessionUtils;

import javax.annotation.PostConstruct;
import javax.ejb.*;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

@Stateful
@TransactionManagement(TransactionManagementType.CONTAINER)
public class ShoppingCardController {

    User user;
    // I create cache of the items to avoid accessing the database to frequently
    List<ShoppingCartItem> shoppingCartItems;

    @PersistenceContext(unitName = "apgPU")
    private EntityManager em;
    private EntityTransaction tx;

    public List<ShoppingCartItem> getAllShoppingCartItemsForUser()
    {
        Query query;

        if(shoppingCartItems == null)
        {
            query = em.createNamedQuery("findScItemsByEmail",ShoppingCartItem.class);
            query.setParameter("email", user.getEmail());
            this.shoppingCartItems = query.getResultList();
        }

        return this.shoppingCartItems;
    }

    public void refreshShoppingCardItems()
    {
        Query query = em.createNamedQuery("findScItemsByEmail",ShoppingCartItem.class);
        query.setParameter("email", user.getEmail());
        this.shoppingCartItems = query.getResultList();
    }


    public User getUser(String email)
    {
        Query query = em.createNamedQuery("findUserWithoutPassword", User.class);
        query.setParameter("email",email);
        User user = (User)query.getSingleResult();

        return user;
    }

    public String getEmail()
    {
        return user.getEmail();
    }

    @PostConstruct
    public void init()
    {
        if(user == null)
        {
            user = getUser(SessionUtils.getUsername());
        }
    }

    public void increaseQuantity(ShoppingCartItem shoppingCartItem)
    {
        ShoppingCartItem scItem = em.find(ShoppingCartItem.class, shoppingCartItem.getId());
        scItem.increaseQuantity();

        //we need to update the cache
        int index = 0;
        for(ShoppingCartItem item : shoppingCartItems )
        {
            if(item.getId() == shoppingCartItem.getId())
                break;
            index++;
        }

        //shoppingCartItems.get(index).increaseQuantity();
        //ShoppingCartItem manageItem = shoppingCartItems.get(index);
        //boolean isManage = em.contains(manageItem);
        //boolean isManage2 = em.contains(shoppingCartItems.get(index));

        //em.flush();

        shoppingCartItems.set(index,scItem);
    }

    public void decreaseQuantity(ShoppingCartItem shoppingCartItem)
    {
        ShoppingCartItem scItem = em.find(ShoppingCartItem.class, shoppingCartItem.getId());
        scItem.decreaseQuantity();

        //we need to update the cache
        int index = 0;
        for(ShoppingCartItem item : shoppingCartItems )
        {
            if(item.getId() == shoppingCartItem.getId())
                break;
            index++;
        }

        //shoppingCartItems.get(index).increaseQuantity();
        //em.flush();

        // If the item is zero then we drop it out of the basket
        if (scItem.getQuantity() == 0)
        {
            em.remove(scItem);
            shoppingCartItems.remove(index);
        }
        else
        {
            shoppingCartItems.set(index,scItem);
        }
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public boolean buy() throws NotEnoughStockException
    {
        if(checkInventory())
        {
            for(ShoppingCartItem scItem : shoppingCartItems) {
                Item item = em.find(Item.class, scItem.getItem().getSKU());

                item.withdraw(scItem.getQuantity());
                removeShoppingCartItemFromDatabase(scItem);
            }

            refreshShoppingCardItems();
            return true;
        }
        else
        {
            return false;
        }
    }

    public boolean checkInventory()
    {
        boolean success = true;

        for (ShoppingCartItem scItem : shoppingCartItems) {

            //Query query = em.createNamedQuery("findItemBySku", Item.class);
            //query.setParameter("SKU", scItem.getItem().getSKU());
            //TODO handle if item is not available anymore.
            //Item item  = (Item) query.getSingleResult();

            Item item = em.find(Item.class, scItem.getItem().getSKU());

            if (item == null) {
                //This should never happen
                removeShoppingCartItemFromDatabase(scItem);
                refreshShoppingCardItems();
                success = false;
            }
            else if (item.getStock() < scItem.getQuantity())
            {
                int amountToDecreaseQuantityBy = scItem.getQuantity() - item.getStock();
                ShoppingCartItem shoppingCartItem = em.find(ShoppingCartItem.class, scItem.getId());
                shoppingCartItem.decreaseQuantity(amountToDecreaseQuantityBy);

                // check if quantity is zero or less. This should never happen but if it does then we remove the shoppinglistItem
                if(shoppingCartItem.getQuantity() < 1)
                {
                    removeShoppingCartItemFromDatabase(shoppingCartItem);
                }

                success = false;
            }
        }

        if(success == false)
        {
            // refresh the shoppingCardItems after modifying
            refreshShoppingCardItems();
        }

        return success;
    }

    private void removeShoppingCartItemFromDatabase(ShoppingCartItem shoppingCartItem)
    {
        ShoppingCartItem scItem = em.find(ShoppingCartItem.class,shoppingCartItem.getId());
        em.remove(scItem);
    }
}
