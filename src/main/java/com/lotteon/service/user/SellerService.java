package com.lotteon.service.user;

import com.lotteon.entity.User.Seller;
import com.lotteon.repository.user.SellerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SellerService {

    private final SellerRepository sellerRepository;

    public List<Seller> getAllSellers() { return sellerRepository.findAll(); }
}
