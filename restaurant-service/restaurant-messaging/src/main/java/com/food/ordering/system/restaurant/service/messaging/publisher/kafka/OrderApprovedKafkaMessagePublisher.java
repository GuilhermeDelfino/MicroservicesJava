package com.food.ordering.system.restaurant.service.messaging.publisher.kafka;

import com.food.ordering.system.kafka.order.avro.model.RestaurantApprovalResponseAvroModel;
import com.food.ordering.system.kafka.producer.KafkaMessageHelper;
import com.food.ordering.system.kafka.producer.service.KafkaProducer;
import com.food.ordering.system.restaurant.service.domain.config.RestaurantServiceConfigDataClass;
import com.food.ordering.system.restaurant.service.domain.event.OrderApprovedEvent;
import com.food.ordering.system.restaurant.service.domain.ports.output.message.publisher.OrderApprovedMessagePublisher;
import com.food.ordering.system.restaurant.service.messaging.mapper.RestaurantMessagingDataMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class OrderApprovedKafkaMessagePublisher implements OrderApprovedMessagePublisher {
    private final RestaurantMessagingDataMapper restaurantMessagingDataMapper;
    private final KafkaProducer<String, RestaurantApprovalResponseAvroModel> kafkaProducer;
    private final RestaurantServiceConfigDataClass restaurantServiceConfigDataClass;
    private final KafkaMessageHelper kafkaMessageHelper;

    public OrderApprovedKafkaMessagePublisher(RestaurantMessagingDataMapper restaurantMessagingDataMapper, KafkaProducer<String, RestaurantApprovalResponseAvroModel> kafkaProducer, RestaurantServiceConfigDataClass restaurantServiceConfigDataClass, KafkaMessageHelper kafkaMessageHelper) {
        this.restaurantMessagingDataMapper = restaurantMessagingDataMapper;
        this.kafkaProducer = kafkaProducer;
        this.restaurantServiceConfigDataClass = restaurantServiceConfigDataClass;
        this.kafkaMessageHelper = kafkaMessageHelper;
    }

    @Override
    public void publish(OrderApprovedEvent domainEvent) {
        String orderId = domainEvent.getOrderApproval().getOrderId().getValue().toString();

        log.info("Received OrderApprovedEvent for order id: {}", orderId);

        try {
            RestaurantApprovalResponseAvroModel restaurantApprovalResponseAvroModel = restaurantMessagingDataMapper.orderApprovedEventToRestaurantApprovalResponseAvroModel(domainEvent);

            kafkaProducer.send(restaurantServiceConfigDataClass.getRestaurantApprovalRequestTopicName(),
                    orderId,
                    restaurantApprovalResponseAvroModel,
                    kafkaMessageHelper.getKafkaCallback(restaurantServiceConfigDataClass.getRestaurantApprovalResponseTopicName(), restaurantApprovalResponseAvroModel, orderId, "RestaurantApprovalResponseAvroModel")
            );
            log.info("RestaurantApprovalResponseAvroModel sent to kafka at: {}", System.nanoTime());
        }catch (Exception e){
            log.error("Error while send RestaurantApprovalResponseAvroModel message to kafka with order id: {}, error: {}", orderId, e.getMessage());
        }
    }
}
