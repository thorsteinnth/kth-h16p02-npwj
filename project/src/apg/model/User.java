package apg.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@NamedQueries({
        @NamedQuery(
                name = "findUserByEmail",
                query = "SELECT user FROM User user WHERE user.email LIKE :email"
        ),
        @NamedQuery(
                name = "findUser",
                query = "SELECT user FROM User user WHERE " +
                        "user.email LIKE :email AND " +
                        "user.password LIKE :password"
        ),
        @NamedQuery(
                name = "findUserWithoutPassword",
                query = "SELECT user FROM User user WHERE " +
                        "user.email LIKE :email "
        )
})

@Entity
@Table(name = "USERS")
public class User implements Serializable
{
    @Id
    @Column(name="EMAIL", nullable = false)
    private String email;
    @Column(name="PASSW", nullable = false)
    private String password;
    @Column(name="ISADMIN", nullable = false)
    private boolean isAdmin;
    @Column(name="ISBANNED", nullable = false)
    private boolean isBanned;
    @OneToMany(mappedBy="user")
    private List<ShoppingCartItem> shoppingCart;

    public User()
    {}

    public User(String email, String password, boolean isAdmin, boolean isBanned)
    {
        this.email = email;
        this.password = password;
        this.isAdmin = isAdmin;
        this.isBanned = isBanned;
    }

    public String getEmail()
    {
        return email;
    }

    public String getPassword()
    {
        return password;
    }

    public boolean isAdmin()
    {
        return isAdmin;
    }

    public boolean isBanned()
    {
        return isBanned;
    }

    public void setBanned(boolean banned)
    {
        isBanned = banned;
    }

    @Override
    public String toString()
    {
        return "User{" +
                "email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", isAdmin=" + isAdmin +
                ", isBanned=" + isBanned +
                '}';
    }
}
