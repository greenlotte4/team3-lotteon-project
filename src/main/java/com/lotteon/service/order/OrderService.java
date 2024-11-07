package com.lotteon.service.order;


import com.lotteon.controller.SellerController;
import com.lotteon.dto.User.SellerDTO;
import com.lotteon.dto.order.OrderCompletedResponseDTO;
import com.lotteon.dto.order.OrderDTO;
import com.lotteon.dto.order.OrderItemDTO;
import com.lotteon.dto.order.OrderResponseDTO;
import com.lotteon.dto.product.OptionDTO;
import com.lotteon.dto.product.ProductDTO;
import com.lotteon.entity.User.Seller;
import com.lotteon.entity.order.Order;
import com.lotteon.entity.order.OrderItem;
import com.lotteon.entity.product.Option;
import com.lotteon.entity.product.Product;
import com.lotteon.entity.product.ProductOptionCombination;
import com.lotteon.repository.order.OrderItemRepository;
import com.lotteon.repository.order.OrderRepository;
import com.lotteon.repository.product.ProductOptionCombinationRepository;
import com.lotteon.repository.product.ProductRepository;
import com.lotteon.service.product.OptionService;
import com.lotteon.service.product.ProductService;
import com.lotteon.service.user.SellerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.AuthenticatedPrincipal;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Log4j2
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final ModelMapper getModelMapper;
    private final ProductService productService;
    private final OptionService optionService;
    private final SellerService sellerService;
    private final SellerController sellerController;
    private final ProductOptionCombinationRepository productOptionCombinationRepository;
    private final ProductRepository productRepository;


    @Transactional
    public long saveOrder(OrderResponseDTO orderResponseDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String uid = authentication.getName();
        log.info("구매중 uid : " + uid);
        long result = 0;
        OrderDTO orderDTO = orderResponseDTO.getOrder();
        log.info("orderDTO!!!!!!!!!:" + orderDTO);
        orderDTO.setUid(uid);
        Order order = getModelMapper.map(orderDTO, Order.class);
        log.info("orderEntity!!!!!!!!!:" + orderDTO);

        Order savedOrder = orderRepository.save(order);
        log.info("productDicount!!! " + savedOrder.getProductDiscount());

        result = savedOrder.getOrderId();


        List<OrderItemDTO> orderItems = orderResponseDTO.getOrderItems();
        for (OrderItemDTO orderItemDTO : orderItems) {
            orderItemDTO.setOrderId(savedOrder.getOrderId());
            orderItemDTO.setOrder(getModelMapper.map(savedOrder, OrderDTO.class));

            ProductDTO product = productService.selectProduct(orderItemDTO.getProductId());


            log.info("sellerUid가 안들어와???" + product.getSeller());
            orderItemDTO.setSellerUid(product.getSellerId());
            orderItemDTO.setProduct(product);
            orderItemDTO.setSavedPrice(product.getPrice());


            if (orderItemDTO.getSelectOption() != null) {
                orderItemDTO.setOptionId(orderItemDTO.getOptionId());

                // option재고 업데이트
                Optional<ProductOptionCombination> productOptionCombination = productOptionCombinationRepository.findById(orderItemDTO.getOptionId());
                if (productOptionCombination.isPresent()) {
                    long savestock = productOptionCombination.get().getStock() - orderItemDTO.getStock();
                    productOptionCombinationRepository.updateQuantity(savestock, orderItemDTO.getCombinationId());
                    log.info("업데이트 재고 : " + savestock);
                }
            }
            long saveStock = product.getStock() - orderItemDTO.getStock();
            productRepository.updateProductQuantity(saveStock, orderItemDTO.getProductId());
            log.info("업데이트 재고 : " + saveStock);


            //각각의 오더아이템 저장
            OrderItem OrderItem = getModelMapper.map(orderItemDTO, OrderItem.class);
            OrderItem savedOrderItem = orderItemRepository.save(OrderItem);

        }


        return result;

    }

    //사용자별 유저 찾기
    public OrderCompletedResponseDTO selectOrderById(long id) {
        Order order = orderRepository.findByOrderId(id);
        log.info("order::::::::::::: " + order);
        //orderDTO에 orderItem이 없다.
        OrderDTO orderDTO = getModelMapper.map(order, OrderDTO.class);
        log.info("OrderDTO::::::::::: " + orderDTO);
        HashSet<SellerDTO> sellers = new HashSet<>();
        List<OrderItem> orderItems = order.getOrderProducts();
        orderDTO.setOrderItems(orderItems.stream().map((element) -> getModelMapper.map(element, OrderItemDTO.class)).collect(Collectors.toList()));

        List<OrderItemDTO> orderItemDtos = new ArrayList<>();
        for (OrderItem orderItem : orderItems) {
            OrderItemDTO orderItemDTO = getModelMapper.map(orderItem, OrderItemDTO.class);
            String sellerid = orderItem.getProduct().getSellerId();

            if (orderItem.getOptionId() != 0) {
                Optional<ProductOptionCombination> opt = productOptionCombinationRepository.findById(orderItem.getOptionId());
                if (opt.isPresent()) {
                    ProductOptionCombination optionCombination = opt.get();
                    orderItemDTO.setCombination(optionCombination.getCombination());
                    orderItemDTO.setCombinationId(optionCombination.getCombinationId());
                    orderItemDTO.setOrderItemId(orderItem.getOrderItemId());

                }
            }

            orderItemDtos.add(orderItemDTO);

            SellerDTO seller = sellerService.getSeller(sellerid);
            seller.setUid(sellerid);
            sellers.add(seller);
        }
        orderDTO.setOrderItems(orderItemDtos);

        log.info("sellereeeeeee:" + sellers);


        return new OrderCompletedResponseDTO(orderDTO, sellers);
    }

    //seller별 orderItem 찾기
    public List<OrderDTO> selectOrderItemsBySeller() {
        return null;
    }


//update Order

//deleteOrder

//반품요청

//환불요청

    public long getSalesCountBySeller(String sellerUid) {
        return orderItemRepository.countBySellerUid(sellerUid);
    }

    public long getTotalSalesAmountBySeller(String sellerUid) {
        return orderItemRepository.findTotalOrderPriceBySellerUid(sellerUid);
    }

    // 모든 판매자의 총 판매 수량을 반환
    public long getTotalSalesCountForAllSellers() {
        return orderItemRepository.findTotalOrderCountForAllSellers();
    }

    // 모든 판매자의 총 판매 금액을 반환
    public long getTotalSalesAmountForAllSellers() {
        return orderItemRepository.findTotalOrderPriceForAllSellers();
    }

    // 특정 판매자의 날짜 범위에 따른 주문 건수
    public long getSalesCountBySellerAndDateRange(String sellerUid, LocalDateTime start, LocalDateTime end) {
        return orderItemRepository.countOrdersBySellerAndDateRange(sellerUid, start, end);
    }

    // 특정 판매자의 날짜 범위에 따른 총 판매 금액
    public long getTotalSalesAmountBySellerAndDateRange(String sellerUid, LocalDateTime start, LocalDateTime end) {
        Long amount = orderItemRepository.sumSalesAmountBySellerAndDateRange(sellerUid, start, end);
        return amount != null ? amount : 0; // 금액이 null일 경우 0으로 처리
    }

    // 모든 판매자의 날짜 범위에 따른 주문 건수
    public long getTotalSalesCountForAllSellersByDateRange(LocalDateTime start, LocalDateTime end) {
        return orderItemRepository.countOrdersByDateRange(start, end);
    }

    // 모든 판매자의 날짜 범위에 따른 총 판매 금액
    public long getTotalSalesAmountForAllSellersByDateRange(LocalDateTime start, LocalDateTime end) {
        Long amount = orderItemRepository.sumSalesAmountByDateRange(start, end);
        return amount != null ? amount : 0;
    }
}




}