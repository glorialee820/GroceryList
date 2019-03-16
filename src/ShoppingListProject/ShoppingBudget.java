package ShoppingListProject;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ShoppingBudget {

    private ObservableList<ShoppingItem> bought;
    private ObservableList <ShoppingItem> unbought;
    private double cost;

    private double budget;
    private ObservableList<ShoppingItem> shoppingItems;

    public ShoppingBudget(double b, ObservableList<ShoppingItem> s) {

        budget = b;
        shoppingItems = s;
        FXCollections.sort(shoppingItems);
        buyItems();

    }

    public ObservableList<ShoppingItem> getBought() {
        return bought;
    }

    public ObservableList<ShoppingItem> getUnbought() {
        return unbought;
    }

    public double getBudget() {
        return budget;
    }

    public ObservableList<ShoppingItem> getShoppingItems() {
        return shoppingItems;
    }

    public double getTotalCost(){
        return cost;
    }

    private void buyItems() {
        int i = 0;

        int MAX_ELEMENT = shoppingItems.size();

        bought = FXCollections.observableArrayList();
        unbought = FXCollections.observableArrayList();

        double tempBudget = budget;

        //differentiate the lists of bought and unbought
        while (i < MAX_ELEMENT) {

            if (tempBudget >= shoppingItems.get(i).getCost()) {
                bought.add(shoppingItems.get(i));
                tempBudget -= shoppingItems.get(i).getCost();
            } else {
                unbought.add(shoppingItems.get(i));
            }
            i++;
        }

        cost = budget - tempBudget;
    }


}
