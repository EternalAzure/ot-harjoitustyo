package fi.orderly.ui.tables;
import fi.orderly.dao.tables.ITable;
import fi.orderly.dao.tables.BalanceTable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;

public class BalanceTableView extends TableViewInfiniteScrolling {


    @Override
    void addItems() {
        int limit = items.size() + 30;
        for (int i = items.size(); i < limit; i++) {
            if (i >= db.balance.size()) {
                break;
            }

            BalanceTable b = new BalanceTable(items.size() + 1, connection);
            if (b.getRoomName().isEmpty()) {
                continue;
            }
            items.add(b);
        }
    }

    @Override
    void setUp() {
        TableColumn<ITable, String> room = new TableColumn<>("Room Name");
        room.setCellValueFactory(new PropertyValueFactory<>("roomName"));

        TableColumn<ITable, String> productName = new TableColumn<>("Product name");
        productName.setCellValueFactory(new PropertyValueFactory<>("productName"));

        TableColumn<ITable, String> code = new TableColumn<>("Product code");
        code.setCellValueFactory(new PropertyValueFactory<>("productCode"));

        TableColumn<ITable, String> batch = new TableColumn<>("Batch");
        batch.setCellValueFactory(new PropertyValueFactory<>("productBatch"));

        TableColumn<ITable, String> amount = new TableColumn<>("amount");
        amount.setCellValueFactory(new PropertyValueFactory<>("productAmount"));

        table.getColumns().setAll(room, productName, code, batch, amount);
    }
}
