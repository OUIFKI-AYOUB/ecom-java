package com.example.Ecom.abstracts;


import com.example.Ecom.dtos.OrderCreate;
import com.example.Ecom.entities.OrderStatus;
import com.example.Ecom.entities.Orders;

import java.util.List;
import java.util.UUID;

public interface OrderService {

    List<Orders> findAll();

    Orders findOne(UUID orderID);

    void deleteOne(UUID orderID);

    Orders createOne(OrderCreate orderCreate);

    Orders updateStatus(UUID orderId, OrderStatus newStatus);

}
