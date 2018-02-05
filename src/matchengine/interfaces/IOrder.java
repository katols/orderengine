package matchengine.interfaces;

public interface IOrder {
    Double getPrice();

    boolean isBuyOrder();

    int getQuantity();
}
