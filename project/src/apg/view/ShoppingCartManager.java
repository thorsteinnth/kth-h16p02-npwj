package apg.view;

import apg.controller.ShoppingCardController;
import apg.exceptions.NotEnoughStockException;
import apg.model.ShoppingCartItem;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;
import javax.faces.bean.ManagedBean;
import java.io.Serializable;
import java.util.List;

@ManagedBean
@RequestScoped
public class ShoppingCartManager implements Serializable
{
    @EJB
    ShoppingCardController shoppingCardController;

    private final static String purchaseFailureEdit ="purchase did not go through." +
            " Some items where not available in stock at the moment." +
            " The basket has been modified to correspond to the inventory status. TODO laga orðalag";
    private final static String purchaseFailureTryAgain = "Purchase did not go through." +
            "Something went wrong. Please try again.";

    private List<ShoppingCartItem> shoppingCartItems;
    private ShoppingCartItem shoppingCartItem;
    private String username;
    private String purchaseFailure;
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

    public boolean isShowTitleBanner()
    {
        if(!showSuccessBanner && !showFailureBanner)
            return true;
        else
            return false;
    }

    public String getPurchaseFailure() {
        return purchaseFailure;
    }

    public void setPurchaseFailure(String purchaseFailure) {
        this.purchaseFailure = purchaseFailure;
    }

    public String getTotal()
    {
        int total = 0;

        if(shoppingCartItems != null && shoppingCartItems.size() > 0)
        {
            for (ShoppingCartItem scItem: shoppingCartItems)
            {
                total += scItem.getQuantity() * scItem.getItem().getPrice();
            }
        }

        return Integer.toString(total);
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
            {
                showSuccessBanner = true;
            }
            else
            {
                purchaseFailure = purchaseFailureEdit;
                showFailureBanner = true;
            }
        }
        catch (NotEnoughStockException notEnoughStockException)
        {
            buyResult = false;
            purchaseFailure = purchaseFailureTryAgain;
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
