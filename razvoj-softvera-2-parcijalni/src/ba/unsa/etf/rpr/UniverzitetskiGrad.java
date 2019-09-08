package ba.unsa.etf.rpr;

public class UniverzitetskiGrad extends Grad {
    String nazivUniverziteta;

    public UniverzitetskiGrad(int id, String naziv, int brojStanovnika, Drzava drzava, String nazivUniverziteta) {
        super(id, naziv, brojStanovnika, drzava);
        this.nazivUniverziteta = nazivUniverziteta;
    }

    public UniverzitetskiGrad(String nazivUniverziteta) {
        this.nazivUniverziteta = nazivUniverziteta;
    }

    public String getNazivUniverziteta() {
        return nazivUniverziteta;
    }

    public void setNazivUniverziteta(String nazivUniverziteta) {
        this.nazivUniverziteta = nazivUniverziteta;
    }

    public UniverzitetskiGrad() {
    }

    @Override
    public String toString() {
        return nazivUniverziteta;
    }
}
