package com.lotteon.repository.admin;

import com.lotteon.entity.admin.Adminqna;
import com.lotteon.repository.custom.AdminQnaRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminQnaRepository extends JpaRepository<Adminqna, Integer> , AdminQnaRepositoryCustom {
}
