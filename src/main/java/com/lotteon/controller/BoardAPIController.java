package com.lotteon.controller;
import com.lotteon.dto.BoardCateDTO;
import com.lotteon.dto.adminQnaDTO;
import com.lotteon.dto.page.FaqPageResponseDTO;
import com.lotteon.dto.page.PageRequestDTO;
import com.lotteon.dto.page.QnaPageResponseDTO;
import com.lotteon.service.BoardService;
import com.lotteon.service.admin.FaqService;
import com.lotteon.service.admin.QnaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Log4j2
@RequiredArgsConstructor
@RequestMapping("/api")
public class BoardAPIController {


    private final QnaService qnaService;
    private final FaqService faqService;
    private final BoardService boardService;

    @ResponseBody
    @GetMapping("/qna/list/page")
    public ResponseEntity<?> adminQnaListPage(@RequestParam(required = false) Long childId, @RequestParam(required = false) Long parentId, @RequestParam(required = false) String qnawriter, PageRequestDTO pageRequestDTO) {
        pageRequestDTO.setParentId(parentId);
        pageRequestDTO.setChildId(childId);
        pageRequestDTO.setQnawriter(qnawriter);

        QnaPageResponseDTO qnaPageResponseDTO = qnaService.selectQnaListAll(pageRequestDTO);

        // qnadtoList에서 writer 아이디 마스킹
        for (adminQnaDTO qnaDTO : qnaPageResponseDTO.getQnadtoList()) {
            if (qnaDTO.getQnawriter() != null) {
                qnaDTO.setQnawriter(maskUsername(qnaDTO.getQnawriter())); // writer 아이디 마스킹 처리
            }
        }
        return ResponseEntity.ok(qnaPageResponseDTO);

    }
    // 아이디 마스킹 메소드
    public String maskUsername(String username) {
        if (username.length() <= 3) {
            return username; // 아이디가 3자 이하일 경우 그대로 반환
        }
        // 앞의 3자는 그대로 두고 나머지는 마스킹 처리
        return username.substring(0, 3) + "****";
    }

    @ResponseBody
    @GetMapping("/faq/list/page")
    public ResponseEntity<?> adminFaqListPage( @RequestParam(required = false) Long childId, @RequestParam(required = false) Long parentId, PageRequestDTO pageRequestDTO) {
        pageRequestDTO.setParentId(parentId);
        log.info("이거먼데!!!!" + pageRequestDTO);
        pageRequestDTO.setChildId(childId);
        log.info("이건또먼데!!!!!" + pageRequestDTO);

        FaqPageResponseDTO faqPageResponseDTO = faqService.selectfaqListAll(pageRequestDTO);
        log.info("asdfasdf!!!! : " + faqPageResponseDTO);

        return ResponseEntity.ok(faqPageResponseDTO);
    }


    @GetMapping("/faq/subcate/{parentId}")
    @ResponseBody
    public List<BoardCateDTO> adminFaqOption(@PathVariable Long parentId){
        List<BoardCateDTO> boardsubCate = boardService.selectBoardSubCate(parentId);
        return boardsubCate;
    }

}