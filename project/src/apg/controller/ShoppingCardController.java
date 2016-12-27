package apg.controller;

import apg.exceptions.NotEnoughStockException;
import apg.exceptions.UnknownUserException;
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

    private static final String unknownUserErrorText = "Unknown user does not have access rights to enter this site. Please log in or register to be able to use the shopping cart";

    User user;
    // I create cache of the items to avoid accessing the database to frequently
    List<ShoppingCartItem> shoppingCartItems;

    @PersistenceContext(unitName = "apgPU")
    private EntityManager em;
    private EntityTransaction tx;

    public List<ShoppingCartItem> getAllShoppingCartItemsForUser() throws UnknownUserException
    {
        if(SessionUtils.getUsername() == SessionUtils.unknownUser)
            throw new UnknownUserException(unknownUserErrorText);

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
        if(user != null)
            return user.getEmail();
        else
            return SessionUtils.unknownUser;
    }

    @PostConstruct
    public void init()
    {
        String username = SessionUtils.getUsername();

        if(user == null && username != SessionUtils.unknownUser)
        {
            user = getUser(SessionUtils.getUsername());
        }
    }

    public void increaseQuantity(ShoppingCartItem shoppingCartItem) throws UnknownUserException
    {
        if(SessionUtils.getUsername() == SessionUtils.unknownUser)
            throw new UnknownUserException(unknownUserErrorText);

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

        shoppingCartItems.set(index,scItem);
    }

    public void decreaseQuantity(ShoppingCartItem shoppingCartItem) throws UnknownUserException
    {
        if(SessionUtils.getUsername() == SessionUtils.unknownUser)
            throw new UnknownUserException(unknownUserErrorText);

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
    public boolean buy() throws NotEnoughStockException, UnknownUserException
    {
        if(SessionUtils.getUsername() == SessionUtils.unknownUser)
            throw new UnknownUserException(unknownUserErrorText);

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
            Item item = em.find(Item.class, scItem.getItem().getSKU());

            if (item == null) {
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
