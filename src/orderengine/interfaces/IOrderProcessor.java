package orderengine.interfaces;

public interface IOrderProcessor {

    int processOrder(IOrder order);

    int getQuantity(double price);

    void registerOrderBook(IOrderBook orderBook);
}
