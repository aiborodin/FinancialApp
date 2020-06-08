package View;

import Entities.Company;
import Entities.FinStatement;
import javafx.beans.property.ReadOnlyIntegerWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.net.URL;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;

public class CompanyController implements Initializable, ControlledScreen {

    private ScreensController myController;

    @FXML
    private Label regCodeLabel;

    @FXML
    private Label directorLabel;

    @FXML
    private Label dirBirthLabel;

    @FXML
    private Label segmentLabel;

    @FXML
    private TableView<Pair<Integer, FinStatement>> finDataTable;

    @FXML
    private TableColumn<Pair<Integer, FinStatement>, Number> yearCol;

    @FXML
    private TableColumn<Pair<Integer, FinStatement>, Number> incomeCol;

    @FXML
    private TableColumn<Pair<Integer, FinStatement>, Number> profitCol;

    @FXML
    private TableColumn<Pair<Integer, FinStatement>, Number> loansCol;

    @FXML
    private TableColumn<Pair<Integer, FinStatement>, Number> finExpensesCol;

    @FXML
    private Button closeButton;

    @FXML
    private Label companyNameLabel;

    @Override
    public void setScreenParent(ScreensController screenPage) {
        myController = screenPage;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        IntegerStringConverter converter = new IntegerStringConverter();

        yearCol.setCellValueFactory(p -> new ReadOnlyIntegerWrapper(p.getValue().getKey()));
        yearCol.setCellFactory(TextFieldTableCell.forTableColumn(converter));

        incomeCol.setCellValueFactory(p -> new ReadOnlyIntegerWrapper(p.getValue().getValue().getIncome()));
        incomeCol.setCellFactory(TextFieldTableCell.forTableColumn(converter));

        profitCol.setCellValueFactory(p -> new ReadOnlyIntegerWrapper(p.getValue().getValue().getProfit()));
        profitCol.setCellFactory(TextFieldTableCell.forTableColumn(converter));

        loansCol.setCellValueFactory(p -> new ReadOnlyIntegerWrapper(p.getValue().getValue().getLoansSum()));
        loansCol.setCellFactory(TextFieldTableCell.forTableColumn(converter));

        finExpensesCol.setCellValueFactory(p -> new ReadOnlyIntegerWrapper(p.getValue().getValue().getFinExpenses()));
        finExpensesCol.setCellFactory(TextFieldTableCell.forTableColumn(converter));
    }

    public void initData(Company company) {
        companyNameLabel.setText(company.getName());

        regCodeLabel.setText(String.valueOf(company.getRegistrationCode()));

        directorLabel.setText(company.getDirectorName());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate birthDay = company.getDirectorBirthDate();
        dirBirthLabel.setText(birthDay.format(formatter) + " ( " + Period.between(birthDay, LocalDate.now()).getYears() + " yrs.)");

        segmentLabel.setText(company.getSegment().name());

        finDataTable.getItems().addAll(company.getAllStatements());
    }

    @FXML
    private void closeScreen() {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }
}
