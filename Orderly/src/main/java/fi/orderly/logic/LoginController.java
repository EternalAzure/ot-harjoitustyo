package fi.orderly.logic;

import fi.orderly.ui.AlertWindow;
import fi.orderly.ui.ConfirmWindow;
import fi.orderly.ui.Hub;
import fi.orderly.ui.Login;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import java.sql.*;

public class LoginController {

    final private Hub hub;

    Connection connection = ServerConnection.createConnection("login");
    public LoginController(Hub hub) {
        this.hub = hub;
    }

    public void login(String username, String password) {
        try {
            PreparedStatement sql = connection.prepareStatement("SELECT name, password, role FROM users WHERE name=?");
            sql.setString(1, username);
            ResultSet resultSet = sql.executeQuery();
            resultSet.next();
            String name = resultSet.getString("name");
            String pw = resultSet.getString("password");
            String role = resultSet.getString("role"); //waits to be implemented

            if (pw.equals(password)) {
                hub.start(new Stage());
                Login.window.close();
            }
        } catch (SQLException e) {
            System.out.println("Empty credentials");
        }
    }

    public void passwordlessLogin() {
        //For testing purposes
        new Hub().start(new Stage());
        Login.window.close();
    }

    public void exit() {
        Platform.exit();
    }
}
