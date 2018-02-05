package matchengine;

import matchengine.interfaces.IOrder;
import matchengine.interfaces.IOrderProcessor;
import matchengine.interfaces.IOrderConsumer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.OptionalDouble;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import java.util.logging.Logger;

public class OrderConsumer implements IOrderConsumer {
    public static final int MAX_CAPACITY = 20; //Depends on desired latency

    private static final Logger logger = Logger.getLogger(OrderConsumer.class.getName());

    private LinkedBlockingQueue<IOrder> orderQueue;
    private IOrderProcessor orderProcessor;
    private AtomicBoolean running = new AtomicBoolean(false);
    private int timeout = 5000; //Default, ms
    private static int consumedMessages = 0;
    private List<Long> timeRecords = new ArrayList<>();

    private List<Long> timeRecordsProducerSide = new ArrayList<>();


    public OrderConsumer() {
        orderQueue = new LinkedBlockingQueue(MAX_CAPACITY);
    }

    @Override
    public void registerOrderProcessor(IOrderProcessor orderProcessor) {
        this.orderProcessor = orderProcessor;
        this.orderProcessor.registerOrderBook(new OrderBook());
    }

    @Override
    public void acceptOrder(IOrder order) {
        try {
            long acceptTimePre = System.nanoTime();
            this.orderQueue.put(order);
            long acceptTimePost = System.nanoTime();
            timeRecordsProducerSide.add(acceptTimePost - acceptTimePre);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }


    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    @Override
    public void run() {
        while (running.get()) {
            try {
                IOrder order = orderQueue.poll(timeout, TimeUnit.MILLISECONDS);
                if (order == null) {
                    break;
                }
                int status = processOrder(order);

                logger.log(Level.FINE, "Consumed: " + order + " with status: " + status);
                consumedMessages++;
            } catch (InterruptedException ex) {
                logger.log(Level.SEVERE, null, ex);
            }
        }
        logger.log(Level.INFO, "Done consuming. Consumed " + consumedMessages + " messages.");
    }


    @Override
    public void initialize() {
        this.running.set(true);
    }

    @Override
    public void shutDown() {
        calculateAndPrintAverageLatency();
        this.running.set(false);
    }

    private int processOrder(IOrder order) {
        long timestampPre = System.nanoTime();
        int status = orderProcessor.processOrder(order);
        long timestampPost = System.nanoTime();
        timeRecords.add(timestampPost - timestampPre);
        return status;
    }

    private void calculateAndPrintAverageLatency() {
        OptionalDouble averageProcessingTime = timeRecords.stream().mapToLong(Long::longValue).average();
        double averageInMicroSeconds = averageProcessingTime.getAsDouble() / 1000.0;
        logger.log(Level.INFO, "Average processing latency on consumer side is " + averageInMicroSeconds + " microseconds");
        logger.log(Level.INFO, "Maximum processing latency on consumer side:  " + Collections.max(timeRecords).doubleValue() / 1000.0 + " microseconds");
        logger.log(Level.INFO, "Maximum waiting time of producer: " + Collections.max(timeRecordsProducerSide).doubleValue() / 1000.0 + "microseconds");
    }
}
