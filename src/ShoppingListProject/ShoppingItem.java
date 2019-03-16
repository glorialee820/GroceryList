package ShoppingListProject;

public class ShoppingItem implements Comparable{
    private double price;
    private int priority;
    private String name;
    private int quantity;
    private double cost;

    public ShoppingItem(String n, double p, int q, int pr){
        price = p;
        name = n;
        quantity = q;
        priority = pr;
    }

    public int getPriority() {

        return priority;
    }

    public String getName() {

        return name;
    }

    public int getQuantity() {

        return quantity;
    }

    public double getPrice() {
        return price;
    }


    public double getCost(){
        cost = getPrice() * getQuantity();
        return cost;
    }

    @Override
    public int compareTo(Object o1) {
        return priority - ((ShoppingItem)o1).priority;
    }



//    //for testing
//    public String toString() {
//        return name + price + quantity+ priority;
//    }
}
