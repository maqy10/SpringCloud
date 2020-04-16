package com.atguigu.springcloud.controller;

import com.atguigu.springcloud.entity.CommonResult;
import com.atguigu.springcloud.entity.Payment;
import com.atguigu.springcloud.service.PaymentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @Value("${server.port}")
    private String serverPort;

    @Autowired
    private DiscoveryClient discoveryClient;

    @PostMapping("/payment/create")
    public CommonResult create(@RequestBody Payment payment){
        int result = paymentService.create(payment);
        log.info("----------------插入结果："+result);
        if(result > 0){
            return new CommonResult(200,"插入数据库成功,serverPort:"+serverPort,result);
        }else{
            return new CommonResult(444,"插入数据库失败,serverPort:"+serverPort,null);
        }
    }

    @GetMapping("/payment/get/{id}")
    public CommonResult<Payment> getPaymentById(@PathVariable("id") Long id){
        Payment payment = paymentService.getPaymentById(id);
        if(payment != null){
            return new CommonResult<>(200,"查询成功,serverPort:"+serverPort,payment);
        }else{
            return new CommonResult<>(444,"没有对应记录，查询ID"+id+",serverPort:"+serverPort,null);
        }
    }

    // 获取服务信息
    @GetMapping("/payment/discovery")
    public Object discovery(){
        //获取服务清单列表
        List<String> service = discoveryClient.getServices();
        for (String s : service) {
            log.info("------------------s:"+s);
        }

        //获取该服务下的全部具体实例
        List<ServiceInstance> instances = discoveryClient.getInstances("CLOUD-PAYMENT-SERVICE");
        for (ServiceInstance instance : instances) {
            // serviceId ：服务名
            // CLOUD-PAYMENT-SERVICE	192.168.72.55	http://192.168.72.55:8001
            log.info(instance.getServiceId()+"\t"+instance.getHost()+"\t"+instance.getUri());
        }

        return this.discoveryClient;
    }


    @GetMapping("/payment/lb")
    public String getPaymentLB(){
        return serverPort;
    }

}
