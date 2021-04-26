package fi.orderly.ui;

import fi.orderly.dao.Shipment;
import fi.orderly.logic.HubController;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import fi.orderly.logic.Utils;
import java.sql.Statement;

public class ShipmentWorkspace {

    Statement statement;
    Utils utils;
    HubController hubController;
    private Shipment shipment;
    final private Label errorMessage = new Label();
    // Has dependency
    // Shipment in getShipmentWorkspace()

    public ShipmentWorkspace(Statement statement){
        this.statement = statement;
        utils = new Utils(statement);
        hubController = new HubController(statement);
        errorMessage.setId("error");
    }

    public VBox getShipmentWorkspace(String shipmentNumber, Button applyButton){
        if (!validateInput(shipmentNumber).isEmpty()) return null;

        //1. Add, set and style header
        VBox vBox = new VBox();
        HBox header = new HBox(new Label("Name"),
                new Label("Code"),
                new Label("Batch"),
                new Label("Amount"),
                new Label("Unit"),
                new Label("Room"));
        for (Node node: header.getChildren()
        ) {
            node.setId("small-field");
        }
        vBox.getChildren().add(header);
        header.setPrefWidth(400);
        header.setId("header");

        //2. Fetch data
        shipment = new Shipment(shipmentNumber, statement);

        //3. Put data to visual table rows
        for (int i = 0; i < shipment.getLength(); i++) {
            vBox.getChildren().add(addRow(shipment.getDataPackage(i)));
        }

        //4. Add button underneath the rows
        vBox.getChildren().addAll(applyButton, errorMessage);
        return vBox;
    }

    private HBox addRow(Shipment.DataPackage data){
        HBox row = new HBox();
        Label productName = new Label(data.getName());
        Label productCode = new Label(data.getCode());
        Label batchNumber = new Label(data.getBatch());
        TextField amount = new TextField(data.getAmount());
        Label unit = new Label(data.getUnit());
        TextField room = new TextField(data.getStorageRoom());

        row.getChildren().addAll(productName, productCode, batchNumber, amount, unit, room);
        row.setId("row1");

        for (Node node: row.getChildren()
             ) {
            node.setId("small-field");
        }

        return row;
    }

    public Shipment getShipment() {
        return shipment;
    }

    public String validateInput(String input){
        if (input.isEmpty() || !Utils.isInt(input)) return "Input needs to be integer";
        String query = "SELECT COUNT(*) FROM shipments WHERE number=" + input;
        int deliverySize = utils.getResultInt(query, "COUNT(*)");
        if (deliverySize == 0) return "No shipments found";

        return "";
    }

    public void setErrorMessage(String message) {
        errorMessage.setText(message);
        if (message.equals("Success")) errorMessage.setId("success");
    }

}
