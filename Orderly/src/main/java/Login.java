import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Login extends Application {

    public static Stage window;

    public static void main(String[] args){ launch(args);}


    @Override
    public void start(Stage loginWindow){
        window = loginWindow;
        window.setResizable(false);
        window.setTitle("Orderly");
        window.initStyle(StageStyle.UNDECORATED);

        //region Pane set up
        BorderPane layout = new BorderPane();
        AnchorPane leftPane = new AnchorPane();
        AnchorPane rightPane = new AnchorPane();
        layout.setRight(rightPane);
        layout.setLeft(leftPane);
        leftPane.setMaxSize(300,400);
        leftPane.setMinSize(300,400);
        rightPane.setMinSize(300,400);
        rightPane.setMaxSize(33,400);
        //endregion
        //region Initialize UI elements
        ImageView forkliftView = new ImageView();
        ImageView logoView = new ImageView();
        Image forklift = new Image("user icon.png");
        Image logo = new Image("ORDERLY_Logo_fix.png");

        Button exit = new Button("X");
        TextField username = new TextField();
        PasswordField password = new PasswordField();
        Button login = new Button("Login");
        Hyperlink credit = new Hyperlink("Image source");
        //endregion
        //region Set children
        leftPane.getChildren().addAll(logoView);
        rightPane.getChildren().addAll(exit, username, password, login, credit, forkliftView);
        //endregion
        //region Set UI positions
        forkliftView.setLayoutX(125);
        forkliftView.setLayoutY(50);
        logoView.setLayoutX(-50);
        logoView.setLayoutY(60);

        login.setLayoutX(115);
        login.setLayoutY(270);

        credit.setLayoutX(107);
        credit.setLayoutY(300);

        exit.setLayoutX(260);
        exit.setLayoutY(10);

        username.setLayoutX(50);
        username.setLayoutY(130);

        password.setLayoutX(50);
        password.setLayoutY(200);
        //endregion

        leftPane.setId("blue-pane");
        forkliftView.setImage(forklift);
        forkliftView.setPreserveRatio(true);
        forkliftView.setFitHeight(50);

        logoView.setImage(logo);
        logoView.setPreserveRatio(true);
        logoView.setFitHeight(280);

        username.setPromptText("Username");
        password.setPromptText("Password");
        exit.setId("exit");

        //Actions
        LoginController logInCont = new LoginController();
        login.setOnAction(event -> logInCont.login());
        exit.setOnAction(event -> logInCont.exit());
        credit.setOnAction(event -> logInCont.link());

        Scene scene = new Scene(layout, 600, 400);
        scene.getStylesheets().add(getClass().getResource("login.css").toExternalForm());
        window.setScene(scene);
        window.show();
    }
}