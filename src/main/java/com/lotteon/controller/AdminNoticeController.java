package com.lotteon.controller;

import com.lotteon.dto.NoticeDTO;
import com.lotteon.entity.Notice;
import com.lotteon.service.admin.NoticeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@RequiredArgsConstructor
@Controller
@RequestMapping("/admin/notice")
public class AdminNoticeController {

    private final NoticeService noticeService;

    @GetMapping("/list")
    public String adminNoticeList(Model model) {
        List<NoticeDTO> notice = noticeService.selectAllNotice();
        model.addAttribute("notice", notice);
        return "content/admin/notice/noticeList";
    }

    @GetMapping("/modify")
    public String adminNoticeModify(Model model) {
        return "content/admin/notice/noticeModify";
    }

    @GetMapping("/view")
    public String adminNoticeView(Model model) {
        return "content/admin/notice/noticeView";
    }

    @GetMapping("/write")
    public String adminNoticeWrite(Model model) {
        return "content/admin/notice/noticeWrite";
    }

    @ResponseBody
    @PostMapping("/write")
    public ResponseEntity<?> adminNoticeWrite(@ModelAttribute NoticeDTO noticeDTO) {
        Notice notice = noticeService.insertNotice(noticeDTO);
        return ResponseEntity.ok().body(notice);
    }

    @ResponseBody
    @DeleteMapping("/delete/check")
    public ResponseEntity<?> adminNoticeDeleteCheck(@RequestBody List<Long> data) {
            if(data == null || data.isEmpty()){
                return ResponseEntity.badRequest().body("삭제할 항목이 없습니다.");
            }
            noticeService.deleteCheck(data);
            return ResponseEntity.ok().build();
        }
    @GetMapping("/delete")
    public String adminFaqDelete(Long no , RedirectAttributes redirectAttributes){
        noticeService.deleteNotice(no);
        redirectAttributes.addFlashAttribute("message", "삭제되었습니다."); // 메시지 추가
        return "redirect:/admin/notice/list";

    }


}


