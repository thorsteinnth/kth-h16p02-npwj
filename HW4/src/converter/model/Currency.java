package converter.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * Created by GretarAtli on 06/12/2016.
 */

@Entity
public class Currency implements Serializable{

    @Id
    private String code;

    public Currency()
    {}

    public Currency(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
