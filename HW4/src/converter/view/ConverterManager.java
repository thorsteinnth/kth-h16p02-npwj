package converter.view;

import converter.controller.ConverterController;

import java.io.Serializable;
import javax.ejb.EJB;
import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.inject.Inject;
import javax.inject.Named;


@Named("converterManager")
@ConversationScoped
public class ConverterManager implements Serializable{

    @EJB
    ConverterController converterController;


    @Inject
    private Conversation conversation;


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


    public String createInitialData() {
        try {
            startConversation();
            converterController.createCurrency();
        } catch (Exception e) {
            handleException(e);
        }
        return jsf22Bugfix();
    }

    public String checkData() {
        try {
            startConversation();
            converterController.checkData();
        } catch (Exception e) {
            handleException(e);
        }
        return jsf22Bugfix();
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
        stopConversation();
        e.printStackTrace(System.err);
    }
}
