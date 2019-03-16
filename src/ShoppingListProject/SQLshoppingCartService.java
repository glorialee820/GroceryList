package ShoppingListProject;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.*;


public class SQLshoppingCartService {


    private static final String URL = "jdbc:mysql://127.0.0.1:3306/shoppingListApp"; //database in mysql
    private static final String USERNAME = "root";
    private static final String PASSWORD = "BADgers2016";

    private Connection connection;
    private PreparedStatement selectAllUnboughtItems;
    private PreparedStatement insertItemInfo;
    private PreparedStatement deleteTableAfterOneTimeUse;
    private PreparedStatement changeSavedPrioritiesToOne;


    public SQLshoppingCartService() {

        try {
            connection = DriverManager.getConnection(URL,USERNAME,PASSWORD);


            selectAllUnboughtItems = connection.prepareStatement(
                    "SELECT * FROM unbought_items");
            insertItemInfo = connection.prepareStatement(
                    "INSERT INTO unbought_items(item_name, price_per_unit, item_quantity, item_priority) VALUES(?,?,?,?)");
            changeSavedPrioritiesToOne = connection.prepareStatement
                    ("UPDATE unbought_items SET item_priority = 1");
            deleteTableAfterOneTimeUse = connection.prepareStatement
                    ("DELETE FROM unbought_items");

        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }

    }


    ObservableList<ShoppingItem> previousShoppingList = FXCollections.observableArrayList();
    public ObservableList<ShoppingItem> getPreviousShoppingList() {

        try {
            ResultSet resultSet = selectAllUnboughtItems.executeQuery();

            while (resultSet.next()) {
                previousShoppingList.add(new ShoppingItem(
                        resultSet.getString("item_name"),
                        resultSet.getDouble("price_per_unit"),
                        resultSet.getInt("item_quantity"),
                        resultSet.getInt("item_priority")));
            }

        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }

       return previousShoppingList;
    }

    public void clearTable(){
        try{
            deleteTableAfterOneTimeUse.executeUpdate();

        }catch(SQLException sqlException){
            sqlException.printStackTrace();
        }
    }

    public int addItem(String itemName, double itemPrice, int itemQuantity, int itemPriority){


        try{
            insertItemInfo.setString(1, itemName);
            insertItemInfo.setDouble(2, itemPrice);
            insertItemInfo.setInt(3, itemQuantity);
            insertItemInfo.setInt(4, itemPriority);

            return insertItemInfo.executeUpdate();
        }

        catch (SQLException sqlException){
            sqlException.printStackTrace();
            return 0;
        }
    }

    public void changePriorities(){
        try {
            changeSavedPrioritiesToOne.executeUpdate();
        }catch(SQLException sqlException){
            sqlException.printStackTrace();
        }
    }

    public void closeConnection(){
        try{
            connection.close();
        }
        catch (SQLException sqlException){
            sqlException.printStackTrace();
        }
    }
}

