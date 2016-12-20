package apg.view;


import apg.controller.HomeController;
import apg.model.Item;
import apg.utils.SessionUtils;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.util.List;

@Named("homeManager")
@RequestScoped
public class HomeManager implements Serializable
{
    @EJB
    HomeController homeController;

    private static final String successBannerTextModel = "You have: %1$d item(s) in your shopping cart" ;
    private static final String welcomeBannerTextNoUser = "Unknown user please log in or register";
    private static final String welcomeBannerTextKnownUser = "%1$s please enjoy our selection of gnomes.";

    private List<Item> items;
    private Item item;

    private String successBannerText;
    private String welcomeBannerText;
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

    public String getWelcomeBannerText()
    {
        if(isUserLoggedIn())
        {
            welcomeBannerText = String.format(welcomeBannerTextKnownUser, homeController.getUsername());
        }
        else
        {
            welcomeBannerText = welcomeBannerTextNoUser;
        }
        return welcomeBannerText;
    }

    public void setWelcomeBannerText(String welcomeBannerText) {
        this.welcomeBannerText = welcomeBannerText;
    }

    public boolean isShowWelcomeBanner()
    {

        boolean toReturn = (!showSuccessBanner && !showFailureBanner);
        return toReturn;
    }

    public String getSuccessBannerText() {
        return successBannerText;
    }

    public void setSuccessBannerText(String successBannerText) {
        this.successBannerText = successBannerText;
    }

    public boolean isUserLoggedIn()
    {
        if(homeController.getUsername() == SessionUtils.unknownUser)
            return false;
        else
            return true;

    }

    public boolean isUserAdmin()
    {
        if(isUserLoggedIn())
        {
            return homeController.isUserAdmin();
        }
        else
        {
            return false;
        }
    }

    //endregion

    public String plusOnClickEventHandler()
    {
        if(item.getStock() > 0)
        {
            homeController.addShoppingCartItem(item);
            int nrOfItems = homeController.getNumberOfShoppingListItems();
            successBannerText = String.format(successBannerTextModel, nrOfItems);
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
