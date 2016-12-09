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
import javax.inject.Inject;
import javax.inject.Named;

@Named("converterManager")
@ConversationScoped
public class ConverterManager implements Serializable
{
    @EJB
    ConverterController converterController;
    @Inject
    private Conversation conversation;

    private String selectedCurrencyFromCode;
    private List<String> currenciesFromCodes;

    private String selectedCurrencyToCode;
    private List<String> currenciesToCodes;

    private float amount;
    private float convertedAmount;

    private Exception exception;

    private void startConversation() {
        if (conversation.isTransient()) {
            conversation.begin();
        }
    }

    private void stopConversation() {
        if (!conversation.isTransient()) {
            conversation.end();
        }
    }


    public String getSelectedCurrencyFromCode()
    {
        return selectedCurrencyFromCode;
    }

    public void setSelectedCurrencyFromCode(String selectedCurrencyFromCode)
    {
        this.selectedCurrencyFromCode = selectedCurrencyFromCode;
    }

    public Exception getException()
    {
        return exception;
    }

    public List<String> getCurrenciesFromCodes()
    {
        return currenciesFromCodes;
    }

    public void setCurrenciesFromCodes(List<String> currenciesFromCodes)
    {
        this.currenciesFromCodes = currenciesFromCodes;
    }

    public String getSelectedCurrencyToCode()
    {
        return selectedCurrencyToCode;
    }

    public void setSelectedCurrencyToCode(String selectedCurrencyToCode)
    {
        this.selectedCurrencyToCode = selectedCurrencyToCode;
    }

    public List<String> getCurrenciesToCodes()
    {
        return currenciesToCodes;
    }

    public void setCurrenciesToCodes(List<String> currenciesToCodes)
    {
        this.currenciesToCodes = currenciesToCodes;
    }

    public float getAmount()
    {
        return amount;
    }

    public void setAmount(float amount)
    {
        this.amount = amount;
    }


    public String getConvertedAmountDisplayString() {
        return  getAmount() + " " + getSelectedCurrencyFromCode() + " = " + getConvertedAmount() + " " + getSelectedCurrencyToCode();
    }

    public float getConvertedAmount()
    {
        return convertedAmount;
    }

    /*
    public String createInitialData()
    {
        try
        {

            converterController.createCurrency();
        }
        catch (Exception e)
        {
            handleException(e);
        }

        return jsf22Bugfix();
    }
    */

    /*
    public String checkData()
    {
        try
        {
            converterController.checkData();
        }
        catch (Exception e)
        {
            handleException(e);
        }

        return jsf22Bugfix();
    }
    */

    public String calculate()
    {
        try
        {
            startConversation();
            Currency currencyFrom = converterController.getCurrency(selectedCurrencyFromCode);
            Currency currencyTo = converterController.getCurrency(selectedCurrencyToCode);

            Rate rate = converterController.getRateForCurrencies(currencyFrom, currencyTo);
            this.convertedAmount = amount * rate.getRate();
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
            //createInitialData();
            startConversation();
            ArrayList<Currency> currencies = this.converterController.getCurrencies();

            ArrayList<String> currenciesFromCodes = new ArrayList<>();
            ArrayList<String> currenciesToCodes = new ArrayList<>();

            for (Currency c : currencies)
            {
                currenciesFromCodes.add(c.getCode());
                currenciesToCodes.add(c.getCode());
            }

            setCurrenciesFromCodes(currenciesFromCodes);
            setCurrenciesToCodes(currenciesToCodes);

            if (currenciesFromCodes.size() > 0)
                setSelectedCurrencyFromCode(currenciesFromCodes.get(0));

            if (currenciesToCodes.size() > 0)
                setSelectedCurrencyToCode(currenciesToCodes.get(0));
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
    private String jsf22Bugfix()
    {
        return "";
    }

    private void handleException(Exception e)
    {
        stopConversation();
        e.printStackTrace(System.err);
        exception = e;
    }

    public boolean getSuccess() {
        return exception == null;
    }
}
