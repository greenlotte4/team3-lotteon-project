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
import com.lotteon.entity.product.QOption;
import com.lotteon.repository.order.OrderItemRepository;
import com.lotteon.repository.order.OrderRepository;
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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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


    @Transactional
    public long saveOrder(OrderResponseDTO orderResponseDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String uid = authentication.getName();
        log.info("구매중 uid : "+uid);
        long result =0;
        OrderDTO orderDTO = orderResponseDTO.getOrder();
        orderDTO.setUid(uid);
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
    public OrderCompletedResponseDTO selectOrderById(long id){
       Order order= orderRepository.findByOrderId(id);
       log.info("order::::::::::::: "+order);
       //orderDTO에 orderItem이 없다.
        OrderDTO orderDTO = getModelMapper.map(order,OrderDTO.class);
        log.info("OrderDTO::::::::::: "+orderDTO);
        HashSet<SellerDTO> sellers= new HashSet<>();
       List<OrderItem> orderItems  = order.getOrderProducts();
       orderDTO.setOrderItems(orderItems.stream().map((element) -> getModelMapper.map(element, OrderItemDTO.class)).collect(Collectors.toList()));

       List<OrderItemDTO> orderItemDtos  = new ArrayList<>();
       for(OrderItem orderItem : orderItems) {
           OrderItemDTO orderItemDTO = getModelMapper.map(orderItem,OrderItemDTO.class);
           String sellerid= orderItem.getProduct().getSellerId();
           List<Option> optiondtos = orderItem.getProduct().getOptions();
           for(Option option : optiondtos) {
               log.info("여긴오녀???"+option);

               if(option.getId() == orderItem.getOptionId()) {
                   log.info("일치!!!!!!!!!"+option);
                   orderItemDTO.setSelectOption(getModelMapper.map(option, OptionDTO.class));
               }
           }
           orderItemDtos.add(orderItemDTO);

           SellerDTO seller = sellerService.getSeller(sellerid);
           seller.setUid(sellerid);
           sellers.add(seller);
       }
       orderDTO.setOrderItems(orderItemDtos);

       log.info("sellereeeeeee:"+sellers);


        return new OrderCompletedResponseDTO(orderDTO,sellers);
    }

    //seller별 orderItem 찾기
    public void selectOrderItemsBySeller(){}


    //update Order

    //deleteOrder

    //반품요청

    //환불요청



}
