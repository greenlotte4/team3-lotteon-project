package com.lotteon.service.product;


import com.lotteon.dto.product.OptionDTO;
import com.lotteon.entity.product.Option;
import com.lotteon.repository.product.OptionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Log4j2
@RequiredArgsConstructor
public class OptionService {

    private final OptionRepository optionRepository;


    public OptionDTO updateStock(OptionDTO optionDTO){
        Optional<Option> option = optionRepository.findById(optionDTO.getId());
        if(option.isPresent()){
            Option optionEntity = option.get();
            optionEntity.setStock(optionDTO.getOptionStock());
            optionRepository.save(optionEntity);
        }


        return optionDTO;
    }

}
