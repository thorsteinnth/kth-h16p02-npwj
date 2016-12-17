package apg.view;

import apg.controller.HomeController;
import apg.model.ShoppingCartItem;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.bean.ManagedBean;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;

@ManagedBean
@SessionScoped
public class ShoppingCardManager implements Serializable
{
    @EJB
    HomeController homeController;

    private ArrayList<ShoppingCartItem> shoppingCartItems;
    private ShoppingCartItem shoppingCartItem;
    private int total;

    //region ########## Getter and Setter
    public ArrayList<ShoppingCartItem> getShoppingCartItems() {
        return shoppingCartItems;
    }

    public void setShoppingCartItems(ArrayList<ShoppingCartItem> shoppingCartItems) {
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
