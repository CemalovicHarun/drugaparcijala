package ba.unsa.etf.rpr;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class IspitDAOTest {

    @Test
    void testUniverzitetskiGrad() {
        UniverzitetskiGrad ug = new UniverzitetskiGrad();
        ug.setNazivUniverziteta("Univerzitet u Sarajevu");
        assertEquals("Univerzitet u Sarajevu", ug.getNazivUniverziteta());
    }

    @Test
    void testUniverzitetskiGradCtor() {
        Drzava drzava = new Drzava();
        UniverzitetskiGrad grad = new UniverzitetskiGrad(0, "Sarajevo", 350000, drzava, "Univerzitet u Sarajevu"); // Naziv univerziteta je posljednji parametar
        assertEquals("Univerzitet u Sarajevu", grad.getNazivUniverziteta());
    }

    @Test
    void testIzmijeniGrad() {
        GeografijaDAO.removeInstance();
        File dbfile = new File("baza.db");
        dbfile.delete();

        GeografijaDAO dao = GeografijaDAO.getInstance();
        Grad bech = dao.glavniGrad("Austrija");
        UniverzitetskiGrad b2 = new UniverzitetskiGrad(bech.getId(), bech.getNaziv(), bech.getBrojStanovnika(), bech.getDrzava(), "TU Wien");
        dao.izmijeniGrad(b2);

        Grad b3 = dao.glavniGrad("Austrija");
        assertTrue(b3 instanceof UniverzitetskiGrad);
        UniverzitetskiGrad ug = (UniverzitetskiGrad) b3;
        assertEquals("TU Wien", ug.getNazivUniverziteta());
    }

    @Test
    void testDodajGrad() {
        GeografijaDAO.removeInstance();
        File dbfile = new File("baza.db");
        dbfile.delete();

        GeografijaDAO dao = GeografijaDAO.getInstance();
        Drzava vb = dao.nadjiDrzavu("Velika Britanija");
        UniverzitetskiGrad oxford = new UniverzitetskiGrad(0, "Oxford", 350000, vb, "Oxford University");

        dao.dodajGrad(oxford);

        Grad s2 = null;
        for(Grad grad : dao.gradovi()) {
            if (grad.getNaziv().equals("Oxford"))
                s2 = grad;
        }
        assertNotNull(s2);

        assertTrue(s2 instanceof UniverzitetskiGrad);
        UniverzitetskiGrad s3 = (UniverzitetskiGrad) s2;
        assertEquals("Oxford University", s3.getNazivUniverziteta());
    }


    @Test
    void testNadjiGrad() {
        GeografijaDAO.removeInstance();
        File dbfile = new File("baza.db");
        dbfile.delete();

        GeografijaDAO dao = GeografijaDAO.getInstance();
        Drzava vb = dao.nadjiDrzavu("Velika Britanija");
        UniverzitetskiGrad oxford = new UniverzitetskiGrad(0, "Oxford", 350000, vb, "Oxford University");

        dao.dodajGrad(oxford);

        Grad s2 = dao.nadjiGrad("Oxford");
        assertNotNull(s2);

        assertTrue(s2 instanceof UniverzitetskiGrad);
        UniverzitetskiGrad s3 = (UniverzitetskiGrad) s2;
        assertEquals("Oxford University", s3.getNazivUniverziteta());
    }


    @Test
    void testNadjiDrzavu() {
        GeografijaDAO.removeInstance();
        File dbfile = new File("baza.db");
        dbfile.delete();

        GeografijaDAO dao = GeografijaDAO.getInstance();
        Drzava vb = dao.nadjiDrzavu("Velika Britanija");
        UniverzitetskiGrad sarajevo = new UniverzitetskiGrad(0, "Sarajevo", 350000, vb, "Univerzitet u Sarajevu");

        dao.dodajGrad(sarajevo);

        Grad s2 = dao.nadjiGrad("Sarajevo");
        assertNotNull(s2);

        Drzava bih = new Drzava(0, "Bosna i Hercegovina", s2);
        dao.dodajDrzavu(bih);

        Drzava d2 = dao.nadjiDrzavu("Bosna i Hercegovina");
        assertNotNull(d2);

        Grad s3 = d2.getGlavniGrad();
        assertTrue(s3 instanceof UniverzitetskiGrad);
        UniverzitetskiGrad s4 = (UniverzitetskiGrad) s3;
        assertEquals("Univerzitet u Sarajevu", s4.getNazivUniverziteta());
    }

    @Test
    void testBazaDirekt() {
        // Test koji direktno pristupa bazi zaobilazeći DAO klasu

        // Regenerišemo bazu ako je promijenjena prethodnim testovima
        GeografijaDAO.removeInstance();
        File dbfile = new File("baza.db");
        dbfile.delete();
        GeografijaDAO dao = GeografijaDAO.getInstance();

        // Sad ćemo se opet diskonektovati jer radimo sa bazom direktno
        GeografijaDAO.removeInstance();

        Connection conn;
        try {
            conn = DriverManager.getConnection("jdbc:sqlite:baza.db");
            try {
                PreparedStatement nadmorskaUpit = conn.prepareStatement("SELECT naziv_univerziteta FROM grad WHERE id=1");
                nadmorskaUpit.execute();
                conn.close();
            } catch (SQLException e) {
                fail("Tabela grad ne sadrži kolonu naziv_univerziteta");
            }
        } catch (SQLException e) {
            fail("Datoteka sa bazom ne postoji ili je nedostupna");
        }

    }
}
