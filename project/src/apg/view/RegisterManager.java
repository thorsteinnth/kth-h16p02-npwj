package apg.view;

import java.io.Serializable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.ejb.EJB;
import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;


@Named("registerManager")
@RequestScoped
public class RegisterManager
{
    private String email;
    private String password;

    private String showErrorEmail;
    private String showErrorPassword;

    private final int minPasswordLength = 8;

    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    //region ########## getters and setters ##########

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

    public String getShowErrorPassword() {
        return showErrorPassword;
    }

    public void setShowErrorPassword(String showErrorPassword) {
        this.showErrorPassword = showErrorPassword;
    }

    //endregion

    //region ########## Action Handlers ##########
    public String add()
    {
        if(isEmailValid() && isPasswordValid())
        {
            return "Success";

            //TODO add the person to database
        }
        else
        {
            if(!isEmailValid())
            {
                setShowErrorEmail("syntexError");
            }

            if (!isPasswordValid())
            {
                setShowErrorPassword("syntexError");
            }
            return "fail";
        }
    }
    //endregion

    //region ########## Function ##########
    private boolean isEmailValid()
    {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX .matcher(email);
        return matcher.find();
    }

    private boolean isPasswordValid()
    {
        if (password.length() < this.minPasswordLength)
        {
            return false;
        }
        return true;
    }
    //endregion

}

