package com.lotteon.service.product;


import com.lotteon.entity.product.Option;
import com.lotteon.repository.product.OptionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Log4j2
@RequiredArgsConstructor
public class OptionService {

    private final OptionRepository optionRepository;


}
