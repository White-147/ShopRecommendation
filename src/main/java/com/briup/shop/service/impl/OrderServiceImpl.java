package com.briup.shop.service.impl;

import com.briup.shop.bean.*;
import com.briup.shop.dao.IOrderDao;
import com.briup.shop.dao.IOrderItemDao;
import com.briup.shop.dao.IShippingAddressDao;
import com.briup.shop.dao.IShopCarDao;
import com.briup.shop.service.IOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author adam
 * @date 2022/1/12
 */
@Service
public class OrderServiceImpl implements IOrderService {
    @Autowired
    private IOrderDao orderDao;
    @Autowired
    private IShopCarDao shopCarDao;
    @Autowired
    private IOrderItemDao itemDao;
    @Autowired
    private IShippingAddressDao addressDao;

    @Override
    public List<Order> findUserAllOrders(Long userId) {
        List<Order> list = orderDao.findByUserIdOrderByCreateDateDesc(userId);
        return list;
    }

    @Override
    @Transactional
    public Order saveOrder(Long[] shopCarIds, User user, Long addressId) {
        // 必须从数据库加载持久化地址（new ShippingAddress(addressId) 是瞬态实例，
        // 保存 Order 时 Hibernate 会抛 TransientPropertyValueException 并回滚）
        ShippingAddress address = addressDao.findById(addressId)
                .orElseThrow(() -> new RuntimeException("收货地址不存在: " + addressId));
        List<Long> asList = Arrays.asList(shopCarIds);
        List<ShopCar> list = shopCarDao.findAllById(asList);
        Order order = new Order(user, address);

        List<OrderItem> items = list.stream().map(i -> {
            OrderItem item = new OrderItem(i);
            item.setOrder(order);
            return item;
        }).collect(Collectors.toList());
        order.setOrderItems(items);
        order.setStatus("1");
        orderDao.save(order);
        itemDao.saveAll(items);
        shopCarDao.deleteAllById(asList);
        return order;

    }

    @Override
    public Order paySuccess(String orderId) {
        // findById 替代懒加载的 getById，避免“订单不存在”的代理初始化异常
        Order order = orderDao.findById(orderId)
                .orElseThrow(() -> new RuntimeException("订单不存在: " + orderId));
        order.setStatus("2");
        orderDao.save(order);
        return order;
    }

    @Override
    public Order findById(String orderId) {
        Order order = orderDao.getById(orderId);
        return order;
    }
}
