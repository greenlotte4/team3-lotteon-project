package com.lotteon.service.admin;

import com.lotteon.dto.FaqDTO;
import com.lotteon.dto.page.FaqPageResponseDTO;
import com.lotteon.dto.page.PageRequestDTO;
import com.lotteon.entity.BoardCate;
import com.lotteon.entity.Faq;
import com.lotteon.repository.BoardRepository;
import com.lotteon.repository.admin.FaqRepository;
import com.lotteon.service.BoardService;
import com.querydsl.core.Tuple;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Log4j2
@RequiredArgsConstructor
@Service
public class FaqService {
    private final FaqRepository faqRepository;
    private final ModelMapper modelMapper;
    private final BoardRepository boardRepository;

    public Faq insertfaq(FaqDTO faqDTO) {
        BoardCate selectedCate = boardRepository.findByBoardCateId(faqDTO.getCate());

        Faq faq = new Faq();
        faq.setCate(selectedCate);
        faq.setFaqtitle(faqDTO.getFaqtitle());
        faq.setFaqcontent(faqDTO.getFaqcontent());

        faqRepository.save(faq);

        return faq;
    }


//    public Faq updatefaq(FaqDTO faqDTO) {
//        Optional<Faq> faq = faqRepository.findById(faqDTO.getFaqNo());
//        if (faq.isPresent()) {
//            Faq faq1 = faq.get();
//            faq1.setFaqtype1(faqDTO.getFaqtype1());
//            faq1.setFaqtype2(faqDTO.getFaqtype2());
//            faq1.setFaqcontent(faqDTO.getFaqcontent());
//            faq1.setFaqtitle(faqDTO.getFaqtitle());
//            return faqRepository.save(faq1);
//        }
//        return null;
//    }


    public List<Faq> selectAllfaq(){
         return faqRepository.findAll();

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

    public void deleteCheck(List<Integer> data){
        for (Integer id : data) {
            faqRepository.deleteById(id);
        }
    }

    public void deletefaq(int no){
        Optional<Faq> optfaq = faqRepository.findById(no);
        if(optfaq.isPresent()){
            Faq faq = optfaq.get();
            faqRepository.delete(faq);
        }
    }



    public FaqPageResponseDTO selectfaqListAll(PageRequestDTO pageRequestDTO){

        Pageable pageable = pageRequestDTO.getPageable("no");

        Page<Tuple> pagefaq = null;
        pagefaq = faqRepository.selectFaqAllForList(pageRequestDTO, pageable);

        List<FaqDTO> faqList = pagefaq.getContent().stream().map(tuple -> {
            Integer id = tuple.get(0, Integer.class); // ID를 가져옴
            Faq faq = faqRepository.findById(id) // ID로 Faq 조회
                    .orElseThrow(() -> new RuntimeException("Faq not found")); // 예외 처리
            return modelMapper.map(faq, FaqDTO.class);
        }).toList();

        int total = (int) pagefaq.getTotalElements();

        return FaqPageResponseDTO.builder()
                .pageRequestDTO(pageRequestDTO)
                .faqdtoList(faqList)
                .total(total)
                .build();

    }

    }

