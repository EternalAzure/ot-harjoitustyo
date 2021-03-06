package dbinterfaces;

import fi.orderly.logic.ServerConnection;
import fi.orderly.logic.dbinterfaces.DatabaseAccess;
import org.junit.Before;
import org.junit.Test;

import java.sql.*;

import static org.junit.Assert.*;

public class BalanceInterfaceTest {

    final String database = ServerConnection.TEST_DATABASE;
    Connection connection = ServerConnection.createConnection(database);
    DatabaseAccess db = new DatabaseAccess(connection);

    @Before
    public void setUp() throws SQLException {
        assert connection != null;
        db.truncateAll();

        String insertRooms = "INSERT INTO rooms (room) VALUES ('Room 1')";
        String insertProducts = "INSERT INTO products (product, code, unit, room_id) VALUES ('Nauris', 1000, 'KG', 1)";
        String insertBalance = "INSERT INTO balance (room_id, product_id, batch, amount) VALUES (1, 1, 1, 100.0)";
        PreparedStatement sql1 = connection.prepareStatement(insertRooms);
        PreparedStatement sql2 = connection.prepareStatement(insertProducts);
        PreparedStatement sql3 = connection.prepareStatement(insertBalance);

        sql1.executeUpdate();
        sql2.executeUpdate();
        sql3.executeUpdate();
    }

    @Test
    public void size() throws SQLException {
        assertEquals(1, db.balance.size());

        String insertBalance = "INSERT INTO balance (room_id, product_id, batch, amount) VALUES (1, 1, 2, 100.0)";
        PreparedStatement sql = connection.prepareStatement(insertBalance);
        sql.executeUpdate(insertBalance);
        assertEquals(2, db.balance.size());

        db.balance.truncate();
        assertEquals(0, db.balance.size());
    }

    @Test
    public void insertBalance() throws SQLException {
        db.balance.insertBalance(1, 1, 2, 200);
        assertEquals(2, db.balance.size());

        db.balance.insertBalance(1, 1, 3, 300);
        assertEquals(3, db.balance.size());
    }

    @Test
    public void updateBalance() throws SQLException {
        db.balance.updateBalance(1, 1, 1, 200);

        String select = "SELECT amount FROM balance WHERE room_id=1 AND product_id=1 AND batch=1";
        PreparedStatement sql = connection.prepareStatement(select);
        ResultSet resultSet = sql.executeQuery();
        resultSet.next();
        double saldo = resultSet.getDouble("amount");

        assertEquals(200, saldo, 0);
    }

    @Test
    public void deleteBalance() throws SQLException {
        db.balance.deleteBalance(1, 1, 1);
        assertEquals(0, db.balance.size());
    }

    @Test
    public void queryBalance() throws SQLException {
        Statement statement = connection.createStatement();
        statement.executeUpdate("INSERT INTO rooms (room) VALUES ('Room 2')");
        statement.executeUpdate("INSERT INTO products (product, code, unit, room_id) VALUES ('Kaali', 2000, 'KG', 2)");
        statement.executeUpdate("INSERT INTO balance (room_id, product_id, batch, amount) VALUES (2, 2, 2, 200)");
        statement.executeUpdate("INSERT INTO balance (room_id, product_id, batch, amount) VALUES (1, 1, 2, 300)");

        assertEquals(100, db.balance.queryBalance(1, 1, 1), 0);

        assertEquals(200, db.balance.queryBalance(2, 2, 2), 0);

        assertEquals(300, db.balance.queryBalance(1, 1, 2), 0);
    }

    @Test
    public void foundBalance() throws SQLException {
        assertTrue(db.balance.foundBalance(1, 1, 1));
        assertFalse(db.balance.foundBalance(1, 1, 2));
    }

    @Test
    public void findId() throws SQLException {
        assertEquals(1, db.balance.findId(1, 1, 1));

        db.balance.insertBalance(1, 1, 2, 200);
        db.balance.insertBalance(1, 1, 3, 300);

        assertEquals(2, db.balance.findId(1, 1, 2));
        assertEquals(3, db.balance.findId(1, 1, 3));
    }

    @Test
    public void load50() throws SQLException {
        int[] list = db.balance.load50(0);
        assertEquals(50, list.length);
        assertEquals(1, list[0]);
        assertEquals(0, list[1]);
        assertEquals(0, list[2]);

        db.balance.truncate();
        list = db.balance.load50(0);
        assertEquals(0, list[0]);
        assertEquals(0, list[1]);
        assertEquals(0, list[2]);

        db.balance.insertBalance(1, 1, 1, 100);
        db.balance.insertBalance(1, 1, 2, 200);
        db.balance.insertBalance(1, 1, 3, 300);
        list = db.balance.load50(0);
        assertEquals(1, list[0]);
        assertEquals(2, list[1]);
        assertEquals(3, list[2]);

        list = db.balance.load50(2);
        assertEquals(3, list[0]);
        assertEquals(0, list[1]);
        assertEquals(0, list[2]);
    }

}
