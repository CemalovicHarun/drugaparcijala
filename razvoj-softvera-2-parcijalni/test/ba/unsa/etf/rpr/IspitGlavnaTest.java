package ba.unsa.etf.rpr;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

import static javafx.scene.layout.Region.USE_COMPUTED_SIZE;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(ApplicationExtension.class)
public class IspitGlavnaTest {
    Stage theStage;
    GlavnaController ctrl;

    @Start
    public void start (Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/glavna.fxml"));
        ctrl = new GlavnaController();
        loader.setController(ctrl);
        Parent root = loader.load();
        stage.setTitle("Grad");
        stage.setScene(new Scene(root, USE_COMPUTED_SIZE, USE_COMPUTED_SIZE));
        stage.setResizable(false);
        stage.show();

        stage.toFront();

        theStage = stage;
    }

    @Test
    public void testDodajGradUniverzitetski(FxRobot robot) {
        ctrl.resetujBazu();

        // Otvaranje forme za dodavanje
        robot.clickOn("#btnDodajGrad");

        // Čekamo da dijalog postane vidljiv
        robot.lookup("#fieldNaziv").tryQuery().isPresent();

        // Postoji li fieldNaziv
        robot.clickOn("#fieldNaziv");
        robot.write("Sarajevo");

        robot.clickOn("#fieldBrojStanovnika");
        robot.write("350000");

        robot.clickOn("#cbUniverzitetski");

        robot.clickOn("#fieldNazivUniverziteta");
        robot.write("Univerzitet u Sarajevu");

        // Klik na dugme Ok
        robot.clickOn("#btnOk");

        // Da li je Sarajevo dodano u bazu?
        GeografijaDAO dao = GeografijaDAO.getInstance();
        assertEquals(6, dao.gradovi().size());

        Grad sarajevo = null;
        for(Grad grad : dao.gradovi())
            if (grad.getNaziv().equals("Sarajevo"))
                sarajevo = grad;
        assertNotNull(sarajevo);
        assertTrue(sarajevo instanceof UniverzitetskiGrad);

        UniverzitetskiGrad ug = (UniverzitetskiGrad) sarajevo;
        assertEquals("Univerzitet u Sarajevu", ug.getNazivUniverziteta());
    }

    @Test
    public void testIzmijeniGradUniverzitetski(FxRobot robot) {
        ctrl.resetujBazu();

        // Graz ne smije biti univerzitetski grad jer je to "varanje"
        GeografijaDAO dao = GeografijaDAO.getInstance();
        Grad graz = dao.nadjiGrad("Graz");
        assertFalse(graz instanceof UniverzitetskiGrad);

        // Mijenjamo grad Graz
        robot.clickOn("Graz");
        robot.clickOn("#btnIzmijeniGrad");

        // Čekamo da dijalog postane vidljiv
        robot.clickOn("#cbUniverzitetski");

        robot.clickOn("#fieldNazivUniverziteta");
        robot.write("TU Graz");

        // Klik na dugme Ok
        robot.clickOn("#btnOk");

        // Da li je Graz univerzitetski grad?
        Grad graz2 = dao.nadjiGrad("Graz");
        assertTrue(graz2 instanceof UniverzitetskiGrad);
        UniverzitetskiGrad ug = (UniverzitetskiGrad) graz2;
        assertEquals("TU Graz", ug.getNazivUniverziteta());
    }

    @Test
    public void testIzmijeniGradNijeUniverzitetski(FxRobot robot) {
        ctrl.resetujBazu();

        // Graz ne smije biti univerzitetski grad jer je to "varanje"
        GeografijaDAO dao = GeografijaDAO.getInstance();
        Grad graz = dao.nadjiGrad("Graz");
        assertFalse(graz instanceof UniverzitetskiGrad);

        // Mijenjamo grad Graz
        robot.clickOn("Graz");
        robot.clickOn("#btnIzmijeniGrad");

        // Čekamo da dijalog postane vidljiv
        robot.clickOn("#cbUniverzitetski");

        robot.clickOn("#fieldNazivUniverziteta");
        robot.write("TU Graz");

        // Klik na dugme Ok
        robot.clickOn("#btnOk");

        // Mijenjamo London
        robot.clickOn("London");
        robot.clickOn("#btnIzmijeniGrad");
        robot.clickOn("#btnOk");

        // Ponovo mijenjamo grad Graz
        robot.clickOn("Graz");
        robot.clickOn("#btnIzmijeniGrad");

        // Čekamo da dijalog postane vidljiv
        robot.clickOn("#cbUniverzitetski");

        // Klik na dugme Ok
        robot.clickOn("#btnOk");

        // Da li je Graz univerzitetski grad?
        Grad graz2 = dao.nadjiGrad("Graz");
        assertFalse(graz2 instanceof UniverzitetskiGrad);
    }

}
