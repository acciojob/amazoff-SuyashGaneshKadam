package com.driver;

import org.springframework.stereotype.Repository;
import java.util.*;

@Repository
public class OrderRepository {

    HashMap<String, Order> orderDb = new HashMap<>();
    HashMap<String, DeliveryPartner> deliveryPartnerDb = new HashMap<>();
    HashMap<String, List<Order>> partnerOrderMappingDb = new HashMap<>();
    HashMap<String, DeliveryPartner> orderPartnerMappingDb = new HashMap<>();

    public void addOrder(Order order) { orderDb.put(order.getId(), order); }
    public void addPartner(String partnerId)
    {
        deliveryPartnerDb.put(partnerId, new DeliveryPartner(partnerId));
    }
    public void addOrderPartnerPair(String orderId, String partnerId)
    {
        Order order = orderDb.get(orderId);
        DeliveryPartner deliveryPartner = deliveryPartnerDb.get(partnerId);
        if(order == null || deliveryPartner == null)
        {
            //We don't have the order or deliveryPartner which is requested
            return;
        }
        List<Order> ordersOfPartner = partnerOrderMappingDb.getOrDefault(partnerId, new ArrayList<>());
        if(ordersOfPartner == null || !ordersOfPartner.contains(order))
        {
            int currentOrders = deliveryPartner.getNumberOfOrders();
            deliveryPartner.setNumberOfOrders(currentOrders + 1);
            deliveryPartnerDb.put(partnerId, deliveryPartner);
            ordersOfPartner.add(order);
            partnerOrderMappingDb.put(partnerId, ordersOfPartner);
            orderPartnerMappingDb.put(orderId, deliveryPartner);
        }
    }
    public Order getOrderById(String orderId){
        return orderDb.get(orderId);
    }
    public DeliveryPartner getPartnerById(String partnerId){
        return deliveryPartnerDb.get(partnerId);
    }
    public List<Order> getOrdersByPartnerId(String partnerId)
    {
        return partnerOrderMappingDb.get(partnerId);
    }
    public HashMap<String, Order> getAllOrders()
    {
        return orderDb;
    }
    public int getCountOfUnassignedOrders()
    {
        return orderDb.size() - orderPartnerMappingDb.size();
    }
    public Order getLastDeliveryTimeByPartnerId(String partnerId)
    {
        List<Order> orders = partnerOrderMappingDb.get(partnerId);
        return orders == null ? null : orders.get(orders.size() - 1);
    }
    public void deletePartnerById(String partnerId)
    {
        if(!deliveryPartnerDb.containsKey(partnerId))
        {
            return;
        }
        deliveryPartnerDb.remove(partnerId);
        if(!partnerOrderMappingDb.containsKey(partnerId))
        {
            return;
        }
        partnerOrderMappingDb.remove(partnerId);
        for(String orderId : orderPartnerMappingDb.keySet())
        {
            if(orderPartnerMappingDb.get(orderId).getId().equals(partnerId))
            {
                orderPartnerMappingDb.remove(orderId);
            }
        }
    }
    public void deleteOrderById(String orderId)
    {
        if(!orderDb.containsKey(orderId))
        {
            return;
        }
        orderDb.remove(orderId);
        if(!orderPartnerMappingDb.containsKey(orderId))
        {
            return;
        }
        String partnerId = orderPartnerMappingDb.get(orderId).getId();
        orderPartnerMappingDb.remove(orderId);
        List<Order> orders = partnerOrderMappingDb.get(partnerId);
        for(Order order : orders)
        {
            if(order.getId().equals(orderId))
            {
                orders.remove(order);
                break;
            }
        }
        if(orders == null){ return; }
        partnerOrderMappingDb.put(partnerId, orders);
    }
}
