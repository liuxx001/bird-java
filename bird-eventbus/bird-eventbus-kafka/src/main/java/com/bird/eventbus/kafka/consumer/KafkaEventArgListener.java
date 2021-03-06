package com.bird.eventbus.kafka.consumer;

import com.bird.eventbus.arg.EventArg;
import com.bird.eventbus.handler.EventMethodInvoker;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.listener.AcknowledgingMessageListener;
import org.springframework.kafka.support.Acknowledgment;

/**
 * kafka事件监听器
 * @author liuxx
 */
public class KafkaEventArgListener implements AcknowledgingMessageListener<String,EventArg> {

    private EventMethodInvoker eventMethodInvoker;

    public KafkaEventArgListener(EventMethodInvoker eventMethodInvoker) {
        this.eventMethodInvoker = eventMethodInvoker;
    }

    /**
     * 消费者拿到消息时就提交offset,消费失败时在消费者内部重试
     * 避免消费者服务中多个事件处理程序情况下，一个处理程序失败，导致其他处理程序重试的问题
     *
     * @param data           data
     * @param acknowledgment acknowledgment
     */
    @Override
    public void onMessage(ConsumerRecord<String, EventArg> data, Acknowledgment acknowledgment) {
        if (data == null) {
            return;
        }
        eventMethodInvoker.invoke(data.value());
        acknowledgment.acknowledge();
    }
}
