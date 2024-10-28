package com.lotteon.service.user;


/*
    날짜 ; 2024.10.27
    이름 : 하진희
    내용 : seller.user.uid로 조회
 */

import com.lotteon.dto.User.SellerDTO;
import com.lotteon.entity.User.Seller;
import com.lotteon.repository.user.SellerRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Log4j2
@RequiredArgsConstructor
public class SellerService {

    private final SellerRepository sellerRepository;


    public List<Seller> getAllSellers() {
        return sellerRepository.findAll();
    }

    private final ModelMapper getModelMapper;

    public SellerDTO getSeller(String sellerId) {
        Optional<Seller> opt = sellerRepository.findByUserUid(sellerId);
        SellerDTO sellerDTO = new SellerDTO();
        if (opt.isPresent()) {
            Seller seller = opt.get();
            sellerDTO = getModelMapper.map(seller, SellerDTO.class);
        }

        log.info("sellerrrrrrrrrrrrrrrr:" + sellerDTO);
        return sellerDTO;
    }

    @Transactional
    public void deleteSellersByIds(List<Long> sellerIds) {
        for (Long sellerId : sellerIds) {
            Optional<Seller> sellerOptional = sellerRepository.findById(sellerId);
            if (sellerOptional.isPresent()) {
                // Seller가 존재하면 삭제
                sellerRepository.delete(sellerOptional.get());
            } else {
                throw new EntityNotFoundException("일치하는 아이디의 seller가 없습니다.: " + sellerId);
            }
        }
    }

}
