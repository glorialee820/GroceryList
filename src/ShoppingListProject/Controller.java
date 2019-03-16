package ShoppingListProject;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import java.net.URL;
import java.text.NumberFormat;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    SQLshoppingCartService sqLshoppingCartService = new SQLshoppingCartService();

//Format the price and cost - https://docs.oracle.com/javafx/2/api/javafx/scene/control/Cell.html
    NumberFormat currencyFormat = NumberFormat.getCurrencyInstance();

    class ShoppingItemTableCell extends TableCell<ShoppingItem, Double> {

        @Override
        protected void updateItem(Double price, boolean empty) {
            super.updateItem(price, empty);
            if (empty) {
                setText(null);
            } else {
                setText(currencyFormat.format(price));
            }
        }
    }

    @FXML
    private TabPane myTabPane;
    /*
    shopping list tab fx:id
     */
    @FXML
    private TableView<ShoppingItem> tableViewSL;
    @FXML
    private TableColumn<ShoppingItem, String> tableName;
    @FXML
    private TableColumn<ShoppingItem, Double> tablePrice;
    @FXML
    private TableColumn<ShoppingItem, Integer> tableQuantity;
    @FXML
    private TableColumn<ShoppingItem, Integer> tablePriority;
    @FXML
    private TextField nameTextSL;
    @FXML
    private TextField priceTextSL;
    @FXML
    private TextField quantityTextSL;
    @FXML
    private TextField priorityTextSL;
    @FXML
    private TextField budgetInputText;
    @FXML
    private Button addButton;

    /*
    shopping cart tab fx:id
     */
    @FXML
    private TableView<ShoppingItem> tableViewBought;
    @FXML
    private TableColumn<ShoppingItem, String> tableNameB;
    @FXML
    private TableColumn<ShoppingItem, Double> tablePriceB;
    @FXML
    private TableColumn<ShoppingItem, Integer> tableQuantityB;
    @FXML
    private TableColumn<ShoppingItem, Double> tableCostB;
    @FXML
    private TableView<ShoppingItem> tableViewUnbought;
    @FXML
    private TableColumn<ShoppingItem, String> tableNameU;
    @FXML
    private TableColumn<ShoppingItem, Double> tablePriceU;
    @FXML
    private TableColumn<ShoppingItem, Integer> tableQuantityU;
    @FXML
    private TableColumn<ShoppingItem, Double> tableCostU;
    @FXML
    private TextField totalCostText;
    @FXML
    private TableView<ShoppingBudget> budgetTableView;
    @FXML
    private TableColumn<ShoppingBudget, Double> budgetTextSC;

    /*
    button actions
     */
    @FXML
    //shopping list tab to priority tab (DONE)
    void handleSLNextButton(ActionEvent event) {
        myTabPane.getSelectionModel().select(1);
    }

    @FXML
        //(ADD ITEM)
    void handleAddButtonAction(ActionEvent event) {
        //check if item name is valid
        for(int i = 0; i< nameTextSL.getLength(); i++){
            char character = nameTextSL.getCharacters().charAt(i);
            if(!isCharacterValid(character)){
                Alert alertName = new Alert(Alert.AlertType.ERROR);
                alertName.setTitle("Error");
                alertName.setHeaderText("Invalid input");
                alertName.setContentText("Please make sure that your item name only includes characters a-z");
                alertName.showAndWait();

                nameTextSL.clear();
                priceTextSL.clear();
                quantityTextSL.clear();
                priorityTextSL.clear();

                return;

            }

        }

        try {
            shoppingItemList.add(new ShoppingItem(nameTextSL.getText(),
                    Double.parseDouble(priceTextSL.getText()),
                    Integer.parseInt(quantityTextSL.getText()), Integer.parseInt(priorityTextSL.getText())));

            nameTextSL.clear();
            priceTextSL.clear();
            quantityTextSL.clear();
            priorityTextSL.clear();


        } catch (NumberFormatException numformatException) {
            Alert alertNum = new Alert(Alert.AlertType.ERROR);
            alertNum.setTitle("Error");
            alertNum.setHeaderText("Invalid input");
            alertNum.setContentText("Please make sure that you only put in digits for price, quantity, and priority");
            alertNum.showAndWait();
        }

        }


    public static boolean isCharacterValid(char character) {

        if ((character >= 65 && character <= 90) || (character >= 97 && character <= 122)) {
            return true;
        } else {
            return false;
        }

    }

    @FXML
        // (ADD BUDGET)
    void handleBudgetButtonAction(ActionEvent event) {

        ShoppingBudget shoppingBudget = new ShoppingBudget(Double.parseDouble(budgetInputText.getText()), shoppingItemList);
        tableViewBought.setItems(shoppingBudget.getBought());
        tableViewUnbought.setItems(shoppingBudget.getUnbought());
        shoppingBudgetList.add(shoppingBudget);
        totalCostText.setText(currencyFormat.format(new Double(shoppingBudget.getTotalCost())));
        shoppingBudget.getUnbought().forEach(shoppingItem -> {
            sqLshoppingCartService.addItem(shoppingItem.getName(), shoppingItem.getPrice(),
                    shoppingItem.getQuantity(), shoppingItem.getPriority());});
        shoppingBudget.getUnbought().forEach(shoppingItem -> {
            sqLshoppingCartService.changePriorities();});
        budgetInputText.clear();
    }

/*
initialize TableView
*/
    @Override
    public void initialize(URL location, ResourceBundle resources) {

 /*
 shopping list tab
  */

        //adding items to table
        tableName.setCellValueFactory(new PropertyValueFactory<>("Name"));
        tablePrice.setCellValueFactory(new PropertyValueFactory<>("Price"));
        tableQuantity.setCellValueFactory(new PropertyValueFactory<>("Quantity"));
        tablePriority.setCellValueFactory(new PropertyValueFactory<>("Priority"));
        tableViewSL.setItems(shoppingItemList);
        tableViewSL.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

/*
shopping cart tab
 */
        //bought table
        tableNameB.setCellValueFactory(new PropertyValueFactory<>("Name"));
        tablePriceB.setCellValueFactory(new PropertyValueFactory<>("Price"));
        tableQuantityB.setCellValueFactory(new PropertyValueFactory<>("Quantity"));
        tableCostB.setCellValueFactory(new PropertyValueFactory<>("Cost"));
        tableViewBought.setItems(shoppingItemBoughtList);
        tableViewBought.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        //unbought table
        tableNameU.setCellValueFactory(new PropertyValueFactory<>("Name"));
        tablePriceU.setCellValueFactory(new PropertyValueFactory<>("Price"));
        tableQuantityU.setCellValueFactory(new PropertyValueFactory<>("Quantity"));
        tableCostU.setCellValueFactory(new PropertyValueFactory<>("Cost"));
        tableViewUnbought.setItems(shoppingItemUnboughtList);
        tableViewUnbought.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        //budget table
        budgetTextSC.setCellValueFactory(new PropertyValueFactory<>("Budget"));
        budgetTableView.setItems(shoppingBudgetList);
        budgetTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);


/*
Format the price and cost - https://docs.oracle.com/javafx/2/api/javafx/scene/control/Cell.html
 */

        tablePrice.setCellFactory(tableCell -> new ShoppingItemTableCell());
        tablePriceB.setCellFactory(tableCell -> new ShoppingItemTableCell());
        tablePriceU.setCellFactory(tableCell -> new ShoppingItemTableCell());
        tableCostB.setCellFactory(tableCell -> new ShoppingItemTableCell());
        tableCostU.setCellFactory(tableCell -> new ShoppingItemTableCell());

        budgetTextSC.setCellFactory(tableCell -> new TableCell<ShoppingBudget, Double>() {

            @Override
            protected void updateItem(Double price, boolean empty) {
                super.updateItem(price, empty);
                if (empty) {
                    setText(null);
                } else {
                    setText(currencyFormat.format(price));
                }
            }
        });

        sqLshoppingCartService.getPreviousShoppingList().forEach(shoppingItem -> {
            shoppingItemList.add(shoppingItem);});
        sqLshoppingCartService.clearTable();
        sqLshoppingCartService.closeConnection();
    }


    private ObservableList<ShoppingItem> shoppingItemList = FXCollections.observableArrayList();

    private ObservableList<ShoppingBudget> shoppingBudgetList = FXCollections.observableArrayList();

    private ObservableList<ShoppingItem> shoppingItemBoughtList = FXCollections.observableArrayList();

    private ObservableList<ShoppingItem> shoppingItemUnboughtList = FXCollections.observableArrayList();

}



