package com.lotteon.service.admin;

import com.lotteon.dto.FaqDTO;
import com.lotteon.entity.Faq;
import com.lotteon.repository.admin.FaqRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class FaqService {
    private final FaqRepository faqRepository;
    private final ModelMapper modelMapper;

    public Faq insertfaq(FaqDTO faqDTO) {
        Faq faq = faqRepository.save(modelMapper.map(faqDTO, Faq.class));
        return faq;
    }


    public Faq updatefaq(FaqDTO faqDTO) {
        boolean result = faqRepository.existsById(faqDTO.getFaqNo());
        if(result) {
           Faq faq = faqRepository.save(modelMapper.map(faqDTO, Faq.class));
           return faq;
        }else {
            throw new EntityNotFoundException("FAQ not found for ID: " + faqDTO.getFaqNo());
        }
    }


    public List<FaqDTO> selectAllfaq(){
        List<Faq> faqs = faqRepository.findAll();
        List<FaqDTO> faqDTOs = new ArrayList<>();
        for (Faq faq : faqs) {
            FaqDTO faqDTO = modelMapper.map(faq, FaqDTO.class);
            faqDTOs.add(faqDTO);
        }
        return faqDTOs;
    }
    public FaqDTO selectfaq(int no){
        Optional<Faq> optfaq = faqRepository.findById(no);
        if(optfaq.isPresent()){
            Faq faq = optfaq.get();
            FaqDTO faqDTO = modelMapper.map(faq, FaqDTO.class);
            return faqDTO;
        }
        return null;
    }

    }

