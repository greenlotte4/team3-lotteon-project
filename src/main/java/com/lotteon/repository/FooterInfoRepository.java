package com.lotteon.repository;

import com.lotteon.DTO.FooterInfoDTO;
import com.lotteon.entity.FooterInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FooterInfoRepository extends JpaRepository<FooterInfo, Integer> {

    FooterInfo findTopByOrderByFt_idDesc(); // 엔티티를 반환
}
