package fi.orderly.logic;

import fi.orderly.logic.dbinterfaces.DeliveriesInterface;
import fi.orderly.logic.dbinterfaces.ProductsInterface;
import fi.orderly.logic.dbinterfaces.RoomsInterface;
import fi.orderly.logic.dbinterfaces.ShipmentsInterface;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Vain ohjelman testaamista varten.
 * Luo tarvittavat tiedot tietokantaan, jotta laitoksessa voidaan testata koko tuotteen elinkaari.
 */
public class TestData {

    RoomsInterface roomsInterface;
    ProductsInterface productsInterface;
    ShipmentsInterface shipmentsInterface;
    DeliveriesInterface deliveriesInterface;

    public TestData(Connection connection) {
        roomsInterface = new RoomsInterface(connection);
        productsInterface = new ProductsInterface(connection);
        shipmentsInterface = new ShipmentsInterface(connection);
        deliveriesInterface = new DeliveriesInterface(connection);
    }

    /**
     * Luo testi dataa tietokantaan, jonka yhteys on annettu konstruktorissa.
     * Tulostaa terminaaliin tiedon, kun valmista.
     */
    public void createShipmentAndDelivery() {
        addRooms();
        addProducts();
        addShipments();
        addDeliveries();
        System.out.println("Created test data");
    }

    private void addRooms() {
        try {
            roomsInterface.insertRoom("Raaka-ainevarasto 1", 4);
            roomsInterface.insertRoom("Raaka-ainevarasto 2", 14);
            roomsInterface.insertRoom("Tuotantotila 1", 14);
            roomsInterface.insertRoom("Valmisvarasto 1", 4);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void addProducts() {
        try {
            productsInterface.insertProduct("Kaali", 1000, "KG", 4, 1);
            productsInterface.insertProduct("Porkkana", 2000, "KG", 4, 1);
            productsInterface.insertProduct("Peruna", 3000, "KG", 4, 1);
            productsInterface.insertProduct("Kurpitsa", 4000, "KG", 14, 2);
            productsInterface.insertProduct("Sipuli", 5000, "KG", 14, 2);

            productsInterface.insertProduct("Kaalilaatikko", 1100, "KG", 4, 4);
            productsInterface.insertProduct("Porkkanasuikaleet", 2200, "KG", 4, 4);
            productsInterface.insertProduct("Perunamuussi", 3300, "KG", 4, 4);
            productsInterface.insertProduct("Kurpitsapalat", 4400, "KG", 4, 4);
            productsInterface.insertProduct("Sipulirenkaat", 5500, "KG", 4, 4);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void addShipments() {
        try {
            shipmentsInterface.insertShipment(1, productsInterface.findIdByName("Kaali"), 1, 10);
            shipmentsInterface.insertShipment(1, productsInterface.findIdByName("Porkkana"), 1, 40);
            shipmentsInterface.insertShipment(1, productsInterface.findIdByName("Peruna"), 1, 160);
            shipmentsInterface.insertShipment(1, productsInterface.findIdByName("Kurpitsa"), 1, 640);
            shipmentsInterface.insertShipment(1, productsInterface.findIdByName("Sipuli"), 1, 2560);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void addDeliveries() {
        try {
            deliveriesInterface.insertDelivery(1, productsInterface.findIdByName("Kaalilaatikko"), 5);
            deliveriesInterface.insertDelivery(1, productsInterface.findIdByName("Porkkanasuikaleet"), 20);
            deliveriesInterface.insertDelivery(1, productsInterface.findIdByName("Perunamuussi"), 80);
            deliveriesInterface.insertDelivery(1, productsInterface.findIdByName("Kurpitsapalat"), 320);
            deliveriesInterface.insertDelivery(1, productsInterface.findIdByName("Sipulirenkaat"), 1280);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
