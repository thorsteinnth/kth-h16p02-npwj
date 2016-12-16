package apg.view;

<<<<<<< HEAD

public class LoginManager
{

=======
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
>>>>>>> 17aec543c556e21ea823a55125bbc792cebd033e
}
