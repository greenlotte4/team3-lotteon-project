package com.lotteon.controller;


import com.lotteon.dto.QnaDTO;
import com.lotteon.entity.QnA;
import com.lotteon.repository.QnaRepository;
import com.lotteon.service.CsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.*;


@Log4j2
@RequiredArgsConstructor
@Controller
@RequestMapping("/cs")
public class CsController {


    private final QnaRepository qnaRepository;
    private final CsService csService;

    @GetMapping("/main")
    public String main(Model model) {
        model.addAttribute("cate", "main");
        return "content/cs/main";
    }


    @GetMapping("/faq/list")
    public String faqList(Model model) {
        return "content/cs/faqList";
    }

    @GetMapping("/faq/view")
    public String faqView(Model model) {
        return "content/cs/faq/faqView";
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
    public String qnaList(Model model, Pageable pageable) {
        Page<QnA> qnaPage = qnaRepository.findAll(pageable);
        model.addAttribute("qnaPage", qnaPage);
        return "content/cs/qna/qnaList";
    }




    @GetMapping("/qna/view")
    public String qnaView(Model model) {
        return "content/cs/qnaView";
    }

    @GetMapping("/qna/write")
    public String qnaWrite(Model model) {
        return "content/cs/qna/qnaWrite";
    }

    @PostMapping("/qna/write")
    public String qnaWrite(@ModelAttribute QnaDTO qnaDTO ) {
        log.info("qnaDTO: {}", qnaDTO);
        qnaDTO.setQna_writer(qnaDTO.getQna_writer());
        csService.writeQnA(qnaDTO);
        return "redirect:/cs/qna/list";
    }


}