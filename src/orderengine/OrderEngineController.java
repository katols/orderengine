package orderengine;

import orderengine.interfaces.IOrderConsumer;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;

public class OrderEngineController {
    private static final Logger logger = Logger.getLogger(OrderEngineController.class.getName());

    private OrderProducer orderProducer;
    private IOrderConsumer orderProcessor;
    private ExecutorService threadPool;

    public static void main(String[] args) {

        OrderEngineController orderController = new OrderEngineController();

        orderController.start();

    }

    public void start() {
        threadPool.submit(orderProducer);
        Future doneConsuming = threadPool.submit(orderProcessor);

        try {
            doneConsuming.get();
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Order production/consumtion failed: ");
            e.printStackTrace();
        }

        stop();
    }

    public void stop() {
        orderProcessor.shutDown();
        threadPool.shutdown();
    }

    public OrderEngineController() {
        threadPool = Executors.newFixedThreadPool(3);
        orderProcessor = initializeOrderProcessor();
        orderProducer = new OrderProducer(orderProcessor);
    }

    private IOrderConsumer initializeOrderProcessor() {
        OrderProcessor orderProcessor = new OrderProcessor();
        this.orderProcessor = new OrderConsumer();
        this.orderProcessor.registerOrderProcessor(orderProcessor);
        this.orderProcessor.initialize();

        return this.orderProcessor;
    }

}
