package converter.controller;

import converter.model.Currency;
import converter.model.Rate;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import java.util.List;

/**
 * A controller. All calls to the model that are executed because of an action taken by
 * the cashier pass through here.
 */
@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
@Stateless
public class ConverterController {
    @PersistenceContext(unitName = "converterPU")
    private EntityManager em;

    private static final String EUR = "EUR";
    private static final String USD = "USD";
    private static final String ISK = "ISK";
    private static final String SEK = "SEK";


    public void createCurrency()
    {
        Currency EURc = new Currency(EUR);
        Currency USDc = new Currency(USD);
        Currency ISKc = new Currency(ISK);
        Currency SEKc = new Currency(SEK);

        em.persist(EURc);
        em.persist(USDc);
        em.persist(ISKc);
        em.persist(SEKc);

        Rate EURToUSDRate = new Rate(EURc,USDc,2);
        Rate EURToISKRate = new Rate(EURc,ISKc,2);
        Rate EURToSEKRate = new Rate(EURc,SEKc,2);
        Rate USDToEURRate = new Rate(USDc,EURc,2);
        Rate USDToISKRate = new Rate(USDc,ISKc,2);
        Rate USDToSEKRate = new Rate(USDc,SEKc,2);
        Rate SEKToEURRate = new Rate(SEKc,EURc,2);
        Rate SEKToUSDRate = new Rate(SEKc,USDc,2);
        Rate SEKToISKRate = new Rate(SEKc,ISKc,2);
        Rate ISKToEURRate = new Rate(ISKc,EURc,2);
        Rate ISKToUSDRate = new Rate(ISKc,USDc,2);
        Rate ISKToSEKRate = new Rate(ISKc,SEKc,2);

        em.persist(EURToUSDRate);
        em.persist(EURToISKRate);
        em.persist(EURToSEKRate);

        em.persist(USDToEURRate);
        em.persist(USDToSEKRate);
        em.persist(USDToISKRate);

        em.persist(SEKToEURRate);
        em.persist(SEKToUSDRate);
        em.persist(SEKToISKRate);

        em.persist(ISKToEURRate);
        em.persist(ISKToUSDRate);
        em.persist(ISKToSEKRate);


        //List<Rate> rates = EURc.getRates();
        //System.out.println(rates);
    }

    public void checkData()
    {
        Currency EURc = em.find(Currency.class,EUR);

        List<Rate> rates = EURc.getRates();

        for (Rate rate: rates)
        {
            System.out.println(rate.getCode1());
        }

        System.out.println(rates);
    }
}

