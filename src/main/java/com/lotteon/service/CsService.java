//package com.lotteon.service;
//
//import org.springframework.stereotype.Service;
//
//@Service
//public class CsService {
//
//}
package com.lotteon.service;

import com.lotteon.dto.QnaDTO; // DTO import
import com.lotteon.entity.QnA; // Entity import
import com.lotteon.repository.QnaRepository; // Repository import
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CsService {

    private final QnaRepository qnaRepository; // 리포지토리 주입

    // QnA 글 등록 메서드
    public void writeQnA(QnaDTO qnaDTO) {


        // DTO에서 엔티티로 변환
        QnA qna = QnA.builder()
                .qna_type1(qnaDTO.getQna_type1())
                .qna_type2(qnaDTO.getQna_type2())
                .qna_title(qnaDTO.getQna_title())
                .qna_writer(qnaDTO.getQna_writer()) // 현재 사용자 이름 설정
                .qna_content(qnaDTO.getQna_content()) // 내용 추가
                .iscompleted("N") // 기본값 설정
                .build();

        // 데이터베이스에 저장
        qnaRepository.save(qna);
    }
}
