package matchengine;


import matchengine.interfaces.IOrder;

public class Order implements IOrder {

    private OrderProcessor orderProcessor;
    private double price;
    private int quantity;
    private boolean buy; //0=1, 1=2

    public Order(double price, int quantity, boolean buy) {
        this.price = price;
        this.quantity = quantity;
        this.buy = buy;
    }

    public OrderProcessor getOrderProcessor() {
        return orderProcessor;
    }

    public void setOrderProcessor(OrderProcessor orderProcessor) {
        this.orderProcessor = orderProcessor;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public boolean isBuyOrder() {
        return buy;
    }

    public void setBuy(boolean buy) {
        this.buy = buy;
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
