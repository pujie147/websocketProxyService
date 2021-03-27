package com.vdegree.february.im.common.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * TODO
 *
 * @author DELL
 * @version 1.0
 * @date 2021/3/26 16:20
 */
@Configuration
public class MQConfig {
    /**
     * 发给 imservice 的消费队列
     * @return
     */
    //队列 起名：TestDirectQueue
    @Bean
    public Queue TestDirectQueue() {
        // durable:是否持久化,默认是false,持久化队列：会被存储在磁盘上，当消息代理重启时仍然存在，暂存队列：当前连接有效
        // exclusive:默认也是false，只能被当前创建的连接使用，而且当连接关闭后队列即被删除。此参考优先级高于durable
        // autoDelete:是否自动删除，当没有生产者或者消费者使用此队列，该队列会自动删除。
        //   return new Queue("TestDirectQueue",true,true,false);
        //一般设置一下队列的持久化就好,其余两个就是默认false
        return new Queue("IMServiceDirectQueue");
    }
    //Direct交换机 起名：TestDirectExchange
    @Bean
    DirectExchange TestDirectExchange() {
        //  return new DirectExchange("TestDirectExchange",true,true);
        return new DirectExchange("IMServiceDirectExchange");
    }
    //绑定  将队列和交换机绑定, 并设置用于匹配键：TestDirectRouting
    @Bean
    Binding bindingDirect() {
        return BindingBuilder.bind(TestDirectQueue()).to(TestDirectExchange()).with("IMServiceDirectRouting");
    }




    /**
     * 延迟队列，把消息放在这里会自动过期
     */
    public static final String DELAY_QUEUE = "im.delay.queue";

    /**
     * 延迟队列交换机
     */
    public static final String DELAY_EXCHANGE = "im.delay.exchange";

    /**
     * 延迟队列路由
     */
    public static final String DELAY_ROUTINGKEY = "im.delay.routingKey";

    /**
     * 死信队列
     */
    public static final String DEAD_LETTER_QUEUE = "im.deadLetter.queue";

    /**
     * 死信交换机
     */
    private static final String DEAD_LETTER_EXCHANGE = "im.deadLetter.exchange";

    /**
     * 死信队列路由
     */
    private final String DEAD_LETTER_ROUTINGKEY = "im.deadLetter.routingKey";

    /**
     *
     * @return FanoutExchange
     */
    @Bean
    public FanoutExchange delayExchange() {
        return new FanoutExchange(DELAY_EXCHANGE);
    }
    /**
     * 延迟队列配置
     * <p>
     * 1、params.put("x-message-ttl", 5 * 1000);
     * 第一种方式是直接设置 Queue 延迟时间 但如果直接给队列设置过期时间,这种做法不是很灵活,（当然二者是兼容的,默认是时间小的优先）
     * 2、rabbitTemplate.convertAndSend(book, message -> {
     * message.getMessageProperties().setExpiration(2 * 1000 + "");
     * return message;
     * });
     * 第二种就是每次发送消息动态设置延迟时间,这样我们可以灵活控制
     **/
    @Bean
    public Queue delayQueue() {
        Map<String, Object> args = new HashMap<>();
        // x-dead-letter-exchange 声明了队列里的死信转发到的DLX名称，
        args.put("x-dead-letter-exchange", DEAD_LETTER_EXCHANGE);
        // x-dead-letter-routing-key 声明了这些死信在转发时携带的 routing-key 名称。
        args.put("x-dead-letter-routing-key", DEAD_LETTER_ROUTINGKEY);
        return new Queue(DELAY_QUEUE, true, false, false, args);
    }
    @Bean
    public Binding delayBinding() {
        return BindingBuilder.bind(delayQueue()).to(delayExchange());
    }
    @Bean
    public Queue deadLetterQueue() {
        return new Queue(DEAD_LETTER_QUEUE, true);
    }
    @Bean
    public DirectExchange deadLetterExchange() {
        return new DirectExchange(DEAD_LETTER_EXCHANGE);
    }
    @Bean
    public Binding deadLetterBinding() {
        return BindingBuilder.bind(deadLetterQueue()).to(deadLetterExchange()).with(DEAD_LETTER_ROUTINGKEY);
    }

}
