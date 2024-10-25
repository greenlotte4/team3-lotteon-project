package com.lotteon.service;

import com.lotteon.dto.BoardCateDTO;
import com.lotteon.entity.BoardCate;
import com.lotteon.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Log4j2
@RequiredArgsConstructor
@Service
public class BoardService {

    private final BoardRepository boardRepository;
    private final ModelMapper modelMapper;

    public List<BoardCateDTO> selectBoardCate() {
       List<BoardCate> boardCate = boardRepository.findByLevel(1);
       log.info("jhygjhgbjhbvjhbhj:::"+boardCate);
       List<BoardCateDTO> boardCateDTOs = new ArrayList<>();
       for(BoardCate boardCate1 : boardCate){
           BoardCateDTO boardcateDTO = modelMapper.map(boardCate1, BoardCateDTO.class);
           boardCateDTOs.add(boardcateDTO);

       }
        return boardCateDTOs;
    }
}
