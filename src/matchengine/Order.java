package matchengine;


import matchengine.interfaces.IOrder;

public class Order implements IOrder {

    private double price;
    private int quantity;
    private boolean buy;

    public Order(double price, int quantity, boolean buy) {
        this.price = price;
        this.quantity = quantity;
        this.buy = buy;
    }

    @Override
    public double getPrice() {
        return price;
    }

    @Override
    public int getQuantity() {
        return quantity;
    }

    @Override
    public boolean isBuyOrder() {
        return buy;
    }

    @Override
    public String toString() {
        return "Price: " + this.price + " Quantity: " + this.quantity + " Buy: " + this.buy;
    }

    @Override
    public int hashCode() {
        int prime = 31;
        int result = 1;
        result = prime * result + (int) price;
        result = prime * result + quantity;
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof Order)) {
            return false;
        }

        Order order = (Order) o;

        return order.price == this.price &&
                order.quantity == this.quantity
                && order.buy == this.buy;
    }


}
