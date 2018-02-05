package orderengine.interfaces;


public interface IOrderBook {
    void addOrder(IOrder order);

    void removeOrder(IOrder order);

    Integer getQuantity(double cutoff);

    boolean isEmpty();

    IOrder matchOrder(IOrder order);
}
