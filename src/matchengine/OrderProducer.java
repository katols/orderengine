package matchengine;

import matchengine.interfaces.IOrderConsumer;

import java.util.logging.Level;
import java.util.logging.Logger;

public class OrderProducer implements Runnable {

    private static final Logger logger = Logger.getLogger(OrderProducer.class.getName());

    private final IOrderConsumer orderProcessor;
    private static int producedMessges = 0;

    public OrderProducer(IOrderConsumer orderProcessor, OrderEngineController controller) {
        this.orderProcessor = orderProcessor;
    }

    @Override
    public void run() {
        for (int i = 0; i < 1000; i++) {
            try {
                produceOrder();
            } catch (Exception ex) {
                logger.log(Level.SEVERE, null, ex);
            }
        }
        logger.log(Level.INFO, "Done producing. Produced "+producedMessges+" messages.");
    }

    public void produceOrder() {
        Order order = createOrder();
        this.orderProcessor.acceptOrder(order);
        producedMessges++;
        logger.log(Level.FINE, "Produced: " + order);
    }

    private Order createOrder() {
        boolean buy = (Math.random() * 10) >= 5 ? true : false;
        return new Order(100, 10, buy);
    }

}
