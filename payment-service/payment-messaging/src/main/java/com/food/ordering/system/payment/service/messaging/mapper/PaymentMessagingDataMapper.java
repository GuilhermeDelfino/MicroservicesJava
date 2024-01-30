package com.food.ordering.system.payment.service.messaging.mapper;

import com.food.ordering.system.domain.valueobject.PaymentOrderStatus;
import com.food.ordering.system.kafka.order.avro.model.PaymentRequestAvroModel;
import com.food.ordering.system.kafka.order.avro.model.PaymentResponseAvroModel;
import com.food.ordering.system.kafka.order.avro.model.PaymentStatus;
import com.food.ordering.system.payment.service.domain.dto.PaymentRequest;
import com.food.ordering.system.payment.service.domain.event.PaymentCancelledEvent;
import com.food.ordering.system.payment.service.domain.event.PaymentCompletedEvent;
import com.food.ordering.system.payment.service.domain.event.PaymentFailedEvent;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class PaymentMessagingDataMapper {

    public PaymentResponseAvroModel paymentCompletedEventToPaymentResponseAvroModel(PaymentCompletedEvent event){
        return PaymentResponseAvroModel.newBuilder()
                .setId(UUID.randomUUID().toString())
                .setSagaId("")
                .setPaymentId(event.getPayment().getId().getValue().toString())
                .setCustomerId(event.getPayment().getCustomerId().getValue().toString())
                .setOrderId(event.getPayment().getOrderId().getValue().toString())
                .setPrice(event.getPayment().getPrice().getAmount())
                .setCreatedAt(event.getCreatedAt().toInstant())
                .setPaymentStatus(PaymentStatus.valueOf(event.getPayment().getPaymentStatus().name()))
                .setFailureMessages(event.getFailureMessages())
                .build();
    }

    public PaymentResponseAvroModel paymentCancelledEventToPaymentResponseAvroModel(PaymentCancelledEvent event){
        return PaymentResponseAvroModel.newBuilder()
                .setId(UUID.randomUUID().toString())
                .setSagaId("")
                .setPaymentId(event.getPayment().getId().getValue().toString())
                .setCustomerId(event.getPayment().getCustomerId().getValue().toString())
                .setOrderId(event.getPayment().getOrderId().getValue().toString())
                .setPrice(event.getPayment().getPrice().getAmount())
                .setCreatedAt(event.getCreatedAt().toInstant())
                .setPaymentStatus(PaymentStatus.valueOf(event.getPayment().getPaymentStatus().name()))
                .setFailureMessages(event.getFailureMessages())
                .build();
    }

    public PaymentResponseAvroModel paymentFailedEventToPaymentResponseAvroModel(PaymentFailedEvent event){
        return PaymentResponseAvroModel.newBuilder()
                .setId(UUID.randomUUID().toString())
                .setSagaId("")
                .setPaymentId(event.getPayment().getId().getValue().toString())
                .setCustomerId(event.getPayment().getCustomerId().getValue().toString())
                .setOrderId(event.getPayment().getOrderId().getValue().toString())
                .setPrice(event.getPayment().getPrice().getAmount())
                .setCreatedAt(event.getCreatedAt().toInstant())
                .setPaymentStatus(PaymentStatus.valueOf(event.getPayment().getPaymentStatus().name()))
                .setFailureMessages(event.getFailureMessages())
                .build();
    }

    public PaymentRequest paymentRequestAvroModelToPaymentRequest(PaymentRequestAvroModel model){
        return PaymentRequest.builder()
                .id(model.getId())
                .sagaId(model.getId())
                .customerId(model.getCustomerId())
                .orderId(model.getOrderId())
                .price(model.getPrice())
                .createdAt(model.getCreatedAt())
                .paymentOrderStatus(PaymentOrderStatus.valueOf(model.getPaymentOrderStatus().name()))
                .build();
    }
}
