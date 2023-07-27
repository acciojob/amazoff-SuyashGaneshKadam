package com.driver;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {
    @Autowired
    OrderRepository orderRepositoryObject;
    public void addOrder(Order order)
    {
        orderRepositoryObject.addOrder(order);
    }
    public void addPartner(String partnerId)
    {
        orderRepositoryObject.addPartner(partnerId);
    }
    public void addOrderPartnerPair(String orderId, String partnerId)
    {
        orderRepositoryObject.addOrderPartnerPair(orderId, partnerId);
    }
    public Order getOrderById(String orderId){
        return orderRepositoryObject.getOrderById(orderId);
    }
    public DeliveryPartner getPartnerById(String partnerId){
        return orderRepositoryObject.getPartnerById(partnerId);
    }
    public int getOrderCountByPartnerId(String partnerId)
    {
        return orderRepositoryObject.getOrderCountByPartnerId(partnerId).getNumberOfOrders();
    }
    public List<String> getOrdersByPartnerId(String partnerId)
    {
        List<String> orders = new ArrayList<>();
        for(Order order : orderRepositoryObject.getOrdersByPartnerId(partnerId))
        {
            orders.add(order.getId());
        }
        return orders;
    }
    public List<String> getAllOrders()
    {
        List<String> orders = new ArrayList<>();
        for(String orderId : orderRepositoryObject.getAllOrders().keySet())
        {
            orders.add(orderId);
        }
        return orders;
    }

    public int getCountOfUnassignedOrders()
    {
        return orderRepositoryObject.getCountOfUnassignedOrders();
    }
    public int getOrdersLeftAfterGivenTimeByPartnerId(String time, String partnerId)
    {
        List<Order> ordersOfPartner = orderRepositoryObject.getOrdersLeftAfterGivenTimeByPartnerId(partnerId);
        int count = 0;
        int timeInMinutes = Integer.parseInt(time.substring(0,2)) * 60 + Integer.parseInt(time.substring(3));
        for(Order order : ordersOfPartner)
        {
            if(timeInMinutes > order.getDeliveryTime())
            {
                count++;
            }
        }
        return count;
    }
    public String getLastDeliveryTimeByPartnerId(String partnerId)
    {
        Order order = orderRepositoryObject.getLastDeliveryTimeByPartnerId(partnerId);
        int time = order.getDeliveryTime();
        String timeInFormat = (time/60) + ":" + (time%60);
        return timeInFormat;
    }
    public void deletePartnerById(String partnerId)
    {
        orderRepositoryObject.deletePartnerById(partnerId);
    }
    public void deleteOrderById(String orderId)
    {
        orderRepositoryObject.deleteOrderById(orderId);
    }
}
