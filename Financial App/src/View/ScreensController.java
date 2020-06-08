package View;

import java.util.HashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import Entities.BankServices;
import Entities.Company;
import Entities.CompanyAnalyzer;
import Entities.CompanySegment;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.DoubleProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;

import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;


public class ScreensController extends StackPane {

    private HashMap<String, Node> screens = new HashMap<>();

    private HashMap<String, ControlledScreen> controllers = new HashMap<>();

    private CompanyAnalyzer analyzer = new CompanyAnalyzer();

    public ScreensController() {
        super();
    }

    public void addScreen(String name, Node screen) {
        screens.put(name, screen);
    }

    public Node getScreen(String name) {
        return screens.get(name);
    }

    public boolean loadScreen(String name, String resource) {
        try {
            FXMLLoader myLoader = new FXMLLoader(getClass().getResource(resource));
            Parent loadScreen = myLoader.load();
            ControlledScreen myScreenController = myLoader.getController();
            myScreenController.setScreenParent(this);
            addScreen(name, loadScreen);
            controllers.put(name, myScreenController);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean changeScreen(final String name) {
        if (screens.get(name) != null) {   //screen loaded
            final DoubleProperty opacity = opacityProperty();

            if (!getChildren().isEmpty()) {    //if there is more than one screen
                Timeline fade = new Timeline(
                        new KeyFrame(Duration.ZERO, new KeyValue(opacity, 1.0)),
                        new KeyFrame(new Duration(200), t -> {
                            getChildren().remove(0);
                            getChildren().add(0, screens.get(name));
                            Timeline fadeIn = new Timeline(
                                    new KeyFrame(Duration.ZERO, new KeyValue(opacity, 0.0)),
                                    new KeyFrame(new Duration(200), new KeyValue(opacity, 1.0)));
                            fadeIn.play();
                        }, new KeyValue(opacity, 0.0)));
                fade.play();

            } else {
                setOpacity(0.0);
                getChildren().add(screens.get(name));       //no one else been displayed, then just show
                Timeline fadeIn = new Timeline(
                        new KeyFrame(Duration.ZERO, new KeyValue(opacity, 0.0)),
                        new KeyFrame(new Duration(200), new KeyValue(opacity, 1.0)));
                fadeIn.play();
            }
            return true;
        } else {
            System.out.println("screen hasn't been loaded!!! \n");
            return false;
        }
    }

    public boolean unloadScreen(String name) {
        if (screens.remove(name) == null) {
            System.out.println("Screen didn't exist");
            return false;
        } else {
            return true;
        }
    }


    public void initTable(boolean underPolicy) {

        Screen1Controller menuController = (Screen1Controller)controllers.get(ScreensFramework.screen1ID);
        BankServices serviceOption = menuController.getServiceOption();
        CompanySegment segment = menuController.getSegment();

        Screen2Controller tableController = (Screen2Controller)controllers.get(ScreensFramework.screen2ID);
        tableController.initTableView(
                analyzer.getRecommendedCompaniesBy(serviceOption, segment, underPolicy).collect(Collectors.toList())
        );
        tableController.setTitle(menuController.getSegment(), menuController.getServiceOption());
        tableController.initColumns(serviceOption);
    }

    public Stream<Company> getCompanies(boolean underPolicy) {

        Screen1Controller menuController = (Screen1Controller)controllers.get(ScreensFramework.screen1ID);
        BankServices serviceOption = menuController.getServiceOption();
        CompanySegment segment = menuController.getSegment();

        return analyzer.getRecommendedCompaniesBy(serviceOption, segment, underPolicy);
    }

    public boolean loadCompanyScreen(Company company) {
        try {
            FXMLLoader myLoader = new FXMLLoader(getClass().getResource(ScreensFramework.screen3File));
            Parent loadScreen = myLoader.load();
            CompanyController companyScreenController = myLoader.getController();
            companyScreenController.initData(company);

            Scene companyScene = new Scene(loadScreen);
            //companyScene.getStylesheets().add("Table styles.css");
            Stage companyStage = new Stage();
            companyStage.setScene(companyScene);
            companyStage.setTitle(company.getName());
            companyStage.show();

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public void resizeStage(double width, double height) {
        this.getScene().getWindow().setWidth(width);
        this.getScene().getWindow().setHeight(height);
    }
}
