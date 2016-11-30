package se.kth.h16p02.npwj.gretarttsi.hw3.bank.entities;

import javax.persistence.*;
import java.io.Serializable;

@Entity(name = "Owner")
public class Owner implements Serializable
{
    private static final long serialVersionUID = 706795289816654474L;

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long accountId;

    @Column(name = "name", nullable = false)
    private String name;

    @Version
    @Column(name = "OPTLOCK")
    private int versionNum;

    public Owner()
    {
        this("");
    }

    public Owner(String name)
    {
        this.name = name;
    }

    public String getName()
    {
        return name;
    }
}