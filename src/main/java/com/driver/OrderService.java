package com.driver;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.*;

@Service
public class OrderService {
    //@Autowired
    OrderRepository orderRepositoryObject = new OrderRepository();
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
        return orderRepositoryObject.getPartnerById(partnerId) == null ? 0 : orderRepositoryObject.getPartnerById(partnerId).getNumberOfOrders();
    }
    public List<String> getOrdersByPartnerId(String partnerId)
    {
        List<String> orders = new ArrayList<>();
        List<Order> ordersOfPartner = orderRepositoryObject.getOrdersByPartnerId(partnerId);
        if(ordersOfPartner == null){ return orders; }
        for(Order order : ordersOfPartner)
        {
            orders.add(order.getId());
        }
        return orders;
    }
    public List<String> getAllOrders()
    {
        List<String> orders = new ArrayList<>();
        HashMap<String, Order> map = orderRepositoryObject.getAllOrders();
        if(map.size() == 0){ return orders; }
        for(String orderId : map.keySet())
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
        int count = 0;
        List<Order> ordersOfPartner = orderRepositoryObject.getOrdersByPartnerId(partnerId);
        if(ordersOfPartner == null) { return count; }
        int timeInMinutes = (Integer.parseInt(time.substring(0,2)) * 60) + Integer.parseInt(time.substring(3));
        for(Order order : ordersOfPartner)
        {
            if(timeInMinutes < order.getDeliveryTime())
            {
                count++;
            }
        }
        return count;
    }
    public String getLastDeliveryTimeByPartnerId(String partnerId)
    {
        Order order = orderRepositoryObject.getLastDeliveryTimeByPartnerId(partnerId);
        if(order == null) { return null; }
        int time = order.getDeliveryTime();
        String HH = (time/60) + "";
        String MM = (time%60) + "";
        if(HH.length() == 1)
        {
            HH = 0 + HH;
        }
        if(MM.length() == 1)
        {
            MM = 0 + MM;
        }
        String timeInFormat = HH + ":" + MM;
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
