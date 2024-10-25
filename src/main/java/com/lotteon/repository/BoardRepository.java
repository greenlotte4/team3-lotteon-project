package com.lotteon.repository;

import com.lotteon.entity.BoardCate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BoardRepository  extends JpaRepository<BoardCate, Integer> {
    public List<BoardCate> findByLevel(int level); // level entity에있는 이름

}
