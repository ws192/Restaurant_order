package com.sky.service;

import com.sky.dto.OrdersSubmitDTO;
import com.sky.result.Result;
import com.sky.vo.OrderSubmitVO;

public interface OrderService {
    OrderSubmitVO submitOrder(OrdersSubmitDTO dto);
}
