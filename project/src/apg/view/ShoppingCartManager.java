package apg.view;

import apg.controller.ShoppingCardController;
import apg.exceptions.NotEnoughStockException;
import apg.model.ShoppingCartItem;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.bean.ManagedBean;
import java.io.Serializable;
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
    private boolean isShoppingCartEmpty;
    private boolean showPopUp;
    private boolean showSuccessBanner;
    private boolean showFailureBanner;

    //region ########## Getter and Setter ##########
    public List<ShoppingCartItem> getShoppingCartItems() {

        shoppingCartItems = shoppingCardController.getAllShoppingCartItemsForUser();

        if(shoppingCartItems.size() == 0)
        {
            isShoppingCartEmpty = true;
        }
        else
        {
            isShoppingCartEmpty = false;
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

    public boolean isShoppingCartEmpty() {
        return isShoppingCartEmpty;
    }

    public void setShoppingCartEmpty(boolean shoppingCartEmpty) {
        isShoppingCartEmpty = shoppingCartEmpty;
    }

    public boolean isShowPopUp() {
        return showPopUp;
    }

    public void setShowPopUp(boolean showPopUp) {
        this.showPopUp = showPopUp;
    }

    public boolean isShowSuccessBanner() {
        return showSuccessBanner;
    }

    public void setShowSuccessBanner(boolean showSuccessBanner) {
        this.showSuccessBanner = showSuccessBanner;
    }

    public boolean isShowFailureBanner() {
        return showFailureBanner;
    }

    public void setShowFailureBanner(boolean showFailureBanner) {
        this.showFailureBanner = showFailureBanner;
    }

    //endregion

    //region ########## Action handler ##########
    public String buy()
    {
        //showPopUp = true;
        hideBanner();
        boolean buyResult;
        try
        {
            buyResult = shoppingCardController.buy();

            if(buyResult)
                showSuccessBanner = true;
            else
                showFailureBanner = true;
        }
        catch (NotEnoughStockException notEnoughStockException)
        {
            buyResult = false;
            showFailureBanner = true;
            //TODO go to error msg view og eyða út shoppingCartinu
        }
        catch (Exception e)
        {
            buyResult = false;
            showFailureBanner = false;
        }

        return jsf22Bugfix();
    }

    public String increaseQuantity()
    {
        hideBanner();
        shoppingCardController.increaseQuantity(shoppingCartItem);
        return jsf22Bugfix();
    }

    public String decreaseQuantity()
    {
        hideBanner();
        shoppingCardController.decreaseQuantity(shoppingCartItem);
        return jsf22Bugfix();
    }

    public String popupYes()
    {
        showPopUp = false;
        return jsf22Bugfix();
    }

    public String popupNo()
    {
        showPopUp = false;
        return jsf22Bugfix();
    }

    private void hideBanner()
    {
        this.showSuccessBanner = false;
        this.showFailureBanner = false;
    }

    //endregion

    /**
     * This return value is needed because of a JSF 2.2 bug. Note 3 on page 7-10
     * of the JSF 2.2 specification states that action handling methods may be
     * void. In JSF 2.2, however, a void action handling method plus an
     * if-element that evaluates to true in the faces-conffoig navigation case
     * causes an exception.
     *
     * @return an empty string.
     */
    private String jsf22Bugfix()
    {
        return "";
    }
}
