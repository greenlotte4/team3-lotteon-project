package com.lotteon.repository.admin;

import com.lotteon.entity.Notice;
import com.lotteon.repository.custom.NoticeRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NoticeRepository extends JpaRepository<Notice, Long> , NoticeRepositoryCustom {
}
