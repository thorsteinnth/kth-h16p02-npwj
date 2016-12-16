package apg.view;

import java.io.Serializable;
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

    private boolean showErrorEmail;
    private boolean showErrorPassword;

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

    public boolean isShowErrorEmail() {
        return showErrorEmail;
    }

    public void setShowErrorEmail(boolean showErrorEmail) {
        this.showErrorEmail = showErrorEmail;
    }

    public boolean isShowErrorPassword() {
        return showErrorPassword;
    }

    public void setShowErrorPassword(boolean showErrorPassword) {
        this.showErrorPassword = showErrorPassword;
    }

//endregion

    //region ########## Action Handlers ##########

    public String add()
    {
        setShowErrorEmail(true);
        setShowErrorPassword(true);
        return "success";
    }

    //endregion

}
