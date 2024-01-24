package com.food.ordering.system.order.service.messaging.publisher.kafka;

import com.food.ordering.system.kafka.order.avro.model.PaymentRequestAvroModel;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFutureCallback;

@Component
@Slf4j
public class OrderKafkaMessageHelper {
    public <T> ListenableFutureCallback<SendResult<String, T>> getKafkaCallback(String responseTopicName, T requestAvroModel, String orderId, String requestAvroModelName) {
        return new ListenableFutureCallback<>() {
            @Override
            public void onFailure(Throwable ex) {
                log.error("Error while sending {} message: {} to topic {}", requestAvroModelName, requestAvroModel.toString(), responseTopicName, ex);
            }

            @Override
            public void onSuccess(SendResult<String, T> result) {
                RecordMetadata metaData = result.getRecordMetadata();
                log.info("Received successful response from Kafka for order id: {} Topic: {} Partition: {} Offset: {} Timestamp: {}",
                        orderId,
                        metaData.topic(),
                        metaData.partition(),
                        metaData.offset(),
                        metaData.timestamp()
                );
            }
        };
    }
}