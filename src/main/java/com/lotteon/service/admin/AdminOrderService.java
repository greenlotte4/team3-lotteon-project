package com.lotteon.service.admin;

import com.lotteon.dto.order.OrderDTO;
import com.lotteon.dto.order.OrderItemDTO;
import com.lotteon.entity.order.Order;
import com.lotteon.entity.order.OrderItem;
import com.lotteon.repository.order.OrderItemRepository;
import com.lotteon.repository.order.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Log4j2
@RequiredArgsConstructor
@Service
public class AdminOrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final ModelMapper modelMapper;
    private final ModelMapper getModelMapper;

    public List<OrderDTO> selectOrdersAll() {
        List<Order> orders = orderRepository.findAll();
        List<OrderDTO> orderDTOs = new ArrayList<>();

        for (Order order : orders) {
            OrderDTO dto = getModelMapper.map(order, OrderDTO.class);
            orderDTOs.add(dto);
            log.info("이값이뭐야 야옹 asdf:" + orderDTOs);
        }
        return orderDTOs;

        }
    public OrderItemDTO selectOrderItemById(Long id) {
        OrderItem orderItem = orderItemRepository.findById(id).orElse(null);
        log.info("이거 값이 나오냐냐?! : " + orderItem );
        OrderItemDTO orderItemDTO = getModelMapper.map(orderItem, OrderItemDTO.class);
        return orderItemDTO;
    }

}


