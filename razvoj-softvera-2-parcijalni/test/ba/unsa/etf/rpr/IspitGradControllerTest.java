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

import static javafx.scene.layout.Region.USE_COMPUTED_SIZE;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(ApplicationExtension.class)
public class IspitGradControllerTest {
    Stage theStage;
    GradController ctrl;

    @Start
    public void start(Stage stage) throws Exception {
        GeografijaDAO dao = GeografijaDAO.getInstance();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/grad.fxml"));
        ctrl = new GradController(null, dao.drzave());
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
    public void testPoljaPostoje(FxRobot robot) {
        TextField fld = robot.lookup("#fieldNazivUniverziteta").queryAs(TextField.class);
        assertNotNull(fld);
        CheckBox cb = robot.lookup("#cbUniverzitetski").queryAs(CheckBox.class);
        assertNotNull(cb);
    }


    @Test
    public void testDisabled(FxRobot robot) {
        TextField fld = robot.lookup("#fieldNazivUniverziteta").queryAs(TextField.class);
        CheckBox cb = robot.lookup("#cbUniverzitetski").queryAs(CheckBox.class);

        // Grad po defaultu nije univerzitetski
        assertFalse(cb.isSelected());

        // Polje je disabled
        assertTrue(fld.isDisabled());

        // Klikamo
        robot.clickOn("#cbUniverzitetski");
        assertFalse(fld.isDisabled());

        // Opet klikamo
        robot.clickOn("#cbUniverzitetski");
        assertTrue(fld.isDisabled());
    }

    @Test
    public void testValidacijaNazivaUniverziteta(FxRobot robot) {
        // Grad po defaultu nije univerzitetski
        CheckBox cb = robot.lookup("#cbUniverzitetski").queryAs(CheckBox.class);
        assertFalse(cb.isSelected());

        // Proglašavamo ga za univerzitetski
        robot.clickOn("#cbUniverzitetski");

        // Polje naziv univerziteta po defaultu treba biti prazno
        robot.clickOn("#fieldNazivUniverziteta");

        // Klik na dugme ok
        robot.clickOn("#btnOk");

        // Naziv univerziteta nije validan
        TextField fld = robot.lookup("#fieldNazivUniverziteta").queryAs(TextField.class);
        Background bg = fld.getBackground();
        boolean colorFound = false;
        for (BackgroundFill bf : bg.getFills())
            if (bf.getFill().toString().contains("ffb6c1"))
                colorFound = true;
        assertTrue(colorFound);
    }

    @Test
    public void testValidacijaNazivaUniverziteta2(FxRobot robot) {
        // Grad po defaultu nije univerzitetski
        // Proglašavamo ga za univerzitetski
        robot.clickOn("#cbUniverzitetski");

        // Polje naziv univerziteta po defaultu treba biti prazno
        robot.clickOn("#fieldNazivUniverziteta");
        robot.write("proba");

        // Klik na dugme ok
        robot.clickOn("#btnOk");
        TextField fld = robot.lookup("#fieldNazivUniverziteta").queryAs(TextField.class);
        Background bg = fld.getBackground();
        boolean colorFound = false;
        for (BackgroundFill bf : bg.getFills())
            if (bf.getFill().toString().contains("adff2f"))
                colorFound = true;
        assertTrue(colorFound);
    }

    @Test
    public void testValidacijaNazivaUniverziteta3(FxRobot robot) {
        // Grad po defaultu nije univerzitetski
        // Proglašavamo ga za univerzitetski
        robot.clickOn("#cbUniverzitetski");

        // Polje naziv univerziteta po defaultu treba biti prazno
        robot.clickOn("#fieldNazivUniverziteta");
        robot.write("proba");
        // Brišemo otkucani tekst
        robot.press(KeyCode.CONTROL).press(KeyCode.A).release(KeyCode.A).release(KeyCode.CONTROL);
        robot.press(KeyCode.DELETE).release(KeyCode.DELETE);

        // Klik na dugme ok
        robot.clickOn("#btnOk");

        // Naziv univerziteta nije validan
        TextField fld = robot.lookup("#fieldNazivUniverziteta").queryAs(TextField.class);
        Background bg = fld.getBackground();
        boolean colorFound = false;
        for (BackgroundFill bf : bg.getFills())
            if (bf.getFill().toString().contains("ffb6c1"))
                colorFound = true;
        assertTrue(colorFound);
    }
}