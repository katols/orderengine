package test;

import orderengine.Order;
import orderengine.OrderBook;
import orderengine.OrderProcessor;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class OrderProcessorTest {


    @Test
    public void test11(){
        OrderProcessor orderProcessor = new OrderProcessor();
        orderProcessor.registerOrderBook(new OrderBook());

        Order order = new Order(100, 10, true);
        int statusAfterFirstOrder = orderProcessor.processOrder(order);
        assertEquals(OrderProcessor.NOT_MATCHED, statusAfterFirstOrder);

        Order order2 = new Order(100, 10, false);
        int statusAfterSecondOrder = orderProcessor.processOrder(order2);
        assertEquals(OrderProcessor.MATCHED, statusAfterSecondOrder);
        assertEquals(0, orderProcessor.getQuantity(100));

    }

    @Test
    public void test12(){
        OrderProcessor orderProcessor = new OrderProcessor();
        orderProcessor.registerOrderBook(new OrderBook());

        Order order1 = new Order(100, 10, false);
        int statusAfterFirstOrder = orderProcessor.processOrder(order1);
        assertEquals(OrderProcessor.NOT_MATCHED, statusAfterFirstOrder);

        Order order2 = new Order(110, 10, false);
        int statusAfterSecondOrder = orderProcessor.processOrder(order2);
        assertEquals(OrderProcessor.NOT_MATCHED, statusAfterSecondOrder);

        Order order3 = new Order(120, 10, false);
        int statusAfterThirdOrder = orderProcessor.processOrder(order3);
        assertEquals(OrderProcessor.NOT_MATCHED, statusAfterThirdOrder);

        Order order4 = new Order(110, 50, true);
        int statusAfterFourthOrder = orderProcessor.processOrder(order4);
        assertEquals(OrderProcessor.PARTIALLY_MATCHED, statusAfterFourthOrder);
        assertEquals(30, orderProcessor.getQuantity(110));
        assertEquals(10, orderProcessor.getQuantity(120));

    }

    @Test
    public void bestDealAtBuyTest(){
        OrderProcessor orderProcessor = new OrderProcessor();
        orderProcessor.registerOrderBook(new OrderBook());

        Order sellOrder1 = new Order(100, 10, false);
        assertEquals(OrderProcessor.NOT_MATCHED, orderProcessor.processOrder(sellOrder1));
        Order sellOrder2 = new Order(110, 5, false);
        assertEquals(OrderProcessor.NOT_MATCHED, orderProcessor.processOrder(sellOrder2));
        Order sellOrder3 = new Order(120, 15, false);
        assertEquals(OrderProcessor.NOT_MATCHED, orderProcessor.processOrder(sellOrder3));

        Order buyOrder = new Order(130, 14, true);
        assertEquals(OrderProcessor.PARTIALLY_MATCHED, orderProcessor.processOrder(buyOrder));
        assertEquals(0, orderProcessor.getQuantity(100));
        assertEquals(1, orderProcessor.getQuantity(110));
        assertEquals(15, orderProcessor.getQuantity(120));

    }

    @Test
    public void bestDealAtSellTest(){
        OrderProcessor orderProcessor = new OrderProcessor();
        orderProcessor.registerOrderBook(new OrderBook());

        Order buyOrder1 = new Order(100, 10, true);
        assertEquals(OrderProcessor.NOT_MATCHED, orderProcessor.processOrder(buyOrder1));
        Order buyOrder2 = new Order(110, 5,true);
        assertEquals(OrderProcessor.NOT_MATCHED, orderProcessor.processOrder(buyOrder2));
        Order buyOrder3 = new Order(120, 15, true);
        assertEquals(OrderProcessor.NOT_MATCHED, orderProcessor.processOrder(buyOrder3));

        Order sellOrder = new Order(100, 16, false);
        assertEquals(OrderProcessor.PARTIALLY_MATCHED, orderProcessor.processOrder(sellOrder));
        assertEquals(10, orderProcessor.getQuantity(100));
        assertEquals(4, orderProcessor.getQuantity(110));
        assertEquals(0, orderProcessor.getQuantity(120));

    }


    @Test
    public void correctStatusTest(){
        OrderProcessor orderProcessor = new OrderProcessor();
        orderProcessor.registerOrderBook(new OrderBook());
        Order sellOrder = new Order(100, 4, true);
        assertEquals(OrderProcessor.NOT_MATCHED, orderProcessor.processOrder(sellOrder));
        Order buyOrder = new Order(100, 8,false);
        assertEquals(OrderProcessor.PARTIALLY_MATCHED, orderProcessor.processOrder(buyOrder));

    }





}


/*package orderengine;

        import orderengine.interfaces.IOrder;
        import orderengine.interfaces.IOrderBook;

        import java.util.Iterator;
        import java.util.Set;
        import java.util.TreeMap;

public class OrderBook implements IOrderBook {
    public static final int BUY = -1;
    public static final int SELL = 1;

    private TreeMap<Double, Integer> orders = new TreeMap<>();

    @Override
    public void addOrder(IOrder order) {
        int quantityFactor = (order.isBuyOrder()) ? BUY : SELL;
        this.orders.put(order.getPrice(), order.getQuantity()*quantityFactor);
    }

    @Override
    public void removeOrder(IOrder order) {
        this.orders.remove(order);
    }


    @Override
    public Integer getQuantity(double cutoff) {
        return (orders.get(cutoff) != null) ? abs(orders.get(cutoff)) : 0;
    }

    @Override
    public boolean isEmpty() {
        return orders.isEmpty();
    }

    @Override
    public IOrder matchOrder(IOrder order) {
        int quantityFactor = (order.isBuyOrder()) ? BUY : SELL;
        int quantityAddedOrRemoved = quantityFactor * order.getQuantity();
        Set<Double> keys = (order.isBuyOrder()) ? orders.keySet() : orders.descendingKeySet();
        Iterator<Double> iterator = keys.iterator();
        while (iterator.hasNext() && abs(quantityAddedOrRemoved) > 0) {
            Double next = iterator.next();

            boolean match = isMatch(order, next);

            if (match) {
                Integer nextQuantity = orders.get(next);
                int remaining = nextQuantity + quantityAddedOrRemoved;
                int discrepance = abs(quantityAddedOrRemoved) - abs(nextQuantity);
                boolean partialMatch = (nextQuantity * quantityAddedOrRemoved) < 0;

                if (discrepance >= 0 && partialMatch) { //Sufficient or more buy or sell
                    iterator.remove();
                } else if (discrepance < 0 && partialMatch) { //insufficient buy or sell
                    orders.put(next, remaining);
                    remaining = 0;
                } else { //consecutive buy or sell
                    remaining = quantityAddedOrRemoved;
                }
                quantityAddedOrRemoved = remaining;
            }
        }
        return new Order(order.getPrice(), abs(quantityAddedOrRemoved), order.isBuyOrder());
    }

    private boolean isMatch(IOrder order, Double next) {
        // boolean matchOrder =
        return (order.isBuyOrder()) ? (order.getPrice() >= next) : (order.getPrice() <= next);
    }

    private int abs(Integer input) {
        return input < 0 ? -input : input;
    }

}*/
