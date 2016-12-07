package converter.model;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.*;

/**
 * Created by GretarAtli on 06/12/2016.
 */

@Entity
@Table(name = "CURRENCY")
public class Currency implements Serializable
{

    @Id
    private String code;

    @OneToMany(mappedBy = "code1")
    private List<Rate> rates;

    @OneToMany(mappedBy = "code2")
    private List<Rate> reverseRates;

    @Version
    @Column(name = "LAST_UPDATED_TIME")
    private Timestamp updatedTime;

    public Currency()
    {}

    public Currency(String code) {
        this.code = code;
        //this.rates = new ArrayList<>();
        //this.reverseRates = new ArrayList<>();
    }

    public String getCode() {
        return code;
    }


    /*
    public ArrayList<Rate> getRates()
    {

    }

    public ArrayList<Rate> getReverseRates()
    {

    }
    */
}

