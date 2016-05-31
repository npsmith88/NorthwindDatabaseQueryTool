/*Java III
 Nic Smith
 Midterm Project - Northwind Database Query Tool */
package northwinddatabasequerytool;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.layout.GridPane;
import javafx.util.Callback;

public class NorthwindDatabaseQueryTool extends Application {

    Statement statement;
    Connection connection;
    GridPane gridPane = new GridPane();
    Label label1 = new Label("Print Order Total For A Given Order Number");
    Label label2 = new Label("Print All Order Details For A Given Order Number");
    Label label3 = new Label("Print The Names And Cities Of All Customers In A Given State");
    Label label4 = new Label("Print The Names Of All Employees Who Have A Birthday In A Given Year");
    TextField textField1 = new TextField();
    TextField textField2 = new TextField();
    TextField textField3 = new TextField();
    TextField textField4 = new TextField();
    Button button1 = new Button("Execute");
    Button button2 = new Button("Execute");
    Button button3 = new Button("Execute");
    Button button4 = new Button("Execute");
    String text1 = textField1.toString();
    String text2 = textField2.toString();
    String text3 = textField3.toString();
    String text4 = textField4.toString();
    TableView tableView = new TableView();
    private ResultSet resultSet;

    @Override
    public void start(Stage primaryStage) {
        gridPane.add(label1, 0, 0);
        gridPane.add(textField1, 1, 0);
        gridPane.add(button1, 2, 0);
        gridPane.add(label2, 0, 1);
        gridPane.add(textField2, 1, 1);
        gridPane.add(button2, 2, 1);
        gridPane.add(label3, 0, 2);
        gridPane.add(textField3, 1, 2);
        gridPane.add(button3, 2, 2);
        gridPane.add(label4, 0, 3);
        gridPane.add(textField4, 1, 3);
        gridPane.add(button4, 2, 3);

        // Print Order Total For A Given Order Number
        button1.setOnAction(e -> {
            try {
                String query = "SELECT OrderID, UnitPrice * Quantity FROM \"Order Details\" WHERE OrderID = " + textField1.getText();
                statement = connection.createStatement();
                resultSet = statement.executeQuery(query);
                populateTableView(resultSet);
            } catch (SQLException ex) {
                Logger.getLogger(NorthwindDatabaseQueryTool.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        // Print All Order Details For A Given Order Number
        button2.setOnAction(e -> {
            try {
                String query = "SELECT * FROM \"Order Details\" WHERE OrderID = " + textField2.getText();
                statement = connection.createStatement();
                resultSet = statement.executeQuery(query);
                populateTableView(resultSet);
            } catch (SQLException ex) {
                Logger.getLogger(NorthwindDatabaseQueryTool.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        // Print The Names And Cities Of All Customers In A Given State
        button3.setOnAction(e -> {
            try {
                String query = "SELECT * FROM Customers WHERE Region = '" + textField3.getText() + "'";
                statement = connection.createStatement();
                resultSet = statement.executeQuery(query);
                populateTableView(resultSet);
            } catch (SQLException ex) {
                Logger.getLogger(NorthwindDatabaseQueryTool.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        // Print The Names Of All Employees Who Have A Birthday In A Given Year
        button4.setOnAction(e -> {
            try {
                String query = "SELECT * FROM Employees WHERE year(BirthDate) = " + textField4.getText();
                statement = connection.createStatement();
                resultSet = statement.executeQuery(query);
                populateTableView(resultSet);
            } catch (SQLException ex) {
                Logger.getLogger(NorthwindDatabaseQueryTool.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        BorderPane borderPane = new BorderPane();
        borderPane.setCenter(gridPane);
        borderPane.setBottom(tableView);
        Scene scene = new Scene(borderPane, 950, 500);
        primaryStage.setTitle("Northwind Database Query Tool");
        primaryStage.setScene(scene);
        primaryStage.show();
        connectToDB();
    }

    public void connectToDB() {
        // Connection to the database
        try {
            // Load the JDBC driver
            Class.forName("jstels.jdbc.mdb.MDBDriver2");
            System.out.println("Driver loaded");

            // Establish a connection to the database
            connection = DriverManager.getConnection(
                    "jdbc:jstels:mdb:C:/data/Northwind.mdb");
            System.out.println("Database connected");
        } catch (java.lang.Exception ex) {
            ex.printStackTrace();
        }
    }

    private void populateTableView(ResultSet rs) {
        ObservableList<ObservableList> data = FXCollections.observableArrayList();
        try {
            for (int i = 0; i < rs.getMetaData().getColumnCount(); i++) {
                // We are using non property style for making dynamic table
                final int j = i;
                TableColumn col = new TableColumn(rs.getMetaData().getColumnName(i + 1));

                // col.setCellValueFactory(TextFieldTableCell.forTableColumn());
                col.setCellValueFactory(new Callback<CellDataFeatures<ObservableList, String>, ObservableValue<String>>() {
                    public ObservableValue<String> call(CellDataFeatures<ObservableList, String> param) {
                        if (param == null || param.getValue() == null || param.getValue().get(j) == null) {
                            return null;
                        }
                        return new SimpleStringProperty(param.getValue().get(j).toString());
                    }
                });

                tableView.getColumns().addAll(col);
                System.out.println("Column [" + i + "] ");
            }

            while (rs.next()) {
                // Iterate Row
                ObservableList<String> row = FXCollections.observableArrayList();
                for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
                    // Iterate Column
                    row.add(rs.getString(i));
                }
                System.out.println("Row [1] added " + row);
                data.add(row);

            }

            // Add to TableView
            tableView.setItems(data);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error on Building Data");
        }
    }

    public static void main(String[] args) {
        launch(args);
    }

}
