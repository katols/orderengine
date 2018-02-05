package matchengine;

import matchengine.interfaces.IOrderConsumer;

import java.util.Scanner;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class OrderEngineController {

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
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
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
        orderProducer = new OrderProducer(orderProcessor,  this);
    }

    private IOrderConsumer initializeOrderProcessor() {
        OrderProcessor orderProcessor = new OrderProcessor();
        this.orderProcessor = new OrderConsumer();
        this.orderProcessor.registerOrderProcessor(orderProcessor);
        this.orderProcessor.initialize();

        return this.orderProcessor;
    }

}
