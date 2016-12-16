package apg.view;

import apg.controller.LoginController;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;

@Named("converterManager")
@ConversationScoped
public class LoginManager implements Serializable
{
    @EJB
    LoginController controller;
    @Inject
    private Conversation conversation;

    @PostConstruct
    public void init()
    {
        controller.createTestUser();
    }
}
