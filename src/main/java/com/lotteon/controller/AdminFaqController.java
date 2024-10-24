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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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

    @ResponseBody
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

    @ResponseBody
    @DeleteMapping("/delete/check")
    public ResponseEntity<?> adminFaqDeleteCheck(@RequestBody List<Integer> data){
        if(data == null || data.isEmpty()){
            return ResponseEntity.badRequest().body("삭제할 항목이 없습니다.");
        }
        faqService.deleteCheck(data);
        return ResponseEntity.ok().build();
    }
    @GetMapping("/delete")
    public String adminFaqDelete(int no , RedirectAttributes redirectAttributes){
        faqService.deletefaq(no);
        redirectAttributes.addFlashAttribute("message", "삭제되었습니다."); // 메시지 추가
        return "redirect:/admin/faq/list";

    }
}
