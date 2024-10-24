package com.lotteon.repository.custom;

import com.lotteon.dto.page.PageRequestDTO;
import com.querydsl.core.Tuple;
import org.springframework.data.domain.Page;

import java.awt.print.Pageable;

public interface FaqRepositoryCustom {

    public Page<Tuple> selectFaqAllForList(PageRequestDTO pageRequestDTO, Pageable pageable);
    public Page<Tuple> selectFaqForSearch(PageRequestDTO pagerequestDTO, Pageable pageable);
}
