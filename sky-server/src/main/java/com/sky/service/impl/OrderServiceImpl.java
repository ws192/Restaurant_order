package com.sky.service.impl;

import com.sky.constant.MessageConstant;
import com.sky.context.BaseContext;
import com.sky.dto.OrdersSubmitDTO;
import com.sky.entity.AddressBook;
import com.sky.entity.OrderDetail;
import com.sky.entity.Orders;
import com.sky.entity.ShoppingCart;
import com.sky.exception.AddressBookBusinessException;
import com.sky.exception.ShoppingCartBusinessException;
import com.sky.mapper.AddressBookMapper;
import com.sky.mapper.OrderDetailMapper;
import com.sky.mapper.OrderMapper;
import com.sky.mapper.ShoppingCartMapper;
import com.sky.result.Result;
import com.sky.service.OrderService;
import com.sky.vo.OrderSubmitVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    OrderMapper orderMapper;

    @Autowired
    OrderDetailMapper orderDetailMapper;

    @Autowired
    AddressBookMapper addressBookMapper;

    @Autowired
    ShoppingCartMapper shoppingCartMapper;

    @Override
    @Transactional
    public OrderSubmitVO submitOrder(OrdersSubmitDTO dto) {

        AddressBook byId = addressBookMapper.getById(dto.getAddressBookId());
        if(byId == null){
            throw new AddressBookBusinessException(MessageConstant.ADDRESS_BOOK_IS_NULL);

        }

        ShoppingCart shoppingCart=new ShoppingCart();
        shoppingCart.setUserId(BaseContext.getCurrentId());
        List<ShoppingCart> list = shoppingCartMapper.list(shoppingCart);

        if(list == null || list.size() == 0){
            throw new ShoppingCartBusinessException(MessageConstant.SHOPPING_CART_IS_NULL);
        }

        Orders orders=new Orders();
        BeanUtils.copyProperties(dto,orders);
        orders.setOrderTime(LocalDateTime.now());
        orders.setStatus(Orders.PENDING_PAYMENT);
        orders.setPayStatus(Orders.UN_PAID);
        orders.setNumber(String.valueOf(System.currentTimeMillis()));
        orders.setPhone(byId.getPhone());
        orders.setConsignee(byId.getConsignee());
        orders.setUserId(BaseContext.getCurrentId());

        orderMapper.insert(orders);

        List<OrderDetail> oL=new ArrayList<OrderDetail>();

        for (ShoppingCart cart : list) {
            OrderDetail orderDetail=new OrderDetail();
            BeanUtils.copyProperties(cart,orderDetail);
            orderDetail.setOrderId(orders.getId());
            oL.add(orderDetail);
        }

        orderDetailMapper.insertBatch(oL);
        shoppingCartMapper.clear(BaseContext.getCurrentId());

        OrderSubmitVO vo=new OrderSubmitVO();
        vo.setOrderTime(LocalDateTime.now());
        vo.setId(orders.getId());
        vo.setOrderAmount(orders.getAmount());
        vo.setOrderNumber(orders.getNumber());
        return vo;
    }
}
