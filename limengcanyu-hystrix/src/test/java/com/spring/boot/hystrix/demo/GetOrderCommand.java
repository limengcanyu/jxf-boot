package com.spring.boot.hystrix.demo;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.examples.demo.Order;

/**
 * Sample HystrixCommand simulating one that would fetch Order objects from a remote service or database.
 * <p>
 * This fails fast with no fallback and does not use request caching.
 */
public class GetOrderCommand extends HystrixCommand<Order> {

    private final int orderId;

    public GetOrderCommand(int orderId) {
        super(HystrixCommandGroupKey.Factory.asKey("Order"));
        this.orderId = orderId;
    }

    @Override
    protected Order run() {
        /* simulate performing network call to retrieve order */
        try {
            Thread.sleep((int) (Math.random() * 200) + 50);
        } catch (InterruptedException e) {
            // do nothing
        }

        /* fail rarely ... but allow failure as this one has no fallback */
        if (Math.random() > 0.9999) {
            throw new RuntimeException("random failure loading order over network");
        }

        /* latency spike 5% of the time */
        if (Math.random() > 0.95) {
            // random latency spike
            try {
                Thread.sleep((int) (Math.random() * 300) + 25);
            } catch (InterruptedException e) {
                // do nothing
            }
        }

        /* success ... create Order with data "from" the remote service response */
        return new Order(orderId);
    }

}
