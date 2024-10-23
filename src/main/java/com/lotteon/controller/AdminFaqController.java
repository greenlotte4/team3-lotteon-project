package com.lotteon.controller;


import com.lotteon.dto.FaqDTO;
import com.lotteon.entity.Faq;
import com.lotteon.service.admin.FaqService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Log4j2
@RequiredArgsConstructor
@Controller
@RequestMapping("/admin/faq")
public class AdminFaqController {

    private final FaqService faqService;

    @GetMapping("/list")
    public String adminFaqList(Model model) {
        List<FaqDTO> faqDTOs = faqService.selectAllfaq();
        model.addAttribute("faqDTOs", faqDTOs);
        return "content/admin/faq/faqList";
    }

    @GetMapping("/modify")
    public String adminFaqModify(Model model, int no) {
        FaqDTO faqDTO = faqService.selectfaq(no);
        model.addAttribute("faq", faqDTO);
        return "content/admin/faq/faqModify";
    }
    @PostMapping("/modify")
    public ResponseEntity<?> adminFaqModify(FaqDTO faqDTO) {
        Faq faq = faqService.updatefaq(faqDTO);
        log.info("faqasdfasdfasdf : " + faq.toString());
        return ResponseEntity.ok().body(faq);
    }

    @GetMapping("/view")
    public String adminFaqView(int no,Model model) {
        FaqDTO faqDTO = faqService.selectfaq(no);
        model.addAttribute("faq", faqDTO);
        return "content/admin/faq/faqView";
    }

    @GetMapping("/write")
    public String adminFaqWrite(Model model) {
        return "content/admin/faq/faqWrite";
    }

    @ResponseBody
    @PostMapping("/write")
    public ResponseEntity<?> adminFaqWrite1(Model model, @ModelAttribute FaqDTO faqDTO) {
        Faq faq = faqService.insertfaq(faqDTO);
        return ResponseEntity.ok().body(faq);
    }
}
