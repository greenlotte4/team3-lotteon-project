package com.lotteon.service.admin;


import com.lotteon.dto.NoticeDTO;
import com.lotteon.dto.page.FaqPageResponseDTO;
import com.lotteon.dto.page.NoticePageResponseDTO;
import com.lotteon.dto.page.PageRequestDTO;
import com.lotteon.entity.Notice;
import com.lotteon.repository.admin.NoticeRepository;
import com.querydsl.core.Tuple;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Log4j2
@RequiredArgsConstructor
@Service
public class NoticeService {
    private final NoticeRepository noticeRepository;
    private final ModelMapper modelMapper;

    //등록
    public Notice insertNotice(NoticeDTO noticeDTO) {
        return noticeRepository.save(modelMapper.map(noticeDTO, Notice.class));
    }

    //전체글목록조회
    public List<NoticeDTO> selectAllNotice(){
        List<Notice> noticeList = noticeRepository.findAll();
        return  noticeList.stream()
                .map(notice -> modelMapper.map(notice, NoticeDTO.class)) // 엔티티를 DTO로 변환
                .collect(Collectors.toList());

    }
    //체크된 글 삭제
    public void deleteCheck(List<Long> data) {
        for (Long id : data) {
            noticeRepository.deleteById(id);
        }

    }
    //단일 삭제
    public void deleteNotice(Long no){
        Optional<Notice> optNotice = noticeRepository.findById(no);
        if(optNotice.isPresent()){
            Notice notice = optNotice.get();
            noticeRepository.delete(notice);
        }
    }
    //글보기
    public NoticeDTO selectNotice(Long no){
       Optional<Notice> notice = noticeRepository.findById(no);
       if(notice.isPresent()){
           NoticeDTO noticeDTO = modelMapper.map(notice.get(), NoticeDTO.class);
           return noticeDTO;
       }
       return null;
    }

    //글 수정
    public Notice UpdateNotice(NoticeDTO noticeDTO) {
        Optional<Notice> notice = noticeRepository.findById(noticeDTO.getNoticeNo());
        log.info("notice :" + notice);
        if(notice.isPresent()){
            Notice notice1 = notice.get();

            if(noticeDTO.getNoticetype() == null){
                notice1.setNoticetype(notice1.getNoticetype());
            }else {
                notice1.setNoticetype(noticeDTO.getNoticetype());
            }
            notice1.setNoticetitle(noticeDTO.getNoticetitle());
            notice1.setNoticecontent(noticeDTO.getNoticecontent());
            return noticeRepository.save(notice1);


        }
        return null;
    }
    //페이징
    public NoticePageResponseDTO selectNoticeListAll(PageRequestDTO pageRequestDTO) {
        Pageable pageable = pageRequestDTO.getPageable("no");
        Page<Tuple> pagenotice = null;
        pagenotice = noticeRepository.selectNoticeAllForList(pageRequestDTO, pageable);

        List<NoticeDTO> noticeList = pagenotice.getContent().stream().map(tuple -> {
            Long id = tuple.get(0, Long.class);
            Notice notice = noticeRepository.findById(id).orElseThrow(() -> new RuntimeException("Notice not fonund with ID : " + id));
            NoticeDTO noticeDTO = modelMapper.map(notice, NoticeDTO.class);
            return noticeDTO;
        }).toList();

        int total = (int) pagenotice.getTotalElements();

        return NoticePageResponseDTO.builder()
                .pageRequestDTO(pageRequestDTO)
                .noticedtoList(noticeList)
                .total(total)
                .build();
    }

}
