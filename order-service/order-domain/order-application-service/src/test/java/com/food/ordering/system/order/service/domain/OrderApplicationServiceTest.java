package com.food.ordering.system.order.service.domain;

import com.food.ordering.system.domain.valueobject.*;
import com.food.ordering.system.order.service.domain.dto.create.CreateOrderCommand;
import com.food.ordering.system.order.service.domain.dto.create.CreateOrderResponse;
import com.food.ordering.system.order.service.domain.dto.create.OrderAddress;
import com.food.ordering.system.order.service.domain.dto.create.OrderItem;
import com.food.ordering.system.order.service.domain.entity.Customer;
import com.food.ordering.system.order.service.domain.entity.Order;
import com.food.ordering.system.order.service.domain.entity.Product;
import com.food.ordering.system.order.service.domain.entity.Restaurant;
import com.food.ordering.system.order.service.domain.exception.OrderDomainException;
import com.food.ordering.system.order.service.domain.mapper.OrderDataMapper;
import com.food.ordering.system.order.service.domain.ports.input.service.OrderApplicationService;
import com.food.ordering.system.order.service.domain.ports.output.repository.CustomerRepository;
import com.food.ordering.system.order.service.domain.ports.output.repository.OrderRepository;
import com.food.ordering.system.order.service.domain.ports.output.repository.RestaurantRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = OrderTestConfiguration.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class OrderApplicationServiceTest {

    @Autowired
    private OrderApplicationService orderApplicationService;

    @Autowired
    private OrderDataMapper orderDataMapper;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private RestaurantRepository restaurantRepository;

    private CreateOrderCommand createOrderCommand;
    private CreateOrderCommand createOrderCommandWrong;
    private CreateOrderCommand createOrderCommandWrongWithProductPrice;
    private final UUID CUSTOMER_ID = UUID.fromString("9e8be5d6-9b74-11ee-8c90-0242ac120002");
    private final UUID RESTAURANT_ID = UUID.fromString("38340996-9b9f-11ee-8c90-0242ac120002");
    private final UUID PRODUCT_ID = UUID.fromString("3e83f900-9b9f-11ee-8c90-0242ac120002");
    private final UUID ORDER_ID = UUID.fromString("421d4d78-9b9f-11ee-8c90-0242ac120002");
    private final BigDecimal PRICE = new BigDecimal("200.00");

    @BeforeAll
    public void init () {
        createOrderCommand = CreateOrderCommand
                .builder()
                .customerId(CUSTOMER_ID)
                .restaurantId(RESTAURANT_ID)
                .address(
                        OrderAddress.builder()
                                .street("street-1")
                                .postalCode("1000AB")
                                .city("Paris")
                                .build()
                )
                .price(PRICE)
                .items(
                        List.of(OrderItem.builder()
                                        .productId(PRODUCT_ID)
                                        .price(new BigDecimal("50.00"))
                                        .subTotal(new BigDecimal("50.00"))
                                        .quantity(1)
                                        .build(),
                                OrderItem.builder()
                                        .productId(PRODUCT_ID)
                                        .price(new BigDecimal("50.00"))
                                        .subTotal(new BigDecimal("150.00"))
                                        .quantity(3)
                                        .build())
                ).build();

        createOrderCommandWrong = CreateOrderCommand
                .builder()
                .customerId(CUSTOMER_ID)
                .restaurantId(RESTAURANT_ID)
                .address(
                        OrderAddress.builder()
                                .street("street-1")
                                .postalCode("1000AB")
                                .city("Paris")
                                .build()
                )
                .price(new BigDecimal("250.00"))
                .items(
                        List.of(OrderItem.builder()
                                        .productId(PRODUCT_ID)
                                        .price(new BigDecimal("50.00"))
                                        .subTotal(new BigDecimal("50.00"))
                                        .quantity(1)
                                        .build(),
                                OrderItem.builder()
                                        .productId(PRODUCT_ID)
                                        .price(new BigDecimal("50.00"))
                                        .subTotal(new BigDecimal("150.00"))
                                        .quantity(3)
                                        .build())
                ).build();

        createOrderCommandWrongWithProductPrice = CreateOrderCommand
                .builder()
                .customerId(CUSTOMER_ID)
                .restaurantId(RESTAURANT_ID)
                .address(
                        OrderAddress.builder()
                                .street("street-1")
                                .postalCode("1000AB")
                                .city("Paris")
                                .build()
                )
                .price(new BigDecimal("210.00"))
                .items(
                        List.of(OrderItem.builder()
                                        .productId(PRODUCT_ID)
                                        .price(new BigDecimal("60.00"))
                                        .subTotal(new BigDecimal("60.00"))
                                        .quantity(1)
                                        .build(),
                                OrderItem.builder()
                                        .productId(PRODUCT_ID)
                                        .price(new BigDecimal("50.00"))
                                        .subTotal(new BigDecimal("150.00"))
                                        .quantity(3)
                                        .build())
                ).build();

        Customer customer = new Customer();
        customer.setId(new CustomerId(CUSTOMER_ID));

        Restaurant restaurant = Restaurant.Builder.builder()
                .id(new RestaurantId(createOrderCommand.getRestaurantId()))
                .products(List.of(
                        new Product(new ProductId(PRODUCT_ID), "product-1", new Money(new BigDecimal("50.00"))),
                        new Product(new ProductId(PRODUCT_ID), "product-2", new Money(new BigDecimal("50.00")))
                            )
                        )
                .active(true)
                .build();

        Order order = orderDataMapper.createOrderCommandToOrder(createOrderCommand);
        order.setId(new OrderId(ORDER_ID));

        when(customerRepository.findCustomer(CUSTOMER_ID))
                .thenReturn(Optional.of(customer));

        when(restaurantRepository.findRestaurantInformation(orderDataMapper.createOrderCommandRestaurant(createOrderCommand)))
                .thenReturn(Optional.of(restaurant));

        when(orderRepository.save(any(Order.class))).thenReturn(order);

    }


    @Test
    public void testCreateOrder(){
        CreateOrderResponse order = orderApplicationService.createOrder(createOrderCommand);
        assertEquals(order.getOrderStatus(), OrderStatus.PENDING);
        assertEquals(order.getMessage().toLowerCase(), "Order Created Successfully".toLowerCase());
        assertNotNull(order.getOrderTrackingId());
    }

    @Test
    public void testCreateOrderWithWrongTotalPrice(){
        OrderDomainException orderDomainException = assertThrows(OrderDomainException.class,
                ()-> orderApplicationService.createOrder(createOrderCommandWrong));

        assertEquals(orderDomainException.getMessage(), "Total price: 250.00 is not equal to Order items total: 200.00");
    }

    @Test
    public void testCreateOrderWrongProductPrice(){
        OrderDomainException orderDomainException = assertThrows(OrderDomainException.class,
                () -> orderApplicationService.createOrder(createOrderCommandWrongWithProductPrice));

        assertEquals(orderDomainException.getMessage(), "Order item price: 60.00 is not valid for product "+ PRODUCT_ID);
    }

    @Test
    public void testCreateOrderWithPassiveRestaurant(){
        Restaurant restaurantPassive = Restaurant.Builder.builder()
                .id(new RestaurantId(createOrderCommand.getRestaurantId()))
                .products(List.of(
                                new Product(new ProductId(PRODUCT_ID), "product-1", new Money(new BigDecimal("50.00"))),
                                new Product(new ProductId(PRODUCT_ID), "product-2", new Money(new BigDecimal("50.00")))
                        )
                )
                .active(false)
                .build();

        when(restaurantRepository.findRestaurantInformation(orderDataMapper.createOrderCommandRestaurant(createOrderCommand)))
                .thenReturn(Optional.of(restaurantPassive));

        OrderDomainException orderDomainException = assertThrows(OrderDomainException.class, () ->
                orderApplicationService.createOrder(createOrderCommand));

        assertEquals(orderDomainException.getMessage(),
                "Restaurant with id: "+ RESTAURANT_ID +" is currently not active");


    }
}
