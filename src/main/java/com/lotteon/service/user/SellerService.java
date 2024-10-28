package com.lotteon.service.user;


/*
    날짜 ; 2024.10.27
    이름 : 하진희
    내용 : seller.user.uid로 조회
 */

import com.lotteon.dto.User.SellerDTO;
import com.lotteon.entity.User.Seller;
import com.lotteon.repository.user.SellerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Log4j2
@RequiredArgsConstructor
public class SellerService {

    private final SellerRepository sellerRepository;


    public List<Seller> getAllSellers() { return sellerRepository.findAll(); }

    private final ModelMapper getModelMapper;

    public SellerDTO getSeller(String sellerId) {
       Optional<Seller> opt=  sellerRepository.findByUserUid(sellerId);
       SellerDTO sellerDTO = new SellerDTO();
       if(opt.isPresent()){
           Seller seller = opt.get();
           sellerDTO  = getModelMapper.map(seller, SellerDTO.class);
       }

       log.info("sellerrrrrrrrrrrrrrrr:"+sellerDTO);
       return sellerDTO;
    }

}
