package converter.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by GretarAtli on 06/12/2016.
 */

@Entity
@Table(name = "currency")
public class Currency implements Serializable
{

    @Id
    private String code;

    @OneToMany(mappedBy = "code1")
    private List<Rate> rates;

    @OneToMany(mappedBy = "code2")
    private List<Rate> reverseRates;


    public Currency()
    {}

    public Currency(String code) {
        this.code = code;
        this.rates = new ArrayList<>();
        this.reverseRates = new ArrayList<>();
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public List<Rate> getRates()
    {
        return this.rates;
    }

    public List<Rate> getReverseRates()
    {
        return  this.reverseRates;
    }
}

