package com.lotteon.service.admin;

import com.lotteon.dto.NoticeDTO;
import com.lotteon.entity.Faq;
import com.lotteon.entity.Notice;
import com.lotteon.repository.admin.NoticeRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class NoticeService {
    private final NoticeRepository noticeRepository;
    private final ModelMapper modelMapper;

    @Autowired
    private NoticeRepository noticeRepositoryy;

    public List<Notice> getAllNotices() {
        return noticeRepository.findAll();
    }

    public Optional<Notice> getNoticeById(Long no) {
        return noticeRepository.findById(no);
    }



    // 전체 공지사항을 페이지 형태로 가져오는 메서드 추가
    public Page<Notice> getNotices(Pageable pageable) {
        return noticeRepository.findAll(pageable); // 페이지 형태로 공지사항을 가져옴
    }

    // 최신 공지사항 5개를 가져오는 메서드 추가
    public List<NoticeDTO> getTop5Notices() {
        Pageable topFive = PageRequest.of(0, 5, Sort.by(Sort.Direction.DESC, "date"));
        return noticeRepository.findAll(topFive).getContent().stream()
                .map(notice -> modelMapper.map(notice, NoticeDTO.class)) // 엔티티를 DTO로 변환
                .collect(Collectors.toList());
    }

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
    public void deleteCheck(List<Long> data) {
        for (Long id : data) {
            noticeRepository.deleteById(id);
        }

    }

    public void deleteNotice(Long no){
        Optional<Notice> optNotice = noticeRepository.findById(no);
        if(optNotice.isPresent()){
            Notice notice = optNotice.get();
            noticeRepository.delete(notice);
        }
    }
}
