package orderengine.interfaces;

public interface IOrderConsumer extends Runnable {

    void registerOrderProcessor(IOrderProcessor orderBook);

    void acceptOrder(IOrder order);

    void initialize();

    void shutDown();
}

