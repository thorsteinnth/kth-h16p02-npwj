package apg.controller;


import apg.model.ShoppingCartItem;
import apg.model.User;
import apg.utils.SessionUtils;

import javax.annotation.PostConstruct;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

@Stateful
public class ShoppingCardController {

    User user;
    // I create cache of the items to avoid accessing the database to frequently
    List<ShoppingCartItem> shoppingCartItems;

    @PersistenceContext(unitName = "apgPU")
    private EntityManager em;

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
}
