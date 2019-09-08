package ba.unsa.etf.rpr;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GradTest {
    @Test
    void testGrad() {
        Grad grad = new Grad();
        grad.setNaziv("Sarajevo");
        assertEquals("Sarajevo", grad.getNaziv());
    }


    @Test
    void testGradCtor() {
        Drzava drzava = new Drzava();
        Grad grad = new Grad(123, "Sarajevo", 350000, drzava);
        assertEquals("Sarajevo", grad.getNaziv());
        assertEquals(123, grad.getId());
        assertEquals(350000, grad.getBrojStanovnika());
    }
}