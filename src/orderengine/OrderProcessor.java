package orderengine;

import orderengine.interfaces.IOrder;
import orderengine.interfaces.IOrderBook;
import orderengine.interfaces.IOrderProcessor;

public class OrderProcessor implements IOrderProcessor {

    public static final int NOT_MATCHED = 4;
    public static final int PARTIALLY_MATCHED = 6;
    public static final int MATCHED = 2;

    private IOrderBook orderBook;


    @Override
    public int processOrder(IOrder order) {

        if (order.getQuantity() <= 0) {
            throw new IllegalArgumentException("Order must have a positive quantity.");
        }

        if (orderBook.isEmpty()) {
            orderBook.addOrder(order);
            return NOT_MATCHED;
        }

        IOrder quantityRemaining = orderBook.matchOrder(order);

        if (quantityRemaining.getQuantity() > 0) {
            orderBook.addOrder(quantityRemaining);
        }

        if (orderBook.isEmpty()) {
            return MATCHED;
        } else {
            return isNotMatched(order, quantityRemaining) ? NOT_MATCHED : PARTIALLY_MATCHED;
        }

    }

    @Override
    public int getQuantity(double orderValue) {
        return this.orderBook.getQuantity(orderValue);
    }

    @Override
    public void registerOrderBook(IOrderBook orderBook) {
        this.orderBook = orderBook;
    }

    private boolean isNotMatched(IOrder order, IOrder quantityRemaining) {
        return order.equals(quantityRemaining);
    }
}
