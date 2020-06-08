package View;

import Entities.BankServices;
import Entities.CompanySegment;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;

import java.net.URL;
import java.util.ResourceBundle;

public class Screen1Controller implements Initializable, ControlledScreen {

    ScreensController myController;

    @FXML
    private ComboBox<BankServices> goalComboBox;

    @FXML
    private ComboBox<CompanySegment> segmentComboBox;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public BankServices getServiceOption() {
        return goalComboBox.getValue();
    }

    public CompanySegment getSegment() {
        return segmentComboBox.getValue();
    }

    @Override
    public void setScreenParent(ScreensController screenPage) {
        myController = screenPage;
    }

    @FXML
    private void goToResultTable() {
        if (goalComboBox.getValue() != null && segmentComboBox.getValue() != null) {
            myController.initTable(true);
            myController.changeScreen(ScreensFramework.screen2ID);
            myController.resizeStage(525, 500);
        }
    }

}
