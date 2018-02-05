package orderengine;

import orderengine.interfaces.IOrder;
import orderengine.interfaces.IOrderBook;

import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

public class OrderBook implements IOrderBook {

    private TreeMap<Double, IOrder> buyOrders = new TreeMap<>(Collections.reverseOrder());
    private TreeMap<Double, IOrder> sellOrders = new TreeMap<>();


    @Override
    public void addOrder(IOrder order) {

        if (order.isBuyOrder()) {
            buyOrders.put(order.getPrice(), order);
        } else {
            sellOrders.put(order.getPrice(), order);

        }
    }

    @Override
    public void removeOrder(IOrder order) {
        if (order.isBuyOrder()) {
            buyOrders.remove(order.getPrice());
        } else {
            sellOrders.remove(order.getPrice());
        }
    }


    @Override
    public Integer getQuantity(double price) {

        int buy = (buyOrders.get(price) != null) ? buyOrders.get(price).getQuantity() : 0;
        int sell = (sellOrders.get(price) != null) ? sellOrders.get(price).getQuantity() : 0;

        return buy + sell;
    }

    @Override
    public boolean isEmpty() {
        return buyOrders.isEmpty() && sellOrders.isEmpty();
    }

    @Override
    public IOrder matchOrder(IOrder order) {
        if (order.isBuyOrder()) {
            return matchExistingOrders(order, sellOrders);
        } else {
            return matchExistingOrders(order, buyOrders);
        }

    }


    private IOrder matchExistingOrders(IOrder order, TreeMap<Double, IOrder> orders) {
        int quantityAddedOrRemoved = order.getQuantity();
        Iterator<Map.Entry<Double, IOrder>> iterator = orders.entrySet().iterator();

        while (iterator.hasNext() && quantityAddedOrRemoved > 0) {
            Map.Entry<Double, IOrder> nextEntry = iterator.next();
            if (isMatch(order, nextEntry.getKey())) {
                IOrder nextOrder = nextEntry.getValue();
                int remaining = nextOrder.getQuantity() - quantityAddedOrRemoved;
                if (remaining <= 0) {
                    iterator.remove();
                    quantityAddedOrRemoved = abs(remaining);
                } else {
                    return new Order(nextOrder.getPrice(), abs(remaining), nextOrder.isBuyOrder());
                }

            }
        }

        return new Order(order.getPrice(), quantityAddedOrRemoved, order.isBuyOrder());
    }


    private boolean isMatch(IOrder order, Double next) {
        return (order.isBuyOrder()) ? (order.getPrice() >= next) : (order.getPrice() <= next);
    }

    private int abs(Integer input) {
        return input < 0 ? -input : input;
    }

}
