package converter.view;

import converter.controller.ConverterController;
import converter.model.Currency;
import converter.model.Rate;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;


@Named("converterManager")
@SessionScoped
public class ConverterManager implements Serializable{

    @EJB
    ConverterController converterController;
    private Currency selectedCurrencyFrom;
    private List<Currency> currenciesFrom;

    private Currency selectedCurrencyTo;
    private List<Currency> currenciesTo;

    private float amount;
    private float convertedAmount;

    //@Inject
    //private Conversation conversation;

    /*
    private void startConversation() {
        if (conversation.isTransient()) {
            conversation.begin();
        }
    }

    private void stopConversation() {
        if (!conversation.isTransient()) {
            conversation.end();
        }
    }*/

    public Currency getSelectedCurrencyFrom() {
        return selectedCurrencyFrom;
    }

    public void setSelectedCurrencyFrom(Currency selectedCurrencyFrom) {
        this.selectedCurrencyFrom = selectedCurrencyFrom;
    }

    public List<Currency> getCurrenciesFrom() {
        return currenciesFrom;
    }

    public void setCurrenciesFrom(List<Currency> currenciesFrom)
    {
        this.currenciesFrom = currenciesFrom;
    }

    public Currency getSelectedCurrencyTo() {
        return selectedCurrencyTo;
    }

    public void setSelectedCurrencyTo(Currency selectedCurrencyTo) {
        this.selectedCurrencyTo = selectedCurrencyTo;
    }

    public List<Currency> getCurrenciesTo() {
        return currenciesTo;
    }

    public void setCurrenciesTo(List<Currency> currenciesTo) {
        this.currenciesTo = currenciesTo;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public float getConvertedAmount() {
        return convertedAmount;
    }

    public String createInitialData() {
        try {
            //startConversation();
            converterController.createCurrency();
        } catch (Exception e) {
            handleException(e);
        }
        return jsf22Bugfix();
    }

    public String checkData() {
        try {
            //startConversation();
            converterController.checkData();
        } catch (Exception e) {
            handleException(e);
        }
        return jsf22Bugfix();
    }

    public String calculate()
    {
        try
        {
            Rate rate = converterController.getRateForCurrencies(selectedCurrencyFrom, selectedCurrencyTo);
            if (rate != null)
            {
                this.convertedAmount = amount * rate.getRate();
            }
            else
            {
                this.convertedAmount = 0;
            }
        }
        catch (Exception ex)
        {
            this.convertedAmount = 0;
            handleException(ex);
        }

        return jsf22Bugfix();
    }

    @PostConstruct
    public void init()
    {
        try
        {
            amount = 0;
            createInitialData();

            ArrayList<Currency> currencies = this.converterController.getCurrencies();

            if (currencies.size() > 0)
            {
                this.setSelectedCurrencyFrom(currencies.get(0));
                this.setCurrenciesFrom(currencies);

                this.setSelectedCurrencyTo(currencies.get(0));
                this.setCurrenciesTo(currencies);
            }
        }
        catch (Exception e)
        {
            handleException(e);
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
    private String jsf22Bugfix() {
        return "";
    }


    private void handleException(Exception e) {
        //stopConversation();
        e.printStackTrace(System.err);
    }
}
