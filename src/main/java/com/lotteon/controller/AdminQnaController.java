package com.lotteon.controller;

import com.lotteon.dto.BoardCateDTO;
import com.lotteon.dto.adminQnaDTO;
import com.lotteon.dto.page.PageRequestDTO;
import com.lotteon.dto.page.QnaPageResponseDTO;
import com.lotteon.entity.BoardCate;
import com.lotteon.service.BoardService;
import com.lotteon.service.admin.QnaService;
import lombok.AllArgsConstructor;
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
@RequestMapping("/admin/qna")
public class AdminQnaController {

    private final BoardService boardService;
    private final QnaService qnaService;


    @GetMapping("/list")
    public String adminQnaList(Model model, PageRequestDTO pageRequestDTO) {

        QnaPageResponseDTO qnaPageResponseDTO = qnaService.selectQnaListAll(pageRequestDTO);
        model.addAttribute(qnaPageResponseDTO);

        List<BoardCateDTO> boardCateDTOS = boardService.selectBoardCate();
        log.info(boardCateDTOS);
        model.addAttribute("boardCate", boardCateDTOS);
        return "content/admin/qna/qnaList";
    }

    @ResponseBody
    @GetMapping("/list/page")
    public ResponseEntity<?> adminQnaListPage(@RequestParam(required = false) Long childId, @RequestParam(required = false) Long parentId, PageRequestDTO pageRequestDTO) {
        pageRequestDTO.setParentId(parentId);
        pageRequestDTO.setChildId(childId);

        QnaPageResponseDTO qnaPageResponseDTO = qnaService.selectQnaListAll(pageRequestDTO);
        return ResponseEntity.ok(qnaPageResponseDTO);
    }

    @ResponseBody
    @GetMapping("/subcate/{parentId}")
    public List<BoardCateDTO> adminQnaOption(@PathVariable Long parentId) {
        List<BoardCateDTO> boardsubCate = boardService.selectBoardSubCate(parentId);
        return boardsubCate;
    }

    @GetMapping("/write")
    public String adminQnaWrite(Model model) {
        return "content/admin/qna/qnaWrite";
    }

    @ResponseBody
    @DeleteMapping("/delete/check")
    public ResponseEntity<?> adminQnaDeleteCheck(@RequestBody List<Integer> data) {
        if (data == null || data.isEmpty()) {
            return ResponseEntity.badRequest().body("삭제할 항목이 없습니다.");
        }
        qnaService.deleteCheck(data);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/reply")
    public String adminQnaReply(Model model, int no) {
        adminQnaDTO adminQnaDTO = qnaService.selectQna(no);
        if (adminQnaDTO.getQnareply() == null) {
            model.addAttribute("qna", adminQnaDTO);
            return "content/admin/qna/qnaReply";
        } else {
            model.addAttribute("adminQnaDTO", adminQnaDTO);
            return "content/admin/qna/qnaView";
        }
    }

    @ResponseBody
    @PostMapping("/reply")
    public ResponseEntity<?> adminQnaReply(@RequestParam int no , @RequestBody adminQnaDTO adminQnaDTO){
        adminQnaDTO qnadto= qnaService.selectQna(no);
        qnadto.setQnareply(adminQnaDTO.getQnareply());

        log.info("와랄 : " + qnadto);
        return ResponseEntity.ok().body(qnadto);
    }

}


