package com.lotteon.repository.Impl;

import com.lotteon.dto.page.PageRequestDTO;
import com.lotteon.entity.QFaq;
import com.lotteon.repository.admin.FaqRepository;
import com.lotteon.repository.custom.FaqRepositoryCustom;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Repository;

import java.awt.print.Pageable;
import java.util.List;

@Log4j2
@RequiredArgsConstructor
@Repository
public class FaqRepositoryImpl implements FaqRepositoryCustom {

    private final JPAQueryFactory queryFactory;
    private QFaq faq = QFaq.faq;

    @Override
    public Page<Tuple> selectFaqAllForList(PageRequestDTO pageRequestDTO, Pageable pageable) {
//
//        List<Tuple> content = queryFactory
//                .select(faq.faqNo)
//                .from(faq).fetch();
//
//
//



        return null;
    }

    @Override
    public Page<Tuple> selectFaqForSearch(PageRequestDTO pagerequestDTO, Pageable pageable) {







        return null;
    }
}
