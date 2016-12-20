package apg.view;


import apg.controller.HomeController;
import apg.model.Item;
import apg.utils.SessionUtils;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.PostActivate;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;
import javax.faces.bean.ManagedBean;
import javax.inject.Named;
import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Named("homeManager")
@RequestScoped
public class HomeManager implements Serializable
{
    @EJB
    HomeController homeController;

    private static final String bannerTextModel = "You have: %1$d in your shopping cart" ;

    private List<Item> items;
    private Item item;

    private String bannerText;
    private boolean showSuccessBanner;
    private boolean showFailureBanner;

    //region ########## Getter and Setter ##########
    public List<Item> getItems() {
        try
        {
            items = homeController.getItems();
        }
        catch (Exception e)
        {
            System.err.println(e);
        }
        //TODO fara í controller og ná í items
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public String getBannerText() {
        return bannerText;
    }

    public void setBannerText(String bannerText) {
        this.bannerText = bannerText;
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

    public String plusOnClickEventHandler()
    {
        if(item.getStock() > 0)
        {
            homeController.addShoppingCartItem(item);
            int nrOfItems = homeController.getNumberOfShoppingListItems();
            bannerText = String.format(bannerTextModel, nrOfItems);
            showSuccessBanner = true;
            return jsf22Bugfix();
        }
        else
        {
            showFailureBanner = true;
            return jsf22Bugfix();
        }
    }

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
