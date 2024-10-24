package com.lotteon.controller;


import com.lotteon.dto.FaqDTO;
import com.lotteon.dto.QnaDTO;
import com.lotteon.entity.QnA;
import com.lotteon.repository.QnaRepository;
import com.lotteon.service.CsService;
import com.lotteon.service.admin.FaqService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;


@Log4j2
@RequiredArgsConstructor
@Controller
@RequestMapping("/cs")
public class CsController {


    private final QnaRepository qnaRepository;
    private final CsService csService;
    private final FaqService faqService;

    @GetMapping("/main")
    public String main(Model model) {
        model.addAttribute("cate", "main");
        return "content/cs/main";
    }


//    @GetMapping("/faq/list")
//    public String faqList(Model model) {
//        return "content/cs/faq/faqList";
//    }

    @GetMapping("/faq/list")
    public String faqList(Model model) {
        // FAQ 목록을 조회하여 모델에 추가
        List<FaqDTO> faqList = faqService.selectAllfaq();
        model.addAttribute("faqList", faqList);
        return "content/cs/faq/faqList";
    }

//    @GetMapping("/faq/view")
//    public String faqView(Model model) {
//        return "content/cs/faq/faqView";
//    }

    @GetMapping("/faq/view/{id}") // ID를 URL로 받도록 수정
    public String faqView(@PathVariable("id") int id, Model model) {
        FaqDTO faq = faqService.selectfaq(id); // ID에 해당하는 FAQ 데이터를 가져옴
        if (faq != null) {
            model.addAttribute("faq", faq); // 모델에 FAQ 데이터를 추가
        } else {
            // FAQ가 없을 경우 처리할 코드 (예: 에러 페이지로 리다이렉트)
            return "redirect:/cs/faq/list"; // FAQ가 없으면 목록으로 리다이렉트
        }
        return "content/cs/faq/faqView"; // faqView.html로 이동
    }


    @GetMapping("/notice/list")
    public String noticeList(Model model) {
        return "content/cs/notice/noticeList";
    }


    @GetMapping("/notice/view")
    public String noticeView(Model model) {
        return "content/cs/notice/noticeView";
    }


    @GetMapping("/qna/list")
    public String qnaList(Model model, @PageableDefault(size = 10, sort = "rdate", direction = Sort.Direction.DESC) Pageable pageable) {
        // 페이지가 첫 번째 페이지일 경우, 1페이지로 리다이렉트
        if (pageable.getPageNumber() == 0) {
            pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "rdate")); // 1페이지로 이동
        }

        Page<QnA> qnaPage = qnaRepository.findAll(pageable);
        model.addAttribute("qnaPage", qnaPage);
        return "content/cs/qna/qnaList";
    }

    @GetMapping("/qna/detail/{id}")
    public String qnaView(@PathVariable("id") int id, Model model) {
        QnA qna = qnaRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid QnA ID: " + id));
        model.addAttribute("qna", qna);
        return "content/cs/qna/qnaView";
    }


    @GetMapping("/qna/write")
    public String qnaWrite(Model model) {
        return "content/cs/qna/qnaWrite";
    }

    @PostMapping("/qna/write")
    public String qnaWrite(@ModelAttribute QnaDTO qnaDTO, Principal principal) {
        String writer = principal.getName();

        qnaDTO.setQna_writer(writer);

        csService.writeQnA(qnaDTO);
        return "redirect:/cs/qna/list";
    }


}