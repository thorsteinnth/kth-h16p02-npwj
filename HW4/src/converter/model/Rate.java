package converter.model;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

@NamedQueries({
        @NamedQuery(
                name = "findRatesForCurrency",
                query = "SELECT rate FROM Rate rate WHERE rate.code1.code LIKE :currencyCode"
        ),
        @NamedQuery(
                name = "findReverseRatesForCurrency",
                query = "SELECT rate FROM Rate rate WHERE rate.code2.code LIKE :currencyCode"
        ),
        @NamedQuery(
                name = "findRateForCurrencies",
                query = "SELECT rate FROM Rate rate WHERE " +
                        "rate.code1.code LIKE :currencyCode1 AND " +
                        "rate.code2.code LIKE :currencyCode2"
        )
})

@Entity
@Table(name = "RATE")
public class Rate implements Serializable
{
    // NOTE: Having trouble getting OneToMany and ManyToOne to work, actually not used. We use queries instead.

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="CODE_1", nullable = false)
    private Currency code1;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="CODE_2", nullable = false)
    private Currency code2;

    @Column(name="RATE", nullable = false)
    private float rate;


    public Rate()
    {}

    public Rate(Currency code1, Currency code2, float rate)
    {
        this.code1 = code1;
        this.code2 = code2;
        this.rate = rate;
    }

    public int getId() {
        return id;
    }

    public Currency getCode1() {
        return code1;
    }

    public Currency getCode2() {
        return code2;
    }

    public float getRate() {
        return rate;
    }

    public void setRate(float rate) {
        this.rate = rate;
    }
}
