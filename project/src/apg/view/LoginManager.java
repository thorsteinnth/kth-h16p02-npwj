package apg.view;

import apg.controller.LoginController;
import apg.model.User;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Named("loginManager")
@RequestScoped
public class LoginManager
{
    @EJB
    LoginController loginController;

    private String email;
    private String password;

    private String showErrorEmail;

    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    //region ########## Getter and Setter ##########
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getShowErrorEmail() {
        return showErrorEmail;
    }

    public void setShowErrorEmail(String showErrorEmail) {
        this.showErrorEmail = showErrorEmail;
    }

    //endregion

    //region ########## Action Handler ##########
    public void test()
    {
        try
        {
            User user = loginController.getUser("bla", "bla");
        }
        catch (Exception e)
        {
            System.err.println(e);
        }
    }


    public String login()
    {
        if(isEmailValid())
        {
            try
            {
                User user = loginController.getUser("bla", "bla");
                return "success";
            }
            catch (Exception e)
            {
                System.err.println(e);
                return "failure";
            }
        }
        else
        {
            setShowErrorEmail("syntexError");
            return "failure";
        }
    }

    //endregion


    //region ########## Functions ##########
    private boolean isEmailValid()
    {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX .matcher(email);
        return matcher.find();
    }
    
    //endregion
}
