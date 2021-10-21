package com.chinatower.framework.consumer.filter;
import lombok.extern.slf4j.Slf4j;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.springframework.kafka.listener.MessageListenerContainer;
import org.springframework.stereotype.Component;

@Slf4j
//@Component
public class CousumerFilter {

    private final String MqConsumerMethod = "execution(* com.chinatower.framework.consumer.ConsumerMessage.*(..))";
    /**
     * MQ 消费端指定拦截器
     *
     * @param point 切入点
     * @return 消费返回结果
     * @throws Throwable 抛出异常
     */
    @Around(MqConsumerMethod)
    public void MqConsumerAround(ProceedingJoinPoint point) throws Throwable {
        //TODO 条件
        boolean condition=true;
        if (condition) {
        	//符合条件执行
        	point.proceed();
        }else {
        	
        }
       
    }
}
