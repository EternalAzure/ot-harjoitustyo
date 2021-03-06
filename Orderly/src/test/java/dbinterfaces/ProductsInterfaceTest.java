package dbinterfaces;
import fi.orderly.logic.ServerConnection;
import fi.orderly.logic.dbinterfaces.DatabaseAccess;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static org.junit.Assert.*;

public class ProductsInterfaceTest {

    final String database = ServerConnection.TEST_DATABASE;
    Connection connection = ServerConnection.createConnection(database);
    DatabaseAccess db = new DatabaseAccess(connection);

    @Before
    public void setUp() throws SQLException {
        assert connection != null;
        db.truncateAll();

        String insertRooms = "INSERT INTO rooms (room) VALUES ('Room 1')";
        String insertProducts = "INSERT INTO products (product, code, unit, room_id) VALUES ('Nauris', '1000', 'KG', 1)";

        PreparedStatement sql6 = connection.prepareStatement(insertRooms);
        PreparedStatement sql7 = connection.prepareStatement(insertProducts);

        sql6.executeUpdate(insertRooms);
        sql7.executeUpdate(insertProducts);
    }

    @Test
    public void insertProduct() throws SQLException {
        db.products.insertProduct("Kaali", 2000, "KG", 1);
        assertEquals(2, db.products.size());

        db.products.insertProduct("Porkkana", 3000, "KG", 6, 1);
        assertEquals(3, db.products.size());

        db.products.insertProduct("Peruna", 4000, "KG", 1);
        assertEquals(4, db.products.size());
    }

    @Test
    public void deleteProduct() throws SQLException {
        db.products.deleteProduct(1000);
        assertEquals(0, db.products.size());

        db.products.insertProduct("Kaali", 1000, "KG", 1);
        db.products.deleteProduct(1000);
        assertEquals(0, db.products.size());

        //Test ON DELETE CASCADE
        db.truncateAll();
        db.rooms.insertRoom("Room 1");
        db.rooms.insertRoom("Room 2");
        db.products.insertProduct("Kaali", 1000, "KG", 1);
        db.products.insertProduct("Porkkana", 2000, "KG", 1);
        db.products.insertProduct("Peruna", 3000, "KG", 2);
        db.balance.insertBalance(1, 1, 1, 1);
        db.balance.insertBalance(1, 2, 1, 1);
        db.balance.insertBalance(2, 3, 1, 1);

        db.products.deleteProduct(1000);
        db.products.deleteProduct(3000);
        //Rooms don't disappear
        assertEquals(2, db.rooms.size());
        //Products were indeed deleted
        assertEquals(1, db.products.size());
        //Did cascade
        assertEquals(1, db.balance.size());
    }

    @Test
    public void foundProduct() throws SQLException {
        assertTrue(db.products.foundProduct("Nauris"));
        assertFalse(db.products.foundProduct("Peruna"));
    }

    @Test
    public void findIdByName() throws SQLException {
        assertEquals(1, db.products.findIdByName("Nauris"));
    }

    @Test
    public void findIdByCode() throws SQLException {
        assertEquals(1, db.products.findIdByCode(1000));
    }

    @Test
    public void findCodeById() throws SQLException {
        assertEquals(1000, db.products.findCodeById(1));
    }

    @Test
    public void load50() throws SQLException {
        int[] list = db.products.load50(0);
        assertEquals(1, list[0]);
        assertEquals(0, list[1]);

        db.products.insertProduct("A", 2000, "PKT", 1);
        db.products.insertProduct("B", 3000, "PKT", 1);
        db.products.insertProduct("C", 4000, "PKT", 1);

        list = db.products.load50(0);
        assertEquals(1, list[0]);
        assertEquals(2, list[1]);
        assertEquals(3, list[2]);
        assertEquals(4, list[3]);
        assertEquals(0, list[4]);

        list = db.products.load50(2);
        assertEquals(3, list[0]);
        assertEquals(4, list[1]);
        assertEquals(0, list[2]);
        assertEquals(0, list[3]);
        assertEquals(0, list[4]);
    }
}
