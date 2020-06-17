package com.atguigu.springcloud.alibaba.service.impl;

import com.atguigu.springcloud.alibaba.dao.OrderDao;
import com.atguigu.springcloud.alibaba.domain.Order;
import com.atguigu.springcloud.alibaba.service.AccountService;
import com.atguigu.springcloud.alibaba.service.OrderService;
import com.atguigu.springcloud.alibaba.service.StorageService;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private StorageService storageService;

    @Autowired
    private AccountService accountService;


    /**
     * 创建订单->调用库存服务扣减库存->调用帐户服务扣减帐户余额->修改订单状态
     *
     * @param order
     */
    @Override
    @GlobalTransactional(name = "fsp-create-order",rollbackFor = Exception.class)
    public void create(Order order) {

        //1.新建订单
        log.info("-------------> 开始新建订单");
        orderDao.create(order);
        //2.扣减库存
        log.info("----------------> 订单微服务开始调用库存，做扣减count");
        storageService.decrease(order.getProductId(),order.getCount());
        log.info("--------------->订单微服务开始调用库存，做扣减end");

        //3. 扣减账户
        log.info("------------> 订单微服务开始调用 账单，做扣减money");
        accountService.decrease(order.getUserId(),order.getMoney());
        log.info("--------------> 订单微服务开始调库存，做扣减end");

        //4.修改订单状态 ,从零到1，1代表已完成
        log.info("------------------>修改订单状态开始");
        orderDao.update(order.getUserId(),0);
        log.info("------------------> 修改订单状态结束");

        log.info("---------->下订单结束了");
    }
}
