package com.whomade.planfAi.front.board.mapper;

import com.whomade.planfAi.common.vo.DataMap;
import com.whomade.planfAi.front.board.vo.FrontBoardVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface FrontBoardMapper {
    List<FrontBoardVo> selectListBoard(DataMap paramMap);
}
