package com.lotteon.repository.custom;

import com.lotteon.dto.page.PageRequestDTO;
import com.lotteon.entity.order.OrderItem;
import com.querydsl.core.Tuple;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderItemRepositoryCustom {

    public Page<OrderItem> selectOrderSearchForList(PageRequestDTO pageRequestDTO, Pageable pageable);
}
