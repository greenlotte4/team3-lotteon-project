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
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class CsService {

    private final QnaRepository qnaRepository; // 리포지토리 주입
    private final ModelMapper getModelMapper;

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

//    public List<QnaDTO> getQnaWriter(String writer) {
//        List<QnA> qnas = qnaRepository.findByQnaWriter(writer);
//        List<QnaDTO> qnaDTOs = qnas.stream().map(qnA -> getModelMapper.map(qnA, QnaDTO.class)).collect(Collectors.toList());
//
//        return qnaDTOs;
//    }
//
//    public Page<QnaDTO> getQnaWriter(String writer, Pageable pageable) {
//        Page<QnA> qnas = qnaRepository.findByQnaWriter(writer, pageable);
//        return qnas.map(qnA -> getModelMapper.map(qnA, QnaDTO.class));
//    }

    // 특정 사용자의 QnA 목록을 가져오는 메서드 (비페이징)
    public List<QnaDTO> getQnaWriter(String writer) {
        List<QnA> qnas = qnaRepository.findByQnaWriter(writer);
        return qnas.stream()
                .map(qnA -> getModelMapper.map(qnA, QnaDTO.class))
                .collect(Collectors.toList());
    }

//    // 특정 사용자의 QnA 목록을 가져오는 메서드 (페이징)
//    public Page<QnaDTO> getQnaWriter(String writer, Pageable pageable) {
//        Page<QnA> qnas = qnaRepository.findByQnaWriter(writer, pageable);
//        return qnas.map(qnA -> getModelMapper.map(qnA, QnaDTO.class));
//    }

    public Page<QnaDTO> getQnaWriter(String writer, Pageable pageable) {
        Page<QnA> qnas = qnaRepository.findByQnaWriter(writer, pageable);
        return qnas.map(qnA -> {
            QnaDTO dto = getModelMapper.map(qnA, QnaDTO.class);
            dto.setQna_status(qnA.getQna_status()); // ENUM을 직접 설정
            return dto;
        });
    }

    public Page<QnaDTO> getAllQnA(Pageable pageable) {
        // 모든 QnA 데이터를 가져와서 DTO로 변환
        Page<QnA> qnas = qnaRepository.findAll(pageable);
        return qnas.map(qnA -> getModelMapper.map(qnA, QnaDTO.class));
    }



}
