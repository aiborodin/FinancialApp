package View;

import Entities.BankServices;
import Entities.Company;
import Entities.CompanySegment;
import javafx.beans.property.*;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.Callback;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.awt.*;
import java.io.*;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Screen2Controller implements ControlledScreen {

    private ScreensController myController;

    @FXML
    private TableView<Company> companiesTable;

    @FXML
    private TableColumn<Company, String> nameCol;

    @FXML
    private TableColumn<Company, Number> finDataCol;

    @FXML
    private TableColumn<Company, String> buttonCol;

    @FXML
    private Button menuButton;

    @FXML
    private Button exportButton;

    @FXML
    private Button view_Potential_Button;

    @FXML
    private Label serviceOptionLabel;

    private Company selectedCompany;

    private BankServices lastOption;

    private String finIndicatorName;

    private boolean viewPotentialSelected = false;

    public void initColumns(BankServices servicesOption) {

        this.lastOption = servicesOption;

        nameCol.setCellValueFactory(p -> new ReadOnlyStringWrapper(p.getValue().getName()));

        switch (servicesOption) {
            case CREDITING:
                finIndicatorName = "Loans Sum";
                finDataCol.setCellValueFactory(p -> new SimpleIntegerProperty(p.getValue().getLastYearStatement().getLoansSum()));
                break;
            case DEPOSITS:
                finIndicatorName = "Cash & Equivalents";
                finDataCol.setCellValueFactory(p -> new SimpleIntegerProperty(p.getValue().getLastYearStatement().getCashAndEquivalents()));
                break;
            case FINANCIAL_SERVICES:
                finIndicatorName = "Financial Expenses";
                finDataCol.setCellValueFactory(p -> new SimpleIntegerProperty(p.getValue().getLastYearStatement().getFinExpenses()));
                break;
        }
        finDataCol.setText(finIndicatorName + " (thsnd.)");

        Callback<TableColumn<Company, String>, TableCell<Company, String>> cellFactory = param -> new TableCell<Company, String>() {

            final Button btn = new Button("Show");

            @Override
            public void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if(empty) {
                    setGraphic(null);
                } else {
                    btn.setOnAction(event -> {
                        selectedCompany = getTableView().getItems().get(getIndex());
                        myController.loadCompanyScreen(selectedCompany);
                    });
                    setGraphic(btn);

                }
                setText(null);
            }
        };
        buttonCol.setCellFactory(cellFactory);
    }


    @Override
    public void setScreenParent(ScreensController screenPage) {
        myController = screenPage;
    }

    @FXML
    private void goToMenu() {
        myController.changeScreen(ScreensFramework.screen1ID);
        myController.resizeStage(535, 355);
    }

    @FXML
    private void exportToExcel() {
        Workbook workbook = new XSSFWorkbook();

        String sheetName;
        if (!viewPotentialSelected) {
            sheetName = "Recommended for " + lastOption.name();
        } else {
            sheetName = "Potential for " + lastOption.name();
        }
        Sheet sheet = workbook.createSheet(sheetName);
        sheet.setColumnWidth(0, 6000);
        sheet.setColumnWidth(1, 6000);

        Row header = sheet.createRow(0);

        CellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setFillForegroundColor(IndexedColors.LIGHT_CORNFLOWER_BLUE.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        XSSFFont font = ((XSSFWorkbook)workbook).createFont();
        font.setFontName("Arial");
        font.setFontHeightInPoints((short) 12);
        font.setBold(true);
        headerStyle.setFont(font);

        Cell headerCell = header.createCell(0);
        headerCell.setCellValue("Company Name");
        headerCell.setCellStyle(headerStyle);

        headerCell = header.createCell(1);

        headerCell.setCellValue(finIndicatorName);
        headerCell.setCellStyle(headerStyle);

        CellStyle style = workbook.createCellStyle();
        style.setWrapText(true);

        Row row;
        Cell cell;
        ObservableList<Company> companies = companiesTable.getItems();

        for (int i = 0; i < companies.size(); i++) {
            row = sheet.createRow(i+1);

            cell = row.createCell(0);
            cell.setCellValue(companies.get(i).getName());
            cell.setCellStyle(style);

            cell = row.createCell(1);
            switch (lastOption) {
                case CREDITING:
                    cell.setCellValue(companies.get(i).getLastYearStatement().getLoansSum());
                    break;
                case DEPOSITS:
                    cell.setCellValue(companies.get(i).getLastYearStatement().getCashAndEquivalents());
                    break;
                case FINANCIAL_SERVICES:
                    cell.setCellValue(companies.get(i).getLastYearStatement().getFinExpenses());
                    break;
            }
            cell.setCellStyle(style);
        }

        File currDir = new File(".");
        String path = currDir.getAbsolutePath();
        String fileLocation = path.substring(0, path.length() - 1) + "companies.xlsx";

        FileOutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(fileLocation);
            workbook.write(outputStream);
            workbook.close();
            Desktop desktop = Desktop.getDesktop();
            desktop.open(new File(fileLocation));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void viewPotentialButtonClicked() {
        Stream<Company> recommendedCompanies = myController.getCompanies(viewPotentialSelected);
        initTableView(recommendedCompanies.collect(Collectors.toList()));
        if (!viewPotentialSelected) {
            view_Potential_Button.setText("View Recommended Clients");
            viewPotentialSelected = true;
        } else {
            view_Potential_Button.setText("View Potential Clients");
            viewPotentialSelected = false;
        }
    }

    public void initTableView(List<Company> companies) {
        companiesTable.getItems().clear();
        companiesTable.getItems().addAll(companies);
    }

    public void setTitle(CompanySegment segment, BankServices option) {
        String beginning;
        if (viewPotentialSelected) {
            beginning = "Potential ";
        } else {
            beginning = "Recommended ";
        }
        serviceOptionLabel.setText(
                beginning + segment.name() + " Companies for " + option.name()
        );
    }
}



