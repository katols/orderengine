package matchengine.interfaces;

public interface IOrder {
    double getPrice();

    boolean isBuyOrder();

    int getQuantity();
}
