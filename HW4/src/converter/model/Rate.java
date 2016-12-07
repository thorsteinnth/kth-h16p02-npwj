package converter.model;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "rate")
public class Rate implements Serializable{

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


    public Rate() {
    }

    public Rate(Currency code1, Currency code2, float rate) {
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

    public void setCode1(Currency code1) {
        this.code1 = code1;
    }

    public Currency getCode2() {
        return code2;
    }

    public void setCode2(Currency code2) {
        this.code2 = code2;
    }

    public float getRate() {
        return rate;
    }

    public void setRate(float rate) {
        this.rate = rate;
    }
}
