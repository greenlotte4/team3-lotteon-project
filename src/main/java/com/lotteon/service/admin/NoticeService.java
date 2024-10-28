package com.lotteon.service.admin;

import com.lotteon.dto.NoticeDTO;
import com.lotteon.entity.Notice;
import com.lotteon.repository.admin.NoticeRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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
}
