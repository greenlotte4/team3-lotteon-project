package com.lotteon.controller;

import com.lotteon.dto.NoticeDTO;
import com.lotteon.entity.Notice;
import com.lotteon.service.admin.NoticeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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

}
