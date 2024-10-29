package com.lotteon.controller;


import com.lotteon.dto.FaqDTO;
import com.lotteon.dto.NoticeDTO;
import com.lotteon.dto.QnaDTO;
import com.lotteon.entity.Notice;
import com.lotteon.entity.QnA;
import com.lotteon.repository.QnaRepository;
import com.lotteon.repository.admin.NoticeRepository;
import com.lotteon.service.CsService;
import com.lotteon.service.admin.FaqService;
import com.lotteon.service.admin.NoticeService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Log4j2
@RequiredArgsConstructor
@Controller
@RequestMapping("/cs")
public class CsController {


    private final QnaRepository qnaRepository;
    private final CsService csService;
    private final FaqService faqService;
    private final NoticeService noticeService;
    private final NoticeRepository noticeRepository;
    private final ModelMapper getModelMapper;

    @GetMapping("/main")
    public String main(Model model, @PageableDefault(size = 5) Pageable pageable) {
        List<NoticeDTO> top5Notices = noticeService.getTop5Notices(); // 최신 공지사항 5개 DTO로 가져옴
        List<QnaDTO> top5QnAs = csService.getTop5QnAs(); // 인스턴스를 통해 호출

        // QnA 데이터 가져오기
        Page<QnaDTO> qnaPage = csService.getAllQnA(pageable);

        model.addAttribute("cate", "main");
        model.addAttribute("top5Notices", top5Notices);
        model.addAttribute("qnaPage", qnaPage); // qnaPage를 모델에 추가
        model.addAttribute("top5QnAs", top5QnAs); // top5QnAs도 모델에 추가

        return "content/cs/main";
    }


    @GetMapping("/faq/list")
    public String faqList(Model model) {
        // FAQ 목록을 조회하여 모델에 추가
        List<FaqDTO> faqList = faqService.selectAllfaq();
        model.addAttribute("faqList", faqList);
        return "content/cs/faq/faqList";
    }

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
    public String noticeList(Model model, Pageable pageable) {
        // 데이터베이스에서 모든 공지사항을 페이지 형태로 가져옴
        Page<Notice> noticePage = noticeService.getNotices(pageable);

        // 모델에 공지사항 페이지 추가
        model.addAttribute("noticePage", noticePage); // Page 객체 추가

        return "content/cs/notice/noticeList"; // 뷰 이름 반환
    }

    @GetMapping("/notice/view")
    public String noticeView(Model model, @RequestParam Long no, RedirectAttributes redirectAttributes) {
        // 공지사항 번호를 이용해 개별 공지사항 정보를 가져옴
        Optional<Notice> noticeOpt = noticeService.getNoticeById(no);
        if (noticeOpt.isPresent()) {
            model.addAttribute("notice", noticeOpt.get()); // 모델에 공지사항 추가
            return "content/cs/notice/noticeView"; // 뷰 이름 반환
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "공지사항을 찾을 수 없습니다."); // 에러 메시지 추가
            return "redirect:/notice/list"; // 공지사항이 없으면 목록으로 리다이렉트
        }
    }


    // 문의하기 전체 내역 조회
    @GetMapping("/qna/list")
    public String qnaList(Model model, @PageableDefault(size = 10, sort = "rdate", direction = Sort.Direction.DESC) Pageable pageable) {
        // 페이지가 첫 번째 페이지일 경우 1페이지로 리다이렉트
        if (pageable.getPageNumber() == 0) {
            pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "rdate"));
        }

        Page<QnA> qnaPage = qnaRepository.findAll(pageable);

        // QnA 목록의 작성자 아이디를 마스킹 처리
        qnaPage.forEach(qna -> qna.setQna_writer(maskUsername(qna.getQna_writer())));

        model.addAttribute("qnaPage", qnaPage);
        return "content/cs/qna/qnaList";
    }

    // 아이디 마스킹 메소드
    public String maskUsername(String username) {
        if (username.length() <= 3) {
            return username; // 아이디가 3자 이하일 경우 그대로 반환
        }
        // 앞의 3자는 그대로 두고 나머지는 마스킹 처리
        return username.substring(0, 3) + "****";
    }

    // 문의하기 상세 조회
    @GetMapping("/qna/detail/{id}")
    public String qnaView(@PathVariable("id") int id, Model model, Principal principal) {
        QnA qna = qnaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid QnA ID: " + id));

        String username = principal.getName();

        // 작성자와 현재 사용자가 다를 경우 팝업 메시지 표시
        if (!qna.getQna_writer().equals(username)) {
            model.addAttribute("popupMessage", "다른 사용자의 게시물입니다.<br>해당 게시물에 접근할 수 없습니다.");
            model.addAttribute("isPopup", true);
        } else {
            model.addAttribute("isPopup", false);
        }

        model.addAttribute("qna", qna);
        return "content/cs/qna/qnaView";
    }



    // 사용자 본인의 게시물만 확인할 수 있도록
//    @GetMapping("/qna/list")
//    public String qnaList(Authentication authentication, Model model,
//                          @PageableDefault(size = 10, sort = "rdate", direction = Sort.Direction.DESC) Pageable pageable) {
//        try {
//            // 현재 사용자의 아이디를 가져옴
//            String uid = authentication.getName();
//
//            // 해당 사용자가 작성한 QnA 목록을 가져옴
//            Page<QnaDTO> qnaPage = csService.getQnaWriter(uid, pageable);
//
//            // 모델에 QnA 페이지를 추가
//            model.addAttribute("qnaPage", qnaPage);
//
//            return "content/cs/qna/qnaList";
//        } catch (Exception e) {
//            log.error("Error fetching QnA list: ", e);
//            return "error"; // error.html로 리다이렉트
//        }
//    }
//
//    @GetMapping("/qna/detail/{id}")
//    public String qnaView(@PathVariable("id") int id, Model model) {
//        QnA qna = qnaRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid QnA ID: " + id));
//        model.addAttribute("qna", qna);
//        return "content/cs/qna/qnaView";
//    }

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