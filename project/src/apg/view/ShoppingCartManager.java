package apg.view;

import apg.controller.HomeController;
import apg.controller.ShoppingCardController;
import apg.model.ShoppingCartItem;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.bean.ManagedBean;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@ManagedBean
@SessionScoped
public class ShoppingCartManager implements Serializable
{
    @EJB
    ShoppingCardController shoppingCardController;

    private List<ShoppingCartItem> shoppingCartItems;
    private ShoppingCartItem shoppingCartItem;
    private int total;
    private String username;

    //region ########## Getter and Setter ##########
    public List<ShoppingCartItem> getShoppingCartItems() {

        if(shoppingCartItems == null)
        {
               shoppingCartItems = shoppingCardController.getAllShoppingCartItemsForUser();
        }

        return shoppingCartItems;
    }

    public void setShoppingCartItems(List<ShoppingCartItem> shoppingCartItems) {
        this.shoppingCartItems = shoppingCartItems;
    }

    public ShoppingCartItem getShoppingCartItem() {
        return shoppingCartItem;
    }

    public void setShoppingCartItem(ShoppingCartItem shoppingCartItem) {
        this.shoppingCartItem = shoppingCartItem;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public String getUsername() {
        return shoppingCardController.getEmail();
    }

    public void setUsername(String username) {
        this.username = username;
    }

    //endregion

    //region ########## Action handler ##########
    public String buy()
    {
        return jsf22Bugfix();
    }
    //endregion


    /**
     * This return value is needed because of a JSF 2.2 bug. Note 3 on page 7-10
     * of the JSF 2.2 specification states that action handling methods may be
     * void. In JSF 2.2, however, a void action handling method plus an
     * if-element that evaluates to true in the faces-config navigation case
     * causes an exception.
     *
     * @return an empty string.
     */
    private String jsf22Bugfix()
    {
        return "";
    }
}
