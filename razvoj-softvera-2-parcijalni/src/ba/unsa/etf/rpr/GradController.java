package ba.unsa.etf.rpr;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.ArrayList;

public class GradController {
    public TextField fieldNaziv;
    public TextField fieldBrojStanovnika;
    public ChoiceBox<Drzava> choiceDrzava;
    public ObservableList<Drzava> listDrzave;
    public CheckBox cbUniverzitetski;
    public TextField fieldNazivUniverziteta;
    private Grad grad;

    public GradController(Grad grad, ArrayList<Drzava> drzave) {
        this.grad = grad;
        listDrzave = FXCollections.observableArrayList(drzave);
    }

    @FXML
    public void initialize() {
        choiceDrzava.setItems(listDrzave);
        if (grad != null) {
            fieldNaziv.setText(grad.getNaziv());
            fieldBrojStanovnika.setText(Integer.toString(grad.getBrojStanovnika()));
            // choiceDrzava.getSelectionModel().select(grad.getDrzava());
            // ovo ne radi jer grad.getDrzava() nije identički jednak objekat kao član listDrzave
            for (Drzava drzava : listDrzave)
                if (drzava.getId() == grad.getDrzava().getId())
                    choiceDrzava.getSelectionModel().select(drzava);
            if (grad instanceof UniverzitetskiGrad) {
                cbUniverzitetski.setSelected(true);
                fieldNazivUniverziteta.setDisable(false);
                fieldNazivUniverziteta.setText(((UniverzitetskiGrad)grad).getNazivUniverziteta());
            } else {
                cbUniverzitetski.setSelected(false);
                fieldNazivUniverziteta.setDisable(true);
            }
        } else {
            choiceDrzava.getSelectionModel().selectFirst();
        }
    }

    public Grad getGrad() {
        return grad;
    }

    public void clickCancel(ActionEvent actionEvent) {
        grad = null;
        Stage stage = (Stage) fieldNaziv.getScene().getWindow();
        stage.close();
    }

    public void clickOk(ActionEvent actionEvent) {
        boolean sveOk = true;

        if (fieldNaziv.getText().trim().isEmpty()) {
            fieldNaziv.getStyleClass().removeAll("poljeIspravno");
            fieldNaziv.getStyleClass().add("poljeNijeIspravno");
            sveOk = false;
        } else {
            fieldNaziv.getStyleClass().removeAll("poljeNijeIspravno");
            fieldNaziv.getStyleClass().add("poljeIspravno");
        }


        int brojStanovnika = 0;
        try {
            brojStanovnika = Integer.parseInt(fieldBrojStanovnika.getText());
        } catch (NumberFormatException e) {
            // ...
        }
        if (brojStanovnika <= 0) {
            fieldBrojStanovnika.getStyleClass().removeAll("poljeIspravno");
            fieldBrojStanovnika.getStyleClass().add("poljeNijeIspravno");
            sveOk = false;
        } else {
            fieldBrojStanovnika.getStyleClass().removeAll("poljeNijeIspravno");
            fieldBrojStanovnika.getStyleClass().add("poljeIspravno");
        }

        if (cbUniverzitetski.isSelected()) {
            if (fieldNazivUniverziteta.getText().trim().isEmpty()) {
                fieldNazivUniverziteta.getStyleClass().removeAll("poljeIspravno");
                fieldNazivUniverziteta.getStyleClass().add("poljeNijeIspravno");
                sveOk = false;
            } else {
                fieldNazivUniverziteta.getStyleClass().removeAll("poljeNijeIspravno");
                fieldNazivUniverziteta.getStyleClass().add("poljeIspravno");
            }
        }

        if (!sveOk) return;

        if (grad == null) grad = new Grad();
        if (cbUniverzitetski.isSelected()) {
            grad = new UniverzitetskiGrad(grad.getId(), fieldNaziv.getText(), Integer.parseInt(fieldBrojStanovnika.getText()), choiceDrzava.getValue(), fieldNazivUniverziteta.getText());
        } else {
            grad = new Grad(grad.getId(), fieldNaziv.getText(), Integer.parseInt(fieldBrojStanovnika.getText()), choiceDrzava.getValue());
        }

        Stage stage = (Stage) fieldNaziv.getScene().getWindow();
        stage.close();
    }


    public void cbAction(ActionEvent actionEvent) {
        if (cbUniverzitetski.isSelected())
            fieldNazivUniverziteta.setDisable(false);
        else
            fieldNazivUniverziteta.setDisable(true);
    }

}
