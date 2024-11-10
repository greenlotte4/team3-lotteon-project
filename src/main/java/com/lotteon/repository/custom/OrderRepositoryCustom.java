package com.lotteon.repository.custom;

import com.lotteon.dto.page.PageRequestDTO;
import com.querydsl.core.Tuple;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderRepositoryCustom {

    public Page<Tuple> selectOrderAllForList(PageRequestDTO pageRequestDTO, Pageable pageable);
}
