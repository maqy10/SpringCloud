package com.atguigu.springcloud.dao;

import com.atguigu.springcloud.entity.Payment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface PaymentMapper {
//    插入数据，返回结果为插入成功的记录数
    public int create(Payment payment);

    public Payment getPaymentById(@Param("id") Long id);
}
