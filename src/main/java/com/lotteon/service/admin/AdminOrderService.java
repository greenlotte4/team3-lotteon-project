package com.lotteon.service.admin;

import com.lotteon.dto.admin.AdminOrderDTO;
import com.lotteon.dto.admin.AdminOrderItemDTO;
import com.lotteon.dto.order.OrderDTO;
import com.lotteon.dto.order.OrderItemDTO;
import com.lotteon.dto.page.AdminOrderPageResponseDTO;
import com.lotteon.dto.page.PageRequestDTO;
import com.lotteon.dto.product.ProductDTO;
import com.lotteon.entity.order.Order;
import com.lotteon.entity.order.OrderItem;
import com.lotteon.entity.product.Product;
import com.lotteon.repository.order.OrderItemRepository;
import com.lotteon.repository.order.OrderRepository;
import com.querydsl.core.Tuple;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
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

    public AdminOrderPageResponseDTO selectOrderListAll(PageRequestDTO pageRequestDTO){
        Pageable pageable = pageRequestDTO.getPageable("no");
        Page<Tuple> pageAdminOrder = null;
        log.info("abababababab:"+ pageRequestDTO.getKeyword());
        if(pageRequestDTO.getKeyword() == null){
            pageAdminOrder = orderRepository.selectOrderAllForList(pageRequestDTO, pageable);
        }else {
            pageAdminOrder = orderRepository.selectOrderSearchForList(pageRequestDTO, pageable);
        }


        List<AdminOrderDTO> orderList = pageAdminOrder.getContent().stream().map(tuple -> {
            Long id = tuple.get(0, Long.class);
            log.info("이거 aaaaaaaid머야 나와?:"+ id);

            Order orders = orderRepository.findById(id).orElseThrow(() -> new RuntimeException("Faq not found with ID: " + id));; //조건주고 조회하기
            log.info("이게 order!! 머야?:"+ orders);

            List<OrderItem> orderItems = orderItemRepository.findByOrder_OrderId(id);
            log.info("아니 orderItems가 계속 조회가 안됨: " + orderItems);

            AdminOrderDTO adminOrderDTO = getModelMapper.map(orders, AdminOrderDTO.class);
            // OrderItem들을 OrderItemDTO로 변환


            List<OrderItemDTO> orderItemDTOs = orderItems.stream()
                    .map(orderItem -> modelMapper.map(orderItem, OrderItemDTO.class))
                    .collect(Collectors.toList());

            log.info("아니 orderItemDTO를 변환해야돼 : " + orderItemDTOs);


            adminOrderDTO.setOrderItems(orderItemDTOs);
            log.info("야옹하고울어요!: " + adminOrderDTO);
            return adminOrderDTO;


        }).toList();
        int total = (int) pageAdminOrder.getTotalElements();

        return AdminOrderPageResponseDTO.builder()
                .pageRequestDTO(pageRequestDTO)
                .adminorderdtoList(orderList)
                .total(total)
                .build();
    }

//    public AdminOrderPageResponseDTO selectOrderListAll(PageRequestDTO pageRequestDTO) {
//        Pageable pageable = pageRequestDTO.getPageable("no");
//
//        // OrderId만 페이징하여 가져오기
//        List<Long> orderIds = orderRepository.findOrderIdsForPage(pageable);
//
//        // 선택된 컬럼만 가져오기
//        List<Tuple> tuples = orderRepository.findSelectedOrdersWithItemsByIds(orderIds);
//        log.info("aaaaaaaaaaaaaaaaaaaaaaaaa: " + tuples);
//
//        // OrderId별로 Grouping하여 DTO 변환
//        Map<Long, AdminOrderDTO> orderMap = new HashMap<>();
//
//        tuples.forEach(tuple -> {
//            Long orderId = tuple.get(0, Long.class);
//
//            // Order 정보가 이미 있는지 확인하고 없으면 AdminOrderDTO 생성
//            AdminOrderDTO adminOrderDTO = orderMap.computeIfAbsent(orderId, id -> {
//                AdminOrderDTO dto = new AdminOrderDTO();
//                dto.setOrderId(id);
//                dto.setMemberName(tuple.get(1, String.class));
//                dto.setTotalPrice(tuple.get(2, Long.class));
//                dto.setOrderDate(tuple.get(3, LocalDateTime.class));
//                dto.setUid(tuple.get(4, String.class));
//                dto.setMemberName(tuple.get(5, String.class));
//                dto.setTotalQuantity(tuple.get(6, Long.class));
//                dto.setOrderItems(new ArrayList<>());
//                return dto;
//            });
//
//            // OrderItemDTO 생성 후 추가
//            AdminOrderItemDTO AdminorderItemDTO = new AdminOrderItemDTO();
//            AdminorderItemDTO.setProduct(tuple.get(7, ProductDTO.class));
//            AdminorderItemDTO.setOrderItemId(tuple.get(8, Long.class));
//            AdminorderItemDTO.setOrderPrice(tuple.get(9, Long.class));
//            AdminorderItemDTO.setPrice(tuple.get(10, Long.class));
//            AdminorderItemDTO.setSavedDiscount(tuple.get(11, Long.class));
//            AdminorderItemDTO.setSavedPrice(tuple.get(12, Long.class));
//            AdminorderItemDTO.setShippingFees(tuple.get(13, Long.class));
//            AdminorderItemDTO.setStock(tuple.get(14, Long.class));
//
//            adminOrderDTO.getOrderItems().add(AdminorderItemDTO);
//        });
//
//        List<AdminOrderDTO> orderList = new ArrayList<>(orderMap.values());
//        int total = (int) orderRepository.countTotalOrders();
//
//        return AdminOrderPageResponseDTO.builder()
//                .pageRequestDTO(pageRequestDTO)
//                .adminorderdtoList(orderList)
//                .total(total)
//                .build();
//    }

}


