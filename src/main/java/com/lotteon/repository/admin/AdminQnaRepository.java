package com.lotteon.repository.admin;

import com.lotteon.entity.QnA;
import com.lotteon.entity.admin.Adminqna;
import com.lotteon.repository.custom.AdminQnaRepositoryCustom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdminQnaRepository extends JpaRepository<Adminqna, Integer> , AdminQnaRepositoryCustom {
    // 특정 작성자(qna_writer)로 QnA 조회
    @Query("SELECT q FROM  QnA q WHERE q.qna_writer = :writer")
    List<QnA> findByQnaWriter(@Param("writer") String writer);

    // 특정 작성자(qna_writer)로 페이징 처리된 QnA 조회
    @Query("SELECT q FROM QnA q WHERE q.qna_writer = :writer")
    Page<QnA> findByQnaWriter(@Param("writer") String writer, Pageable pageable);
}
