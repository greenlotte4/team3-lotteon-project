package com.lotteon.service.order;


import com.lotteon.dto.order.OrderDTO;
import com.lotteon.dto.order.OrderItemDTO;
import com.lotteon.dto.order.OrderResponseDTO;
import com.lotteon.dto.product.OptionDTO;
import com.lotteon.dto.product.ProductDTO;
import com.lotteon.entity.order.Order;
import com.lotteon.entity.order.OrderItem;
import com.lotteon.repository.order.OrderItemRepository;
import com.lotteon.repository.order.OrderRepository;
import com.lotteon.service.product.OptionService;
import com.lotteon.service.product.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Log4j2
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final ModelMapper getModelMapper;
    private final ProductService productService;
    private final OptionService optionService;


    @Transactional
    public long saveOrder(OrderResponseDTO orderResponseDTO) {
        long result =0;
        OrderDTO orderDTO = orderResponseDTO.getOrder();
        Order order =  orderDTO.toEntity();
        Order savedOrder = orderRepository.save(order);
        result=savedOrder.getOrderId();

        List<OrderItemDTO> orderItems = orderResponseDTO.getOrderItems();
        for(OrderItemDTO orderItemDTO : orderItems) {
            orderItemDTO.setOrderId(savedOrder.getOrderId());
            orderItemDTO.setOrder(getModelMapper.map(savedOrder,OrderDTO.class));

            ProductDTO product = productService.selectProduct(orderItemDTO.getProductId());
            orderItemDTO.setProduct(product);
            orderItemDTO.setOptionId(orderItemDTO.getOptionId());

            // option재고 업데이트
            List<OptionDTO>  options = product.getOptions();
            for(OptionDTO optionDTO : options) {
                if(optionDTO.getId() == orderItemDTO.getOptionId()) {
                    long currentStock = optionDTO.getOptionStock();
                    long orderStock = orderItemDTO.getStock();
                    long resultStock = currentStock - orderStock;
                    if(resultStock < 0) {
                        result=0;
                    }
                    optionDTO.setOptionStock(resultStock);
                    optionService.updateStock(optionDTO);
                    log.info("option stock update!");
                }
            }

            //각각의 오더아이템 저장
            OrderItem OrderItem = getModelMapper.map(orderItemDTO,OrderItem.class);
           OrderItem savedOrderItem = orderItemRepository.save(OrderItem);

        }

        return result;

    }

    //사용자별 유저 찾기
    public void selectOrderByUid(){}

    //seller별 orderItem 찾기
    public void selectOrderItemsBySeller(){}

    //update Order

    //deleteOrder

    //반품요청

    //환불요청



}
