package ba.unsa.etf.rpr;


import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

import java.security.Key;

import static javafx.scene.layout.Region.USE_COMPUTED_SIZE;
import static org.junit.jupiter.api.Assertions.*;

// Testovi za klasu GradController ako se pošalje UniverzitetskiGrad

@ExtendWith(ApplicationExtension.class)
public class IspitUGControllerTest {
    Stage theStage;
    GradController ctrl;

    @Start
    public void start(Stage stage) throws Exception {
        GeografijaDAO dao = GeografijaDAO.getInstance();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/grad.fxml"));
        Drzava vb = dao.nadjiDrzavu("Velika Britanija");
        UniverzitetskiGrad sarajevo = new UniverzitetskiGrad(0, "Sarajevo", 350000, vb, "Univerzitet u Sarajevu");
        ctrl = new GradController(sarajevo, dao.drzave());
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
    public void testPoljaValidneVrijednosti(FxRobot robot) {
        TextField fld = robot.lookup("#fieldNazivUniverziteta").queryAs(TextField.class);
        assertNotNull(fld);
        CheckBox cb = robot.lookup("#cbUniverzitetski").queryAs(CheckBox.class);
        assertNotNull(cb);
    }


    @Test
    public void testDisabled(FxRobot robot) {
        TextField fld = robot.lookup("#fieldNazivUniverziteta").queryAs(TextField.class);
        CheckBox cb = robot.lookup("#cbUniverzitetski").queryAs(CheckBox.class);

        // Sarajevo je univerzitetski grad
        assertTrue(cb.isSelected());

        // Naziv univerziteta nije disabled
        assertFalse(fld.isDisabled());

        // Naziv univerziteta je ispravan
        assertEquals("Univerzitet u Sarajevu", fld.getText());
    }

    @Test
    public void testCbAction(FxRobot robot) {
        // Proglašavamo da grad nije univerzitetski
        robot.clickOn("#cbUniverzitetski");

        // Naziv univerziteta je disabled
        TextField fld = robot.lookup("#fieldNazivUniverziteta").queryAs(TextField.class);
        assertTrue(fld.isDisabled());

        // Klik na dugme ok
        robot.clickOn("#btnOk");

        // Vraćen je grad koji više nije univerzitetski
        Grad grad = ctrl.getGrad();
        assertFalse(grad instanceof UniverzitetskiGrad);
    }

    @Test
    public void testValidacijaNazivaUniverziteta(FxRobot robot) {
        TextField fld = robot.lookup("#fieldNazivUniverziteta").queryAs(TextField.class);

        // Brišemo postojeću vrijednost naziva univerziteta
        robot.clickOn("#fieldNazivUniverziteta");
        robot.press(KeyCode.CONTROL).press(KeyCode.A).release(KeyCode.A).release(KeyCode.CONTROL);
        robot.press(KeyCode.DELETE).release(KeyCode.DELETE);

        // Klik na dugme ok
        robot.clickOn("#btnOk");

        // Naziv univerziteta nije validan
        Background bg = fld.getBackground();
        boolean colorFound = false;
        for (BackgroundFill bf : bg.getFills())
            if (bf.getFill().toString().contains("ffb6c1"))
                colorFound = true;
        assertTrue(colorFound);
    }

    @Test
    public void testPromjenaNazivaUniverziteta(FxRobot robot) {
        // Dvaput ćemo kliknuti na cb da malo testiramo
        robot.clickOn("#cbUniverzitetski");
        robot.clickOn("#cbUniverzitetski");

        // Polje naziv univerziteta po defaultu treba biti prazno
        robot.clickOn("#fieldNazivUniverziteta");
        // Brišemo otkucani tekst
        robot.press(KeyCode.CONTROL).press(KeyCode.A).release(KeyCode.A).release(KeyCode.CONTROL);
        robot.write("proba");

        // Klik na dugme ok
        robot.clickOn("#btnOk");

        // Vraćen je grad koji je univerzitetski
        Grad grad = ctrl.getGrad();
        assertTrue(grad instanceof UniverzitetskiGrad);
        UniverzitetskiGrad ug = (UniverzitetskiGrad) grad;
        assertEquals("proba", ug.getNazivUniverziteta());
    }
}