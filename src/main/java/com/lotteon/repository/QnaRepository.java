package com.lotteon.repository;

import com.lotteon.entity.QnA;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface QnaRepository extends JpaRepository<QnA, Integer> {
     @Query("SELECT q FROM QnA q WHERE q.qna_writer = :writer")
     List<QnA> findByQnaWriter(@Param("writer") String writer);

     @Query("SELECT q FROM QnA q WHERE q.qna_writer = :writer")
     Page<QnA> findByQnaWriter(@Param("writer") String writer, Pageable pageable);
}
