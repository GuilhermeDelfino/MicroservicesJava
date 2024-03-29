package com.food.ordering.system.payment.service.messaging.listener.kafka;

import com.food.ordering.system.kafka.consumer.KafkaConsumer;
import com.food.ordering.system.kafka.order.avro.model.PaymentOrderStatus;
import com.food.ordering.system.kafka.order.avro.model.PaymentRequestAvroModel;
import com.food.ordering.system.payment.service.domain.ports.input.message.listener.PaymentRequestMessageListener;
import com.food.ordering.system.payment.service.messaging.mapper.PaymentMessagingDataMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class PaymentRequestKafkaListener implements KafkaConsumer<PaymentRequestAvroModel> {
    private final PaymentRequestMessageListener paymentRequestMessageListener;
    private final PaymentMessagingDataMapper paymentMessagingDataMapper;

    public PaymentRequestKafkaListener(PaymentRequestMessageListener paymentRequestMessageListener, PaymentMessagingDataMapper paymentMessagingDataMapper) {
        this.paymentRequestMessageListener = paymentRequestMessageListener;
        this.paymentMessagingDataMapper = paymentMessagingDataMapper;
    }


    @Override
    @KafkaListener(id = "${kafka-consumer-config.payment-consumer-group-id}", topics = "${payment-service.payment-request-topic-name}")
    public void receive(
            @Payload List<PaymentRequestAvroModel> messages,
            @Header(KafkaHeaders.RECEIVED_MESSAGE_KEY) List<String> keys,
            @Header(KafkaHeaders.RECEIVED_PARTITION_ID) List<Integer> partitions,
            @Header(KafkaHeaders.OFFSET) List<Long> offsets) {

        log.info("{} number of payment request received with keys: {}, partitions: {} and offsets: {}", messages.size(), keys.toString(), partitions.toString(), offsets.toString());

        messages.forEach(model -> {
            if(model.getPaymentOrderStatus() == PaymentOrderStatus.PENDING){
                log.info("Processing payment for order id: {}", model.getOrderId());
                paymentRequestMessageListener.completePayment( paymentMessagingDataMapper.paymentRequestAvroModelToPaymentRequest(model) );

            } else if (model.getPaymentOrderStatus() == PaymentOrderStatus.CANCELLED) {
                log.info("Cancelling payment for order id: {}", model.getOrderId());
                paymentRequestMessageListener.cancelPayment( paymentMessagingDataMapper.paymentRequestAvroModelToPaymentRequest(model) );
            }
        });

    }
}
